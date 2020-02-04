package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor
@RequiredArgsConstructor //final로 선언된 값만 생성인젝션으로 세팅해줌 (권장!)
//JPA는 반드시 트랜젝션 안에서 데이터 변경이 이뤄져야
public class MemberService {

    //필드인젝션
//    @Autowired
    private final MemberRepository memberRepository;
    //생성자 인젝션(필드와 세터인젝션의 장점만 모았음)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //setter인젝션 (장점: 테스트 코드 작성때 mockBean을 통해 단위테스트 진행 가능, 단점: runtime시점에 누군가 값을 변경할 수 있음)
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }


    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member){
        //중복 회원 검
        validateDuplicatedMember(member);

        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        //WAS의 경우 멀티 쓰레드환경 또는 여러 사용자의 웹 접근때문에 동시에 회원 등록을 시도하는 경우
        //비즈니스 로직을 통한 벨리데이션 체크를 하더라도 동일한 회원 정보가 삽입될 수 있음으로
        //DB에 name을 유닉크한 값으로 설정하는 것을 권장함.

        //id는 매번 새로운 값으로 생성됨으로 이름으로 검증
        List<Member> findMembers = memberRepository.findName(member.getName());

        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //회원 1건 조회
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }
}
