package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 1.사용자 정의 인터페이스
 */
public interface ItemRepositoryCustom {


    //상품관리
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    //메인페이지 상품 리스트
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);



}
