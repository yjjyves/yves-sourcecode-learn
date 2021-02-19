package com.yves.others.yvesleetcode.titles;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LeastActiveLoadBalance {

    private static final Random random = new Random();

    private static Map<Integer, Integer> map = new HashMap();

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            int randomInt = random.nextInt(10) + 1;
            Integer value = map.get(randomInt);
            if (value != null) {
                map.put(randomInt, ++value);
            } else {
                map.put(randomInt, 1);
            }
        }

        System.err.println("" + JSONObject.toJSONString(map));
    }

}
