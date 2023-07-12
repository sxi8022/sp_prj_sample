package com.spr.expost.dao;

import com.spr.expost.dto.CommentRequestDto;
import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.vo.Comment;

public class CommentDao {

    /* Dto -> Entity */
    // 등록시 사용
    public CommentResponseDto ConvertToDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .updateDate(String.valueOf(comment.getUpdateDate()))
                .createDate(String.valueOf(comment.getCreateDate()))
                .postId(comment.getPost().getId())
                .postTitle(comment.getPost().getTitle())
                .likeCount(comment.getLikeCount())
                .build();
    }

    // 수정할때 사용할 엔티티
    public Comment toUpdateEntity(CommentRequestDto dto) {
        Comment comments = Comment.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .user(dto.getUser())
                .post(dto.getPost())
                .likeCount(dto.getLikeCount())
                .build();

        return comments;
    }


}
