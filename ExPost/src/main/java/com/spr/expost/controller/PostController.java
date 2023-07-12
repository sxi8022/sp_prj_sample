package com.spr.expost.controller;

import com.spr.expost.dto.PostDto;
import com.spr.expost.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
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
    @GetMapping("/posts/{id}")
    @ResponseBody
    public String view(@PathVariable("id") Long id) {
        PostDto dto = postService.getPost(id);
        String result = dto.toViewString(dto);
        return result;
    }

    /*
    * 등록
    * */
    @PostMapping("/posts")
    public String write(@RequestBody @Valid PostDto postDto, HttpServletRequest request) {
        PostDto dto = new PostDto();
        Long key =postService.savePost(postDto, request);
        PostDto resultDto = postService.getPost(key);
        String result = dto.toViewString(resultDto);
        return result;
    }

    /*
     * 수정
     * */
    @PutMapping("/posts/{id}")
    public String update(@PathVariable("id") Long id, @RequestBody @Valid PostDto postDto, HttpServletRequest request) {
        PostDto origin = postService.getPost(id);
        String result = "";
        HashMap<String, String> resultMap = new HashMap<>();

        if (origin == null) {
            result = "수정할 게시글이 존재하지 않습니다.";
            return result;
        }

        if (checkPassword(origin.getPostPassword(), postDto.getPostPassword())) {
            postDto.setId(id);
            resultMap = postService.updatePost(postDto, request);
            result = resultMap.get("result");
        } else {
            result = "비밀번호가 올바르지 않습니다.";
        }

        return result;
    }

    /*
    * 삭제
    * */
    @DeleteMapping("/posts/{id}")
    public String delete(@PathVariable("id") Long id, String password, HttpServletRequest request) {
        PostDto origin = postService.getPost(id);
        String result = "";

        if (origin == null) {
            result = "삭제할 게시글이 존재하지 않습니다.";
            return result;
        }
        int deleteResult = 0;
        if (checkPassword(origin.getPostPassword(), password)) {
            deleteResult = postService.deletePost(id, request);
            if (deleteResult > 0) {
                result = "게시글을 삭제하였습니다.";
            } else if (deleteResult == -2) {
                result = "삭제하려는 게시글이 본인이 아니거나, 관리자가 아닙니다.";
            }  else {
                result = "삭제되지 않았습니다.";
            }
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