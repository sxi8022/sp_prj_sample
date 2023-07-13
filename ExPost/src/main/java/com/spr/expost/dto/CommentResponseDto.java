package com.spr.expost.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponseDto {

    private Long id;  //id
    private String content;  // 댓글 내용
    private LocalDateTime createDate; // 작성일시
    private LocalDateTime updateDate; // 수정일시
    private String username; // 사용자이름
    private Long postId;  // 게시글 id
    private String postTitle; // 게시글 제목
    private int likeCount;  // 좋아요 수
    private Long parentId; // 부모 댓글 id
    private List<CommentResponseDto> children; // 자식 댓글들


    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}