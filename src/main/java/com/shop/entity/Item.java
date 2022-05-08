package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * 나중에 추가할 것 : p75
 */

@Entity //item클래스를 entity로 선언
@Table(name = "item")  // 어떤 테이블과 매핑될지 지정, item 테이블과 매핑되도록 이름을 item이라 지정
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id //entity는 반드시 기본키 가져야한다.
    @Column(name = "item_id") //테이블에 매핑될 컬럼의 이름
    //item클래스의 id변수와 item테이블의 item_id컬럼이 매핑되도록
    @GeneratedValue(strategy = GenerationType.AUTO) //기본키 생성전략
    private Long id; //상품코드

    @Column(nullable = false ,length = 50) //default값 255
    private String itemNm; // 상품명

    @Column(name="price",nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태
    //enum : 재고가 없을때 프론트에 노출x or 판매중

   /* private LocalDateTime regTime; //등록 시간

    private LocalDateTime updateTime; //수정 시간*/


}
