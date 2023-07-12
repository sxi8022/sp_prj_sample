package com.spr.expost.repository;

import com.spr.expost.vo.Comment;
import com.spr.expost.vo.CommentLike;
import com.spr.expost.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}