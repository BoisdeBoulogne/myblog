package com.demo.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.myblog.entry.dto.Query;
import com.demo.myblog.entry.dto.UserLoginDTO;
import com.demo.myblog.entry.dto.UserRegisterDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.User;
import com.demo.myblog.entry.vo.HomePageVo;
import com.demo.myblog.entry.vo.UserVo;

public interface IUserService extends IService<User> {


    Result login(UserLoginDTO user);

    Result register(UserRegisterDTO user);

    Result getCode(String email);

    Result follow(Integer id);

    Result unfollow(Integer id);

    Result followers();

    Result followee();

    Result historyList();

    Result collect(Integer id);

    Result uncollect(Integer id);

    Result myCollects();

    Result changeAvatar(String url);

    Result<HomePageVo> homePage(Integer page);

    Result<UserVo> search(Integer page, Query query);
}
