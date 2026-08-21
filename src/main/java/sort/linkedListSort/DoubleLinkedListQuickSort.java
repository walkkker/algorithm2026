package sort.linkedListSort;

import java.util.ArrayList;
import java.util.Comparator;

// 双向链表的随机快速排序
// 课上没有讲，因为这是群里同学问的问题
// 作为补充放在这，有需要的同学可以看看
// 和课上讲的数组的经典快速排序在算法上没有区别
// 但是coding需要更小心
public class DoubleLinkedListQuickSort {

    public static class Node {
        public int value;
        public Node last;
        public Node next;

        public Node(int v) {
            value = v;
        }
    }

    public static Node quickSort(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        int N = 0;
        Node cur = head;

        while (cur.next != null) {
            N++;
            cur = cur.next;
        }

        N++;     // 因为到达最后一个节点没有执行循环体，所以需要额外 N++

        HeadTail ht = process(head, cur, N);
        return ht.head;
    }


    // 定义：给定L，R，size。    返回排序好的  HeadTail
    // 语义：上述，实现链表排序
    // 拆解：选择随机pivot， partition() + 左右区间递归 + 三区间整合
    public static HeadTail process(Node L, Node R, int len) {
        if (L == R) {   // 此处其实包含两种场景：(1) L!=null (2) L==null
            return new HeadTail(L, R);
        }
        int index = (int) (Math.random() * (len)) + 1; // [1, len]  因为不是数组，是计算要跳到 第几个 节点，从1开始
        Node pivot = L;
        while (index-- > 1) {
            pivot = pivot.next;
        }
        // 断开该节点，把两头接上。
        // 但是要分类讨论，左右边界 + 中间位置
        if (pivot == L) {
            L = L.next;
            L.last = null;
        } else if (pivot == R) {
            R = R.last;
            R.next = null;
        } else {   // 此时只有中间位置
            pivot.last.next = pivot.next;
            pivot.next.last = pivot.last;
        }
        pivot.next = null;
        pivot.last = null;
        Info info = partition(L, pivot);
        HeadTail lht = process(info.lh, info.lt, info.ls);
        HeadTail mht = process(info.mh, info.mt, info.ms);
        // 递归完成后，还要把 当前eq 左侧less 和右侧more 连接起来
        Node dummyHead = new Node(0);
        Node tail = dummyHead;
        // TODO： 【错误】双向链表 ， 每一次指针调整都要步步惊心！！！ 一定要注意来回两个指针的调整！
        if (lht.head != null) {
            tail.next = lht.head;
            // TODO: 少了下面这句
            lht.head.last = tail;
            tail = lht.tail;
        }
        // 中间区域一定存在，因为是 从当前链表里面选的
        tail.next = info.eh;
        // TODO: 下面这句我也忘了
        info.eh.last = tail;
        tail = info.et;

        if (mht.head != null) {
            tail.next = mht.head;
            // TODO: SAME，下面这句我也忘了
            mht.head.last = tail;
            tail = mht.tail;
        }

        // TODO: 【超级错误！又错了！！！】 重排/整合链表区间，一定要清理指针！！！
        //  上面是双链表 要记得一次连两个指针！    这个是重组链表一定要记得 结尾清理指针
        tail.next = null;
        dummyHead.next.last = null;
        return new HeadTail(dummyHead.next, tail);

    }

