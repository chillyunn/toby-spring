package com.book.tobyspring.user.service;

import com.book.tobyspring.Level;
import com.book.tobyspring.user.User;
import com.book.tobyspring.user.dao.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
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
    UserDao userDao;

    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("bumjin","박범진","p1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER - 1,0),
                new User("joytouch","강명성","p2", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("erwins","신승한","p3", Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1","이상호","p4", Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD),
                new User("green","오민규","p5", Level.GOLD,100,Integer.MAX_VALUE)
                );
    }

    @Test
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for(User user: users){
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgrade(users.get(0),false);
        checkLevelUpgrade(users.get(1),true);
        checkLevelUpgrade(users.get(2),false);
        checkLevelUpgrade(users.get(3),true);
        checkLevelUpgrade(users.get(4),false);
    }

    private void checkLevelUpgrade(User user, boolean canUpgrade) {
        User userUpdate = userDao.get(user.getId());
        if (canUpgrade) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }else{
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }

    }

    @Test
    public void add(){
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
    void upgradeAllOrNothing() throws Exception{
        UserService testUserService = new UserService.TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setDataSource(this.dataSource);

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