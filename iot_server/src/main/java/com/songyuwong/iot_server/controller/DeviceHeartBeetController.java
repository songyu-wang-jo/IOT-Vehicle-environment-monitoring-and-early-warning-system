package com.songyuwong.iot_server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songyuwong.iot_server.common.BaseDataResponse;
import com.songyuwong.iot_server.commonUtils.ApiUtils;
import com.songyuwong.iot_server.dto.DeviceHeartBeetDTO;
import com.songyuwong.iot_server.entity.DeviceHeartBeet;
import com.songyuwong.iot_server.service.DeviceHeartBeetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/iot_device")
@Slf4j
public class DeviceHeartBeetController {

    @Autowired
    DeviceHeartBeetService deviceHeartBeetService;

    @PostMapping("/page")
    private BaseDataResponse getDeviceList(@RequestBody DeviceHeartBeetDTO deviceHeartBeetDTO){
        log.info("设备心跳数据请求：size {},current {}", deviceHeartBeetDTO.getSize(),deviceHeartBeetDTO.getCurrent());
        Page<DeviceHeartBeet> deviceHeartBeetPage = deviceHeartBeetService.getBaseMapper().deviceHeartBeetPage(deviceHeartBeetDTO);
        return ApiUtils.buildSuccessResponse("2000",deviceHeartBeetPage,"获取设备心跳数据记录成功");
    }
}
