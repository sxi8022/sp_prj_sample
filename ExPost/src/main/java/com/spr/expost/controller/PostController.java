package com.spr.expost.controller;

import com.spr.expost.dto.PostDto;
import com.spr.expost.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/*
* 게시글
* */
@RestController
@RequestMapping("/api/board")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /*
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

    /*
    * 상세 조회
    * */
    @GetMapping("/post/{postNo}")
    @ResponseBody
    public String view(@PathVariable("postNo") Long postNo) {
        PostDto dto = postService.getPost(postNo);
        String result = dto.toViewString(dto);
        return result;
    }

    /*
    * 등록
    * */
    @PostMapping("/post")
    public String write(@RequestBody @Valid PostDto postDto) {
        PostDto dto = new PostDto();
        Long key =postService.savePost(postDto);
        PostDto resultDto = postService.getPost(key);
        String result = dto.toViewString(resultDto);
        return result;
    }

    /*
     * 수정
     * */
    @PutMapping("/post/update/{postNo}")
    public String update(@PathVariable("postNo") Long postNo, @RequestBody @Valid PostDto postDto) {
        PostDto origin = postService.getPost(postNo);
        String result = "";
        HashMap<String, String> resultMap = new HashMap<>();

        if (origin == null) {
            result = "수정할 게시글이 존재하지 않습니다.";
            return result;
        }

        if (checkPassword(origin.getPostPassword(), postDto.getPostPassword())) {
            postDto.setPostNo(postNo);
            resultMap = postService.updatePost(postDto);
            result = resultMap.get("result");
        } else {
            result = "비밀번호가 올바르지 않습니다.";
        }

        return result;
    }

    /*
    * 삭제
    * */
    @DeleteMapping("/post/delete/{postNo}")
    public String delete(@PathVariable("postNo") Long postNo, String password) {
        PostDto origin = postService.getPost(postNo);
        String result = "";

        if (origin == null) {
            result = "삭제할 게시글이 존재하지 않습니다.";
            return result;
        }

        if (checkPassword(origin.getPostPassword(), password)) {
            postService.deletePost(postNo);
            result = "게시글을 삭제하였습니다.";
        } else {
            result = "비밀번호가 올바르지 않습니다.";
        }
        return result;
    }

    /**패스워드 체크
     *
     * */
    public boolean checkPassword(String originPassword, String newPassword) {
        if (originPassword.equals(newPassword)) {
            return true;
        }
        return false;
    }
}