package com.spr.expost.comment.repository;

import com.spr.expost.comment.entity.Comment;
import com.spr.expost.comment.entity.CommentLike;
import com.spr.expost.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    List<CommentLike> findByCommentId(Long id);
}