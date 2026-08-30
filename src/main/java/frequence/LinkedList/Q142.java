package frequence.LinkedList;

/**
 * 142. 环形链表 II。
 *
 * <p><b>专题归类：</b>快慢指针首次相遇、入环距离关系以及第二次同步相遇。
 * 复盘参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q141/Q142条目。
 */
public class Q142 {

    /**
     * 2026-08-30 我的快慢指针实现。
     *
     * <p><b>思路：</b>
     * <ol>
     *     <li>一开始检查{@code head、head.next、head.next.next}是否为{@code null}；
     *     三者均非空时，才可以按照本实现的初始化方式让快慢指针提前前进。</li>
     *     <li>检查快慢指针是否相遇。如果快指针先走到空节点，则链表无环；如果相遇，则链表有环。</li>
     *     <li>第一次相遇后令{@code fast = head}，随后{@code slow、fast}每次都前进一步；
     *     二者再次相遇的位置就是入环节点。</li>
     * </ol>
     *
     * <p><b>边界说明：</b>前三项空检查是为了配合本版本的
     * {@code slow = head.next、fast = head.next.next}初始化方式，并不是Floyd算法本身要求
     * 链表必须至少包含三个节点。自环和两个节点组成的环不会被该检查错误排除，因为此时
     * {@code head.next.next}并不为{@code null}。
     */
    public class Solution20260830 {

        public ListNode detectCycle(ListNode head) {
            if (head == null || head.next == null || head.next.next == null) {
                return null;
            }

            ListNode slow = head.next;
            ListNode fast = head.next.next;

            // 第一阶段：要么fast无法继续走两步，证明无环；要么slow和fast在环内相遇。
            while (fast.next != null && fast.next.next != null) {
                if (slow == fast) {
                    break;
                }
                slow = slow.next;
                fast = fast.next.next;
            }

            if (slow != fast) {
                return null;
            }

            // 第二阶段：从head和第一次相遇点同步前进，再次相遇的位置就是入环节点。
            fast = head;
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        }
    }

    public class Solution {
        /**
         TODO：因为本题是不确定是否有环，所以第一步快慢指针要要同时1.检查快指针是否走null or 2. 快慢是否会相遇。于是出现下列问题。


         TODO: 【错误】这道题我做错了。
         做错的语句见下方注释。 如果快慢指针初始化时都是Head，那么第一次while就会退出，这是错误的。
         TODO： 所以尽量快慢指针初始化时就往前跳一步，然后一定要搭配if检查slow/fast的null情况。


         */
        public ListNode detectCycle(ListNode head) {
            // TODO: 【错误】补充了后两个if
            if (head == null || head.next == null || head.next.next == null) {
                return null;
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
                return null;
            }
            fast = head;
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        }
    }
}
