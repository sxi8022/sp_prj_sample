package com.spr.expost.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.spr.expost.comment.entity.Comment;
import com.spr.expost.config.QueryDslConfig;
import com.spr.expost.post.entity.Post;
import com.spr.expost.post.repository.PostRepository;
import com.spr.expost.user.entity.User;
import com.spr.expost.user.entity.UserRoleEnum;
import com.spr.expost.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
public class CommentRepositoryTest {

  @Autowired
  PostRepository postRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CommentRepository commentRepository;

  @Test
  void dynamicInsertTest() {
    // given
    var newUser = new User("sofia", "sofia247#", "sofia@gmail.com", UserRoleEnum.USER);
    userRepository.save(newUser);
    var newPost = Post.builder().title("뉴테스트").content("내용이뭘까").user(newUser).build();
    var savedPost = postRepository.save(newPost);
    var newComment = Comment.builder().content("뉴테스트").post(newPost).user(newUser).build();

    // when

    var savedComment = commentRepository.save(newComment);

    // then
    assertThat(savedComment).isNotNull();
  }

  @Test
  void dynamicUpdateTest() {

    // given
    var newUser = new User("sofia", "sofia247#", "sofia@gmail.com", UserRoleEnum.USER);
    userRepository.save(newUser);
    var newPost = Post.builder().title("뉴테스트").content("내용이뭘까").user(newUser).build();
    var savedPost = postRepository.save(newPost);
    var newComment = Comment.builder().content("뉴테스트").post(newPost).user(newUser).build();

    // when
    var savedComment = commentRepository.save(newComment);
    String updateContent = "내용은이것";
    savedComment.updateTest(updateContent);
    var updateComment = commentRepository.save(savedComment);


    // then
    assertThat(updateComment.getContent()).isEqualTo(updateContent);
  }
}
