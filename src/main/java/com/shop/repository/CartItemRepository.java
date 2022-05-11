package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
/**
 * 장바구니에 들어갈 상품 저장, 조회
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //카트아이디, 상품아이디를 이용, 상품이 장바구니에 들어있는지 조회
   CartItem findByCartIdAndItemId(Long cartId, Long itemID);

   //장바구니페이지에 전달할 CartDetailDto리스트를 쿼리 하나로 조회
   @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
           "from CartItem ci, ItemImg im " +
           "join ci.item i " +
           "where ci.cart.id = :cartId " +
           "and im.item.id = ci.item.id " + //대표이미지만
           "and im.repimgYn = 'Y' " +
           "order by ci.regTime desc"
   )
   List<CartDetailDto> findCartDetailDtoList(Long cartId);


}
