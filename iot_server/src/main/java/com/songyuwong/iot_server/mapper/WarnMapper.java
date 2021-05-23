package com.songyuwong.iot_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songyuwong.iot_server.entity.Warn;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarnMapper extends BaseMapper<Warn> {
    Page<Warn> getWarnPage(Page<Warn> warnPage);
}
