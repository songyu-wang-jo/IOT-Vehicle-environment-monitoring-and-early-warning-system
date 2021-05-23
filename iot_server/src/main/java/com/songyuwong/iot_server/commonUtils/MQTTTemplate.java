package com.songyuwong.iot_server.commonUtils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.cmp.api.CommonResource;
import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.core.base.*;
import com.aliyun.alink.linksdk.cmp.core.listener.*;
import com.aliyun.alink.linksdk.tools.AError;
import com.songyuwong.iot_server.common.WebSocketServer;
import com.songyuwong.iot_server.entity.DeviceHeartBeet;
import com.songyuwong.iot_server.entity.Warn;
import com.songyuwong.iot_server.service.DeviceHeartBeetService;
import com.songyuwong.iot_server.service.WarnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Slf4j
@Component
public class MQTTTemplate {

    @Autowired
    DeviceHeartBeetService deviceHeartBeetService;

    @Autowired
    WarnService warnService;

    @Autowired
    WebSocketServer webSocketServer;

    private final static String pk = "a1fCnFS7KyA";
    private final static String hubDn = "DataHub";
    private final static String hubDs = "fd451dc751ff7652d440a885c3b8eb30";
    private final static String stableDn = "DataStable";
    private final static String stableDs = "2a8777c24cc735b8d28a5ec6a5e296ca";
    private String currentDn;
    private String currentDs;
    private final static String STABLE_TOPIC = "/sys/a1fCnFS7KyA/DataStable/thing/service/property/set";
    private final static String[] params = new String[]{"TEMP", "HUMI", "JSDX", "JSDY", "JSDZ", "JDX", "JDY", "MQ2", "MQ3"};
    private String formatDate;
    private String deviceName;
    private JSONObject paramJsonObject;
    private Warn warn;

    public MQTTTemplate() {
//        initDevice("hub");
        initDevice("stable");
    }

    private void initDevice(String nameFlag) {
        if (nameFlag.contains("a")) {
            this.currentDn = stableDn;
            this.currentDs = stableDs;
        } else {
            this.currentDn = hubDn;
            this.currentDs = hubDs;
        }
        initSDK(currentDn, currentDs);
    }

    /**
     * 初始化SDK--连接至MQTT服务
     */
    public void initSDK(String dn, String ds) {
        LinkKitInitParams params = new LinkKitInitParams();
        /*
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = pk;
        config.deviceName = dn;
        config.deviceSecret = ds;
        /*
         *是否接受离线消息
         *对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;
        /*
         *设置初始化三元组信息，用户传入
         */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = pk;
        deviceInfo.deviceName = dn;
        deviceInfo.deviceSecret = ds;
        params.deviceInfo = deviceInfo;
        /*
         * 设置设备当前的初始状态值，属性需要和云端创建的物模型属性一致
         *如果这里什么属性都不填，物模型就没有当前设备相关属性的初始值。
         *用户调用物模型上报接口之后，物模型会有相关数据缓存。
         */
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                log.info("初始化SDK错误：" + aError);
                initSDK(currentDn, currentDs);
            }

