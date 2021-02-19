package com.yves.others.yvesleetcode.titles;

import com.alibaba.fastjson.JSONObject;
import com.yves.yvesleetcode.base.ListNode;

/**
 * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
 *
 * @author yijinjin
 * @date 2020/9/22 -15:20
 */
public class Solution92 {

    public static void main(String[] args) {
     /*   ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(4);
        ListNode node4 = new ListNode(5);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        System.err.println(JSONObject.toJSONString(head));
        ListNode sortNode = reverseBetween(head, 2, 4);
        System.err.println(JSONObject.toJSONString(sortNode));
*/

        //[3,5] 1 2

        ListNode head77 = new ListNode(3);
        ListNode node78 = new ListNode(5);
        ListNode node79 = new ListNode(7);
        head77.next = node78;
        node78.next = node79;

        System.err.println(JSONObject.toJSONString(head77));
        ListNode sortNode2 = reverseBetween(head77, 1, 2);
        System.err.println(JSONObject.toJSONString(sortNode2));
    }

    /**
     * 1 ≤ m ≤ n ≤ 链表长度。
     * <p>
     * 输入: 1->2->3->4->5->NULL, m = 2, n = 4
     * 输出: 1->4->3->2->5->NULL
     *
     * @return
     * @author yijinjin
     * @date 2020/9/22 -15:21
     */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        if (head.next == null) {
            return head;
        }
        if (n == 1) {
            return head;
        }
        int i = 1;
        ListNode curr = head;
        ListNode pre = null;
        ListNode midEnd = null;
        ListNode mid = null;
        ListNode h = null;
        while (curr != null) {
            //1.先把首节点断开
            ListNode tempNext = curr.next;
            if (i >= m && i <= n) {
                //2.将前置节点放到尾部
                curr.next = pre;
                mid = curr;

                pre = curr;

                curr = tempNext;

            } else if (i == m - 1) {
                h = curr;
                h.next = null;
                curr = tempNext;
                midEnd = tempNext;
            } else if ((i == n + 1) && midEnd != null) {
                midEnd.next = curr;
                break;
            }
            i++;
        }

        if (h != null) {
            h.next = mid;
            return h;
        } else {
            return mid;
        }
    }


    // Object level variables since we need the changes
    // to persist across recursive calls and Java is pass by value.
    private boolean stop;
    private ListNode left;

    public void recurseAndReverse(ListNode right, int m, int n) {

        // base case. Don't proceed any further
        if (n == 1) {
            return;
        }

        // Keep moving the right pointer one step forward until (n == 1)
        right = right.next;

        // Keep moving left pointer to the right until we reach the proper node
        // from where the reversal is to start.
        if (m > 1) {
            this.left = this.left.next;
        }

        // Recurse with m and n reduced.
        this.recurseAndReverse(right, m - 1, n - 1);

        // In case both the pointers cross each other or become equal, we
        // stop i.e. don't swap data any further. We are done reversing at this
        // point.
        if (this.left == right || right.next == this.left) {
            this.stop = true;
        }

        // Until the boolean stop is false, swap data between the two pointers
        if (!this.stop) {
            int t = this.left.val;
            this.left.val = right.val;
            right.val = t;

            // Move left one step to the right.
            // The right pointer moves one step back via backtracking.
            this.left = this.left.next;
        }
    }

    public ListNode reverseBetween_2(ListNode head, int m, int n) {
        this.left = head;
        this.stop = false;
        this.recurseAndReverse(head, m, n);
        return head;
    }


    public ListNode reverseBetween_3(ListNode head, int m, int n) {

        // Empty list
        if (head == null) {
            return null;
        }

        // Move the two pointers until they reach the proper starting point
        // in the list.
        ListNode cur = head, prev = null;
        while (m > 1) {
            prev = cur;
            cur = cur.next;
            m--;
            n--;
        }

        // The two pointers that will fix the final connections.
        ListNode con = prev, tail = cur;

        // Iteratively reverse the nodes until n becomes 0.
        ListNode third = null;
        while (n > 0) {
            third = cur.next;
            cur.next = prev;
            prev = cur;
            cur = third;
            n--;
        }

        // Adjust the final connections as explained in the algorithm
        if (con != null) {
            con.next = prev;
        } else {
            head = prev;
        }

        tail.next = cur;
        return head;
    }

}
