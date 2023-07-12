package com.spr.expost.repository;

import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 댓글
 * */
public interface CommentRepository  extends JpaRepository<Comment, Long> {
    // Optional<Comment> findByUserId(Long userid);

    // id(게시글번호)에 따라 댓글조회
    List<Comment> findCommentsByPostOrderByCreateDateDesc(Post post);

}
