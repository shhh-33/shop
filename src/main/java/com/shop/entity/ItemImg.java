package com.shop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 상품 이미지 엔티티
 */
@Entity
@Getter @Setter
@Table(name = "item_img")
public class ItemImg {


    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY) //itemimg의 입장에서 하나의 item에 여러개의 사진 가능
    @JoinColumn(name = "item_id") //fk
    private Item item;


    //이미지 정보 업데이트
    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
