package com.lr.blog.service.impl;

import com.lr.blog.mapper.ArticleMapper;
import com.lr.blog.mapper.UserMapper;
import com.lr.blog.entity.User;
import com.lr.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户管理
 * Created by lr
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Override
    public List<User> listUser() {
        List<User> userList = userMapper.listUser();
        for (int i = 0; i < userList.size(); i++) {
            Integer articleCount = articleMapper.countArticleByUser(userList.get(i).getUserId());
            userList.get(i).setArticleCount(articleCount);
        }
        return userList;
    }

    @Override
    @Cacheable(value = "default", key = "'user:'+#id")
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    @CacheEvict(value = "default", key = "'user:'+#user.userId")
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Override
    @CacheEvict(value = "default", key = "'user:'+#id")
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    @CachePut(value = "default", key = "'user:'+#result.userId")
    public User insertUser(User user) {
        user.setUserRegisterTime(new Date());
        userMapper.insert(user);
        return user;
    }

    @Override
    public User getUserByNameOrEmail(String str) {
        return userMapper.getUserByNameOrEmail(str);
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }


}
