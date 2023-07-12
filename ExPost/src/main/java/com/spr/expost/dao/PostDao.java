package com.spr.expost.dao;

import com.spr.expost.dto.PostDto;
import com.spr.expost.dto.PostResponseDto;
import com.spr.expost.vo.Post;

public class PostDao {
    public PostResponseDto ConvertToDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updateDate(post.getUpdateDate())
                .createDate(post.getCreateDate())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .build();
    }

    public Post toEntity(PostDto postDto) {
        Post build = Post.builder()
                //.author(author)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                //.postPassword(postPassword)
                .user(postDto.getUser())
                .likeCount(postDto.getLikeCount())
                .viewCount(postDto.getViewCount())
                .build();
        return build;
    }

    public Post toUpdateEntity(PostDto postDto) {
        Post build = Post.builder()
                .id(postDto.getId())
                //.author(author)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .likeCount(postDto.getLikeCount())
                .viewCount(postDto.getViewCount())
                .user(postDto.getUser())
                .build();
        return build;
    }


}
