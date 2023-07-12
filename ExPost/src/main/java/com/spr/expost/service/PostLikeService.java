package com.spr.expost.service;

import com.spr.expost.dto.ApiResponseDto;
import com.spr.expost.dto.PostLikeRequestDto;
import com.spr.expost.repository.PostLikeRepository;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.repository.UserRepository;
import com.spr.expost.vo.Post;
import com.spr.expost.vo.PostLike;
import com.spr.expost.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "좋아요를 누른 사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(PostLikeRequestDto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "좋아요를 누른 사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(PostLikeRequestDto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        PostLike like = (PostLike) likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "좋아요를 누른 기록을 찾을 수 없습니다."));

        likeRepository.delete(like);
        postRepository.subLikeCount(post); // 상속받은 클래스에서 수행
    }
}
