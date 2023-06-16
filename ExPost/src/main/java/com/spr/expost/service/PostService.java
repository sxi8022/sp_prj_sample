package com.spr.expost.service;

import com.spr.expost.dto.PostDto;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.vo.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /*
    * 등록
    * */
    @Transactional
    public Long savePost(PostDto postDto) {
        return postRepository.save(postDto.toEntity()).getPostNo();
    }

    /*
    * 수정
    * */
    @Transactional
    public Long updatePost(PostDto postDto) {
        return postRepository.save(postDto.toUpdateEntity()).getPostNo();
    }

    /*
    * 삭제
    * */
    @Transactional
    public void deletePost(Long postNo) {
        postRepository.deleteById(postNo);
    }

    /*
    * 조회
    * */
    @Transactional
    public List<PostDto> getPostList() {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        List<PostDto> postDtoList = new ArrayList<>();

        for(Post post : postList) {
            PostDto postDto = PostDto.builder()
                    .postNo(post.getPostNo())
                    .author(post.getAuthor())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createDate(post.getCreateDate())
                    .build();
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    /*
     * 게시글 정보 얻기
     * */
    @Transactional
    public PostDto getPost(Long postNo) {
        Optional<Post> postWrapper = postRepository.findById(postNo);
        PostDto postDto = null;
        if (postWrapper.isPresent()) {
           Post post = postWrapper.get();

            postDto = PostDto.builder()
                    .postNo(post.getPostNo())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .author(post.getAuthor())
                    .postPassword(post.getPostPassword())
                    .createDate(post.getCreateDate())
                    .updateDate(post.getUpdateDate())
                    .build();
        }

        return  postDto;
    }

}
