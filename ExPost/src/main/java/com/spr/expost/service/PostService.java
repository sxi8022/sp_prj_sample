package com.spr.expost.service;

import com.spr.expost.dto.PostDto;
import com.spr.expost.exception.ExtException;
import com.spr.expost.repository.CommentRepository;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.util.CommonErrorCode;
import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;

    private CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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
    public HashMap<String, String> updatePost(PostDto postDto) {
        HashMap<String, String> map = new HashMap<>();
        Long key;




        key = postRepository.save(postDto.toUpdateEntity()).getPostNo();
        map.put("key", String.valueOf(key));
        postDto.setPostNo(postDto.getPostNo());
        PostDto dto = new PostDto();
        PostDto resultDto = this.getPost(key);
        String result = dto.toViewString(resultDto);
        map.put("result", result);
        return map;
    }


    /**
     *  댓글이 있는지 확인
     */
    private Post checkValidPost(Long postNo) {
        return postRepository.findById(postNo).orElseThrow(
                () -> new ExtException(CommonErrorCode.NOT_FOUND_POST, null)
        );
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
            List<Comment> commentList = commentRepository.findCommentsByPostOrderByCreateDateDesc(post);

            postDto = PostDto.builder()
                    .postNo(post.getPostNo())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .author(post.getAuthor())
                    .postPassword(post.getPostPassword())
                    .createDate(post.getCreateDate())
                    .updateDate(post.getUpdateDate())
                    .comments(commentList)
                    .build();
        }

        return  postDto;
    }

}
