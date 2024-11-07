package com.demo.myblog.controller;

import com.demo.myblog.entry.dto.ChangeRoleDTO;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private IUserService userService;

    @GetMapping("/list/{pageNum}")
    public Result userList(@PathVariable Integer pageNum) {
        return userService.userList(pageNum);
    }
    @PutMapping("/banUser/{id}")
    public Result banUser(@PathVariable Integer id){
        return userService.banUser(id);
    }
    @PutMapping("/changeRole")
    public Result changeRole(@RequestBody ChangeRoleDTO changeRoleDTO){
        return userService.changeRole(changeRoleDTO);
    }


}
