package com.spr.expost.repository;

import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    Optional<Comment> findByUserId(Long userid);

    // postNo(게시글번호)에 따라 댓글조회
    List<Comment> findCommentsByPostOrderByCreateDateDesc(Post post);

}
