package com.spr.expost.repository;

import com.spr.expost.vo.Comment;

public interface CustomCommentRepository {

    // 좋아요
    void addLikeCount(Comment comment);

    //싫어요
    void subLikeCount(Comment comment);
}
