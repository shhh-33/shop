package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;


/*JpaRepository를 상속 : 기본적인 crud,페이징 처리를 위한 메소드 정의돼 있다.
  <엔티티 타입 클래스, 기본키 타입>
  Item클래스는 기본키 타입이 Long이므로 Long을 넣어준다

*/
public interface ItemRepository extends JpaRepository<Item,Long> {


}
