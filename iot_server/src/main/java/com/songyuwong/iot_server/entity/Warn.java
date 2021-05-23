package com.songyuwong.iot_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("warn")
public class Warn {

    @TableId(type = IdType.AUTO,value = "warn_id")
    private Integer warnId;

    private String warnType;

    private String warnDesc;

    private String deviceName;

    private String datetime;

}
