package com.spr.expost.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String createDate;
    private String updateDate;
    private String username;
    private Long postId;
    private String postTitle;
    private int likeCount;

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}