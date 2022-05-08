package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    /*http 요청에 대한 보안 설정
     페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{


        http.formLogin()
                .loginPage("/members/login") //로그인 페이지 url
                .defaultSuccessUrl("/") //로그인 성공시 이동할 url
                .usernameParameter("email") //로그인 시 사용할 파라미터 이름
                .failureUrl("/members/login/error") //로그인 실패시 이동할 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url
                .logoutSuccessUrl("/");  //로그아웃 성공시 이동할 url

        http.authorizeRequests() //시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll() // permitAll을 통해 모든 사용자가 인증(로그인)없이 해당경로에 접근 -메인,회원관련url,상품 상세페이지, 상품 이미지 경로
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());


    }


    //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    /*
     시큐리티에서 인증은 AuthenticationManagerBuilder를 통해 이루어진다.
     AuthenticationManagerBuilder가 AuthenticationManager를 생성한다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService) // userDetailsService를 구현하고 있는 객체로 memberService를 지정
                .passwordEncoder(passwordEncoder()); //비밀번호 암호화를 위해 passwordEncoder를 지정
    }


}
