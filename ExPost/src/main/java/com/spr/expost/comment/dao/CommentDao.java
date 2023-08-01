package com.spr.expost.comment.dao;

import com.spr.expost.comment.dto.CommentRequestDto;
import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.comment.vo.Comment;

import java.util.ArrayList;

public class CommentDao {

    /* Dto -> Entity */
    // 등록시 사용
    public CommentResponseDto ConvertToDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .updateDate(comment.getUpdateDate())
                .createDate(comment.getCreateDate())
                .postId(comment.getPost().getId())
                .postTitle(comment.getPost().getTitle())
                .likeCount(comment.getLikeCount())
                .username(comment.getUser().getUsername())
                .build();
    }

    /* Dto -> Entity */
    // 부모댓글이 있을때 사용
    public CommentResponseDto ConvertToDtoWithParent(Comment comment) {
        CommentDao dao = new CommentDao();
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .updateDate(comment.getUpdateDate())
                .createDate(comment.getCreateDate())
                .postId(comment.getPost().getId())
                .postTitle(comment.getPost().getTitle())
                .likeCount(comment.getLikeCount())
                .username(comment.getUser().getUsername())
                .parentId(comment.getParent() == null ? 0 : comment.getParent().getId())
                .children(comment.getChildren() == null ? new ArrayList<CommentResponseDto>() : comment.getChildren().stream().map(v -> dao.ConvertToDto(v)).toList())
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
                .commentLikes(dto.getCommentLikes())
                .build();

        return comments;
    }


}
