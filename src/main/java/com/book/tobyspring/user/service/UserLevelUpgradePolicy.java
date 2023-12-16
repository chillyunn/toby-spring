package com.book.tobyspring.user.service;

import com.book.tobyspring.user.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);

}


