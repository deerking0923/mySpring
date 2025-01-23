package com.springboot.ezenbackend;

import org.hibernate.annotations.TimeZoneStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.ezenbackend.domain.Member;
import com.springboot.ezenbackend.domain.MemberRole;
import com.springboot.ezenbackend.repository.MemberRepository;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Test
    // public void testInsertMember() {
    // for (int i = 0; i < 10; i++) {
    // Member member = Member.builder()
    // .email("user" + i + "@aaa.com").password(passwordEncoder.encode("1234"))
    // .nickname("USER" + i).build();
    // member.addRole(MemberRole.USER);

    // if (i >= 5)
    // member.addRole(MemberRole.MANAGER);
    // if (i >= 8)
    // member.addRole(MemberRole.ADMIN);

    // memberRepository.save(member);
    // }
    // }

    @Test
    public void testRead() {
        String email = "user0@aaa.com";
        Member member = memberRepository.getWithRoles(email);
        System.out.println(member);
    }
}
