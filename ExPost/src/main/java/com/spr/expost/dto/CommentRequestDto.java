package com.spr.expost.dto;

import com.spr.expost.vo.Post;
import com.spr.expost.vo.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private Long id;
    private String content;
    private String createDate;
    private String updateDate;
    private User user;
    private Post post;  // 게시글
    private int likeCount; //좋아요 최초 0으로 입력
}