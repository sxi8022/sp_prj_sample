package com.spr.expost.board;

import com.spr.expost.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        /*List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post post : postList) {
            PostDto postDto = PostDto.builder()
                    .id(post.getId())
                    .user(post.getUser())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createDate(post.getCreateDate())
                    .build();
            postDtoList.add(postDto);
        }

        JSONArray json = new JSONArray(postDtoList);
        System.out.println(json.toString());*/
    }

    /*
    * 특정 게시글 조회
    * */
    @Test
    void findByPostNo() {
      /*  Long id = 1L;
        Optional<Post> postWrapper = postRepository.findById(id);
        Post post = postWrapper.get();

        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(post.getUser())
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
                .build();

        JSONObject object = new JSONObject(postDto);
        System.out.println(object.toString());*/

    }

    /*
     * 등록
     *
     */
    @Test
    public void insert() {
     /*   String uuid = UUID.randomUUID().toString().substring(0,11);
        String title = "제목 " + uuid;
        String content = "내용 " + uuid ;
        //String author = "작성자" + ((int)(Math.random()*100) + 1);
       // String postPassword = "ko2457#";

        User user = new User("1","test", "test", UserRoleEnum.USER);
        PostDao dao = new PostDao();
        PostDto postDto = new PostDto(title, content, user);
        Post data = postRepository.save(dao.toEntity(postDto));
        Long dataKey = data.getId();
        System.out.println("결과값은 " + dataKey);*/
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
