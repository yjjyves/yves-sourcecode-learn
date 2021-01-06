package com.yves.others.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MqttClientUtil {
    public static final Logger log = LoggerFactory.getLogger(MqttClientUtil.class);

    public static IMqttClient connect(String productKey, String deviceName, String deviceSecret, String serverUri) {
        try {
            String username = deviceName + "&" + productKey;
            String clientidPrefix = productKey + "_" + deviceName;
            long timestamp = System.currentTimeMillis();
            String clientid = clientidPrefix + "|securemode=3,signmethod=hmacsha1,timestamp=" + timestamp + "|";
            String sb = "clientId" + clientidPrefix +
                    "deviceName" + deviceName +
                    "productKey" + productKey +
                    "timestamp" + timestamp;
            String password = HMACUtil.hmacSha1Encrypt(sb, deviceSecret);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName(username);
            mqttConnectOptions.setPassword(Objects.requireNonNull(password).toCharArray());
            mqttConnectOptions.setCleanSession(true);
            MqttClient mqttClient = new MqttClient(serverUri, clientid, new MemoryPersistence());
            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    log.info("连接完成, 是否重连: {}", reconnect);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    log.error("连接丢失", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    log.info("{} -> {}", topic, new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            mqttClient.connect(mqttConnectOptions);
            return mqttClient;
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
