package com.spr.expost;

import com.spr.expost.dto.PostDto;
import com.spr.expost.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/board")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/postlist")
    @ResponseBody
    public String list() {
        List<PostDto> postDtoList = postService.getPostList();
        PostDto tempDto = new PostDto();
        // 전체 리스트 문자열로 출력
        List<String> toListString = tempDto.toListString(postDtoList);
        String sb = "";
        for (int i=0; i < toListString.size(); i++) {
           sb += (toListString.get(i) + "<br />");
        }
        return sb;
    }

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
    public String write(@RequestBody PostDto postDto) {
        PostDto dto = new PostDto();
        Long key =postService.savePost(postDto);
        String result = dto.toViewString(postDto);
        postDto.setPostNo(key);
        postDto.setCreateDate(LocalDateTime.now());
        return result;
        // return "redirect:/board/postlist";
    }

    /*
     * 수정
     * */
    @PostMapping("/post/update/{postNo}")
    public String update(@PathVariable("postNo") Long postNo, @RequestBody  PostDto postDto) {
        PostDto origin = postService.getPost(postNo);
        String resultStr = "";
        if(origin.getPostPassword().equals(postDto.getPostPassword())) {
            PostDto dto = new PostDto();
            Long key = postService.updatePost(postDto);
            postDto.setPostNo(key);
            postDto.setCreateDate(LocalDateTime.now());
            postDto.setUpdateDate(LocalDateTime.now());
            resultStr = dto.toViewString(postDto);
        } else {
            resultStr = "비밀번호가 올바르지 않습니다.";
        }
        return resultStr;
    }

    /*
    * 삭제
    * */
    @DeleteMapping("/post/delete/{postNo}")
    public String delete(@PathVariable("postNo") Long postNo, @RequestBody  PostDto postDto) {
        PostDto origin = postService.getPost(postNo);
        String resultStr = "";
        if(origin.getPostPassword().equals(postDto.getPostPassword())) {
            postService.deletePost(postNo);
            resultStr = "게시글을 삭제하였습니다.";
        } else {
            resultStr = "비밀번호가 올바르지 않습니다.";
        }

        return resultStr;
    }
    
}