package com.demo.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.myblog.entry.dto.UserLoginDTO;
import com.demo.myblog.entry.dto.UserRegisterDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.User;

public interface IUserService extends IService<User> {


    Result login(UserLoginDTO user);

    Result register(UserRegisterDTO user);

    Result getCode(String email);

    Result follow(Integer id);

    Result unfollow(Integer id);

    Result followers();

    Result followee();
}
