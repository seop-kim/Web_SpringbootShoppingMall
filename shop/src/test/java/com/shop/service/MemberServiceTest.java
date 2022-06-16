package com.shop.service;

import com.shop.dto.MemberFormDTO;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member 멤버생성() {
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setEmail("test@email.com");
        memberFormDTO.setName("홍길동");
        memberFormDTO.setAddress("서울시 용산구 청파로");
        memberFormDTO.setPassword("1234");
        return Member.createMember(memberFormDTO, passwordEncoder);
    }

    public void 정상저장확인(Member checkedMember, Member savedMember) { // 요청했던 값과 실제 저장된 값이 일치하는지 확인을 위한 메소드
        assertEquals(checkedMember.getEmail(), savedMember.getEmail());
        assertEquals(checkedMember.getName(), savedMember.getName());
        assertEquals(checkedMember.getAddress(), savedMember.getAddress());
        assertEquals(checkedMember.getPassword(), savedMember.getPassword());
        assertEquals(checkedMember.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void 회원가입테스트() {
        Member member = 멤버생성();
        Member savedMember = memberService.saveMember(member);
        정상저장확인(member, savedMember);
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void 중복회원가입테스트() {
        Member memberA = 멤버생성();
        Member memberB = 멤버생성();

        memberService.saveMember(memberA);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(memberB);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }

}