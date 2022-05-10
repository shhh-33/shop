package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStiockException;
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

    /*
    엔티티 클래스에 비즈니스 로직을 추가하면 객체 지향적 코딩, 코드 재활용 가능
    데이터 변경 포인트를 한군데에서 관리할 수 있다.
     */

    //상품 업데이트 로직
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price =itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail =itemFormDto.getItemDetail();
        this.itemSellStatus =itemFormDto.getItemSellStatus();
    }

    //상품 재고 감소 로직
    public void removeStock(int stockNumber){
        int restStock =this.stockNumber -stockNumber; //상품 재고수량 - 주문 = 남은 재고 수량

        //주문수량 > 재고
        if(restStock<0){
            throw new OutOfStiockException("상품의 재고가 부족합니다. 현재 재고 수량 : " + this.stockNumber);
        }
        //상품의 현재 재고값으로 할당 <= 주문 후 남은 재고 수량을
        this.stockNumber = restStock;
    }

    //상품 재고 증가 (주문취소시)
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }






}
