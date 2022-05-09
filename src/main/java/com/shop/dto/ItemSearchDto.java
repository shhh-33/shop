package com.shop.dto;


import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 조회 조건을 가지고 있다.
 */
@Getter @Setter
public class ItemSearchDto {

    private String searchDateType; //현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회

    private ItemSellStatus searchSellStatus; //상품의 판매상태를 기준으로 상품데이터 조회

    private String searchBy; //상품을 조회할 때 어떤 유형으로 조회할지 선택 itemNm:상품명, createBy:상품 등록자 아이디

    private String searchQuery = ""; //조회할 검색어 저장
}

/*
  상품관리 -> 선택한 상품 상세 페이지로 이동할 수 있는 기능 구현
  조회조건 : 상품등록일, 상품 판매 상태, 상품명 or 상품 등록자 아이디
  조회조건이 복잡한 화면은 Querydsl을 이용해 조건에 맞는 쿼리를 동적으로 쉽게 생성하자.
  <사용법- 3단계 과정>
  1.사용자 정의 인터페이스 작성
  2.사용자 정의 인터페이스 구현
  3.Spring Data Jpa 리포지토리에서 사용자 정의 인터페이스 상속
 */