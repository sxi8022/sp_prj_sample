package com.spr.expost.post.repository;

import com.spr.expost.post.entity.Post;
import com.spr.expost.post.entity.PostLike;
import com.spr.expost.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

    List<PostLike> findAllByPostId(Long id);
}
