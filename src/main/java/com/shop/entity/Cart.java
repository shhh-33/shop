package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
     장바구니 엔티티가 회원 엔티티를 참조하는 일대일 단방향 매핑
     장바구니 엔티티를 조회하면서 회원 엔티티의 정보도 동시에 가져올 수 있다.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //매핑할 외래키 지정
    private Member member;

}
