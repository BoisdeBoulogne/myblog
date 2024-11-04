package com.demo.myblog.exception;

import com.demo.myblog.enums.AppEnum;

public class SystemException extends RuntimeException {
    private AppEnum appEnum;
    public AppEnum getAppEnum() {
        return appEnum;
    }
    public SystemException(AppEnum appEnum) {
        super(appEnum.getMsg());
        this.appEnum = appEnum;
    }

}
