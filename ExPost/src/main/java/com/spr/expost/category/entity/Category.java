package com.spr.expost.category.entity;

import com.spr.expost.post.entity.PostCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<PostCategory> postCategoryList = new ArrayList<>();

    public Category(String name){
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name =name;
    }
}