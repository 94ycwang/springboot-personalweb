package com.yw.blog.service;

import com.yw.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);

    User checkUsername(String username);

    User checkEmail(String email);

    User checkNickname(String nickname);

    User saveUser(User user);
}
