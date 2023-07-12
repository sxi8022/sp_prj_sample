package com.spr.expost.service;

import com.spr.expost.dao.CommentDao;
import com.spr.expost.dto.ApiResponseDto;
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
import org.springframework.http.HttpStatus;
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
        // 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
        // 댓글 저장
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setLikeCount(0); // 처음에 좋아요는 0

        Comment newComment = commentRepository.save(comment);
        CommentDao dao = new CommentDao();
        CommentResponseDto responseDto = dao.ConvertToDto(newComment);

        return responseDto;
    }

    // 댓글 수정
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long id, HttpServletRequest request) {
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
        Post post = postsRepository.findById(comment.getPost().getId()).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        CommentResponseDto responseDto;
        CommentDao dao = new CommentDao();
        /*
         * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         *  checkValid 결과 true 면 데이터리턴 아니면 예외 발생
         */
        if (this.checkValidUser(user, comment.getUser())) {
            requestDto.setUser(user);
            requestDto.setPost(post);
            requestDto.setId(comment.getId());

            Comment newComment = dao.toUpdateEntity(requestDto);
            responseDto = dao.ConvertToDto(commentRepository.save(newComment));
            responseDto.setCreateDate(createDate);
        } else {
            responseDto = dao.ConvertToDto(new Comment((long) -2, "수정하려는 댓글이 본인이 아니거나, 관리자가 아닙니다.", post, user, -1));
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
    private boolean checkValidUser(User user, User commentUser) {
        return !(
                !user.getUsername().equals(commentUser.getUsername())
                && !user.getRole().equals(UserRoleEnum.ADMIN)
        );
    }

    /**
     * 댓글 삭제
     */
    public ApiResponseDto deleteComment(Long id, HttpServletRequest request) {
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
            if (this.checkValidUser(user, opComment.get().getUser())) {
                commentRepository.delete(opComment.get());
                return ApiResponseDto.builder()
                        .msg("success")
                        .statusCode(HttpStatus.OK.value())
                        .build();
            } else {
                return ApiResponseDto.builder()
                        .msg("삭제하려는 댓글이 본인이 아니거나, 관리자가 아닙니다.")
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .build();
            }
        } else {
            return ApiResponseDto.builder()
                    .msg("삭제하려는 댓글이 존재하지 않습니다.")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }
}
