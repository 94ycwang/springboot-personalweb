package com.yw.blog.service;

import com.yw.blog.dao.UserRepository;
import com.yw.blog.po.User;
import com.yw.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User checkUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public User checkEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public User checkNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);
        return user;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }
}
