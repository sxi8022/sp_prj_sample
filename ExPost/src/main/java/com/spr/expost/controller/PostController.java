package com.spr.expost.controller;

import com.spr.expost.dto.PostDto;
import com.spr.expost.dto.PostResponseDto;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* 게시글
* */
@RestController
@RequestMapping("/boards")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 조회
     * */
    @GetMapping("/postlist")
    @ResponseBody
    public String list() {
        List<PostDto> postDtoList = postService.getPostList();
        PostDto tempDto = new PostDto();
        // 전체 리스트 문자열로 출력
        List<String> toListString = tempDto.toListString(postDtoList);
        String sb = "";
        for (int i=0; i < toListString.size(); i++) {
           sb += (toListString.get(i) + "\n");
        }
        return sb;
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
    public ResponseEntity write(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid PostDto postDto) {
        PostResponseDto responseDto = postService.savePost(postDto, userDetails);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 수정
     * */
    @PutMapping("/posts/{id}")
    public ResponseEntity update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("id") Long id, @RequestBody @Valid PostDto postDto) {
        // 키값 dto에 추가
        postDto.setId(id);
        PostResponseDto responseDto = postService.updatePost(postDto, userDetails);

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
        } else if (deleteResult == -2) {
            result = "삭제하려는 게시글이 본인이 아니거나, 관리자가 아닙니다.";
        }  else {
            result = "삭제되지 않았습니다.";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*패스워드 체크(사용안함 , 로그인 유저 체크로 역할 변경)
     *
     * */
/*    public boolean checkPassword(String originPassword, String newPassword) {
        if (originPassword.equals(newPassword)) {
            return true;
        }
        return false;
    }*/
}