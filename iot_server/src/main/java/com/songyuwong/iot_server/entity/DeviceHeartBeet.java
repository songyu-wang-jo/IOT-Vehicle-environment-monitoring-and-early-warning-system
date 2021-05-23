package com.songyuwong.iot_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("device_heart_beet")
public class DeviceHeartBeet {

    @TableId(type = IdType.AUTO,value = "device_heart_id")
    private Integer deviceHeartId;

    private Integer temp;

    private Integer humi;

    private String deviceName;

    private String datetime;
}
