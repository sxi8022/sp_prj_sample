package com.spr.expost.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spr.expost.dao.CommentDao;
import com.spr.expost.dto.CommentResponseDto;
import com.spr.expost.repository.CustomCommentRepository;
import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // 게시글의 댓글 전체 가져오기
    @Override
    public List<CommentResponseDto> findAllByPost(Post post){

        // 댓글 정보 dto로 변환에서 부모 댓글 리스트로 생성후 자식댓글들을 연결
        ArrayList<Comment> comments = (ArrayList<Comment>) queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(post.getId()).and(comment.parent.id.isNull()))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createDate.asc())
                .fetch();
        CommentDao dao = new CommentDao();
        List<CommentResponseDto> responseList = comments.stream().map(v->dao.ConvertToDtoWithParent(v)).toList();

        List<Comment> childComments = (ArrayList<Comment>) queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(post.getId()).and(comment.parent.id.isNotNull()))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createDate.asc())
                .fetch();

        List<CommentResponseDto> responseChildList = childComments.stream().map(v->dao.ConvertToDtoWithParent(v)).toList();

        responseList.stream()
        .forEach(parent -> {
            parent.setChildren(responseChildList.stream()
                    .filter(child -> child.getParentId().equals(parent.getId()))
                    .collect(Collectors.toList()));
        });
        return responseList;
    }
}
