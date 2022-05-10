package com.shop.service;

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

import javax.persistence.EntityNotFoundException;

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

    public Long addCart(CartItemDto cartItemDto, String email){

        Item item =itemRepository.findById(cartItemDto.getItemId()) //장바구니에 담을 상품엔티티 조회
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email); //현재 로그인한 회원엔티티 조회

        Cart cart = cartRepository.findByMemberId(member.getId()); //현재 로그인한 회원의 장바구니엔티티 조회

        if(cart ==null){ //상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem saveCartItem = //현재 상품이 장바구니에 이미 들어가있는지 조회
                cartItemRepository.findByCartIdAndItemId(cart.getId(),item.getId());

        if(saveCartItem != null){//장바구니에 이미 있다면
            saveCartItem.addCount(cartItemDto.getCount()); //기존 수량에 현재 장바구니에 담을 수량만큼 더함
            return saveCartItem.getId();
        }else {//없다면
            //장바구니,상품엔티티,장바구니에 담을 수량을 이용해 CartItem 엔티티 생성
            CartItem cartItem = CartItem.createCartItem(cart,item,cartItemDto.getCount());
            cartItemRepository.save(cartItem); //장바구니에 들어갈 상품 저장

            return cartItem.getId();
        }


    }
}
