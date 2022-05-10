package com.shop.controller;

import com.shop.dto.CartItemDto;
import com.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto,
                         BindingResult bindingResult, Principal principal){ //Principal: 시큐리티,시스템을 사용하고자하는 사용자

        //CartItemDto 객체에 데이터 바인딩시 에러가 있는지 검사
        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();//현재 로그인한 회원의 이메일 정보 저장
        Long cartItemId;

        //화면으로부터 넘어옴옴
        try { //장바구니에 담을 상품 정보,현재 로그인한 회원의 이메일 정보이용 장바구니에 상품담는 로직
            cartItemId =cartService.addCart(cartItemDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); //결과값으로 생성된 장바구니 상품아이디와 요청이 성공했다는 http응답상태코드 반환
    }
}
