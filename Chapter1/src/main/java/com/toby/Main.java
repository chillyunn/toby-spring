package com.toby;

import com.toby.dao.UserDao;
import com.toby.domain.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("giyun");
        user.setName("한기윤");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId()+"등록 성공  ");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId()+"조회 성공");

    }
}