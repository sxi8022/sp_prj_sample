package com.spr.expost.repository;

import com.spr.expost.vo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 
 * */
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

}
