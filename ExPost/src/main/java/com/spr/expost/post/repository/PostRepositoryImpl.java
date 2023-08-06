package com.spr.expost.post.repository;

import static com.spr.expost.post.entity.QPost.post;
import static com.spr.expost.user.entity.QUser.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spr.expost.post.dto.PostResponseDto;
import com.spr.expost.post.entity.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

// 게시글 관리 커스텀 기능 repository
@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements CustomPostRepository {

  private final JPAQueryFactory queryFactory;

  /**
   * 조회수 증가
   * @param selectedPost
   * @return
   */
  @Override
  public int addViewCount(Post selectedPost) {
    // 조회수 올리기
    long result = queryFactory.update(post)
        .set(post.viewCount, selectedPost.getViewCount() + 1)
        .where(post.id.eq(selectedPost.getId()))
        .execute();
    return (int) result;
  }

  /**
   * 리스트 정렬
   * @param name
   * @param pageable
   * @return
   */
  @Override
  public Page<PostResponseDto> findByName(String name,
      Pageable pageable) {

    var query = queryFactory.select(post)
        .from(post)
        .leftJoin(post.user, user)
        .leftJoin(post.comments).fetchJoin()
        .where(
            post.title.contains(name)
                .and(post.user.isNotNull())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(postSort(pageable));

    var posts = query.fetch();

    List<PostResponseDto> responseList = posts.stream().map(v ->
        PostResponseDto.builder()
            .id(v.getId())
            .title(v.getTitle())
            .content(v.getContent())
            .updateDate(v.getUpdateDate())
            .likeCount(v.getPostLikes().size())
            .createDate(v.getCreateDate())
            .username(v.getUser().getUsername())
            .viewCount(v.getViewCount())
            .build()
    ).toList();

    return new PageImpl<>(responseList, pageable, posts.size());
  }

  /**
   * OrderSpecifier 를 쿼리로 반환하여 정렬조건을 맞춰준다.
   * 리스트 정렬
   * @param page
   * @return
   */
  private OrderSpecifier<?> postSort(Pageable page) {
    //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
    if (!page.getSort().isEmpty()) {
      //정렬값이 들어 있으면 for 사용하여 값을 가져온다
      for (Sort.Order order : page.getSort()) {
        // 서비스에서 넣어준 DESC or ASC 를 가져온다.
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
        switch (order.getProperty()){
          case "title":
            return new OrderSpecifier(direction, post.title);
          case "content":
            return new OrderSpecifier(direction, post.content);
          case "username":
            return new OrderSpecifier(direction, post.user.username);
          case "createDate":
            return new OrderSpecifier(direction, post.createDate);
        }
      }
    }
    return null;
  }

}
