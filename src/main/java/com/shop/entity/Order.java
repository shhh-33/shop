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
@Getter @Setter
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


    //주문 객체 만들기
    public void addOrderItem(OrderItem orderItem) { //주문 상품 정보 담아줌
        orderItems.add(orderItem); //orderItem 객체를 order 객체의 orderItems에 추가합니다.
        orderItem.setOrder(this);
    }


    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member); //상품을 주문한 회원의 정보를 세팅합니다.

        /*
        상품 페이지에서는 1개의 상품을 주문하지만,
        장바구니 페이지에서는 한번에 여러개의 상품을 주문한다.
        따라서 여러개의 주문 상품을 담을 수 있도록 리스트 형태로 파라미터 값을 받으며
        주문 객체에 ordrItem 객체 추가
         */
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); //주문 상태 세팅
        order.setOrderDate(LocalDateTime.now()); //현재 시간을 주문 시간으로 세팅팅

        return order;
    }

    //총 주문 금액
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice +=orderItem.getTotalPrice();
        }
        return  totalPrice;
    }






    



}
