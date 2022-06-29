package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager entityManager;

    public Item 상품만들기() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }


    @Test
    @DisplayName("영속성 전이 테스트")
    public void 영속성전이테스트() {
        Order order = new Order(); // 새로운 주문을 만든다.

        for (int i = 0; i < 3; i++) {
            Item item = 상품만들기(); // 상품을 만든다. item_id 는 반복할때마다 증가
            itemRepository.save(item); // item을 데이터베이스에 저장한다.
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            /*
             * 새로운 주문 상품 엔티티 생성
             * 주문 상품 엔티티에 item 과 order를 주입시키고 order 엔티티 내 저장
             */


        }
        orderRepository.saveAndFlush(order);
        entityManager.clear();

        Order saveOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItems().size());
        System.out.println("Test print :: " + "Order Count is " + 3 + ", saveOrder size is " + saveOrder.getOrderItems().size());
    }

    public Order 주문만들기() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = 상품만들기();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void 고아객체제거테스트() {
        Order order = 주문만들기();
        order.getOrderItems().remove(0);
        entityManager.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void 지연로딩테스트() {
        Order order = 주문만들기();
        Long orderItemId = order.getOrderItems().get(0).getId();
        entityManager.flush();
        entityManager.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("=============================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=============================");
    }


}
