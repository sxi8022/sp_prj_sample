package com.spr.expost.dto;

import com.spr.expost.vo.Comment;
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
    private Post post;

    /* Dto -> Entity */
    // 등록시 사용
    public Comment toEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .post(post)
                .build();

        return comments;
    }

    // 수정할때 사용할 엔티티 
    public Comment toUpdateEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .post(post)
                .build();

        return comments;
    }
}