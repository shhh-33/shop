package com.shop.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;

import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 2.사용자 정의 인터페이스 구현 (Impl 꼭 붙일 것)
 */
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory; //동적으로 쿼리를 생성하기 위해 JPAQueryFactory 클래스 사용


    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); //JPAQueryFactory의 생성자로 EntityManager 객체를 넣어준다.

    }

    /*
    상품 판매 조건이 전체(null)일 경우 : null 리턴, where절에서 해당 조건 무시
    null이 아니라 판매중 or 품절 상태라면 해당 조건의 상품만 조회
     */
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    /*searchDateType의 값에 따라서 dateTime의 값을 이전 시간의 값으로 세팅후,
      해당 시간 이후로 등록된 상품만 조회
      ex) searchDateType 값이 "1m"인 경우 dateTime의 시간을 한달전으로 세팅후 최근 한달동안 등록된 상품만 조회하도록 조건값 반환
     */
    private com.querydsl.core.types.dsl.BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) { //상품 등록일 전체 or 전체
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) { //하루
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) { //일주일
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) { //한달
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) { //6개월
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }


    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemNm", searchBy)) { //   searchBy 값에 따라서
            return QItem.item.itemNm.like("%" + searchQuery + "%"); // 상품명에 검색어를 포함하고 있는 상품
        } else if (StringUtils.equals("createdBy", searchBy)) { //or 상품 생성자의 아이디에 검색어를 포함하고 있는 상품 조회
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }


    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        //queryFactory를 이용해 쿼리 생성
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item) //select : 상품 데이터를 조회하기 위해 Qitem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), //where : BooleanExpression 반환하는 조건물 넣어줌 ,는 and
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc()) //orderBy
                .offset(pageable.getOffset()) //offset : 데이터를 가져올 시작 인덱스
                .limit(pageable.getPageSize()) //limit : 한번에 가지고 올 최대 개수
                .fetchResults(); //조회한 리스트 및 전체 개수를 포함하는 QueryResults 반환 (2번의 쿼리 실행..상품데이터리스트조회, 전체 개수 조회)

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total); //Page로 반환
    }

    private BooleanExpression itemNmLike(String searchQuery) { //검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건 반환
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }



    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto( //QMainItemDto의 생성자에 반환할 값 넣어줌
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item) //itemImg과 item을 내부조인
                .where(itemImg.repimgYn.eq("Y")) //대표 상품 이미지만 불러옴
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl(content, pageable, total);
    }

}
