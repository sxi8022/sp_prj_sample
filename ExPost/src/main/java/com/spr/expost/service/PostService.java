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
        postDto.setLikeCount(0); // 좋아요 최초 0
        postDto.setViewCount(0); // 조회수 최초 0

        return postRepository.save(postDto.toEntity()).getId();
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
        Post origin = this.checkValidPost(postDto.getId());
        // 요청할때는 좋아요 조회수를 가져오지않으므로 DB 에서 확인
        postDto.setLikeCount(origin.getLikeCount());
        postDto.setViewCount(origin.getViewCount());
        /*
         * 수정하려고 하는 게시글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, origin)) {
            key = postRepository.save(postDto.toUpdateEntity()).getId();
            map.put("key", String.valueOf(key));
            postDto.setId(postDto.getId());
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
    private Post checkValidPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new ExtException(CommonErrorCode.NOT_FOUND_POST, null)
        );
    }

    /*
    * 삭제
    * */
    @Transactional
    public int deletePost(Long id, HttpServletRequest request) {
        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);
        if (user == null) {
            throw new ExtException(CommonErrorCode.NOT_FOUND_USER, null);
        }
        PostDto postDto = this.getPost(id);

        /*
         * 삭제하려고 하는 게시글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, postDto.toEntity())) {
            postRepository.deleteById(id);
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
                    .id(post.getId())
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
    public PostDto getPost(Long id) {
        Optional<Post> postWrapper = postRepository.findById(id);
        PostDto postDto = null;
        if (postWrapper.isPresent()) {
           Post post = postWrapper.get();
            List<Comment> commentList = commentRepository.findCommentsByPostOrderByCreateDateDesc(post);

            postDto = PostDto.builder()
                    .id(post.getId())
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
