package frequence.LinkedList;

/**
 * 234. 回文链表。
 *
 * <p><b>专题归类：</b>快慢指针找中点、反转后半区、双指针比较和恢复现场。
 * 复盘参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q234条目。
 */
public class Q234 {
    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode() {}
     *     ListNode(int val) { this.val = val; }
     *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {
        public boolean isPalindrome(ListNode head) {
            // 上中或中
            ListNode slow = head;
            ListNode fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            ListNode end = reverse(slow);
            boolean ans = true;
            while (head != null && end != null) {
                if (head.val != end.val) {
                    ans = false;
                }
                head = head.next;
                end = end.next;
            }
            // TODO: 【当前实现遗漏-恢复现场无效】比较循环结束后end已经移动到null，reverse(end)不会恢复链表。
            // 正确方向：额外保存反转后的入口reversedHead；比较使用独立游标，最后reverse(reversedHead)。
            reverse(end);
            return ans;
        }

        public ListNode reverse(ListNode head) {
            ListNode pre = null;
            ListNode cur = head;
            while (cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }
    }
}
