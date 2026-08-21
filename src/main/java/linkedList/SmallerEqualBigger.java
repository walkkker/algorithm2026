package linkedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 验证：自己实现对数器
 * 主要预防任意一个区间没有节点。 要考虑这种情况进行链表整合。 -> DummyHead
 */
public class SmallerEqualBigger {


    // LeetCode 86 — Partition List（分隔链表）

    // TODO: 【错误点！！！】另外顺带提醒一点：你最后的拼接部分建议在 tail（即 lt/et/ht）后面补一句 tail.next = null（或各自段的尾 .next = null），防止原链表有残余指针形成环。
    public static ListNode listPartition(ListNode head, int pivot) {
        if (head == null) {
            return null;
        }
        ListNode lh = null;
        ListNode lt = null;
        ListNode eh = null;
        ListNode et = null;
        ListNode hh = null;
        ListNode ht = null;
        ListNode cur = head;
        while (cur != null) {
            if (cur.val < pivot) {
                if (lh == null) {
                    lh = lt = cur;
                } else {
                    lt.next = cur;
                    lt = lt.next;
                }
            } else if (cur.val == pivot) {
                if (eh == null) {
                    eh = et = cur;
                } else {
                    et.next = cur;
                    et = et.next;
                }
            } else {
                if (hh == null) {
                    hh = ht = cur;
                } else {
                    ht.next = cur;
                    ht = ht.next;
                }
            }
            // TODO: 【错误点】千万别忘了，我们在这遍历呢！！！
            cur = cur.next;
        }
        // TODO: 链接LinkedList，一定需要head和tail。 head标记头，tail用来连接后面的链表
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        if (lh != null) {
            tail.next = lh;
            tail = lt;
        }
        if (eh != null) {
            tail.next = eh;
            tail = et;
        }
        if (hh != null) {
            tail.next = hh;
            tail = ht;
        }
        // TODO: 【错误点】一定不能漏掉 tail.next = null！！！
        tail.next = null;
        return dummy.next;

    }




    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    public static ListNode listPartition2(ListNode head, int pivot) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode smallHead = null;
        ListNode smallTail = null;
        ListNode equalHead = null;
        ListNode equalTail = null;
        ListNode bigHead = null;
        ListNode bigTail = null;

        ListNode cur = head;

        // S1: 分层
        while (cur != null) {

            if (cur.val == pivot) {
                if (equalHead == null) {
                    equalHead = equalTail = cur;
                } else {
                    equalTail.next = cur;
                    equalTail = equalTail.next;
                }
            } else if (cur.val > pivot) {
                if (bigHead == null) {
                    bigHead = bigTail = cur;
                } else {
                    bigTail.next = cur;
                    bigTail = bigTail.next;
                }
            } else {      // cur.val < pivot
                if (smallHead == null) {
                    smallHead = smallTail = cur;
                } else {
                    smallTail.next = cur;
                    smallTail = smallTail.next;
                }
            }
            cur = cur.next;
        }

        // TODO: S2: 整合 - 我选择使用 dummyHead + finalTail 两个变量组织整合链
        ListNode dummy = new ListNode(0);
        ListNode finalTail = dummy;
        // 这样写逻辑很简单。并且你任何一个区间为null，对我finalTail没影响，我全部一样的逻辑。
        if (smallHead != null) {
            finalTail.next = smallHead;
            finalTail = smallTail;
        }
        if (equalHead != null) {
            finalTail.next = equalHead;
            finalTail = equalTail;
        }
        if (bigHead != null) {
            finalTail.next = bigHead;
            finalTail = bigTail;
        }
        // TODO: 【错误】漏了下面这行，导致链表成环了！！！
        //  重组链表，最后一定要清理tail.next指针。
        finalTail.next = null;
        return dummy.next;

    }

    public static ListNode listPartition1(ListNode head, int pivot) {
        if (head == null) {
            return head;
        }
        ListNode cur = head;
        int i = 0;
        while (cur != null) {
            i++;
            cur = cur.next;
        }
        ListNode[] nodeArr = new ListNode[i];
        i = 0;
        cur = head;
        for (i = 0; i != nodeArr.length; i++) {
            nodeArr[i] = cur;
            cur = cur.next;
        }
        arrPartition(nodeArr, pivot);
        for (i = 1; i != nodeArr.length; i++) {
            nodeArr[i - 1].next = nodeArr[i];
        }
        nodeArr[i - 1].next = null;
        return nodeArr[0];
    }

    public static void arrPartition(ListNode[] nodeArr, int pivot) {
        int small = -1;
        int big = nodeArr.length;
        int index = 0;
        while (index != big) {
            if (nodeArr[index].val < pivot) {
                swap(nodeArr, ++small, index++);
            } else if (nodeArr[index].val == pivot) {
                index++;
            } else {
                swap(nodeArr, --big, index);
            }
        }
    }


    public static void swap(ListNode[] nodeArr, int a, int b) {
        ListNode tmp = nodeArr[a];
        nodeArr[a] = nodeArr[b];
        nodeArr[b] = tmp;
    }


    public static void printLinkedList(ListNode ListNode) {
        System.out.print("Linked List: ");
        while (ListNode != null) {
            System.out.print(ListNode.val + " ");
            ListNode = ListNode.next;
        }
        System.out.println();
    }

