package com.club.boot5.security.service;

import com.club.boot5.entity.ClubMember;
import com.club.boot5.repository.ClubMemberRepository;
import com.club.boot5.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
//@Service 어노테이션으로 자동으로 스프링에서 빈으로 처리
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    //UserDetailsService의 loadUserByUsername 메서드 오버라이딩
    //: 별도의 처리 없이 로그 기록
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername " + username);

        //이메일로 회원 찾을 경우 -> 이름과 소셜로그인 유무를 파라미터로 전달한다.
        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
        if (!result.isPresent()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }
        ClubMember clubMember = result.get();

        log.info("----------------");
        log.info(clubMember);

        //빌더 패턴을 사용하지 않는 이유 : 모든 속성이 꼭 필요하므로
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                //권한의 경우 한 가지의 권한만 가지지 않을 수 있으므로 스트림 이용
                clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority(
                                "ROLE_" + role.name()
                        )).collect(Collectors.toSet()));
        //DTO에 필요한 name, fromSocial Setter
        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }

}
