package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member_id")
@Getter@Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true) //회원은 이메일을 통해 유일하게 구분, 동일한 값 DB에 들어올 수 없게
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING) //eum 순서가 바뀌지 않도록록
    private Role role;

    /*Member 엔티티를 생성하는 메소드
      여기에서 관리를 한다면 코드가 변경되더라도 한군데만 수정하면 되는 이점
     */
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        //스프링 시큐리티 설정 클래스에 등록한 BCrptPasswordEncoder Bean을 파라미터로 넘겨서 비밀번호 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);

        return member;

    }

}
