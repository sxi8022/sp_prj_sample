package com.spr.expost.dto;

import com.spr.expost.vo.Comment;
import com.spr.expost.vo.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDto {
    private Long postNo;
    private String title;
    private String content;
    private String author;
    private String postPassword;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<Comment> comments;

    public Post toEntity() {
        Post build = Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .postPassword(postPassword)
                .build();
        return build;
    }

    public Post toUpdateEntity() {
        Post build = Post.builder()
                .postNo(postNo)
                .author(author)
                .title(title)
                .content(content)
                .postPassword(postPassword)
                .build();
        return build;
    }

    @Builder
    public PostDto(Long postNo, String title, String content, String author, String postPassword,LocalDateTime createDate, LocalDateTime updateDate, List<Comment> comments) {
        this.postNo = postNo;
        this.author = author;
        this.postPassword = postPassword;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.comments = comments;
    }

    // 등록
    public PostDto(String title, String content, String author, String postPassword) {
        this.author = author;
        this.postPassword = postPassword;
        this.title = title;
        this.content = content;
    }

    //전체내용 표시
    public List<String> toListString(List<PostDto> dtoList) {
        List<String> listString = new ArrayList<>();
        for (int i = 0; i <dtoList.size(); i ++) {
            listString.add(
                   "글번호 = " + dtoList.get(i).getPostNo()
                     + " {" +
                            "제목='" + dtoList.get(i).getTitle() + '\'' +
                            ", 작성자명='" + dtoList.get(i).getAuthor() + '\'' +
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
                    ", 작성자명='" + dto.getAuthor() + '\'' +
                    ", 작성내용='" + dto.getContent() + '\'' +
                    ", 작성날짜=" + nowDate +
                    '}');
            // 게시글에 따라 댓글 조회
            if (dto.comments!= null && !dto.comments.isEmpty()) {
               for (int i = 0 ; i < dto.comments.size(); i ++) {
                   sb.append("\n");
                   sb.append("댓글 :" + dto.comments.get(i).getComment() + " ");
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
