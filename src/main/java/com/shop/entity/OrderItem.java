package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; //하나의 상품은 여러 주문 상품으로 들어갈 수 있다. 주문 상품 기준으로 다대일 단방향

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") //fk
    private Order order; //하나의 주문에 여러개의 상품을 주문할 수 있다. 주문상품과 주문 엔티티를 다대일 단방향

    private int orderPrice; //주문가격

    private int count; //수량

    //상품 주문
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item); //주문 상품
        orderItem.setCount(count); //주문 수량 세팅
        orderItem.setOrderPrice(item.getPrice()); //나중에 쿠폰이나 할인 설정

        //주문한 수량만큼 재고 감소
        item.removeStock(count);
        return orderItem;
    }

    //주문 가격 *수량 = 총 가격
    public int getTotalPrice(){
        return orderPrice*count;
    }

    //주문 취소
    public void cancel(){
        this.getItem().addStock(count);
    }



}
