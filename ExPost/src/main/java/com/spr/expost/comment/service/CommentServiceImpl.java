package com.spr.expost.comment.service;

import com.spr.expost.comment.dto.CommentRequestDto;
import com.spr.expost.comment.dto.CommentResponseDto;
import com.spr.expost.comment.repository.CommentLikeRepository;
import com.spr.expost.comment.repository.CommentRepository;
import com.spr.expost.comment.vo.Comment;
import com.spr.expost.comment.vo.CommentLike;
import com.spr.expost.common.dto.ApiResponseDto;
import com.spr.expost.exception.PostNotFoundException;
import com.spr.expost.post.repository.PostRepository;
import com.spr.expost.post.vo.Post;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.user.repository.UserRepository;
import com.spr.expost.user.vo.User;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postsRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성

    /* 댓글저장 */
    @Transactional
    public CommentResponseDto commentSave(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (user == null) {
            throw new NullPointerException(messagesource.getMessage(
                    "not.found.user",
                    null,
                    "Wrong user",
                    Locale.getDefault() //기본언어 설정
            ));
        }

        /*
         * 게시글 있는지 확인
         * */
        Post post = postsRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException(
                        messagesource.getMessage(
                                "not.found.post",
                                null,
                                "Wrong post",
                                Locale.getDefault() //기본언어 설정
                        )
                )
        );
        requestDto.setPost(post); // post 객체 주입

        Comment parent = null;
        // 자식댓글인 경우
        if (requestDto.getParentId() != null && requestDto.getParentId() != 0) {
            parent = this.checkValidComment(requestDto.getParentId());
            if (null == parent) {
                throw new NullPointerException(
                        messagesource.getMessage(
                                "not.found.comment",
                                null,
                                "Wrong comment",
                                Locale.getDefault() //기본언어 설정
                        )
                );
            }
            // 부모댓글의 게시글 번호와 자식댓글의 게시글 번호 같은지 체크하기
            if (parent.getPost().getId() != requestDto.getPost().getId()) {
                throw new IllegalArgumentException(
                        messagesource.getMessage(
                                "not.comment.correct.with.parent.child",
                                null,
                                "Wrong comment",
                                Locale.getDefault() //기본언어 설정
                        )
                );
            }
        }


        // 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
        // 댓글 저장
        Comment comment = new Comment();
        comment.setContent(requestDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setLikeCount(0); // 처음에 좋아요는 0
        if (null != parent) {
            comment.updateParent(parent);
        }

        Comment newComment = commentRepository.save(comment);

        CommentResponseDto responseDto = null;
        if (parent != null) {
            responseDto = requestDto.ConvertToDtoWithParent(newComment);
        } else {
            responseDto = requestDto.ConvertToDto(newComment);
        }

        return responseDto;
    }

    // 댓글 수정
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long id, UserDetailsImpl userDetails) {
        /*
         * 로그인 검증.
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

        /*
         * 작성한 댓글이 존재하는지 확인.
         */
        Comment comment = this.checkValidComment(id);
        String createDate = String.valueOf(comment.getCreateDate());

        /*
         * 게시글 있는지 확인
         * */
        Post post = postsRepository.findById(comment.getPost().getId()).orElseThrow(() ->
                new PostNotFoundException(
                        messagesource.getMessage(
                                "not.found.post",
                                null,
                                "Wrong post",
                                Locale.getDefault() //기본언어 설정
                        )
                )
        );

        CommentResponseDto responseDto;
        /*
         * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        requestDto.setUser(user);
        requestDto.setPost(post);
        requestDto.setId(comment.getId());
        List<CommentLike> commentLikes = commentLikeRepository.findByCommentId(comment.getId());
        requestDto.setCommentLikes(commentLikes);
        Comment newComment = requestDto.toUpdateEntity(requestDto);
        Comment resultComm = commentRepository.save(newComment);
        responseDto = requestDto.ConvertToDto(resultComm);
        return responseDto;
    }

    /**
     * 댓글이 있는지 확인
     */
    public Comment checkValidComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException(
                        messagesource.getMessage(
                                "not.found.comment",
                                null,
                                "Wrong post",
                                Locale.getDefault() //기본언어 설정
                        )
                )
        );
    }


    /**
     * 댓글 삭제
     */
    public ApiResponseDto deleteComment(Long id, UserDetailsImpl userDetails) {
        /*
         * 로그인 검증.
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

        /*
         * 작성한 댓글이 존재하는지 확인.
         */
        Optional<Comment> opComment = commentRepository.findById(id);

        if (opComment.isPresent()) {
            Comment deleteComment = opComment.get();
            commentRepository.delete(deleteComment);
            return ApiResponseDto.builder()
                .msg(messagesource.getMessage(
                    "success.comment.delete",
                    null,
                    "Wrong comment",
                    Locale.getDefault() //기본언어 설정
                ))
                .statusCode(HttpStatus.OK.value())
                .build();
        } else {
            return ApiResponseDto.builder()
                    .msg(messagesource.getMessage(
                            "not.comment.delete.exist",
                            null,
                            "Wrong comment",
                            Locale.getDefault() //기본언어 설정
                    ))
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }
}
