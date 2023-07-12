package com.spr.expost.repository;

import com.spr.expost.vo.Post;
import com.spr.expost.vo.PostLike;
import com.spr.expost.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
