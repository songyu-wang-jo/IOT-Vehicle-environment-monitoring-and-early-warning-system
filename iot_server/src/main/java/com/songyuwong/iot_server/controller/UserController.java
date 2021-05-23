package com.songyuwong.iot_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.songyuwong.iot_server.common.BaseDataResponse;
import com.songyuwong.iot_server.commonUtils.ApiUtils;
import com.songyuwong.iot_server.dto.UserReqDTO;
import com.songyuwong.iot_server.entity.User;
import com.songyuwong.iot_server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/iot_user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("login")
    public BaseDataResponse<Object> login(@RequestBody UserReqDTO userReqDTO){
        log.info("用户登录请求：{}", userReqDTO.toString());
        QueryWrapper userQueryWrapper = Wrappers.query().eq("username", userReqDTO.getUsername()).eq("password", userReqDTO.getPassword());
        User one = userService.getOne(userQueryWrapper, false);
        if (one!=null){
            log.info("登录成功！！！{}", one.toString());
            return ApiUtils.buildSuccessResponse("2000", one.getUsername(),"loginOK");
        }else {
            log.warn("登录失败！！！");
            return ApiUtils.buildSuccessResponse("5000", null, "loginFailure");
        }
    }
}
