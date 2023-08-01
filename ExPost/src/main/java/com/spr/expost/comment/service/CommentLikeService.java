package com.spr.expost.comment.service;

import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.comment.dto.CommentLikeRequestDto;
import com.spr.expost.comment.repository.CommentLikeRepository;
import com.spr.expost.comment.repository.CommentRepository;
import com.spr.expost.user.repository.UserRepository;
import com.spr.expost.comment.vo.Comment;
import com.spr.expost.comment.vo.CommentLike;
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
public class CommentLikeService {
    private final CommentLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성

    /*
     * 좋아요
     * */
    @Transactional
    public ResponseEntity<ApiResponseDto> insert(CommentLikeRequestDto commentLikeRequestDto) throws Exception {
        User user = userRepository.findById(commentLikeRequestDto.getUserId())
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.user",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        Comment comment = commentRepository.findById(commentLikeRequestDto.getCommentId())
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.comment",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        // 이미 좋아요되어있으면 에러 반환
        if (likeRepository.findByUserAndComment(user, comment).isPresent()) {
            //TODO 409에러로 변경
            return ResponseEntity.ok().body(new ApiResponseDto("이미 좋아요를 누른 글입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        CommentLike like = CommentLike.builder()
                .comment(comment)
                .user(user)
                .build();

        likeRepository.save(like);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }

    /*
     * 좋아요 취소
     * */
    @Transactional
    public void delete(CommentLikeRequestDto commentLikeRequestDto) {

        User user = userRepository.findById(commentLikeRequestDto.getUserId())
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.user",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        Comment comment = commentRepository.findById(commentLikeRequestDto.getCommentId())
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.comment",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );

        CommentLike like = (CommentLike) likeRepository.findByUserAndComment(user, comment)
                .orElseThrow(() -> new NullPointerException(
                                messagesource.getMessage(
                                        "not.found.commentLike",
                                        null,
                                        "Wrong post",
                                        Locale.getDefault() //기본언어 설정
                                )
                        )
                );
        
        likeRepository.delete(like);
    }
}
