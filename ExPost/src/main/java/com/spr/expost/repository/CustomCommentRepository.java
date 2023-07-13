package com.spr.expost.repository;

import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;

import java.util.List;

public interface CustomCommentRepository {

    // 좋아요
    void addLikeCount(Comment comment);

    //싫어요
    void subLikeCount(Comment comment);

    // 게시글의 댓글 찾기
    List<Comment> findAllByPost(Post post);
}
