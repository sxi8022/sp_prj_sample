package com.spr.expost.post.dao;

import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.post.dto.PostRequestDto;
import com.spr.expost.post.dto.PostResponseDto;
import com.spr.expost.post.vo.Post;
import com.spr.expost.post.vo.PostCategory;

import java.util.ArrayList;
import java.util.List;

public class PostDao {
    public PostResponseDto ConvertToDto(Post post) {
        List<PostCategory> postCategoryList = post.getPostCategoryList();
        List<String> categoryStringList = new ArrayList<>();
        for (PostCategory postCategory : postCategoryList) {
            categoryStringList.add(postCategory.getCategory().getName());
        }

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updateDate(post.getUpdateDate())
                .createDate(post.getCreateDate())
                .likeCount(post.getPostLikes().size())
                .viewCount(post.getViewCount())
                .categories(categoryStringList)
                .build();
    }

    public PostResponseDto ConvertToDtoWithCategories(Post post, List<String> categories) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updateDate(post.getUpdateDate())
                .createDate(post.getCreateDate())
                .likeCount(post.getPostLikes().size())
                .viewCount(post.getViewCount())
                .categories(categories)
                .build();
    }

    /*
    * commentList : 댓글 목록
    * postLikes : 좋아요 목록
    * */
    public PostResponseDto ConvertToDtoWithLists(Post post, List<CommentResponseDto> commentList) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updateDate(post.getUpdateDate())
                .createDate(post.getCreateDate())
                .likeCount(post.getPostLikes().size())
                .viewCount(post.getViewCount())
                .comments(commentList)
                //.postLikes(postLikes)
                .build();
    }

    public Post toEntity(PostRequestDto postDto) {
        Post build = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .user(postDto.getUser())
                .build();
        return build;
    }
}
