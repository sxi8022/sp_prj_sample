package com.spr.expost.post.controller;

import com.spr.expost.post.dto.PostRequestDto;
import com.spr.expost.post.dto.PostResponseDto;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.post.service.PostServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/*
* 게시글
* */
@RestController
@RequestMapping("/boards")
public class PostController {
    private PostServiceImpl postService;

    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    /**
     * 조회
     * postName 값이 비어있으면 기존 조회 기능 수행
     * postName 값이 있으면 검색기능 수행
     * */
    @GetMapping("/postlist")
    @ResponseBody
    public ResponseEntity list(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @RequestParam(value="postName", required = false) String postName,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<PostResponseDto> postDtoList;
        if (postName != null && !postName.isEmpty()) {
            postDtoList = postService.getPostListByPostName(page-1, size, sortBy, isAsc, postName);

        } else {
            postDtoList = postService.getPostList(userDetails,
                page-1, size, sortBy, isAsc);
        }
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
     * 카테고리별 조회
     * */
    @GetMapping("/postlist/categories")
    public ResponseEntity listByCategory(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @RequestParam("categoryName") String categoryName
    ){
        Page<PostResponseDto> postDtoList = postService.getPostListByCategory(page-1, size, sortBy, isAsc, categoryName);
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
    * 상세 조회
    * */
    @GetMapping("/posts/{id}")
    @ResponseBody
    public ResponseEntity view(@PathVariable("id") Long id) {
        PostResponseDto responseDto = postService.getPost(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
    * 등록
    * */
    @PostMapping("/posts")
    public ResponseEntity write(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid PostRequestDto postDto) {
        PostResponseDto responseDto = postService.savePost(postDto, userDetails);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 수정
     * */
    @PutMapping("/posts/{id}")
    public ResponseEntity update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("id") Long id, @RequestBody @Valid PostRequestDto postRequestDto) {
        // 키값 dto에 추가
        postRequestDto.setId(id);
        PostResponseDto responseDto = postService.updatePost(postRequestDto, userDetails);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
    * 삭제
    * */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("id") Long id) {
        String result = "";

        int deleteResult = postService.deletePost(id, userDetails);
        if (deleteResult > 0) {
            result = "게시글을 삭제하였습니다.";
        } else {
            result = "삭제되지 않았습니다.";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}