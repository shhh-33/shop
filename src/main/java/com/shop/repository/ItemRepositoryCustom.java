package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 1.사용자 정의 인터페이스
 */
public interface ItemRepositoryCustom {


    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
