package frequence.LinkedList;

public class Q141 {
    public class Solution {
        /**
         TODO：因为本题是不确定是否有环，所以第一步快慢指针要要同时1.检查快指针是否走null or 2. 快慢是否会相遇。于是出现下列问题。


         TODO: 【错误】这道题我做错了。
         做错的语句见下方注释。 如果快慢指针初始化时都是Head，那么第一次while就会退出，这是错误的。
         TODO： 所以尽量快慢指针初始化时就往前跳一步，然后一定要搭配if检查slow/fast的null情况。


         */
        public boolean hasCycle(ListNode head) {
            // TODO: 【错误】补充了后两个if
            if (head == null || head.next == null || head.next.next == null) {
                return false;
            }

            // TODO: 【错误】这样初始化的话，搭配下面的while是错的，永远一开始就退出循环。所以一开始就要往前跳一步赋值slow/fast。
            // ListNode slow = head;
            // ListNode fast = head;
            ListNode slow = head.next;
            ListNode fast = head.next.next;
            while (fast.next != null && fast.next.next != null) {
                if (slow == fast) {
                    break;
                }
                slow = slow.next;
                fast = fast.next.next;
            }
            if (fast.next == null || fast.next.next == null) {
                return false;
            }
            fast = head;
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return true;
        }
    }
}
