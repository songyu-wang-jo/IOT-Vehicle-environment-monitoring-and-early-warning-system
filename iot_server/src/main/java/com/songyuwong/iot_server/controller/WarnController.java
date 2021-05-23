package com.songyuwong.iot_server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songyuwong.iot_server.common.BaseDataResponse;
import com.songyuwong.iot_server.commonUtils.ApiUtils;
import com.songyuwong.iot_server.dto.WarnDTO;
import com.songyuwong.iot_server.entity.Warn;
import com.songyuwong.iot_server.service.WarnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iot_warn")
@Slf4j
public class WarnController {

    @Autowired
    WarnService warnService;

    @PostMapping("/page")
    public BaseDataResponse getWarnPage(@RequestBody WarnDTO warnDTO){
        Page<Warn> warnPage = warnService.getBaseMapper().getWarnPage(warnDTO);
        return ApiUtils.buildSuccessResponse("2000", warnPage, "获取设备警告请求成功");
    }
}
