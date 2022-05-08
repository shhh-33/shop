package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberService가 UserDetailsService를 구현한다.
 *    <UserDetailsService>
 *  - DB에서 회원정보를 가지고 오는 역할
 *  - loadUserByUsername()메소드가 존재하며, 회원 정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스(내장)를 반환
 *
 */
@Service
@Transactional //비즈니스 로직을 담당하는 서비스 계층에 작성, 로직처리 중 에러 발생시 변경된 데이터를 로직 수행 전으로 콜백
@RequiredArgsConstructor //bean 주입(@Setter,@Autowired) 이건 final이나 @NotNull이 붙은 필드에 생성자 생성
public class MemberService implements UserDetailsService {

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

    /*
     throws
     메소드의 내부 소스코드에서 에러가 발생했을시 예외처리를 "try ~ catch"로 자기자신이 예외처리를 하는 것이 아니라,
     이 메소드를 사용하는 곳으로 책임을 전가하는 행위

      throw new 발생시킬 예외;
     */

    /**
     *
     *  <UserDetail>
     *  - 시큐리티에서 회원의 정보를 담기위해 사용하는 인터페이스
     *  - 직접구현 or 시큐리티에서 제공하는 User클래스 사용
     *  - User 클래스는 UserDetails 인터페이스를 구현하고 있는 클래스
     *
     * @param email : 로그인할 유저의 email 전달받음
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member =memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email); //내장
        }

        //UserDetail을 구현하고 있는 User 객체를 반환, User 객체를 생성하기 위해 생성자로 회원의 이메일,비밀번호,role을 파라미터로 넘김
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}


