package BinaryTree.RecursiveTemplate;

import java.util.ArrayList;

/**
 * 给定一棵二叉树，找到其中节点数最多的二叉搜索树（BST），并返回这棵 BST 的 头结点。
 * 如果整棵树都是 BST，则返回整棵树的头结点。
 */
public class MaxSubBSTHead {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static class Info {
        Node maxSubBSTHead;
        int maxSubBSTSize;
        boolean isBST;
        int max;
        int min;

        public Info(Node _maxSubBSTHead, int _maxSubBSTSize, boolean _isBST, int _max, int _min) {
            maxSubBSTHead = _maxSubBSTHead;
            maxSubBSTSize = _maxSubBSTSize;
            isBST = _isBST;
            max = _max;
            min = _min;
        }
    }

    public static Info process(Node head) {
        if (head == null) {
            return null;
        }
        Info left = process(head.left);
        Info right = process(head.right);

        if (left == null && right == null) {
            return new Info(head, 1, true, head.value, head.value);
        }

        Node maxSubBSTHead;
        int maxSubBSTSize;
        boolean isBST;
        int max;
        int min;

        if (left != null && right == null) {
            if (left.isBST && left.max < head.value) {
                maxSubBSTHead = head;
                maxSubBSTSize = left.maxSubBSTSize + 1;
                isBST = true;
            } else {
                maxSubBSTHead = left.maxSubBSTHead;
                maxSubBSTSize = left.maxSubBSTSize;
                isBST = false;
            }
            max = Math.max(left.max, head.value);
            min = Math.min(left.min, head.value);
            return new Info(maxSubBSTHead, maxSubBSTSize, isBST, max, min);
        }

        // TODO: 又是直接复制上面的，然后 漏掉了一个，留了一个left！！！
        //   其实按照目前经验，当左!null右null，解决后，如果想要直接复制的话，1）先把所有的left改成right 2) 修改代码逻辑
        //   因为左null右!null的情况下，不可能会用到Left，所以大胆换成right就行了！！！
        if (left == null && right != null) {
            if (right.isBST && right.min > head.value) {
                maxSubBSTHead = head;
//         TODO: 就是这个有没改成功   maxSubBSTSize = left.maxSubBSTSize + 1;
                maxSubBSTSize = right.maxSubBSTSize + 1;
                isBST = true;
            } else {
                maxSubBSTHead = right.maxSubBSTHead;
                maxSubBSTSize = right.maxSubBSTSize;
                isBST = false;
            }
            max = Math.max(right.max, head.value);
            min = Math.min(right.min, head.value);
            return new Info(maxSubBSTHead, maxSubBSTSize, isBST, max, min);
        }

        if (left.isBST && right.isBST && left.max < head.value && right.min > head.value) {
            maxSubBSTHead = head;
            maxSubBSTSize = left.maxSubBSTSize + right.maxSubBSTSize + 1;
            isBST = true;
        } else {
            // TODO: 【不能说是逻辑错误】第一次写下面这行错了，是因为左神对数器 左右subSize相同时，会取左侧的subHead。
            // maxSubBSTHead = left.maxSubBSTSize > right.maxSubBSTSize ? left.maxSubBSTHead : right.maxSubBSTHead;
            maxSubBSTHead = left.maxSubBSTSize >= right.maxSubBSTSize ? left.maxSubBSTHead : right.maxSubBSTHead;
            maxSubBSTSize = Math.max(left.maxSubBSTSize, right.maxSubBSTSize);
            isBST = false;
        }
        max = Math.max(Math.max(left.max, right.max), head.value);
        min = Math.min(Math.min(left.min, right.min), head.value);
        return new Info(maxSubBSTHead, maxSubBSTSize, isBST, max, min);
    }

    public static Node maxSubBSTHead2(Node head) {
        if (head == null) {
            return null;
        }
        return process(head).maxSubBSTHead;
    }


