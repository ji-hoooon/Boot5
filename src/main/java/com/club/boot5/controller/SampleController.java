package com.club.boot5.controller;

import com.club.boot5.security.dto.ClubAuthMemberDTO;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    @GetMapping("/all")
    public void exAll(){
        log.info("exAll..........");
    }

    @GetMapping("/member")
    //@AuthenticationPrincipal 로그인한 사용자의 정보를 파라미터로 받고 싶을때 사용하는 어노테이션
    //UserDetailsService에서 인가된 사용자 정보를 리턴 받아서 컨트롤러에 출력 -> Object 타입의 반환타입으로 캐스팅없이 출력 가능
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){

        log.info("exMember.........");

        log.info("--------------------");

        log.info(clubAuthMember);
    }

    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin..........");
    }
}
