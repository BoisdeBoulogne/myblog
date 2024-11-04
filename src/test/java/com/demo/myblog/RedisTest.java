package com.demo.myblog;

import com.demo.myblog.entry.table.User;
import com.demo.myblog.mapper.UserMapper;
import com.demo.myblog.service.IUserService;
import com.demo.myblog.service.impl.IUserServiceImpl;
import com.demo.myblog.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest

public class RedisTest {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    IUserService userService;
    @Test
    public void testRedis() {
        redisUtils.set("hello", "world");

        redisUtils.set("hello", "world1",5, TimeUnit.MINUTES);
        String hello = redisUtils.get("hello");
        System.out.println(hello);
    }

    @Test
    public void testMP(){
        User user = userService.getById(1);
        System.out.println(user);
    }
}
