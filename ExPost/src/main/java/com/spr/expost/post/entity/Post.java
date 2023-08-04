package com.spr.expost.post.entity;

import com.spr.expost.comment.entity.Comment;
import com.spr.expost.common.entity.Timestamped;
import com.spr.expost.post.dto.PostRequestDto;
import com.spr.expost.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.Id;

// 게시글
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Post extends Timestamped {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*
    @Column(length = 10, nullable = false)
    private String author;
*/

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /*
    // 사용자 인증으로 대체
    @Column(length = 255, nullable = false)
    private String postPassword;
    */

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;

    // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
    @ManyToOne
    @JoinColumn(name = "user_id", updatable=false)
    private User user; // 작성자
/*
    컬럼 별도 생성 (다량의 데이터 조회 시 )
    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    private Integer likeCount;
*/

    @Column(name = "view_count")
    private Integer viewCount;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCategory> postCategoryList = new ArrayList<>();


    @Builder
    public Post(String title, String content, User user){
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}