package com.club.boot5.repository;

import com.club.boot5.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String > {

    //회원 데이터 조회 테스트
    //@EntityGraph : 연관관계의 객체를 지연 로딩으로 조회할 경우, 객체 생성을 하고, 특정 데이터를 위해 새로운 객체를 생성해야 할때
    //fetch 조인을 사용하여 여러 번의 쿼리를 한 번에 해결하는 어노테이션
    // attributePaths 속성으로 연관관계의 객체에서 가져오고 싶은 데이터를 Set<ClubMemberRole> roleSet로 지정하고
    //

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.fromSocial=:social and m.email=:email")
    //사용자 이메일과 소셜로 추가된 회원 여부를 선택해서 동작하도록 설계 -> @EntityGraph를 이용해 외부조인으로 ClubMemberRole처리 되도록
    Optional<ClubMember> findByEmail(@Param("email") String email, @Param("social") boolean social);

}
