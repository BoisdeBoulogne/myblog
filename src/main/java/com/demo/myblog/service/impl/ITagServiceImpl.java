package com.demo.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.myblog.entry.result.Result;
import com.demo.myblog.entry.table.Tag;
import com.demo.myblog.mapper.TagMapper;
import com.demo.myblog.service.ITagService;
import org.springframework.stereotype.Service;

@Service
public class ITagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Override
    public Result getTags() {
        return Result.ok(list());
    }
}
