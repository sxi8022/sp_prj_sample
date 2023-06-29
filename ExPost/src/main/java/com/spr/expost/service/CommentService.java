package com.spr.expost.service;

import com.spr.expost.dto.CommentRequestDto;
import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.exception.ExtException;
import com.spr.expost.jwt.JwtUtil;
import com.spr.expost.repository.CommentRepository;
import com.spr.expost.repository.PostRepository;
import com.spr.expost.repository.UserRepository;
import com.spr.expost.util.CommonErrorCode;
import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import com.spr.expost.vo.User;
import com.spr.expost.vo.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postsRepository;
    private final JwtUtil jwtUtil;

    /* 댓글저장 */
    @Transactional
    public CommentResponseDto commentSave(Long id, CommentRequestDto dto, HttpServletRequest request) {
        User user = jwtUtil.checkToken(request);

        if (user == null) {
            throw new ExtException(CommonErrorCode.NOT_FOUND_USER, null);
        }

        /*
         * 게시글 있는지 확인
         * */
        Post post = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        dto.setUser(user);
        dto.setPost(post);

        Comment comment = dto.toEntity();
        CommentResponseDto responseDto = new CommentResponseDto(commentRepository.save(comment));

        return responseDto;
    }

    // 댓글 수정
    public CommentResponseDto updateComment(CommentRequestDto dto, Long id, HttpServletRequest request) {
        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);

        /*
         * 작성한 댓글이 존재하는지 확인.
         */
        Comment comment = this.checkValidComment(id);
        String createDate = String.valueOf(comment.getCreateDate());

        /*
        * 게시글 있는지 확인
        * */
        Post post = postsRepository.findById(comment.getPost().getPostNo()).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        dto.setUser(user);
        dto.setPost(post);
        dto.setId(comment.getId());

        Comment newComment = dto.toUpdateEntity();
        CommentResponseDto responseDto;
        /*
         * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, comment)) {
            responseDto = new CommentResponseDto(commentRepository.save(newComment));
            responseDto.setCreateDate(createDate);
        } else {
            responseDto = new CommentResponseDto(new Comment((long) -2, "수정하려는 댓글이 본인이 아니거나, 관리자가 아닙니다.", post, user));
            // throw new ExtException(CommonErrorCode.UNAUTHORIZED_USER, null);
        }
        return responseDto;
    }

    /**
     *  댓글이 있는지 확인
     */
    private Comment checkValidComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new ExtException(CommonErrorCode.NOT_FOUND_COMMENT, null)
        );
    }


    /**
     * 유효한 등록자인지 확인
     */
    private boolean checkValidUser(User user, Comment comment) {
        return !(
                !user.getUsername().equals(comment.getUser().getUsername())
                && !user.getRole().equals(UserRoleEnum.ADMIN)
        );
    }

    /**
     * 댓글 삭제
     */
    public int deleteComment(Long id, HttpServletRequest request) {
        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);

        /*
         * 작성한 댓글이 존재하는지 확인.
         */
        Optional<Comment> opComment = commentRepository.findById(id);
        if (opComment.isPresent()) {

            /*
             * 삭제하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
             *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
             */
            if (this.checkValidUser(user, opComment.get())) {
                commentRepository.delete(opComment.get());
                return 1;
            } else {
                return -2;
                // throw new ExtException(CommonErrorCode.UNAUTHORIZED_USER, null);
            }
        }
        return 0;
    }
}
