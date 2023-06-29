package com.spr.expost.board;

import com.spr.expost.dto.PostDto;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.vo.Post;
import com.spr.expost.vo.User;
import com.spr.expost.vo.UserRoleEnum;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class BoardTest {
    @Autowired
    PostRepository postRepository;

    /*
    * 모든 게시글 조회
    * 
    * */
    @Test
    void findAll() {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post post : postList) {
            PostDto postDto = PostDto.builder()
                    .postNo(post.getPostNo())
                    .user(post.getUser())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createDate(post.getCreateDate())
                    .build();
            postDtoList.add(postDto);
        }

        JSONArray json = new JSONArray(postDtoList);
        System.out.println(json.toString());
    }

    /*
    * 특정 게시글 조회
    * */
    @Test
    void findByPostNo() {
        Long postNo = 1L;
        Optional<Post> postWrapper = postRepository.findById(postNo);
        Post post = postWrapper.get();

        PostDto postDto = PostDto.builder()
                .postNo(post.getPostNo())
                .title(post.getTitle())
                .content(post.getContent())
                .user(post.getUser())
                .postPassword(post.getPostPassword())
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
                .build();

        JSONObject object = new JSONObject(postDto);
        System.out.println(object.toString());

    }

    /*
     * 등록
     *
     */
    @Test
    public void insert() {
        String uuid = UUID.randomUUID().toString().substring(0,11);
        String title = "제목 " + uuid;
        String content = "내용 " + uuid ;
        //String author = "작성자" + ((int)(Math.random()*100) + 1);
        String postPassword = "ko2457#";

        User user = new User("1","test", "test", UserRoleEnum.USER);

        PostDto post = new PostDto(title, content, user, postPassword);
        Post data = postRepository.save(post.toEntity());
        Long dataKey = data.getPostNo();
        System.out.println("결과값은 " + dataKey);
    }


    /*
    * 수정
    *
    * */
    public void update() {

    }

    /*
    *삭제
    *
    * */
    public void delete() {

    }

}
