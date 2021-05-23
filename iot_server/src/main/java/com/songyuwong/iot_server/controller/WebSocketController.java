package com.songyuwong.iot_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.songyuwong.iot_server.common.BaseDataResponse;
import com.songyuwong.iot_server.common.WebSocketServer;
import com.songyuwong.iot_server.commonUtils.ApiUtils;
import com.songyuwong.iot_server.dto.WebSocketDTO;
import com.songyuwong.iot_server.entity.Warn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/iot_websocket")
public class WebSocketController {

    @Autowired
    WebSocketServer webSocketServer;

    @PostMapping("/sendMessage")
    public BaseDataResponse sendMessage(@RequestBody Warn warn){
        log.info("异常测试数据：{}", warn);
        String resp = "";
        try {
            webSocketServer.sendMessage(JSONObject.toJSONString(warn));
            resp = "ok";
        } catch (IOException e) {
            resp = e.getMessage();
        }
        return ApiUtils.buildSuccessResponse(resp);
    }
}
