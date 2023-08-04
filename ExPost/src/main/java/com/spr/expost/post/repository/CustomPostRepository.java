package com.spr.expost.post.repository;

import com.spr.expost.post.entity.Post;

public interface CustomPostRepository {

    // 조회수 올리기
    int addViewCount(Post post);
}
