package com.yves.others.concurrent;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class InstantTest {




    public static void main(String[] args) {
        Instant now = Instant.now();
        System.out.println("now:" + now);

        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());//获取当前时区时间
        System.out.println("当前时区时间:" + zonedDateTime);


        long shi = zonedDateTime.toEpochSecond(); //获取十位时间戳
        System.out.println("当前时区时间的时间戳:" + shi);
    }
}
