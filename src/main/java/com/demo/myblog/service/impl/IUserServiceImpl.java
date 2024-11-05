package com.demo.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.myblog.entry.dto.LoginUser;
import com.demo.myblog.entry.dto.UserLoginDTO;
import com.demo.myblog.entry.dto.UserRegisterDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.User;
import com.demo.myblog.entry.table.User2User;
import com.demo.myblog.entry.vo.UserVo;
import com.demo.myblog.entry.vo.UserVoAfterLogin;
import com.demo.myblog.enums.AppEnum;
import com.demo.myblog.exception.SystemException;
import com.demo.myblog.mapper.User2UserMapper;
import com.demo.myblog.mapper.UserMapper;
import com.demo.myblog.service.IUserService;
import com.demo.myblog.utils.JwtUtils;
import com.demo.myblog.utils.RedisUtils;
import com.demo.myblog.utils.SecurityUtils;
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

import java.util.ArrayList;
import java.util.List;
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
    @Resource
    private User2UserMapper user2UserMapper;



    @Override
    public Result login(UserLoginDTO user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication == null) {
            throw new SystemException(AppEnum.ERROR_AUTH);
        }
        User userInfo =  ((LoginUser) authentication.getPrincipal()).getUser();
        Integer userId = userInfo.getId();

        redisUtils.set(LOGIN_USER+userInfo.getId(),userInfo,30, TimeUnit.MINUTES);
        UserVoAfterLogin userVo = new UserVoAfterLogin();
        BeanUtils.copyProperties(userInfo,userVo);
        userVo.setToken(JwtUtils.generateToken(userInfo.getId()));
        userVo.setFolloweeCount(getFolloweeCount(userId));
        userVo.setFollowerCount(getFollowerCount(userId));
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

        redisUtils.del(CODE+user.getEmail());
        User userForDB = new User();
        BeanUtils.copyProperties(user, userForDB);
        userForDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userForDB.setUnlocked(true);
        userForDB.setRole(USER);

        save(userForDB);
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
        log.info(code.toString());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("code");
        redisUtils.set(CODE+email, code,5, TimeUnit.MINUTES);
        message.setText(code.toString());
        message.setFrom("you_persona_use@163.com");
        javaMailSender.send(message);
        return Result.ok("验证码:"+code);
    }

    //关注
    @Override
    public Result follow(Integer id) {
        LambdaQueryWrapper<User> exists = new LambdaQueryWrapper<>();
        exists.eq(User::getId, id);
        boolean isExists = this.exists(exists);
        if (!isExists) {
            throw new SystemException(AppEnum.NO_SUCH_ENTRY);
        }
        User2User user2User = new User2User();
        user2User.setFolloweeId(SecurityUtils.getUserId());
        user2User.setFollowerId(id);
        user2UserMapper.insert(user2User);
        return Result.ok();
    }
    //取消关注
    @Override
    public Result unfollow(Integer id) {
        LambdaQueryWrapper<User2User> exists = new LambdaQueryWrapper<>();
        exists.eq(User2User::getFollowerId, id);
        exists.eq(User2User::getFolloweeId, SecurityUtils.getUserId());
        boolean isExists = user2UserMapper.exists(exists);
        if (!isExists) {
            throw new SystemException(AppEnum.NO_SUCH_ENTRY);
        }
        user2UserMapper.delete(exists);
        return Result.ok();
    }

    @Override
    public Result followers() {
        LambdaQueryWrapper<User2User> list = new LambdaQueryWrapper<>();
        list.eq(User2User::getFolloweeId, SecurityUtils.getUserId());
        List<Integer> followersIds = user2UserMapper.selectList(list).stream().map(User2User::getFollowerId).toList();
        List<UserVo> userVos = new ArrayList<>();
        for (Integer followerId : followersIds) {
            User user = getById(followerId);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVo.setFollowingCount(getFolloweeCount(user.getId()));
            userVo.setFollowerCount(getFollowerCount(user.getId()));
            userVo.setIsFollowing(ifFollower(user.getId(),SecurityUtils.getUserId()));
            userVos.add(userVo);
        }
        return Result.ok(userVos);

    }

    @Override
    public Result followee() {
        LambdaQueryWrapper<User2User> list = new LambdaQueryWrapper<>();
        list.eq(User2User::getFollowerId, SecurityUtils.getUserId());
        List<User2User> user2Users = user2UserMapper.selectList(list);
        List<Integer> followeeIds = user2Users.stream().map(User2User::getFolloweeId).toList();
        List<UserVo> userVos = new ArrayList<>();
        for (Integer followeeId : followeeIds) {
            User user = getById(followeeId);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVo.setFollowingCount(getFolloweeCount(user.getId()));
            userVo.setFollowerCount(getFollowerCount(user.getId()));
            userVo.setIsFollowing(ifFollower(user.getId(),SecurityUtils.getUserId()));
            userVos.add(userVo);
        }
        return Result.ok(userVos);
    }


    


    private Integer getFolloweeCount(Integer userId) {
        LambdaQueryWrapper<User2User> followeeQueryWrapper = new LambdaQueryWrapper<>();
        followeeQueryWrapper.eq(User2User::getFollowerId,userId);
        return user2UserMapper.selectCount(followeeQueryWrapper).intValue();
    }
    //粉丝数目
    private Integer getFollowerCount(Integer userId) {
        LambdaQueryWrapper<User2User> followeeQueryWrapper = new LambdaQueryWrapper<>();
        followeeQueryWrapper.eq(User2User::getFolloweeId,userId);
        return user2UserMapper.selectCount(followeeQueryWrapper).intValue();
    }
    //关注的人的数目

    private boolean ifFollower(Integer userId, Integer followerId) {
        LambdaQueryWrapper<User2User> followeeQueryWrapper = new LambdaQueryWrapper<>();
        followeeQueryWrapper.eq(User2User::getFolloweeId,userId);
        followeeQueryWrapper.eq(User2User::getFollowerId,followerId);
        return user2UserMapper.exists(followeeQueryWrapper);
    }




}
