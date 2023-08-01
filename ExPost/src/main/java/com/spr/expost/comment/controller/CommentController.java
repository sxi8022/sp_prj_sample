package com.spr.expost.comment.controller;

import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.comment.dto.CommentRequestDto;
import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/*
 * 댓글
 * */
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class CommentController {
    private final CommentService commentService;

    /**
     *  댓글 등록
     */
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<CommentResponseDto> commentSave(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto dto) {
        CommentResponseDto responseDto = commentService.commentSave(id, dto, userDetails);
        if (responseDto.getId() > 0 && responseDto != null) {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  댓글 수정
     */
    @PutMapping("/posts/comments/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto responseDto = commentService.updateComment(commentRequestDto, id, userDetails);
        if (responseDto.getId() > 0 && responseDto != null) {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else if (responseDto.getId() == -2){
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     *  댓글 삭제
     */
    @DeleteMapping("/posts/comments/{id}")
    public ApiResponseDto deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        ApiResponseDto result = commentService.deleteComment(id, userDetails);
        return result;
    }


}
