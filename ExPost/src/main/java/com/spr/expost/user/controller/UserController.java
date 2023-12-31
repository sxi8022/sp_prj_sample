package com.spr.expost.user.controller;

import com.spr.expost.user.dto.SignupRequestDto;
import com.spr.expost.user.dto.UserRequestDto;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController  // 주소가 아닌 문자열로 그대로 리턴 가능함
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    @ResponseBody
    public HashMap<String, Object> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = userService.signup(signupRequestDto);

        return map;
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {

        return userService.logout(requestAccessToken);
    }

    /**
     * 재발급(유효기간 연장)
     */
    @PostMapping("/reissue")
    @ResponseBody
    public ResponseEntity<?> reissue(@Validated @RequestBody UserRequestDto.Reissue reissue) {

        return userService.reissue(reissue);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/leave")
    public ResponseEntity<String> leave(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.leave(userDetails.getUser());
    }


}
