package com.club.boot5.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Log4j2
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //인메모리를 통해 인증절차를 수행하는 메서드를 빈으로 등록
    // : ClubUserDetailsService를 빈으로 등록해, 스프링 시큐리티에서 UserDetailsService로 인식하므로
    // 임시로 직접 설정한 메서드를 사용하지 않도록 설정
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user1")
//                //.password(passwordEncoder().encode("1111"))
//                .password("$2a$10$wO8xgibV2LVtJScXhtFLtOXkM4jppH9/gjPIsVjsHWFkQ6ukIzw4.")
//                .roles("USER")
//                .build();
//
//        log.info("userDetailsService............................");
//        log.info(user);
//
//        return new InMemoryUserDetailsManager(user);
//    }

    //시큐리티 필터체인을 반환형으로
    //HttpSecurity 객체인 http의 authorizeHttpRequests 메서드로 인가 절차를 수행하는데,
    //앤트 스타일 패턴으로 원하는 자원을 선택 -> AuthenticationManagerBuilder 객체인 auth를 이용해 선택
    //antMatchers() : 원하는 자원을 선택할 수 있다. -> 접근 제한 페이지 관리
    //hasRole() : 권한에 따라 접근 제한을 선택할 수 있다.
    //permitAll() : 권한에 관계없이 모든 접근을 허용한다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> {
            auth.antMatchers("/sample/all").permitAll();
            auth.antMatchers("/sample/member").hasRole("USER");
        });

        //인가/인증 절차에서 문제 발생시 권한 획득을 유도하는 페이지 리턴
        http.formLogin();
        //csrf 토큰 비활성화
        http.csrf().disable();
//        //스프링 시큐리티에서 제공하는 로그아웃 처리
//        http.logout();
        //oauth2 로그인을 위한 메서드 추가
        http.oauth2Login();

        return http.build();
    }

}
