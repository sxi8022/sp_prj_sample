package com.spr.expost.post.service;

import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.post.dto.PostLikeRequestDto;
import com.spr.expost.common.exception.PostNotFoundException;
import com.spr.expost.post.repository.PostLikeRepository;
import com.spr.expost.post.repository.PostRepository;
import com.spr.expost.user.repository.UserRepository;
import com.spr.expost.post.vo.Post;
import com.spr.expost.post.vo.PostLike;
import com.spr.expost.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;


@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성

    /*
     * 좋아요
     * */
    @Transactional
    public ResponseEntity<ApiResponseDto> insert(PostLikeRequestDto PostLikeRequestDto) throws Exception {

        User user = userRepository.findById(PostLikeRequestDto.getUserId())
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.user",
                                        null,
                                        "Wrong user",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        Post post = postRepository.findById(PostLikeRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(
                                messagesource.getMessage(
                                        "not.found.post",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        // 이미 좋아요되어있으면 에러 반환
        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            //TODO 409에러로 변경
            return ResponseEntity.ok().body(new ApiResponseDto("이미 좋아요를 누른 글입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        likeRepository.save(like);
        postRepository.addLikeCount(post); // CustomPostRepository 메소드를 상속받아 수행
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }

    /*
     * 좋아요 취소
     * */
    @Transactional
    public void delete(PostLikeRequestDto PostLikeRequestDto) {

        User user = userRepository.findById(PostLikeRequestDto.getUserId())
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.user",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        Post post = postRepository.findById(PostLikeRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(
                                messagesource.getMessage(
                                        "not.found.post",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        PostLike like = (PostLike) likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new PostNotFoundException(
                                messagesource.getMessage(
                                        "not.found.postLike",
                                        null,
                                        "Wrong postLike",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        likeRepository.delete(like);
        postRepository.subLikeCount(post); // 상속받은 클래스에서 수행
    }
}
