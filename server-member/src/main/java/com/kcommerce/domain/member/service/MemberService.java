package com.kcommerce.domain.member.service;

import com.kcommerce.domain.member.domain.Member;
import com.kcommerce.domain.member.dto.MemberDto;
import com.kcommerce.domain.member.mapper.MemberMapper;
import com.kcommerce.domain.member.repository.MemberRepository;
import com.kcommerce.global.error.exception.BusinessException;
import com.kcommerce.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MemberDto login(MemberDto.LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_CREDENTIALS));
        validatePassword(request.getPassword(), member.getPassword());
        return memberMapper.toDto(member);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BusinessException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    @Transactional
    public void join(MemberDto.JoinRequest request) {
        validateDuplicate(request.getUsername());
        Member member = memberMapper.toEntity(request);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        member.changePassword(encodedPassword);
        memberRepository.save(member);
    }

    private void validateDuplicate(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(member -> {
                    throw new BusinessException(ErrorCode.USERNAME_DUPLICATE);
                });
    }
}
