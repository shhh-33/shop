package com.shop.exception;

/**
 * - 고객이 상품을 주문하면 현재 상품의 재고에서 주문수량만큼 재고를 감소시켜야한다.
 * - 주문수량 > 재고수량 == 주문x 이럴때 발생시킬 exception 정의
 */
public class OutOfStiockException extends RuntimeException {

    public OutOfStiockException(String message){
        super(message);
    }
}
