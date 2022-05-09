package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/*JpaRepository를 상속 : 기본적인 crud,페이징 처리를 위한 메소드 정의돼 있다.
  <엔티티 타입 클래스, 기본키 타입>
  Item클래스는 기본키 타입이 Long이므로 Long을 넣어준다

3.Spring Data Jpa 리포지토리에서 사용자 정의 인터페이스 상속
->querydsl로 구현한 상품관리 페이지 목록을 불러오는 getAdminItemPage()메소드 사용할 수 있다.
*/
public interface ItemRepository extends JpaRepository<Item,Long> ,QuerydslPredicateExecutor<Item> ,ItemRepositoryCustom{

    /* 쿼리메소드 : 데이터 조회 기능
       Repository 인터페이스에 간단한 네이밍 룰을 이용하여 메소드를 작성하면 원하는 쿼리를 실행할수있다.
       find + 엔티티 이름(생략가능) + By + 변수이름
     */


    //상품명을 이용하여 데이터 조회
    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

   /*
    @Query 어노테이션을 이용해 JPQL 이라는 객체지향 쿼리언어를 사용
    -> 복잡한 쿼리를 다루기에 적합
    -> 엔티티 객체를 대상으로 쿼리 수행
    -> SQL을 추상화해서 사용하기 때문에 특정 DB SQL에 의존하지 않음
    */

    /*
     상품 상세설명을 파라미터로 받아 상품 데이터를 조회
     (상품 상세 설명에 포함하고 있는 데이터를 가격 높은 순으로 조회)
     */
    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);




}
