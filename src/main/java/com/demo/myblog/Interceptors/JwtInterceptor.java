package com.demo.myblog.Interceptors;

import com.demo.myblog.entry.dto.LoginUser;
import com.demo.myblog.entry.table.User;
import com.demo.myblog.enums.AppEnum;
import com.demo.myblog.exception.SystemException;
import com.demo.myblog.utils.JwtUtils;
import com.demo.myblog.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.demo.myblog.constant.Constants.LOGIN_USER;

@Component
public class JwtInterceptor extends OncePerRequestFilter {

    @Resource
    private RedisUtils redisUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader==null || authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        Integer userId = JwtUtils.parseToken(authHeader);
        User user = redisUtils.get(LOGIN_USER+userId);
        if (user==null) {
            throw new SystemException(AppEnum.FORCE_LOGOUT);
        }
        LoginUser loginUser =  new LoginUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
