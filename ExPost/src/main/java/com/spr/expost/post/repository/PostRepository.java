package com.spr.expost.post.repository;

import com.spr.expost.post.entity.Post;
import com.spr.expost.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 게시글 
 * */
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    // 사용자별 게시글 전체조회
    Page<Post> findAllByUser(User user, Pageable pageable);

    // 카테고리 페이지별 조회 (검색한카테고리에 해당하는 postId를 모아 검색)
    Page<Post> findAllByIdIn(List<Long> postIdList, Pageable pageable);

    User findPostById(Long postId);
}
