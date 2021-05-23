package com.songyuwong.iot_server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.songyuwong.iot_server.entity.User;
import com.songyuwong.iot_server.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
