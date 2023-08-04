package com.spr.expost.comment.repository;

import com.spr.expost.comment.entity.Comment;
import com.spr.expost.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 댓글
 * */
public interface CommentRepository  extends JpaRepository<Comment, Long>, CustomCommentRepository {
    // Optional<Comment> findByUserId(Long userid);

    // id(게시글번호)에 따라 댓글조회
    List<Comment> findCommentsByPostOrderByCreateDateDesc(Post post);

}
