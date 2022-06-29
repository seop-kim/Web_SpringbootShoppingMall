package com.shop.service;

import com.shop.dto.OrderDTO;
import com.shop.dto.OrderHistDTO;
import com.shop.dto.OrderItemDTO;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDTO orderDTO, String email) {
        Item item = itemRepository.findById(orderDTO.getItemId()) // 주문할 상품을 조회한다.
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email); // 주문한 회원을 조회한다.

        List<OrderItem> orderItemList = new ArrayList<>(); // 주문 상품을 담을 리스트 선언

        // 주문할 상품의 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성한다.
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDTO.getCount());
        orderItemList.add(orderItem);

        // 회원 정보와 주문상품이 담긴 리스트를 이용해 주문 엔티티를 생성한다.
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order); // 생성한 주문 엔티티를 저장한다.

        return order.getId();
    }


    @Transactional(readOnly = true)
    public Page<OrderHistDTO> getOrderList(String email, Pageable pageable) {
        // 유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회한다.
        List<Order> orders = orderRepository.findOrders(email, pageable);

        // 유저의 모든 주문 수를 변수에 저장한다.
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDTO> orderHistDTOs = new ArrayList<>();

        for (Order order : orders) { // 주문 리스트를 순회하며 구매 이력 페이지에 전달할 DTO 를 생성한다.
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                // 주문한 상품의 대표 이미지를 조회한다.
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, itemImg.getImgUrl());
                orderHistDTO.addOrderItemDTO(orderItemDTO);
            }
            orderHistDTOs.add(orderHistDTO);
        }
        // 페이지 구현 객체를 생성하여 반환한다.
        return new PageImpl<OrderHistDTO>(orderHistDTOs, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) { // 생성한 주문의 주문자와 현재 사용자가 같은지를 확인한다.
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        Member saveMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // 주문 취소 상태로 변경되면 트랜잭션이 끝날 때 update 쿼리가 실행된다.
        order.cancelOrder();
    }
}
