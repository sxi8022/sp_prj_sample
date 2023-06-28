package com.spr.expost.dto;

import com.spr.expost.vo.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String comment;
    private String createDate;
    private String updateDate;
    private String username;
    private Long postsId;

    /* Entity -> Dto*/
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.createDate = String.valueOf(comment.getCreateDate());
        this.updateDate = String.valueOf(comment.getUpdateDate());
        this.username = comment.getUser().getUsername();
        this.postsId = comment.getPost().getPostNo();
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}