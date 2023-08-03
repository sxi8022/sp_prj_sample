package com.spr.expost.comment.dto;

import com.spr.expost.comment.vo.Comment;
import com.spr.expost.comment.vo.CommentLike;
import com.spr.expost.post.vo.Post;
import com.spr.expost.user.vo.User;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Long parentId; // 부모글 id

    private List<CommentLike> commentLikes;

    public void setCommentLikes(List<CommentLike> commentLikes) {
        this.commentLikes = commentLikes;
    }



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
            .children(comment.getChildren() == null ? new ArrayList<CommentResponseDto>() : comment.getChildren().stream().map(v -> ConvertToDto(v)).toList())
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