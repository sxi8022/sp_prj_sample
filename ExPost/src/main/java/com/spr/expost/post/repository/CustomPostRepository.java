package com.spr.expost.post.repository;

import com.spr.expost.post.vo.Post;

public interface CustomPostRepository {

    // 좋아요
    void addLikeCount(Post post);

    // 싫어요
    void subLikeCount(Post post);

    // 조회수 올리기
    int addViewCount(Post post);
}
