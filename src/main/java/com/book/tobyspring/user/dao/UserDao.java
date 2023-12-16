package com.book.tobyspring.user.dao;

import com.book.tobyspring.user.User;

import java.util.List;

public interface UserDao {
    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    public void update(User user1);
}