package com.spr.expost.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeRequestDto {
    private Long userId;
    private Long commentId;

    public CommentLikeRequestDto(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}