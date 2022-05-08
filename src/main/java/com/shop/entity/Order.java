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
public class Order extends BaseEntity {

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
    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL  //연관관계 주인, 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 변이
            ,orphanRemoval = true) //고아객체제거(부모엔티티와 연관관계 끊어짐) , 참조하는 기능이 하나일때만 사용할 것
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문 상품을 가지므로 List사용

   /* private LocalDateTime regTime;

    private LocalDateTime updateTime;
*/
}
