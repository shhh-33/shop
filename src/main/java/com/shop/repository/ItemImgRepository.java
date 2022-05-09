package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {
    /*
    이미지가 잘 저장됐는지 테스트 코드 작성 위해...
    매개변수로 넘겨준 상품id를 가지며, 상품 이미지 아이디를 오름차순으로 가져옴
     */
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}
