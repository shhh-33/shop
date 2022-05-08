package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")

class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    //회원 엔티티 생성
    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member); //회원 저장

        Cart cart = new Cart();
        cart.setMember(member); //장바구니에 회원 set
        cartRepository.save(cart); //장바구니 저장

        em.flush(); //영속성 컨텍스트에 데이터 저장 후 트랜잭션 끝날 때 flush()호출하여 DB에 반영
        em.clear(); //영속성 컨텍스트로부터 엔티티 조회후 없을 경우 DB를 조회, 실제 DB에서 장바구니 엔티티 가지고 올때 회원 엔티티도 같이 가지고 오는지 보기위해 비운다.

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new); //저장된 장바구니 엔티티 조회

        assertEquals(savedCart.getMember().getId(), member.getId()); //처음 저장한 member 엔티티의 id와 saveCart에 매핑된 member엔티티 id비교
    }

}