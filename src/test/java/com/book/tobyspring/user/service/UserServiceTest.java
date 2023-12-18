package com.book.tobyspring.user.service;

import com.book.tobyspring.Level;
import com.book.tobyspring.user.User;
import com.book.tobyspring.user.dao.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.book.tobyspring.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.book.tobyspring.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    UserDao userDao;
    @Autowired
    MailSender mailSender;

    List<User> users;

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }


    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", "bumjin@naver.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", "joytouch@naver.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", "erwins@naver.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", "madnite1@naver.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", "green@naver.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size()).isEqualTo(2);
        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkLevelUpgrade(User user, boolean canUpgrade) {
        User userUpdate = userDao.get(user.getId());
        if (canUpgrade) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }

    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new UserService.TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            Assertions.fail("TestUserServiceException expected");
        } catch (UserService.TestUserService.TestUserServiceException e) {

        }

        checkLevelUpgrade(users.get(1), false);

    }
}