package frequence.LinkedList;
/**
 * TODO: 【注意点】每次移动节点，都要移动两个链表(list1/list2 和 tail)
 *  - List1/List2 要移动(=list.next) && tail要移动（tail=tail.next）
 *
 * <p><b>专题归类：</b>两路有序归并、{@code dummy + tail}和双输入消费不变量。
 * 复盘参见同目录《链表通用技巧与题型分类.md》和《链表错题本.md》的Q21条目。
 */
public class Q21 {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                tail.next = list1;
                list1 = list1.next;
            } else {
                tail.next = list2;
                list2 = list2.next;
            }
            tail = tail.next;
        }
        while (list1 != null) {
            tail.next = list1;
            list1 = list1.next;
            tail = tail.next;
        }
        while (list2 != null) {
            tail.next = list2;
            list2 = list2.next;
            tail = tail.next;
        }
        return dummy.next;
    }
}
