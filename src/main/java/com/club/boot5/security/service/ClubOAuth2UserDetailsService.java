package com.club.boot5.security.service;

import com.club.boot5.entity.ClubMember;
import com.club.boot5.entity.ClubMemberRole;
import com.club.boot5.repository.ClubMemberRepository;
import com.club.boot5.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
//@Service 어노테이션이 스프링의 빈으로 자동 등록
@Service
//소셜 로그인한 이메일을 이용해 회원 가입 처리하기 위해 의존성 추가
//: ClubmemberRepository, PasswordEncoder
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {
    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    //DefaultOAuth2UserService의 loadUser() 메서드 오버라이딩
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//                return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
//            }
//        }
//    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException{
        log.info("--------------");

        log.info("userRequest : "+ userRequest);
        //OAuth2UserRequest 객체

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName : "+ clientName); //google로 출력
        //additionalParameters () 메소드를 사용해 맵을 전달 하여 OAuth2AuthorizationRequest에 매개변수를 추가한다.
        //즉, 최대한 많은 정보를 얻기 위해 사용하는 메서드
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("===============");
        //키, 값으로 이루어진 Map<String, Object>형태로 데이터를 전달하므로
        oAuth2User.getAttributes().forEach((k,v)->{
            log.info(k+":"+v);
            //sub, pictrue, email, email_verified, EMAIL 출력
        });

        //회원가입된 이메일 정보가 없는걸로 간주
        String email = null;

        //oAuth2User의 이메일 정보 추출
        if(clientName.equals("Google")){
            email=oAuth2User.getAttribute("email");
        }
        log.info("EMAIL: "+ email);

        //먼저 OAuth2User 데이터를 ClubAuthMemberDTO로 전달하기 위한 작업 수행

//        //해당 이메일 정보를 이용해 소셜로그인한 정보를 이용해 회원가입처리
//        ClubMember member=saveSocialMember(email);
//        //return super.loadUser(userRequest);
//        return oAuth2User;

        ClubMember clubMember=saveSocialMember(email);
        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                true,
                clubMember.getRoleSet().stream().map(
                        role->new SimpleGrantedAuthority("ROLE_"+role.name())
                ).collect(Collectors.toList()),
                //oAuth2User의 인증정보를 제공한다.
                oAuth2User.getAttributes()
        );
        clubAuthMemberDTO.setName(clubMember.getName());
        return clubAuthMemberDTO;

        //OAuth2UserRequest 타입의 파라미터와 OAuth2User라는 타입의 리턴타입을 반환하는데
        //기존의 로그인 처리에 사용하던 파라미터와 리턴타입의 불일치 문제 발생
        //-> Map 자료구조를 통해 변환 수행
    }

    //소셜 로그인한 이메일을 이용해 회원가입 메서드 -> 리턴타입이 ClubMember로 작성해, 기존 데이터베이스에 인서트할 수 있도록
    private ClubMember saveSocialMember(String email){

        //기존에 동일한 이메일로 가입 여부 확인 -> 존재할 경우 조회만
        //: null확인을 할 수 있는 Optional로 작성 -> (이메일, 소셜이메일여부)
        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        //없다면 회원 추가 패스워드를 1111 / 이름은 이메일 주소
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        //권한 설정 후, 리포지토리에 엔티티 저장
        clubMember.addMemberRole(ClubMemberRole.USER);
        clubMemberRepository.save(clubMember);

        return clubMember;
    }
}
