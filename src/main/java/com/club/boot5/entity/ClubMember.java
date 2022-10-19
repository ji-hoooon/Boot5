package com.club.boot5.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity{
    @Id
    private String email;

    private String password;

    private String name;

    //소셜 로그인 여부
    private boolean fromSocial;


    //회원 권한을 위한 타입값 처리 -> @ElementCollection을 이용해 별도의 키 없이 컬렉션 데이터 처리한다.
    //: 한 회원이 하나의 권한만을 가지는 것이 아니기 때문에 Set 자료구조로 설정하고, PK가 없는 테이블로 설정

    @ElementCollection(fetch = FetchType.LAZY)
    //객체 생성시 기본값으로 가지게 설정
    @Builder.Default
    private Set<ClubMemberRole> roleSet=new HashSet<>();

    public void addMemberRole(ClubMemberRole clubMemberRole){
        roleSet.add(clubMemberRole);
    }
}
