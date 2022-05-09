package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

/**
 * 메인 페이지에서 상품을 보여줄때 사용
 */
@Getter @Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;


    @QueryProjection //Querydsl 결과 조회시 MainItemDto 객체로 바로 받아옴
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
