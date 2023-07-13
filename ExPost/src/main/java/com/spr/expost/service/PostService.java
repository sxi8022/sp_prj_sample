package com.spr.expost.service;

import com.spr.expost.dao.CommentDao;
import com.spr.expost.dao.PostDao;
import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.dto.PostDto;
import com.spr.expost.dto.PostResponseDto;
import com.spr.expost.exception.PostNotFoundException;
import com.spr.expost.jwt.JwtUtil;
import com.spr.expost.repository.*;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.vo.*;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PostService {


    private CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성
    private final PostLikeRepository postLikeRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;
    private PostRepository postRepository;

    public PostService(CommentRepository commentRepository, JwtUtil jwtUtil, MessageSource messagesource, PostLikeRepository postLikeRepository, CategoryRepository categoryRepository, PostCategoryRepository postCategoryRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
        this.messagesource = messagesource;
        this.postLikeRepository = postLikeRepository;
        this.categoryRepository = categoryRepository;
        this.postCategoryRepository = postCategoryRepository;
        this.postRepository = postRepository;
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
        Post post = postRepository.save(dao.toEntity(postDto));
        List<String> categories = postDto.getCategories();
        // 게시글별 카테고리  테이블에 추가
        for (String categoryName : categories) {
            Category category = new Category(categoryName);
            Optional<Category> existCategory = categoryRepository.findByName(categoryName);
            if (existCategory.isEmpty()) {
                postCategoryRepository.save(new PostCategory(post, category));
            } else {
                postCategoryRepository.save(new PostCategory(post, existCategory.get()));
            }
        }
        PostResponseDto responseDto = dao.ConvertToDto(post);
        return responseDto;
    }

    /*
     * 게시글 정보 얻기
     * */
    @Transactional
    public PostResponseDto getPost (Long id) {
        Optional<Post> postWrapper = postRepository.findById(id);
        PostResponseDto postResponseDto;
        PostDao dao = new PostDao();
        if (postWrapper.isPresent()) {
            Post post = postWrapper.get();
            List<Comment> commentList = commentRepository.findAllByPost(post);
            List<CommentResponseDto> commDtoList = new ArrayList<>();
            CommentDao commDao = new CommentDao();
            Map<Long, CommentResponseDto> map = new HashMap<>();

            // dto로 치환
            for (Comment item : commentList) {
                CommentResponseDto resDto = commDao.ConvertToDtoWithParent(item);
                if (item.getParent() == null) {
                    commDtoList.add(resDto);
                }
            }

            // 좋아요 테이블 값 가져오기
            List<PostLike> postLikes = postLikeRepository.findByPostId(post.getId());
            PostResponseDto dto = dao.ConvertToDto(post);
            // dto.setComments(commDtoList);
            // dto.setPostLikes(postLikes);
            return dto;

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
    }

    /*
     * 조회
     * */
    @Transactional
    public Page<PostResponseDto> getPostList (UserDetailsImpl userDetails,int page, int size, String sortBy,
                                              boolean isAsc){
        User user = userDetails.getUser();

        if (user == null) {
            throw new NullPointerException(messagesource.getMessage(
                    "not.found.user",
                    null,
                    "Wrong user",
                    Locale.getDefault() //기본언어 설정
            ));
        }

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        UserRoleEnum userRoleEnum = user.getRole();
        Page<Post> postList;

        if (userRoleEnum == UserRoleEnum.USER) {
            postList = postRepository.findAllByUser(user, pageable);
        } else {
            postList = postRepository.findAll(pageable);
        }
        PostDao dao = new PostDao();
        Page<PostResponseDto> resultDtoList = postList.map(v -> dao.ConvertToDto(v));

        return resultDtoList;
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
            Post vo = dao.toUpdateEntity(postDto);
            List<String> categories = postDto.getCategories();
            // 게시글별 카테고리  테이블에 추가
            for (String categoryName : categories) {
                Category category = new Category(categoryName);
                Optional<Category> existCategory = categoryRepository.findByName(categoryName);
                if (existCategory.isEmpty()) {
                    postCategoryRepository.save(new PostCategory(vo, category));
                } else {
                    postCategoryRepository.save(new PostCategory(vo, existCategory.get()));
                }
            }
            Post result = postRepository.save(vo);
            responseDto = dao.ConvertToDto(result);
        } else {
            throw new IllegalArgumentException(
                    messagesource.getMessage(
                            "not.auth.post.writer",
                            null,
                            "Wrong post",
                            Locale.getDefault() //기본언어 설정
                    )
            );
        }
        return responseDto;
    }


    /*
     * 삭제
     * */
    @Transactional
    public int deletePost (Long id, UserDetailsImpl userDetails){
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




    /**
     * 게시글이 있는지 확인
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

    /**
     * 유효한 등록자인지 확인
     */
    private boolean checkValidUser(User user, User user2) {
        return !(
                !user.getUsername().equals(user2.getUsername())
                        && !user.getRole().equals(UserRoleEnum.ADMIN)
        );
    }


}
