package com.spr.expost.aop;


import com.spr.expost.comment.dto.CommentRequestDto;
import com.spr.expost.post.dto.PostRequestDto;
import com.spr.expost.post.service.PostServiceImpl;
import com.spr.expost.post.vo.Post;
import com.spr.expost.security.UserDetailsImpl;
import com.spr.expost.user.vo.User;
import com.spr.expost.user.vo.UserRoleEnum;
import java.util.Locale;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RoleCheckAop")
@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckEvent {

  private final MessageSource messagesource; // MessageSource  포로퍼티 값을 자동으로 읽어와 bean 생성

  private final PostServiceImpl postService;


  @Pointcut("execution(* com.spr.expost.post.service.PostServiceImpl.updatePost(..))")
  private void updatePost() {}

  @Pointcut("execution(* com.spr.expost.post.service.PostServiceImpl.deletePost(..))")
  private void deletePost() {}

  @Pointcut("execution(* com.spr.expost.comment.service.CommentServiceImpl.updateComment(..))")
  private void updateComment() {}

  @Pointcut("execution(* com.spr.expost.comment.service.CommentServiceImpl.deleteComment(..))")
  private void deleteComment() {}


  @Around("updatePost() || deletePost()")
  public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
    // 첫번째 매개변수로 게시글 받아옴
    PostRequestDto postDto = (PostRequestDto) joinPoint.getArgs()[0];

    Post origin = postService.checkValidPost(postDto.getId());

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
      // 로그인 회원 정보
      UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
      User loginUser = userDetails.getUser();

      // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
      if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || origin.getUser().getUserId().equals(loginUser.getUserId()))) {
        throw new RejectedExecutionException(
            messagesource.getMessage(
                "not.auth.post.updateAndDelete.writer",
                null,
                "Wrong post",
                Locale.getDefault() //기본언어 설정
            )
        );
      }
    }

    // 핵심기능 수행
    return joinPoint.proceed();
  }

  @Around("updateComment() || deleteComment()")
  public Object executeCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
    // 첫번째 매개변수로 게시글 받아옴
    CommentRequestDto comment = (CommentRequestDto)joinPoint.getArgs()[0];

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
      // 로그인 회원 정보
      UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
      User loginUser = userDetails.getUser();

      // 댓글 작성자(comment.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
      if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().equals(loginUser))) {
        throw new RejectedExecutionException(
            messagesource.getMessage(
                "not.auth.comment.updateAndDelete.writer",
                null,
                "Wrong post",
                Locale.getDefault() //기본언어 설정
            )
        );

      }
    }

    // 핵심기능 수행
    return joinPoint.proceed();
  }
}
