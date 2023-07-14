package com.spr.expost.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Category category;

    public PostCategory(Post post, Category category){
        this.post = post;
        this.category = category;
    }

    public PostCategory(Post post, String name){
        this.post = post;
        this.category = new Category(name);
    }

}
