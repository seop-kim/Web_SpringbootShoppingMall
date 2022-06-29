package com.shop.entity;

import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    public void Auditing테스트() {
        Member newMember = new Member();
        memberRepository.save(newMember);

        entityManager.flush();
        entityManager.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityExistsException::new);

        System.out.println("register time : " + member.getRegTime());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("create member : " + member.getCreatedBy());
        System.out.println("modify member : " + member.getModifiedBy());

    }
}