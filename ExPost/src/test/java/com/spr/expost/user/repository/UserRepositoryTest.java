package com.spr.expost.user.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.spr.expost.config.QueryDslConfig;
import com.spr.expost.user.entity.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.spr.expost.user.entity.User;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Test
  void dynamicInsertTest() {
    // given
    var newUser = new User("sofia", "sofia247#", "sofia@gmail.com", UserRoleEnum.USER);

    // when
    var savedUser = userRepository.save(newUser);

    // then
    assertThat(savedUser).isNotNull();
   }

  @Test
  void dynamicUpdateTest() {
    // given
    var newUser = new User("sofia", "sofia247#", "sofia@gmail.com", UserRoleEnum.USER);
    userRepository.save(newUser);
    var newPassword = "sofia247@";

    // when
    newUser.setPassword(newPassword);
    var savedUser = userRepository.save(newUser);

    // then
    assertThat(savedUser.getPassword()).isEqualTo(newPassword);
  }
}