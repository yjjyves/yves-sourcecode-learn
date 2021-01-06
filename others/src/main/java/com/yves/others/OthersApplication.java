package com.yves.others;

import com.yves.others.mqtt.MqttClientUtil;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OthersApplication {

    public static void main(String[] args) {
        SpringApplication.run(OthersApplication.class, args);

        String productKey = "gNO0stxs52o";
        String deviceName = "Yves_001";
        String deviceSecret = "019446ec29b2496d8001e5708444f797";
        String serverUri = "tcp://115.159.79.14:1883";
        IMqttClient mqttClient = MqttClientUtil.connect(productKey, deviceName, deviceSecret, serverUri);
        System.err.println("============================");
    }

}
