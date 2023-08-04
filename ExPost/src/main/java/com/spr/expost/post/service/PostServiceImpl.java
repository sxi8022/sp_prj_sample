package com.spr.expost.post.service;

import com.spr.expost.category.repository.CategoryRepository;
import com.spr.expost.category.vo.Category;
import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.comment.repository.CommentRepository;
import com.spr.expost.exception.PostNotFoundException;
import com.spr.expost.jwt.JwtUtil;
import com.spr.expost.post.dto.PostConvert;
import com.spr.expost.post.dto.PostRequestDto;
import com.spr.expost.post.dto.PostResponseDto;
import com.spr.expost.post.repository.PostCategoryRepository;
import com.spr.expost.post.repository.PostLikeRepository;
import com.spr.expost.post.repository.PostRepository;
import com.spr.expost.post.vo.Post;
import com.spr.expost.post.vo.PostCategory;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.user.vo.User;
import com.spr.expost.user.vo.UserRoleEnum;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl implements PostService{


    private CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성
    private final PostLikeRepository postLikeRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;
    private PostRepository postRepository;

    public PostServiceImpl(CommentRepository commentRepository, JwtUtil jwtUtil, MessageSource messagesource, PostLikeRepository postLikeRepository, CategoryRepository categoryRepository, PostCategoryRepository postCategoryRepository, PostRepository postRepository) {
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
    public PostResponseDto savePost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
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

        requestDto.setUser(user);
        requestDto.setViewCount(0); // 조회수 최초 0
        PostConvert postConvert = new PostConvert();
        Post post = postConvert.toEntity(requestDto);
        // 자식테이블에서 저장 로직이 실행되면 불필요
        //Post post = postRepository.save(postConvert.toEntity(requestDto));
        // 카테고리가 없으면 카테고리 테이블에 신규생성, 있으면 생성하지 않음
        List<String> categories = requestDto.getCategories();
        for (String categoryName : categories) {
            Optional<Category> originCategory = categoryRepository.findByName(categoryName);
            if (originCategory.isEmpty()){
                postCategoryRepository.save(new PostCategory(post, categoryName));
            }
            else{
                postCategoryRepository.save(new PostCategory(post, originCategory.get()));
            }
        }

        PostResponseDto responseDto = postConvert.convertToDto(post);

        return responseDto;
    }

    /*
     * 게시글 정보 얻기
     * */
    @Transactional
    public PostResponseDto getPost(Long id) {
        Optional<Post> postWrapper = postRepository.findById(id);
        PostConvert postConvert = new PostConvert();
        if (postWrapper.isPresent()) {
            Post post = postWrapper.get();
            List<CommentResponseDto> commentList = commentRepository.findAllByPost(post);
            // dto로 리턴
            PostResponseDto responseDto = postConvert.convertToDtoWithLists(post, commentList);

            // 조회수 증가
            postRepository.addViewCount(post);
            responseDto.setViewCount(responseDto.getViewCount()+1);
            return responseDto;
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
    public Page<PostResponseDto> getPostList(UserDetailsImpl userDetails, int page, int size, String sortBy,
                                             boolean isAsc) {
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
        PostConvert postConvert = new PostConvert();

        if (userRoleEnum == UserRoleEnum.USER) {
            postList = postRepository.findAllByUser(user, pageable);
        } else {
            postList = postRepository.findAll(pageable);
        }
        Page<PostResponseDto> resultDtoList = postList.map(v -> postConvert.convertToDto(v));

        return resultDtoList;
    }

    /*
     * 카테고리별 조회
     * */
    public Page<PostResponseDto> getPostListByCategory(int page, int size, String sortBy, boolean isAsc, String categoryName) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Category vo = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException(messagesource.getMessage(
                                "not.found.category.post",
                                null,
                                "Wrong category",
                                Locale.getDefault() //기본언어 설정
                        )
                        )
                );
        PostConvert postConvert = new PostConvert();

        List<Long> postIdList = postCategoryRepository.findAllByCategory(vo)
                .stream().map(category -> category.getPost().getId()).toList();
        return postRepository.findAllByIdIn(postIdList, pageable).map(v->postConvert.convertToDto(v));
    }


    /*
     * 수정
     * */
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        // 수정 시 카테고리가 한건도없으면 시스템 에러
        List<Category> mainCategories = categoryRepository.findAll();
        if (mainCategories == null || mainCategories.size() == 0) {
            throw new IllegalArgumentException(
                    messagesource.getMessage(
                            "not.found.category.post.ask.manager",
                            null,
                            "category",
                            Locale.getDefault() //기본언어 설정
                    )
            );
        }

        PostConvert postConvert = new PostConvert();
        requestDto.setUser(user);
        // 원래 정보
        Post origin = this.checkValidPost(requestDto.getId());
        PostRequestDto originDto = postConvert.convertToRequestDto(origin);
        originDto.setContent(requestDto.getContent());
        originDto.setTitle(requestDto.getTitle());
        PostResponseDto responseDto;
        origin.update(originDto);
        responseDto = postConvert.convertToDto(origin);

        return responseDto;
    }


    /*
     * 삭제
     * */
    @Transactional
    public int deletePost(Long id, UserDetailsImpl userDetails) {
        Post post = checkValidPost(id);

        postRepository.deleteById(id);
        return 1;
    }


    /**
     * 게시글이 있는지 확인
     */
    public Post checkValidPost(Long id) {
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


}
