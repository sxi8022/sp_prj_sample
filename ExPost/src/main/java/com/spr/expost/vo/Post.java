package com.spr.expost.vo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 게시글
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다. */
public class Post {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @Column(length = 10, nullable = false)
    private String author;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Column(length = 255, nullable = false)
    private String postPassword;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Post(Long postNo,String title, String content,  String author, String postPassword) {
        this.postNo = postNo;
        this.author = author;
        this.title = title;
        this.content = content;
        this.postPassword = postPassword;
    }

    public void setPostNo(Long postNo) {
        this.postNo = postNo;
    }

    @PrePersist
    protected void prePersist() {
        if (this.createDate == null) createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected  void preUpdate() {
        if (this.updateDate == null) updateDate = LocalDateTime.now();
    }

    public Long getPostNo() {
        return postNo;
    }
}