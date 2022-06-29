package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문 일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addOrderItem(OrderItem orderItem) { // 주문 상품 정보를 담기위한 메소드
        orderItems.add(orderItem);
        orderItem.setOrder(this); // 양방향 참조 관계이므로 orderItem 객체에도 order 객체를 세팅한다.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member); // 상품을 주문한 회원의 정보를 셋팅한다.

        for (OrderItem orderItem : orderItemList) {
            // 장바구니에서는 여러개의 상품을 주문할 수 있으므로 리스트로 주문상품을 받아 상품별 객체를 생성한다.
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 ORDER 로 수정한다.
        order.setOrderDate(LocalDateTime.now()); // 주문 시간을 현재로 수정한다.
        System.out.println("now time : " + LocalDateTime.now());

        return order;
    }

    public int getTotalPrice() { // 주문상품들에 대한 총 금액을 구하여 반환한다.
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }

    public void cancelOrder() { // 주문 취소 시 주문 상태 값을 취소로 변경
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
