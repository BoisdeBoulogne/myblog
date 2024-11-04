package com.demo.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.myblog.entry.table.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
