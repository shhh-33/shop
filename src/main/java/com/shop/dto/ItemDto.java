package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
  데이터를 주고 받을 때는 Entity 클래스 자체를 반환하면 안되고
  dto==data transfer object를 생성해 사용해야한다.
  DB설계를 외부에 노출할 필요도 없으며, 요청과 응답 객체가 항상 엔티티와 같지 않기 때문
 */
@Getter
@Setter
public class ItemDto {

    private  Long id;

    private String itemNm;

    private Integer price;

    private String itemDetail;

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
