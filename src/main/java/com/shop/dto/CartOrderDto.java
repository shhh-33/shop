package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 장바구니 페이지에서 주문할 상품 데이터를 전달
 */
@Getter
@Setter
public class CartOrderDto {
    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList; //장바구니에서 여러개의 상품을 주문하므로 자기 자신을 List로 가지고 있도록


}
