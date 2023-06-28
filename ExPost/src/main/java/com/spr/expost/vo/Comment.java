package com.spr.expost.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comment")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment; // 댓글 내용


    @ManyToOne
    @JoinColumn(name = "post_no")
    private Post post;

    // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
    @ManyToOne
    @JoinColumn(name = "user_id", updatable=false)
    private User user; // 작성자

    @Override
    public LocalDateTime getUpdateDate() {
        return super.getUpdateDate();
    }

    @Override
    public LocalDateTime getCreateDate() {
        return super.getCreateDate();
    }
}

