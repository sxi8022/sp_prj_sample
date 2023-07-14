package com.spr.expost.repository;

import com.spr.expost.vo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
   // Optional<CategoryResponseDto> findByName(String name);

   // Optional<Category> findCategoryByName(String name);
    Optional<Category> findByName(String name);

}