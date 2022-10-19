package com.club.boot5.repository;

import com.club.boot5.entity.ClubMember;
import com.club.boot5.entity.ClubMemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){
        //1-80 USER
        //81-90 USER,MANAGER
        //91-100, USER,MANAGER,ADMIN
        IntStream.rangeClosed(1,100).forEach(i->{
            ClubMember clubMember=ClubMember.builder()
                    .email("user"+i+"@naver.com")
                    .name("사용자"+i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            //기본 권한
            clubMember.addMemberRole(ClubMemberRole.USER);
            if(i>80){
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }

            if(i>90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            clubMemberRepository.save(clubMember);
        });
    }

    //일반 로그인 사용자와 소셜 로그인 사용자 구분 메서드 테스트
    @Test
    public void testRead(){
        //Optional형은 기본적으로 null 체크를 하기 위해 주로 사용하는데, isPresent()메서드를 이용해 null일 때 처리를 가능하게 해준다.
        Optional<ClubMember> result = clubMemberRepository.findByEmail("user95@naver.com", false);

        ClubMember clubMember = result.get();
        System.out.println(clubMember);
    }// : 외부조인으로 처리하면서 권한도 함께 로딩한다.


}
