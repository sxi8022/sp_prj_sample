package com.spr.expost.service;

import com.spr.expost.dto.PostDto;
import com.spr.expost.exception.ExtException;
import com.spr.expost.jwt.JwtUtil;
import com.spr.expost.repository.CommentRepository;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.util.CommonErrorCode;
import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import com.spr.expost.vo.User;
import com.spr.expost.vo.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
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

    private final JwtUtil jwtUtil;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
    }

    /*
    * 등록
    * */
    @Transactional
    public Long savePost(PostDto postDto, HttpServletRequest request) {
        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);
        if (user == null) {
            throw new ExtException(CommonErrorCode.NOT_FOUND_USER, null);
        }
        postDto.setUser(user);

        return postRepository.save(postDto.toEntity()).getPostNo();
    }

    /*
    * 수정
    * */
    @Transactional
    public HashMap<String, String> updatePost(PostDto postDto, HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        Long key;

        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);
        if (user == null) {
            throw new ExtException(CommonErrorCode.NOT_FOUND_USER, null);
        }
        postDto.setUser(user);
        // 원래 정보
        Post origin = this.checkValidPost(postDto.getPostNo());

        /*
         * 수정하려고 하는 게시글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, origin)) {
            key = postRepository.save(postDto.toUpdateEntity()).getPostNo();
            map.put("key", String.valueOf(key));
            postDto.setPostNo(postDto.getPostNo());
            PostDto dto = new PostDto();
            PostDto resultDto = this.getPost(key);
            String result = dto.toViewString(resultDto);
            map.put("result", result);
        } else {
            map.put("key", "-2");
            map.put("result", "수정하려는 게시글이 본인이 아니거나, 관리자가 아닙니다.");
        }

        return map;
    }


    /**
     *  게시글이 있는지 확인
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
    public int deletePost(Long postNo, HttpServletRequest request) {
        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);
        if (user == null) {
            throw new ExtException(CommonErrorCode.NOT_FOUND_USER, null);
        }
        PostDto postDto = this.getPost(postNo);

        /*
         * 삭제하려고 하는 게시글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, postDto.toEntity())) {
            postRepository.deleteById(postNo);
            return 1;
        } else {
            return -2;
        }
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
                    .user(post.getUser())
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
                    .user(post.getUser())
                    .postPassword(post.getPostPassword())
                    .createDate(post.getCreateDate())
                    .updateDate(post.getUpdateDate())
                    .comments(commentList)
                    .build();
        }

        return  postDto;
    }

    /**
     * 유효한 등록자인지 확인
     */
    private boolean checkValidUser(User user, Post post) {
        return !(
                !user.getUsername().equals(post.getUser().getUsername())
                        && !user.getRole().equals(UserRoleEnum.ADMIN)
        );
    }


}
