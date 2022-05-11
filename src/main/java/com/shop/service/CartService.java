package com.shop.service;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니에 상품 담기
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {

        Item item = itemRepository.findById(cartItemDto.getItemId()) //장바구니에 담을 상품엔티티 조회
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email); //현재 로그인한 회원엔티티 조회

        Cart cart = cartRepository.findByMemberId(member.getId()); //현재 로그인한 회원의 장바구니엔티티 조회

        if (cart == null) { //상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem saveCartItem = //현재 상품이 장바구니에 이미 들어가있는지 조회
                cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (saveCartItem != null) {//장바구니에 이미 있다면
            saveCartItem.addCount(cartItemDto.getCount()); //기존 수량에 현재 장바구니에 담을 수량만큼 더함
            return saveCartItem.getId();
        } else {//없다면
            //장바구니,상품엔티티,장바구니에 담을 수량을 이용해 CartItem 엔티티 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem); //장바구니에 들어갈 상품 저장

            return cartItem.getId();
        }

    }

    //현재 로그인한 회원의 정보를 이용, 장바구니에 들어있는 상품 조회
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId()); //로그인한 회원 장바구니 엔티티 조회

        if (cart == null) { //장바구니에 상품을 한번도 안담았을 경우 장바구니 엔티티가 없으므로
            return cartDetailDtoList; //빈리스트 반환
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId()); //장바구니에 담긴 상품정보 조회
        return cartDetailDtoList;
    }


    //현재 로그인한 회원과 해당 장바구니 상품을 저장한 회원이 같은지 검사
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){

        Member curMember = memberRepository.findByEmail(email); //로그인한 회원 조회

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember(); //장바구니 상품을 저장한 회원조회

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false; //로그인한 회원가 장바구니 상품을 저장한 회원이 다르면 false
        }

        return true; //같으면 true
    }

    //장바구니 수량 업데이트
    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }
}
