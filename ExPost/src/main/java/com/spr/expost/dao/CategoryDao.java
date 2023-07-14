package com.spr.expost.dao;

import com.spr.expost.dto.CategoryResponseDto;
import com.spr.expost.vo.Category;

public class CategoryDao {
    public CategoryResponseDto ConvertToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .build();
    }


}
