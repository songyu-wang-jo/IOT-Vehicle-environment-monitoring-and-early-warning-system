package com.songyuwong.iot_server.commonUtils;

import com.songyuwong.iot_server.common.BaseDataResponse;

public class ApiUtils {

    public static BaseDataResponse<Object> buildSuccessResponse(String code,Object data,String message){
        return buildDataResponse(code,data,message);
    }

    private static BaseDataResponse<Object> buildDataResponse(String code, Object data, String message){
        BaseDataResponse<Object> objectBaseDataResponse = new BaseDataResponse<>();
        objectBaseDataResponse.setCode(code);
        objectBaseDataResponse.setData(data);
        objectBaseDataResponse.setMessage(message);
        return objectBaseDataResponse;
    }

    public static BaseDataResponse buildSuccessResponse(String resp) {
        BaseDataResponse<Object> objectBaseDataResponse = new BaseDataResponse<>();
        objectBaseDataResponse.setCode("2000");
        objectBaseDataResponse.setData(resp);
        objectBaseDataResponse.setMessage("OK");
        return objectBaseDataResponse;
    }
}
