package com.book.tobyspring.user.service;

import com.book.tobyspring.user.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
