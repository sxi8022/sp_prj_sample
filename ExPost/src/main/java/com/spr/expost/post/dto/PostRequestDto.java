package com.spr.expost.post.dto;

import com.spr.expost.user.vo.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@Builder
public class PostRequestDto {
    private Long id;
    @Length(max = 100, message = "제목은 100자 이하여야합니다.")
    private String title;
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private User user;
    private Integer likeCount; //좋아요 수
    private Integer viewCount; // 조회수
    private List<String> categories;
}
