package com.club.boot5.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
//DTO 역할 수행 + 스프링 시큐리티에서 인가/인증
public class ClubAuthMemberDTO extends User implements OAuth2User {
    private String email;
    private String name;
    private boolean fromSocial;
    private Map<String, Object> attr;


    //소셜 로그인으로 인한 OAuth2User를 ClubAuthMemberDTO로 변환하기 위한 생성자
    public ClubAuthMemberDTO(String username, String password,
                             boolean fromSocial, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {


        this(username,password,fromSocial,authorities);
        this.attr=attr;
    }
    @Override
    public Map<String, Object> getAttributes(){
        return this.attr;
    }
    //기존 로그인을 통한 인증을 위한 생성자
    public ClubAuthMemberDTO(String username, String password,
                             boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
        //email -> username
        //name -> name
        //fromSocial -> fromSocial
        //password는 부모 클래스 사용하므로 변수로 선언하지 않는다.

        //따라서, email과 fromSocial은 별도로 setter 작성

        //부모의 클래스에 사용자 정의 생성자가 존재하기 때문에 반드시 별도로 호출해야한다.
        super(username, password, authorities);

        this.email=username;
        this.fromSocial=fromSocial;

    }
}
