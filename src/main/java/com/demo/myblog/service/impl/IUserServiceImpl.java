package com.demo.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.myblog.entry.dto.LoginUser;
import com.demo.myblog.entry.dto.UserLoginDTO;
import com.demo.myblog.entry.dto.UserRegisterDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.User;
import com.demo.myblog.entry.vo.UserVoAfterLogin;
import com.demo.myblog.enums.AppEnum;
import com.demo.myblog.exception.SystemException;
import com.demo.myblog.mapper.UserMapper;
import com.demo.myblog.service.IUserService;
import com.demo.myblog.utils.JwtUtils;
import com.demo.myblog.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.demo.myblog.constant.Constants.*;

@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private PasswordEncoder passwordEncoder;



    @Override
    public Result login(UserLoginDTO user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication == null) {
            throw new SystemException(AppEnum.ERROR_AUTH);
        }
        User userInfo =  ((LoginUser) authentication.getPrincipal()).getUser();
        redisUtils.set(LOGIN_USER+userInfo.getId(),userInfo,30, TimeUnit.MINUTES);
        UserVoAfterLogin userVo = new UserVoAfterLogin();
        BeanUtils.copyProperties(userInfo,userVo);
        userVo.setToken(JwtUtils.generateToken(userInfo.getId()));
        return Result.ok(userVo);
    }

    @Override
    public Result register(UserRegisterDTO user) {
        Integer codeInRedis = redisUtils.get(CODE+user.getEmail());
        Integer codeInDTO = user.getCode();
        if (codeInRedis == null){
            throw new SystemException(AppEnum.NULL_CODE);
        }
        if (!codeInRedis.equals(codeInDTO)){
            throw new SystemException(AppEnum.WRONG_CODE);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());

        redisUtils.del(CODE+user.getEmail());
        User userForDB = new User();
        BeanUtils.copyProperties(user, userForDB);
        userForDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userForDB.setUnlocked(true);
        userForDB.setRole(USER);
        try {
            save(userForDB);
        } catch (DuplicateKeyException e) {
            throw new SystemException(AppEnum.EXIST_DATA);
        }
        return Result.ok();
    }
    @Resource
    private JavaMailSender javaMailSender;
    @Override
    public Result getCode(String email) {
        if (redisUtils.get(CODE+email) != null) {
            throw new SystemException(AppEnum.EXIST_CODE);
        }
        Random random = new Random();
        Integer code = random.nextInt(900000) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("code");
        redisUtils.set(CODE+email, code,5, TimeUnit.MINUTES);
        message.setText(code.toString());
        message.setFrom("you_persona_use@163.com");
        javaMailSender.send(message);
        return Result.ok();
    }
}
