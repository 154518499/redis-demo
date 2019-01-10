package com.demo.testredis.service;

import com.demo.testredis.entity.User;
import com.demo.testredis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: scp
 * @Date: 2018/12/4 11:43
 * @Description:
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findByUserId(userId);
    }
}
