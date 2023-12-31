package com.spr.expost.post.repository;

import com.spr.expost.category.entity.Category;
import com.spr.expost.post.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    List<PostCategory> findAllByCategory(Category category);

    Optional<PostCategory> findByPostIdAndCategoryName(Long postId, String categoryName);
}