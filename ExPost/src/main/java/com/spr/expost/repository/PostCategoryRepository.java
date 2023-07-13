package com.spr.expost.repository;

import com.spr.expost.vo.Category;
import com.spr.expost.vo.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    List<PostCategory> findAllByCategory(Category category);
}