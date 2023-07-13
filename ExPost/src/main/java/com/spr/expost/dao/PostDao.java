package com.spr.expost.dao;

import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.dto.PostDto;
import com.spr.expost.dto.PostResponseDto;
import com.spr.expost.vo.Post;
import com.spr.expost.vo.PostLike;

import java.util.List;

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
                // .categories(post.getPostCategoryList().get)
                .build();
    }

    /*
    * commentList : 댓글 목록
    * postLikes : 좋아요 목록
    * */
    public PostResponseDto ConvertToDtoWithLists(Post post, List<CommentResponseDto> commentList, List<PostLike> postLikes) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updateDate(post.getUpdateDate())
                .createDate(post.getCreateDate())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .comments(commentList)
                .postLikes(postLikes)
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
                .postLikes(postDto.getPostLikes())
                .build();
        return build;
    }

    public Post responseToEntity(PostResponseDto responseDto) {
        Post build = Post.builder()
                //.author(author)
                .title(responseDto.getTitle())
                .content(responseDto.getContent())
                //.postPassword(postPassword)
                .user(responseDto.getUser())
                .likeCount(responseDto.getLikeCount())
                .viewCount(responseDto.getViewCount())
                .postLikes(responseDto.getPostLikes())
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
                .postLikes(postDto.getPostLikes())
                .build();
        return build;
    }

    /*
    *  조회수 추가
    * */
    public PostDto addViewCount(PostDto postDto) {
        postDto.setViewCount(postDto.getViewCount() + 1);

       return postDto;
    }



}
