package com.demo.myblog.entry.result;

import com.demo.myblog.enums.AppEnum;
import lombok.Data;

@Data
public class Result<T> {
    public int code;
    public String msg;
    public T data;
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static Result error(AppEnum appEnum){
        return new Result(appEnum.getCode(), appEnum.getMsg(), null);
    }

    public static <T>Result<T> ok(T data){
        return new Result<>(AppEnum.SUCCESS.getCode(), AppEnum.SUCCESS.getMsg(),data);
    }
    public static Result ok(){
        return new Result(AppEnum.SUCCESS.getCode(), AppEnum.SUCCESS.getMsg(), null);
    }
    public static Result errorOnArgument(String msg){
        return new Result(400, msg, null);
    }
}
