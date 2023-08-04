package com.spr.expost.comment.service;

import com.spr.expost.comment.dto.CommentRequestDto;
import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.comment.vo.Comment;
import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.security.UserDetailsImpl;

// 댓글 작성
public interface CommentService {

  //  댓글 저장
  public CommentResponseDto commentSave(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails);

  // 댓글 수정
  public CommentResponseDto updateComment(CommentRequestDto requestDto, Long id, UserDetailsImpl userDetails);

  // 댓글 상세 조회
  public Comment checkValidComment(Long commentId);

  // 댓글 삭제
  public ApiResponseDto deleteComment(Long id, UserDetailsImpl userDetails);

}
