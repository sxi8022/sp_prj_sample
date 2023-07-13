package com.spr.expost.dto;

import com.spr.expost.vo.Comment;
import com.spr.expost.vo.PostLike;
import com.spr.expost.vo.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDto {
    private Long id;
    @Length(max = 100, message = "제목은 100자 이하여야합니다.")
    private String title;
    private String content;
/*    @Length(max = 10, message = "작성자는 10자 이하여야합니다.")
    private String author;*/
    // @Length(max = 255, message = "비밀번호는 255자 이하여야합니다.")
    //private String postPassword;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<Comment> comments;

    private User user;

    private Integer likeCount; //좋아요 수
    private Integer viewCount; // 조회수

    private List<PostLike> postLikes; // 좋아요 목록


    private List<String> categories;

    public void setPostLikes(List<PostLike> postLikes) {
        this.postLikes = postLikes;
    }

    @Builder
    public PostDto(Long id, String title, String content, User user, LocalDateTime createDate, LocalDateTime updateDate, List<Comment> comments, int likeCount, int viewCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.comments = comments;
        this.user = user;
        this.likeCount =likeCount;
        this.viewCount = viewCount;
    }

    // 등록
    public PostDto(String title, String content, User user) {
        this.title = title;
        this.content = content;
    }

    //전체내용 표시
    public List<String> toListString(List<PostDto> dtoList) {
        List<String> listString = new ArrayList<>();
        for (int i = 0; i <dtoList.size(); i ++) {
            listString.add(
                   "글번호 = " + dtoList.get(i).getId()
                     + " {" +
                            "제목='" + dtoList.get(i).getTitle() + '\'' +
                            ", 작성자명='" + dtoList.get(i).getUser().getUsername() + '\'' +
                            ", 작성내용='" + dtoList.get(i).getContent() + '\'' +
                            ", 작성날짜=" + dtoList.get(i).getCreateDate() +
                            '}'

            );
        }

        return listString;
    }

    //상세내용 표시
    public String toViewString(PostDto dto) {
        String nowDate = "";
        StringBuilder sb = new StringBuilder();

        try {
            // 수정일자가 있으면 수정일자 우선출력
            if (dto.getUpdateDate() != null) {
                nowDate = dto.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                nowDate =  dto.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            sb.append("상세정보 {" +
                    "제목='" + dto.getTitle() + '\'' +
                    ", 작성자명='" + dto.getUser().getUsername() + '\'' +
                    ", 작성내용='" + dto.getContent() + '\'' +
                    ", 작성날짜=" + nowDate +
                    '}');
            // 게시글에 따라 댓글 조회
            if (dto.comments!= null && !dto.comments.isEmpty()) {
               for (int i = 0 ; i < dto.comments.size(); i ++) {
                   sb.append("\n");
                   sb.append("댓글 :" + dto.comments.get(i).getContent() + " ");
                   sb.append("작성일시 :" + dto.comments.get(i).getUpdateDate() + " ");
                   sb.append("작성자 :" + dto.comments.get(i).getUser().getUsername() + " ");
               }
            }
            return sb.toString();
        } catch (NullPointerException ex) {
            System.out.println("필수값입력이 누락되었습니다. 관리자에게 문의하여 주세요.");
        }

        return  "";
    }
}
