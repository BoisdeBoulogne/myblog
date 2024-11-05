package com.demo.myblog.handler;

import com.demo.myblog.entry.result.Result;
import com.demo.myblog.enums.AppEnum;
import com.demo.myblog.exception.SystemException;
import io.jsonwebtoken.JwtException;
import jakarta.mail.internet.AddressException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)
    public Result systemException(SystemException e) {
        return Result.error(e.getAppEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void methodArgumentNotValidException(MethodArgumentNotValidException e) {
        throw new SystemException(AppEnum.WRONG_QUERY);
    }

    @ExceptionHandler(AddressException.class)
    public void addressException(AddressException e) {
        throw new SystemException(AppEnum.WRONG_EMAIL);
    }
    @ExceptionHandler(JwtException.class)
    public void jwtException(JwtException e) {
        throw new SystemException(AppEnum.WRONG_JWT);
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public void duplicateKeyException(DuplicateKeyException e) {
        throw new SystemException(AppEnum.EXIST_DATA);
    }
}
