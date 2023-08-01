package com.spr.expost.comment.controller;

import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.comment.dto.CommentLikeRequestDto;
import com.spr.expost.comment.service.CommentLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/posts/comments/likes")
public class CommentLikeController {

    private final CommentLikeService likeService;
    /**
     * 좋아요
     * */
    // 좋아요
    @PostMapping
    public ResponseEntity<ApiResponseDto> insert(@RequestBody @Valid CommentLikeRequestDto likeRequestDto) throws Exception {
        ResponseEntity<ApiResponseDto> result = likeService.insert(likeRequestDto);
        return result;
    }

    /**
     * 좋아요 취소
     * */
    @DeleteMapping
    public ResponseEntity<ApiResponseDto> delete(@RequestBody @Valid CommentLikeRequestDto likeRequestDto) {
        likeService.delete(likeRequestDto);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }
}
