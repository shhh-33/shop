package com.shop.repository;

import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 장바구니에 들어갈 상품 저장, 조회
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //카트아이디, 상품아이디를 이용, 상품이 장바구니에 들어있는지 조회회
   CartItem findByCartIdAndItemId(Long cartId, Long itemID);
}