    public static int getBSTSize(Node head) {
        if (head == null) {
            return 0;
        }
        ArrayList<Node> arr = new ArrayList<>();
        in(head, arr);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).value <= arr.get(i - 1).value) {
                return 0;
            }
        }
        return arr.size();
    }

    public static void in(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    public static Node maxSubBSTHead1(Node head) {
        if (head == null) {
            return null;
        }
        if (getBSTSize(head) != 0) {
            return head;
        }
        Node leftAns = maxSubBSTHead1(head.left);
        Node rightAns = maxSubBSTHead1(head.right);
        return getBSTSize(leftAns) >= getBSTSize(rightAns) ? leftAns : rightAns;
    }


    // for test
    public static Node generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // for test
    public static Node generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            if (maxSubBSTHead1(head) != maxSubBSTHead2(head)) {
                System.out.println("Oops!");
            }
            if (maxSubBSTHead1(head) != MaxSubBSTHeadTest(head)) {
                System.out.println("Oops! for MaxSubBSTHeadTest");
            }
        }
        System.out.println("finish!");
    }

    public static Node MaxSubBSTHeadTest(Node root) {
        if (root == null) {
            return null;
        }
        return process1(root).maxSubBSTHead;
    }

    public static class Info1 {
        Node maxSubBSTHead;
        boolean isBST;
        int maxSubBSTSize;
        int max;
        int min;

        public Info1(Node _maxSubBSTHead, Boolean _isBST, int _maxSubBSTSize, int _max, int _min) {
            maxSubBSTHead = _maxSubBSTHead;
            isBST = _isBST;
            maxSubBSTSize = _maxSubBSTSize;
            max = _max;
            min = _min;
        }
    }

    public static Info1 process1(Node cur) {
        if (cur == null) {
            return null;
        }

        Info1 l = process1(cur.left);
        Info1 r = process1(cur.right);

        Node maxSubBSTHead ;
        boolean isBST;
        int maxSubBSTSize;
        int max;
        int min;

        if (l == null && r == null) {
            maxSubBSTHead = cur;
            isBST = true;
            maxSubBSTSize = 1;
            max = cur.value;
            min = cur.value;
        } else if (l != null && r == null) {
            if (l.isBST && l.max < cur.value) {
                maxSubBSTHead = cur;
                isBST = true;
                maxSubBSTSize = l.maxSubBSTSize + 1;
                max = Math.max(l.max, cur.value);
                min = Math.min(l.min, cur.value);
            } else {
                maxSubBSTHead = l.maxSubBSTHead;
                isBST = false;
                maxSubBSTSize = l.maxSubBSTSize;
                max = Math.max(l.max, cur.value);
                min = Math.min(l.min, cur.value);
            }
        } else if (l == null && r != null) {
            if (r.isBST && r.min > cur.value) {
                maxSubBSTHead = cur;
                isBST = true;
                maxSubBSTSize = r.maxSubBSTSize + 1;
                max = Math.max(r.max, cur.value);
                min = Math.min(r.min, cur.value);
            } else {
                maxSubBSTHead = r.maxSubBSTHead;
                isBST = false;
                maxSubBSTSize = r.maxSubBSTSize;
                max = Math.max(r.max, cur.value);
                min = Math.min(r.min, cur.value);
            }
        } else {
            if (l.isBST && r.isBST && cur.value > l.max && cur.value < r.min) {
                maxSubBSTHead = cur;
                isBST = true;
                maxSubBSTSize = l.maxSubBSTSize + r.maxSubBSTSize + 1;
                max = Math.max(Math.max(l.max, r.max), cur.value);
                min = Math.min(Math.min(l.min, r.min), cur.value);
            } else {
                // TODO: 【错误点-也不算错误】对数器的答案是 左右subBST大小相同时，选左head；而下面注释的语句是选择右head，所以报错了。
                //  单纯想要左右subBSTSize相同时，选择左侧head，则只需把 l.subSize > r.subSize 改为 l.subSize >= r.subSize ? l.subHead : r.subHead;
//                maxSubBSTHead = l.maxSubBSTSize > r.maxSubBSTSize ? l.maxSubBSTHead : r.maxSubBSTHead;
                maxSubBSTHead = l.maxSubBSTSize >= r.maxSubBSTSize ? l.maxSubBSTHead : r.maxSubBSTHead;
                isBST = false;
                maxSubBSTSize = Math.max(l.maxSubBSTSize, r.maxSubBSTSize);
                max = Math.max(Math.max(l.max, r.max), cur.value);
                min = Math.min(Math.min(l.min, r.min), cur.value);
            }
        }
        return new Info1(maxSubBSTHead, isBST, maxSubBSTSize, max, min);
    }


}
