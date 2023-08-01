package com.spr.expost.category.dao;

import com.spr.expost.category.dto.CategoryResponseDto;
import com.spr.expost.category.vo.Category;

public class CategoryDao {
    public CategoryResponseDto ConvertToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .build();
    }


}
