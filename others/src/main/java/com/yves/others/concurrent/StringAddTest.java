package com.yves.others.concurrent;

public class StringAddTest {

    /**
     * 2个正整数字符串的相加，即‘1111111111111111’+’1999999999999999999’
     *
     * @param args
     */
    public static void main(String[] args) {
        System.err.println(add("1110","220"));
    }

    private static String add(String s1, String s2) {
        StringBuffer result = new StringBuffer();

        //反转
        s1 = new StringBuffer(s1).reverse().toString();
        s2 = new StringBuffer(s2).reverse().toString();

        int s1Length = s1.length();
        int s2Length = s2.length();
        int maxLength = Math.max(s1Length, s2Length);

        //对齐
        if (s1Length < s2Length) {
            for (int i = s1Length; i < s2Length; i++) {
                s1 = s1 + "0";
            }
        } else if (s2Length < s1Length){
            for (int i = s2Length; i < s1Length; i++) {
                s2 = s2 + "0";
            }
        }

        //相加
        boolean isOverFlow = false;
        int nOver = 0;
        for (int i = 0; i < maxLength; i++) {
            int sum = Integer.valueOf(s1.charAt(i) + "") + Integer.valueOf(s2.charAt(i) + "") + nOver;

            //大于10进位
            if (sum >= 10) {
                //如果是最后一位累加,判断是否越位
                if (i == (maxLength - 1)) {
                    isOverFlow = true;
                }
                nOver = 1;
                result.append(sum - 10);
            }else {
                nOver = 0;
                result.append(sum);
            }
        }

        if(isOverFlow){
            result.append(nOver);
        }

        return result.reverse().toString();
    }

}