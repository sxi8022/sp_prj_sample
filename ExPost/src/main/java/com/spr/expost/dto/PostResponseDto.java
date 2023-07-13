package com.spr.expost.dto;

import com.spr.expost.vo.PostLike;
import com.spr.expost.vo.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponseDto {

    private Long id;
    @Length(max = 100, message = "제목은 100자 이하여야합니다.")
    private String title;
    private String content;

    // private String postPassword;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<CommentResponseDto> comments;

    private User user;

    private Integer likeCount; //좋아요
    private Integer viewCount; // 조회수

    private List<PostLike> postLikes; // 좋아요 목록
}
