package com.spr.expost.repository;

import com.spr.expost.vo.Post;
import com.spr.expost.vo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 
 * */
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    Page<Post> findAllByUser(User user, Pageable pageable);
}
