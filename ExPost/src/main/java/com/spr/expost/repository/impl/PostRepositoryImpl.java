package com.spr.expost.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spr.expost.repository.CustomPostRepository;
import com.spr.expost.vo.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.spr.expost.vo.QPost.post;


// 게시글 관리 커스텀 기능 repository
@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    // 좋아요 추가
    @Override
    public void addLikeCount(Post selectedPost) {
        queryFactory.update(post)
                .set(post.likeCount, post.likeCount.add(1))
                .where(post.eq(selectedPost))
                .execute();
    }

    // 좋아요 취소
    @Override
    public void subLikeCount(Post selectedPost) {
        queryFactory.update(post)
                .set(post.likeCount, post.likeCount.subtract(1))
                .where(post.eq(selectedPost))
                .execute();
    }

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
