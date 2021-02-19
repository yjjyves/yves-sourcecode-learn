package com.yves.others.yvesleetcode.titles;

public class Insertion {
    public static void sort(Comparable[] a) {
        //将a[]按升序排列
        int N = a.length;
        for (int i = 1; i < N; i++) {
            //将a[i]插入到a[i-1]，a[i-2]，a[i-3]……之中
            for (int j = i; j > 0 && (a[j].compareTo(a[j - 1]) < 0); j--) {
                Comparable temp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = temp;
            }
        }
    }

    public static void insertion(int[] a) {
        //将a[]按升序排列
        int length = a.length;
        for (int i = 1; i < length; i++) {

            //将a[i]插入到a[i-1]，a[i-2]，a[i-3]……之中
            for (int j = i; j > 0 && (a[j] < a[j - 1]); j--) {
                int temp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = temp;
            }
        }
    }
}