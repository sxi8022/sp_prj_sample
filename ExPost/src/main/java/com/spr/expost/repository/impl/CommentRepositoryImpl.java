package com.spr.expost.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spr.expost.repository.CustomCommentRepository;
import com.spr.expost.vo.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.spr.expost.vo.QComment.comment;

// 댓글 커스텀 기능 repository
@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    // 좋아요
    @Override
    public void addLikeCount(Comment selectedComment) {
        queryFactory.update(comment)
                .set(comment.likeCount, comment.likeCount.add(1))
                .where(comment.eq(selectedComment))
                .execute();
    }

    // 좋아요 취소
    @Override
    public void subLikeCount(Comment selectedComment) {
        queryFactory.update(comment)
                .set(comment.likeCount, comment.likeCount.subtract(1))
                .where(comment.eq(selectedComment))
                .execute();
    }
}
