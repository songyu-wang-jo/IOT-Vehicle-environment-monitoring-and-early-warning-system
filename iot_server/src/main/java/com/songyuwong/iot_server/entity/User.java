package com.songyuwong.iot_server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {

    @TableId("user_id")
    private Integer userId;

    private String username;

    private String password;
}
