package com.spr.expost.comment.repository;

import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.post.vo.Post;
import java.util.List;

public interface CustomCommentRepository {

    // 게시글의 댓글 찾기
    List<CommentResponseDto> findAllByPost(Post post);
}
