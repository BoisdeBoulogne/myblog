package com.demo.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Tag;

public interface ITagService extends IService<Tag> {

    Result getTags();
}
