package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false) => transactional 기본값이 롤백임으로 해당 설정을 통해 db에 반영되게 할 수 있음.
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("Jin");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush(); //강제로 flush 함으로 insert문 발생한 뒤 롤백 처리됨
       assertEquals(member, memberService.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복회원_검증() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("Jin");

        Member member2 = new Member();
        member2.setName("Jin");

        //when
        memberService.join(member1);
        memberService.join(member2);
//        try {
//            memberService.join(member2);//예외가 발생해야 됨.
//        } catch (IllegalStateException e){
//            return;
//        }
        //then
        fail("예외가 발생해야 된다.");
    }


}