            public void onInitDone(InitResult initResult) {
                log.info("设备{}初始化SDK连接任务成功：" + initResult, currentDn);
                log.info("{}主题信息初始化中.....", STABLE_TOPIC);
                subscribeTopic(STABLE_TOPIC);
                registerResourceTopic(STABLE_TOPIC);
            }
        });
    }

    /**
     * 订阅主题
     *
     * @param topic 对应要订阅的主题
     */
    public void subscribeTopic(String topic) {
        // 订阅
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        // topic 定义的主题
        request.topic = topic;
        request.isSubscribe = true;
        LinkKit.getInstance().subscribe(request, new IConnectSubscribeListener() {
            @Override
            public void onSuccess() {
                // 订阅成功
                log.info("订阅成功");
            }

            @Override
            public void onFailure(AError aError) {
                // 订阅失败
                log.info("订阅失败" + (aError == null ? "" : (aError.getCode() + aError.getMsg())));
            }
        });
    }

    /**
     * 取消主题订阅
     *
     * @param topic 对应要取消订阅的主题
     */
    public void unsubscribeTopic(String topic) {
        // 取消订阅
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        // topic 要取消的主题
        request.topic = topic;
        request.isSubscribe = false;
        LinkKit.getInstance().unsubscribe(request, new IConnectUnscribeListener() {
            @Override
            public void onSuccess() {
                // 取消订阅成功
                log.info("取消订阅成功");
            }

            @Override
            public void onFailure(AError aError) {
                // 取消订阅失败
                log.info("取消订阅失败" + (aError == null ? "" : (aError.getCode() + aError.getMsg())));
            }
        });
    }

    /**
     * 处理某个主题的数据消息
     *
     * @param topic 对应主题
     */
    public void registerResourceTopic(String topic) {
        final CommonResource resource = new CommonResource();
        resource.topic = topic;
        resource.replyTopic = resource.topic;
        LinkKit.getInstance().registerResource(resource, new IResourceRequestListener() {
            @Override
            public void onHandleRequest(AResource aResource, ResourceRequest resourceRequest, IResourceResponseListener iResourceResponseListener) {
                // 收到云端数据下行
                warn = null;
                JSONObject jsonObject = JSONObject.parseObject(new String((byte[]) resourceRequest.payloadObj));
                DeviceHeartBeet deviceHeartBeet = new DeviceHeartBeet();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                formatDate = formatter.format(new Date(System.currentTimeMillis()));
                deviceHeartBeet.setDatetime(formatDate);
                deviceName = jsonObject.getString("deviceName");
                deviceHeartBeet.setDeviceName(deviceName);
                JSONObject items = jsonObject.getJSONObject("items");
                log.info("deviceMessage:{}", items);
                for (String param : params) {
                    paramJsonObject = items.getJSONObject(param);
                    switch (param) {
                        case "TEMP":
                            stableWarn("TEMP ", "温度异常");
                            deviceHeartBeet.setTemp(paramJsonObject.getInteger("value"));
                            break;
                        case "HUMI":
                            deviceHeartBeet.setHumi(paramJsonObject.getInteger("value"));
                            break;
                        case "JSDX":
                            stableWarn("JSDX ", "姿态异常");
                            break;
                        case "JSDY":
                            stableWarn("JSDY ", "姿态异常");
                            break;
                        case "JSDZ":
                            stableWarn("JSDZ ", "姿态异常");
                            break;
                        case "JDX":
                            stableWarn("JDX ", "姿态异常");
                            break;
                        case "JDY":
                            stableWarn("JDY ", "姿态异常");
                            break;
                        case "MQ2":
                            stableWarn("MQ2 ", "气敏异常");
                            break;
                        case "MQ3":
                            stableWarn("MQ3 ", "气敏异常");
                            break;
                        default:
                    }
                }
                deviceHeartBeetService.save(deviceHeartBeet);
                log.info("持久化完毕");
            }

            @Override
            public void onSuccess() {
                log.info("注册资源成功");
            }

            @Override
            public void onFailure(AError aError) {
                log.info("注册资源失败" + getError(aError));
            }
        });
    }

    private void stableWarn(String descName, String type) {
        Double value = 0.0;
        if (descName.contains("JSD")) {//姿态加速度异常
            value = paramJsonObject.getDouble("value");
            if (value < -1 || value > 1) {
                saveWarn(descName, type, value);
            }
        } else if (descName.contains("JD")) {//姿态角度异常
            value = paramJsonObject.getInteger("value").doubleValue();
            if (value != 0) {
                if (value < 70 || value > 110) {
                    saveWarn(descName, type, value);
                }
            }
        } else if (descName.contains("TEMP")) {//温度异常
            value = paramJsonObject.getInteger("value").doubleValue();
            if (value > 30 || value < 0) {
                saveWarn(descName, type, value);
            }
        } else if (descName.contains("MQ3")) {//气敏烟雾异常
            value = paramJsonObject.getInteger("value").doubleValue();
            if (value > 17) {
                saveWarn(descName, type, value);
            }
        } else if (descName.contains("MQ2")) {//气敏酒精异常
            value = paramJsonObject.getInteger("value").doubleValue();
            if (value > 18) {
                saveWarn(descName, type, value);
            }
        }
    }

    @Async
    protected void saveWarn(String descName, String type, Double value) {
        warn = new Warn();
        warn.setDatetime(formatDate);
        warn.setDeviceName(deviceName);
        warn.setWarnDesc(descName + value);
        warn.setWarnType(type);
        warnService.save(warn);
        log.info("异常持久化成功");
        //发送数据到websocket连接
        try {
            webSocketServer.sendMessage(JSONObject.toJSONString(warn));
            log.info("异常信息分发成功！！！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布主题消息
     *
     * @param topic   对应主题
     * @param message 对应消息
     * @param qos     发布模式：0 发一次不保证接收， 1 保证接收可能重复， 2 保证接收一次
     * @param isReply 是否订阅回复主题
     */
    public void publishTopic(String topic, String message, Integer qos, boolean isReply) {
        // 发布主题请求封装
        MqttPublishRequest request = new MqttPublishRequest();
        // topic 用户根据实际场景填写
        request.topic = topic;
        //回复主题订阅
        request.replyTopic = request.topic + "_reply";
        request.isRPC = isReply;
        //发布模式
        request.qos = qos;
        // 发布信息
        // payloadObj 替换成用户需要发布的数据 json String
        // request.payloadObj = "{\"data\": "+message+"}";
        request.payloadObj = message;
        LinkKit.getInstance().publish(request, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                // publish 结果
                log.info("发布结果：" + (aResponse == null ? "" : (aResponse.getData() == null ? "" : aResponse.getData())));
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                // publish 失败
                log.info("失败信息：" + (aError == null ? "" : (aError.getCode() + aError.getMsg())));
            }
        });
    }


    /**
     * 下行数据接收&处理
     * 设备连接状态变化
     */
    private final IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
        public void onNotify(String connectId, String topic, AMessage aMessage) {
            // 云端下行数据通知
            log.info("{}接受收到下行数据：{}", topic, aMessage);
        }

        public void onConnectStateChange(String connectId, ConnectState connectState) {
            // 设备连接状态通知
            log.info("设备连接状态改变：{}", connectState);
        }

        public boolean shouldHandle(String connectId, String topic) {
            //是否需要处理
            log.info("不对该改变处理");
            return false; // 根据实际场景设置
        }
    };

    /**
     * 所有topic的下行数据入口（前提是先订阅了该 topic，才会在这里收到）
     * 如果想要在这里收到对应 topic 的下行数据，需要先订阅该 topic
     */
    public void registerNotifyListener() {
        LinkKit.getInstance().registerOnNotifyListener(notifyListener);
    }

    /**
     * 取消注册下行的监听器，该 listener 需要保持和注册的 listener 是同一个对象
     */
    public void unregisterNotifyListener() {
        LinkKit.getInstance().unRegisterOnNotifyListener(notifyListener);
    }

    protected String getError(AError error) {
        if (error == null) {
            return null;
        }
        return "[code=" + error.getCode() + ",msg=" + error.getMsg() + ",subCode=" + error.getSubCode() + ",subMsg=" + error.getSubMsg() + "]";
    }
}
