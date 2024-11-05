package com.demo.myblog.controller;

import com.demo.myblog.entry.dto.UserLoginDTO;
import com.demo.myblog.entry.dto.UserRegisterDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.vo.UserVoAfterLogin;
import com.demo.myblog.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Email;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    IUserService userService;
    @PostMapping("/login")
    public Result<UserVoAfterLogin> login(@RequestBody UserLoginDTO user){
        return userService.login(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserRegisterDTO user){
        return userService.register(user);
    }

    @GetMapping("/getCode")
    public Result getCode(@RequestParam @Email(message = "邮箱格式不正确") String email) {
        return userService.getCode(email);
    }

    @GetMapping("/follow/{id}")
    public Result follow(@PathVariable Integer id) {
        return userService.follow(id);
    }

    @GetMapping("/unfollow/{id}")
    public Result unfollow(@PathVariable Integer id) {
        return userService.unfollow(id);
    }

    //关注列表
    @GetMapping("/followers")
    public Result followers() {
        return userService.followers();
    }

    @GetMapping("/followee")
    public Result followee() {
        return userService.followee();
    }

}
