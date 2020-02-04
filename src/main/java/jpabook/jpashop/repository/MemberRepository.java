package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //PersistenceContext : 엔티티 매니저를 자동으로 주입해줌
    //PersistenceUnit ; 엔티티 매니저 팩토리를 주입해줌
    @Autowired //SpringBoot JPA가 제공함 (Autowired => PersistenceContext)
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
       Member findMember =  em.find(Member.class, id);

       return findMember;
    }

    public List<Member> findAll(){
        //전부 조회는 JPQL을 사용 => from의 대상이 테이블이 아닌 엔티티 객체를 대상으로 쿼리를 수행함.
        return em.createQuery("select m from MEMBER m", Member.class)
                .getResultList();
    }

    public List<Member> findName(String name){
        //:name을 통해 파라미터 name을 바인딩해줌.
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
