package com.yves.others.yvesleetcode.titles;

import com.yves.others.yvesleetcode.base.ListNode;

/**
 * 反转链表
 *
 * @author yijinjin
 * @date 2020/8/31 -19:18
 */
public class Solution206 {
    /**
     * A->B->C->D
     * D->C->B->A
     *
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode curr = head;
        ListNode pre = null;
        while (curr.next != null) {
            //1.先把首节点断开
            ListNode tempNext = curr.next;

            //2.把前置节点变为自己的后置节点
            curr.next = pre;

            //3.当前节点置为前置节点
            pre = curr;

            //3.记录下个节点为要遍历的节点
            curr = tempNext;
        }
        return pre;
    }

    public ListNode reverseList_2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode p = reverseList_2(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }


}
