package com.spr.expost.post.controller;

import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.post.dto.PostLikeRequestDto;
import com.spr.expost.post.service.PostLikeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/posts/likes")
public class PostLikeController {
    private final PostLikeServiceImpl likeService;

    /**
     * 좋아요
     * */
    @PostMapping
    public ResponseEntity<ApiResponseDto> insert(@RequestBody @Valid PostLikeRequestDto likeRequestDto) throws Exception {
        ResponseEntity<ApiResponseDto> result = likeService.insert(likeRequestDto);
        return result;
    }

    /**
     * 좋아요 취소
     * */
    @DeleteMapping
    public ResponseEntity<ApiResponseDto> delete(@RequestBody @Valid PostLikeRequestDto likeRequestDto) {
        likeService.delete(likeRequestDto);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }
}
