package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


/**
 * JPA에서는 Auditing 기능을 제공하여
 * 엔티티가 저장,수정 될때 자동으로 등록일,수정일,등록자,수정자를 입력해준다.
 * 엔티티의 생성과 수정을 감시하고 있는 것
 *
 * 이런 공통 멤버 변수들을 추상클래스로 만들고, 해당 추상 클래스를 상속받는 형태로 바꾸자
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = "";
        if (authentication != null) {
            userId = authentication.getName(); //로그인한 사용자의 정보를 조회, 사용자의 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userId);
    }
}

