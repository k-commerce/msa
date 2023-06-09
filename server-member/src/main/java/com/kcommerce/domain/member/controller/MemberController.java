package com.kcommerce.domain.member.controller;

import com.kcommerce.domain.member.dto.MemberDto;
import com.kcommerce.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/auth")
    public ResponseEntity<MemberDto> login(@RequestBody @Valid MemberDto.LoginRequest request) {
        MemberDto memberDto = memberService.login(request);
        return ResponseEntity.ok()
                .header("X-Authorization-Id", String.valueOf(memberDto.getId()))
                .body(memberDto);
    }

    @PostMapping("/api/members")
    public ResponseEntity<Void> join(@RequestBody @Valid MemberDto.JoinRequest request) {
        memberService.join(request);
        return ResponseEntity.ok().build();
    }
}
