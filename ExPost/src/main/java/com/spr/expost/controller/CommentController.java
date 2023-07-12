package com.spr.expost.controller;

import com.spr.expost.dto.ApiResponseDto;
import com.spr.expost.dto.CommentRequestDto;
import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * 댓글
 * */
@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class CommentController {
    private final CommentService commentService;

    /* 댓글 생성  */
    // id는 게시글 id
    @PostMapping("/post/{id}/comment")
    public ResponseEntity<CommentResponseDto> commentSave(@PathVariable Long id, @RequestBody CommentRequestDto dto, HttpServletRequest request) {
        CommentResponseDto responseDto = commentService.commentSave(id, dto, request);
        if (responseDto.getId() > 0 && responseDto != null) {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  댓글 수정
     */
    @PutMapping("/post/comment/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        CommentResponseDto responseDto = commentService.updateComment(commentRequestDto, id, request);
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
    @DeleteMapping("/post/comment/{id}")
    public ApiResponseDto deleteComment(@PathVariable Long id, HttpServletRequest request) {
        ApiResponseDto result = commentService.deleteComment(id, request);
        return result;
    }


}
