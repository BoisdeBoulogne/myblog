package com.demo.myblog.entry.table;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("article")
public class Article {
    private Integer id;
    private String title;
    private String avatar;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField("user_id")
    private Integer userId;
    @TableField(fill = FieldFill.INSERT)
    private String writer;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableLogic
    private Integer deFlag;

    private Integer views;

}
