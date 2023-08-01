package com.spr.expost.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeRequestDto {
    private Long userId;
    private Long postId;

    public PostLikeRequestDto(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
