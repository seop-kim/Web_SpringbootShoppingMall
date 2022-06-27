package com.shop.service;

import com.shop.dto.OrderDTO;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
