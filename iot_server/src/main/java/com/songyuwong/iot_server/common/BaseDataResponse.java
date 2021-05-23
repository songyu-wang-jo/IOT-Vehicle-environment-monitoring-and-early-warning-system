package com.songyuwong.iot_server.common;

import lombok.Data;

@Data
public class BaseDataResponse<T> {

    private String code;

    private T data;

    private String message;
}
