package com.songyuwong.iot_server.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songyuwong.iot_server.entity.Warn;
import lombok.Data;

@Data
public class WarnDTO extends Page<Warn> {
}
