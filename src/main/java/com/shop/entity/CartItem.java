package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter@Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id") //foreign key (cart_id)
    private Cart cart; //하나의 장바구니에 여러 상품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; //하나의 상품은 여러 장바구니의 장바구니 상품

    private int count; //같은 상품을 장바구니에 몇개 담을지


}
