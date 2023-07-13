package com.spr.expost.service;

import com.spr.expost.dao.CommentDao;
import com.spr.expost.dao.PostDao;
import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.dto.PostDto;
import com.spr.expost.dto.PostResponseDto;
import com.spr.expost.exception.PostNotFoundException;
import com.spr.expost.jwt.JwtUtil;
import com.spr.expost.repository.CommentRepository;
import com.spr.expost.repository.PostLikeRepository;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.vo.*;
import jakarta.transaction.Transactional;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;

    private CommentRepository commentRepository;

    private final JwtUtil jwtUtil;

    private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성


    private final PostLikeRepository postLikeRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, JwtUtil jwtUtil, MessageSource messagesource, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
        this.messagesource = messagesource;
        this.postLikeRepository = postLikeRepository;
    }

    /*
    * 등록
    * */
    @Transactional
    public PostResponseDto savePost(PostDto postDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        /*
         * 로그인 검증
         */
        if (user == null) {
            throw new NullPointerException(messagesource.getMessage(
                    "not.found.user",
                    null,
                    "Wrong user",
                    Locale.getDefault() //기본언어 설정
            ));
        }

        PostDao dao = new PostDao();
        postDto.setUser(user);
        postDto.setLikeCount(0); // 좋아요 최초 0
        postDto.setViewCount(0); // 조회수 최초 0
        postDto.setPostLikes(new ArrayList<PostLike>());
        Post post =  postRepository.save(dao.toEntity(postDto));
        PostResponseDto responseDto = dao.ConvertToDto(post);
        return responseDto;
    }

    /*
    * 수정
    * */
    @Transactional
    public PostResponseDto updatePost(PostDto postDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        /*
         * 로그인 검증
         */
        if (user == null) {
            throw new NullPointerException(messagesource.getMessage(
                    "not.found.user",
                    null,
                    "Wrong user",
                    Locale.getDefault() //기본언어 설정
            ));
        }

        PostDao dao = new PostDao();
        postDto.setUser(user);
        // 원래 정보
        Post origin = this.checkValidPost(postDto.getId());
        // 요청할때는 좋아요 조회수를 가져오지않으므로 DB 에서 확인
        postDto.setLikeCount(origin.getLikeCount());
        postDto.setViewCount(origin.getViewCount());
        // 좋아요 테이블 값 가져오기
        List<PostLike> postLikes = postLikeRepository.findByPostId(postDto.getId());
        postDto.setPostLikes(postLikes);
        /*
         * 수정하려고 하는 게시글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        PostResponseDto responseDto;
        if (this.checkValidUser(user, origin.getUser())) {
          Post result = postRepository.save(dao.toUpdateEntity(postDto));
            responseDto = dao.ConvertToDto(result);
        } else {
            throw new IllegalArgumentException("삭제하려는 댓글이 본인이 아니거나, 관리자가 아닙니다.");
        }

        return responseDto;
    }


    /**
     *  게시글이 있는지 확인
     */
    private Post checkValidPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException(
                        messagesource.getMessage(
                                "not.found.post",
                                null,
                                "Wrong post",
                                Locale.getDefault() //기본언어 설정
                        )
                )
        );
    }

    /*
    * 삭제
    * */
    @Transactional
    public int deletePost(Long id, UserDetailsImpl userDetails) {
        /*
         * 로그인 검증
         */
        User user = userDetails.getUser();

        if (user == null) {
            throw new NullPointerException(messagesource.getMessage(
                    "not.found.user",
                    null,
                    "Wrong user",
                    Locale.getDefault() //기본언어 설정
            ));
        }

        PostDao dao = new PostDao();
        Post post = postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException(
                        messagesource.getMessage(
                                "not.found.post",
                                null,
                                "Wrong post",
                                Locale.getDefault() //기본언어 설정
                        )
                )
        );

        /*
         * 삭제하려고 하는 게시글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, post.getUser())) {
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
    public PostResponseDto getPost(Long id) {
        Optional<Post> postWrapper = postRepository.findById(id);
        PostResponseDto postResponseDto = null;
        PostDao dao = new PostDao();
        if (postWrapper.isPresent()) {
           Post post = postWrapper.get();
           List<Comment> commentList = commentRepository.findCommentsByPostOrderByCreateDateDesc(post);
            List<CommentResponseDto> commDtoList = new ArrayList<>();
            CommentDao commDao = new CommentDao();
            // dto로 치환
            for (Comment comment: commentList) {
                CommentResponseDto resDto = commDao.ConvertToDto(comment);
                commDtoList.add(resDto);
            }
           // List<PostLike> postLikes = postLikeRepository.findByPostId(id);
           postResponseDto = dao.ConvertToDtoWithLists(post, commDtoList);
        } else {
           throw new PostNotFoundException(
                   messagesource.getMessage(
                           "not.found.post",
                           null,
                           "Wrong post",
                           Locale.getDefault() //기본언어 설정
                   )
           );
        }

        return  postResponseDto;
    }

    /**
     * 유효한 등록자인지 확인
     */
    private boolean checkValidUser(User user, User targetUser) {
        return !(
                !user.getUsername().equals(targetUser.getUsername())
                        && !user.getRole().equals(UserRoleEnum.ADMIN)
        );
    }


}
