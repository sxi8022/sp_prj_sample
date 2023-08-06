package com.spr.expost.post.repository;

import com.spr.expost.post.dto.PostResponseDto;
import com.spr.expost.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    // 조회수 올리기
    int addViewCount(Post post);

    //게시글 검색
    Page<PostResponseDto> findByName(String name, Pageable pageable);
}
