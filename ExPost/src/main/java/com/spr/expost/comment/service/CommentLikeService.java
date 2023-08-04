package com.spr.expost.comment.service;

import com.spr.expost.comment.dto.CommentLikeRequestDto;
import com.spr.expost.common.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;

// 댓글 좋아요
public interface CommentLikeService {

  // 좋아요
  public ResponseEntity<ApiResponseDto> insert(CommentLikeRequestDto commentLikeRequestDto);

  // 좋아요 취소
  public void delete(CommentLikeRequestDto commentLikeRequestDto);

}
