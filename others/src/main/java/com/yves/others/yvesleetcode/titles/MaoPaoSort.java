package com.yves.others.yvesleetcode.titles;

public class MaoPaoSort {
    public static void main(String[] args) {
        int[] attr = new int[]{2, 3, -2, 4};

        System.err.println();

        System.err.println("---------------------");
    }

    public static void maopao(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        int length = nums.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[ j + 1] = nums[j];
                    nums[ j] =temp;
                }
            }
        }
    }


}
