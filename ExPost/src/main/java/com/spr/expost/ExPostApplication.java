package com.spr.expost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// EnableJpaAuditing 날짜 자동저장, 수정일자는 최초생성시에 null로 저장되게 처리
@SpringBootApplication
@EnableJpaAuditing
// @EnableScheduling
public class ExPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExPostApplication.class, args);
    }

}
