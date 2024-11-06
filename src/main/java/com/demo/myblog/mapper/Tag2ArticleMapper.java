package com.demo.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.myblog.entry.table.Tag2Article;
import jakarta.mail.MailSessionDefinition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Tag2ArticleMapper extends BaseMapper<Tag2Article> {

}
