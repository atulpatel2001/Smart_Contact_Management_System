package com.smart.contact.repository;

import com.smart.contact.dao.UserRepository;
import com.smart.contact.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserRepositoryTest {

@Autowired
    private UserRepository userRepository;
    @Test
    void getUserByUserName() {
        User user = this.userRepository.getUserByUserName("jspatel95745@gmail.com");
         int actualId=user.getId();
         int expectedId=302;
         assertThat(actualId).isEqualTo(expectedId);
    }

    @AfterEach
    void tearDown() {

    }

    @BeforeEach
    void setUp() {

    }


}