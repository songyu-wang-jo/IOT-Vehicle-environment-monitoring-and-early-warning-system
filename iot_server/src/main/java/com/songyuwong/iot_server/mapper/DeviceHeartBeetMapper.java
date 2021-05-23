package com.songyuwong.iot_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songyuwong.iot_server.entity.DeviceHeartBeet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceHeartBeetMapper extends BaseMapper<DeviceHeartBeet> {

    Page<DeviceHeartBeet> deviceHeartBeetPage(Page<DeviceHeartBeet> deviceHeartBeetPage);
}
