package com.demo.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.myblog.entry.table.History;
import com.demo.myblog.entry.vo.ArticlePerVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HistoryMapper extends BaseMapper<History> {
    List<ArticlePerVo> getPreByIds();
}
