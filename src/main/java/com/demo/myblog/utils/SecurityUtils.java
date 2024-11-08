package com.demo.myblog.utils;
import com.demo.myblog.entry.dto.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取用户的userid
     **/
    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }



    public static Integer getUserId() {
        return getLoginUser().getUser().getId();
    }
    public static String getUserName() {
        return getLoginUser().getUser().getNickname();
    }
}
