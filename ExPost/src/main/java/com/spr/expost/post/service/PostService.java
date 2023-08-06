package com.spr.expost.post.service;

import com.spr.expost.post.dto.PostRequestDto;
import com.spr.expost.post.dto.PostResponseDto;
import com.spr.expost.post.entity.Post;
import com.spr.expost.security.UserDetailsImpl;
import org.springframework.data.domain.Page;

// 게시글 작성
public interface PostService {

  // 게시글 저장
  public PostResponseDto savePost(PostRequestDto requestDto, UserDetailsImpl userDetails);

  // 게시글 상세조회
  public PostResponseDto getPost(Long id);

  // 게시글 목록
  public Page<PostResponseDto> getPostList(UserDetailsImpl userDetails, int page, int size, String sortBy,
      boolean isAsc);

  // 게시글 카테고리별 조회
  public Page<PostResponseDto> getPostListByCategory(int page, int size, String sortBy, boolean isAsc, String categoryName);

  // 게시글 수정
  public PostResponseDto updatePost(PostRequestDto requestDto, UserDetailsImpl userDetails);

  // 게시글 삭제
  public int deletePost(Long id, UserDetailsImpl userDetails);

  // 게시글 1건 조회
  public Post checkValidPost(Long id);

  // 게시글 이름으로 검색
  Page<PostResponseDto> getPostListByPostName(int i, int size, String sortBy, boolean isAsc, String postName);
}
