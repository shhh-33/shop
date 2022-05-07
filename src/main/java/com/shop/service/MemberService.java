package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional //비즈니스 로직을 담당하는 서비스 계층에 작성, 로직처리 중 에러 발생시 변경된 데이터를 로직 수행 전으로 콜백
@RequiredArgsConstructor //bean 주입(@Setter,@Autowired) 이건 final이나 @NotNull이 붙은 필드에 생성자 생성
public class MemberService {

    //final:다른 객체로 바꾸지 않기위해(재할당x)
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }


    //이미 가입된 회원인 경우 IllegalStateException 예외 발생
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    }


