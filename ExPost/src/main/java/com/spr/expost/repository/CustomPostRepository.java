package com.spr.expost.repository;

import com.spr.expost.vo.Post;

public interface CustomPostRepository {

    // 좋아요
    void addLikeCount(Post post);

    //싫어요
    void subLikeCount(Post post);
}
