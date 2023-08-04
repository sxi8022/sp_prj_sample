package com.spr.expost.user.service;

import com.spr.expost.user.dto.SignupRequestDto;
import com.spr.expost.user.dto.UserRequestDto;
import com.spr.expost.user.vo.User;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;

// 사용자 인증 및 관리 서비스
public interface UserService {

  // 회원가입
  public HashMap<String, Object> signup(SignupRequestDto requestDto);

  // 로그아웃
  public ResponseEntity<?> logout(String requestAccessToken);

  // 토큰재발급
  public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue);


  // 회원탈퇴
  public ResponseEntity<String> leave(User user);

}
