package com.shop.entity;


import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="orders")
@Getter
@Setter
@ToString
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //한명의 회원은 여러번의 주문 할 수 있다. (주문엔티티 기준 다대일 단방향)

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    /*
    한쪽이 일대다면 다른쪽은 다대일
    주문 상품 엔티티와 일대다 매핑 ( private Order order; )
     */
    @OneToMany(mappedBy = "order") //연관관계 주인
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문 상품을 가지므로 List사용

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