    // 只需要一个头结点和一个pivot，做 荷兰国旗
    // 跟三向分区一样，画六个指针变量，外加3组对应的大小
    public static Info partition(Node head, Node pivot) {
        // TODO: 这里的双指针 也都只连了next。    全部忘了 last
        Node lh = null;
        Node lt = null;
        int ls = 0;

        // 注意这里的初始化
        Node eh = pivot;
        Node et = pivot;
        int es = 1;
        Node mh = null;
        Node mt = null;
        int ms = 0;

        Node cur = head;
        // TODO：  【大错误】partition 这里的 每个区间都要清理指针！！！ 因为不好判断null与否，所以我直接在while循环里面 清指针了
        //    通过提前 Node next = cur.next; 记录下一个节点（在末尾 cur = next），就可以避免清理指针后，跑到null了
        //    这里必须清理每个区间的 head,tail指针，因为Partition后的链表分区会 递归给process ，并由process while()遍历，如果没有及时清理tail.next这戏，会导致无限环
        while (cur != null) {
            Node next = cur.next;
            if (cur.value < pivot.value) {
                ls++;
                if (lh == null) {
                    lh = cur;
                    lt = cur;
                    // TODO: 这里
                    lh.last = null;
                } else {
                    lt.next = cur;
                    // TODO: 忘了
                    cur.last = lt;
                    lt = cur;
                    // TODO: 这里，清理尾指针 。  谁知道是不是这个区间的最后一个tail了
                    lt.next = null;
                }
            } else if (cur.value > pivot.value) {
                ms++;
                if (mh == null) {
                    mh = cur;
                    mt = cur;
                    // TODO: 清理头指针
                    mh.last = null;
                } else {
                    mt.next = cur;
                    // TODO: 忘了
                    cur.last = mt;
                    mt = cur;
                    // TODO: 这里，清理尾指针 。  谁知道是不是这个区间的最后一个tail了
                    mt.next = null;
                }
            } else {
                es++;
                et.next = cur;
                // TODO: 忘了
                cur.last = et;
                et = cur;
                // TODO: 这里，清理尾指针 。  谁知道是不是这个区间的最后一个tail了
                et.next = null;
            }
            cur = next;
        }
        // TODO：  【大错误】partition 返回前也要清理指针！！！
        return new Info(lh, lt, ls, eh, et, es, mh, mt, ms);
    }


    // partition用于返回 三个区间的 head, tail, size
    public static class Info {
        public Node lh;
        public Node lt;
        public int ls;
        public Node eh;
        public Node et;
        public int es;
        public Node mh;
        public Node mt;
        public int ms;

        public Info(Node lH, Node lT, int lS, Node eH, Node eT, int eS, Node mH, Node mT, int mS) {
            lh = lH;
            lt = lT;
            ls = lS;
            eh = eH;
            et = eT;
            es = eS;
            mh = mH;
            mt = mT;
            ms = mS;
        }
    }

    public static class HeadTail {
        public Node head;
        public Node tail;

        public HeadTail(Node head, Node tail) {
            this.head = head;
            this.tail = tail;
        }
    }


    // 为了测试
    public static class NodeComp implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.value - o2.value;
        }

    }

    // 为了测试
    public static Node sort(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        while (head != null) {
            arr.add(head);
            head = head.next;
        }
        arr.sort(new NodeComp());
        Node h = arr.get(0);
        h.last = null;
        Node p = h;
        for (int i = 1; i < arr.size(); i++) {
            Node c = arr.get(i);
            p.next = c;
            c.last = p;
            c.next = null;
            p = c;
        }
        return h;
    }

    // 为了测试
    public static Node generateRandomDoubleLinkedList(int n, int v) {
        if (n == 0) {
            return null;
        }
        Node[] arr = new Node[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Node((int) (Math.random() * v));
        }
        Node head = arr[0];
        Node pre = head;
        for (int i = 1; i < n; i++) {
            pre.next = arr[i];
            arr[i].last = pre;
            pre = arr[i];
        }
        return head;
    }

    // 为了测试
    public static Node cloneDoubleLinkedList(Node head) {
        if (head == null) {
            return null;
        }
        Node h = new Node(head.value);
        Node p = h;
        head = head.next;
        while (head != null) {
            Node c = new Node(head.value);
            p.next = c;
            c.last = p;
            p = c;
            head = head.next;
        }
        return h;
    }

    // 为了测试
    public static boolean equal(Node h1, Node h2) {
        return doubleLinkedListToString(h1).equals(doubleLinkedListToString(h2));
    }

    // 为了测试
    public static String doubleLinkedListToString(Node head) {
        Node cur = head;
        Node end = null;
        StringBuilder builder = new StringBuilder();
        while (cur != null) {
            builder.append(cur.value + " ");
            end = cur;
            cur = cur.next;
        }
        builder.append("| ");
        while (end != null) {
            builder.append(end.value + " ");
            end = end.last;
        }
        return builder.toString();
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 500;
        int V = 500;
        int testTime = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int size = (int) (Math.random() * N);
            Node head1 = generateRandomDoubleLinkedList(size, V);
            Node head2 = cloneDoubleLinkedList(head1);
            Node sort1 = quickSort(head1);
            Node sort2 = sort(head2);
            if (!equal(sort1, sort2)) {
                System.out.println("出错了!");
                break;
            }
        }
        System.out.println("测试结束");
    }


}