//    public static void main(String[] args) {
//        ListNode head1 = new ListNode(7);
//        head1.next = new ListNode(9);
//        head1.next.next = new ListNode(1);
//        head1.next.next.next = new ListNode(8);
//        head1.next.next.next.next = new ListNode(5);
//        head1.next.next.next.next.next = new ListNode(2);
//        head1.next.next.next.next.next.next = new ListNode(5);
//        printLinkedList(head1);
//        // head1 = listPartition1(head1, 4);
//        head1 = listPartition2(head1, 5);
//        printLinkedList(head1);
//
//    }


    /**************   自己写一个对数器   *********************/
    /*
    输入：
        1. 生成随机数组
        2. 根据随机数组生成链表
    对数器：
        1. 哈希表 三向key，  遍历随机数组，元素加入hashmap
     */
    public static int[] generateRandomArray(int len, int val) {
        len = (int) (Math.random() * (len + 1));
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (val + 1)) - (int) (Math.random() * (val + 1));
        }
        return arr;
    }

    public static ListNode generateLinkedList(int[] arr) {
        if (arr == null || arr.length < 1) {
            return null;
        }
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        for (int i = 0; i < arr.length; i++) {
            ListNode node = new ListNode(arr[i]);
            tail.next = node;
            tail = tail.next;
        }
        tail.next = null;
        return dummyHead.next;
    }

    public static ListNode comparator(ListNode head, int pivot) {
        if (head == null || head.next == null) {
            return head;
        }
        ArrayList<ListNode> smallList = new ArrayList<>();
        ArrayList<ListNode> equalList = new ArrayList<>();
        ArrayList<ListNode> bigList = new ArrayList<>();
        ListNode cur = head;
        while (cur != null) {
            if (cur.val < pivot) {
                smallList.add(cur);
            } else if (cur.val == pivot) {
                equalList.add(cur);
            } else {
                bigList.add(cur);
            }
            cur = cur.next;
        }
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        smallList.addAll(equalList);
        smallList.addAll(bigList);
        for (ListNode node : smallList) {
            tail.next = node;
            tail = node;
//            tail.next = null;
        }

        // TODO: 【错误】你是将原来的链表添加到arrayList里面，然后重新组织链表的！！！   怎么能tail.next不置空呢？  不成环了吗？
        tail.next = null;
        return dummyHead.next;
    }


    public static ListNode copyLinkedList(ListNode head) {
        if (head == null) {
            return null;
        }
        // 链表 一定 >= 1个节点

        // TODO： 下面写错了，不想用dummyHead，结果逻辑错了！！！ 头结点怎么能不复制！！！怎么可以复用head???? Fuck!!!
        //        ListNode copyHead = head;
        //        ListNode copyTail = head;
        ListNode copyHead = new ListNode(head.val);
        ListNode copyTail = copyHead;
        ListNode cur = head.next;
        while (cur != null) {
            copyTail.next = new ListNode(cur.val);
            copyTail = copyTail.next;
            cur = cur.next;
        }
        return copyHead;
    }


    public static boolean isRight(ListNode head1, ListNode head2) {
        // 双指针
        while (head1 != null && head2 != null) {
            if (head1.val != head2.val) {
                return false;
            }
            head1 = head1.next;
            head2 = head2.next;
        }
        if (head1 != null || head2 != null) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int len = 1000;
        int valRange = 10000;
        int times = 100000;
        for (int i = 0; i < times; i++) {
            int[] arr = generateRandomArray(len, valRange);

            // TODO： 下面这样写，会报错 ArrayIndexOutOfBoundsException: 0
            // int pivotIndex = (int) (Math.random() * arr.length);   //[0, n-1]
            // 因为 arr.length == 0 时， pivotIndex = 0 但是这是不对的！！! 会得到下标0的，不符现实
            int pivotIndex = arr.length == 0 ? -1 : (int) (Math.random() * arr.length);
            int pivot = pivotIndex == -1 ? -1 : arr[pivotIndex];
            ListNode head1 = generateLinkedList(arr);
            ListNode head2 = copyLinkedList(head1);
            head1 = comparator(head1, pivot);
            head2 = listPartition(head2, pivot);
            boolean right = isRight(head1, head2);
            if (!right) {
//                System.out.println(arr[pivotIndex]);
                System.out.println("mistakes!");
                printLinkedList(head1);
                printLinkedList(head2);
                return;
            }
        }
        System.out.println("测试通过");
    }






}
