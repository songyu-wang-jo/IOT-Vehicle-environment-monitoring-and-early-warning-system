package com.songyuwong.iot_server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.songyuwong.iot_server.entity.Warn;
import com.songyuwong.iot_server.mapper.WarnMapper;
import org.springframework.stereotype.Service;

@Service
public class WarnService extends ServiceImpl<WarnMapper, Warn> {
}
