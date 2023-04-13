package com.kcommerce.domain.member.service;

import com.kcommerce.domain.member.domain.Member;
import com.kcommerce.domain.member.dto.MemberDto;
import com.kcommerce.domain.member.mapper.MemberMapper;
import com.kcommerce.domain.member.repository.MemberRepository;
import com.kcommerce.global.error.exception.BusinessException;
import com.kcommerce.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberDto login(MemberDto.LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_CREDENTIALS));
        validatePassword(member, request);
        return memberMapper.toDto(member);
    }

    private void validatePassword(Member member, MemberDto.LoginRequest request) {
        if (!Objects.equals(member.getPassword(), request.getPassword())) {
            throw new BusinessException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    @Transactional
    public void join(MemberDto.JoinRequest request) {
        validateDuplicate(request.getUsername());
        Member member = memberMapper.toEntity(request);
        memberRepository.save(member);
    }

    private void validateDuplicate(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(member -> {
                    throw new BusinessException(ErrorCode.USERNAME_DUPLICATE);
                });
    }
}
