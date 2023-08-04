package com.spr.expost.post.dto;

import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.post.entity.Post;
import com.spr.expost.post.entity.PostCategory;
import java.util.ArrayList;
import java.util.List;

public class PostConvert {


  // requestDto -> entity
  public Post toEntity(PostRequestDto postDto) {
    Post build = Post.builder()
        .title(postDto.getTitle())
        .content(postDto.getContent())
        .user(postDto.getUser())
        .build();
    return build;
  }

  // entity -> responseDto
  public PostResponseDto convertToDto(Post post) {
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

  public PostRequestDto convertToRequestDto(Post post) {
    List<PostCategory> postCategoryList = post.getPostCategoryList();
    List<String> categoryStringList = new ArrayList<>();
    for (PostCategory postCategory : postCategoryList) {
      categoryStringList.add(postCategory.getCategory().getName());
    }

    return PostRequestDto.builder()
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

  /*
   * commentList : 댓글 목록
   * postLikes : 좋아요 목록
   * */
  public PostResponseDto convertToDtoWithLists(Post post, List<CommentResponseDto> commentList) {
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

}
