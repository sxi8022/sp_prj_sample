package com.spr.expost.post.repository;

import static com.spr.expost.post.entity.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spr.expost.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// 게시글 관리 커스텀 기능 repository
@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public int addViewCount(Post selectedPost) {
        // 조회수 올리기
        long result = queryFactory.update(post)
                .set(post.viewCount, selectedPost.getViewCount()+1)
                .where(post.id.eq(selectedPost.getId()))
                .execute();
        return (int) result;
    }
}
