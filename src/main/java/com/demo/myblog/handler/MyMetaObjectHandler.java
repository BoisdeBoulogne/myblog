package com.demo.myblog.handler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.demo.myblog.enums.AppEnum;
import com.demo.myblog.exception.SystemException;
import com.demo.myblog.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override

    public void insertFill(MetaObject metaObject) {
        Integer userId = null;
        String userName = null;
        try {
            userId = SecurityUtils.getUserId();
            userName = SecurityUtils.getUserName();
        } catch (Exception e) {
            throw new SystemException(AppEnum.FORCE_LOGOUT);
        }
        //自动新增
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("writer", userName, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
