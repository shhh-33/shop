package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

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


    //장바구니에 담을 상품 엔티티 생성
    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;

    }


    //장바구니에 담을 수량 증가
    public void addCount(int count){
        this.count += count;
    }

    //장바구니 수량 변경
    public void updateCount(int count){
        this.count =count;
    }

}
