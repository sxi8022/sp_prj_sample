package com.spr.expost.post.service;

import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.post.dto.PostLikeRequestDto;
import org.springframework.http.ResponseEntity;

// 게시글 좋아요
public interface PostLikeService {

  // 게시글 좋아요
  public ResponseEntity<ApiResponseDto> insert(PostLikeRequestDto PostLikeRequestDto);

  // 게시글 좋아요 취소
  public void delete(PostLikeRequestDto PostLikeRequestDto);
}
