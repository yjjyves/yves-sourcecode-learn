package com.yves.others.yvesleetcode.titles;

public class SelectionSort {

    //选择排序，每次选择最小的元素放在第一个
    public static void selectionSort(int[] arr) {
        int length = arr.length;

        for (int i = 0; i < arr.length - 1; i++) {//需要比较的次数，数组长度减一
            //先假设每次循环时，最小数的索引为i
            int minIndex = i;
            for (int j = i + 1; i < length; i++) {
                if (arr[j] < arr[i]) {
                    minIndex = j;
                }
            }
            //交换元素
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }

    }
}
