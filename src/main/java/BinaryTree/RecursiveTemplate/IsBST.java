package BinaryTree.RecursiveTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Method 1(面试肯定用这个): BST 使用中序遍历 -> 必须是升序的
 * Method 2: 套路
 * 优化一下 -> base case -> Info包含isBST,max,min -> 使用null的话构建不出来 -> 所以base case = head
 */
public class IsBST {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static boolean isBST1(Node head) {
        if (head == null) {
            return true;
        }
        List<Integer> list = new ArrayList<>();
        in(head, list);

        // 此时要检查是否是升序， 如果不是 ，直接返回false
        for (int i = 0; i <= list.size() - 2; i++) {
            // TODO: 【错误】这里一定要注意，BST不允许存在相同值。 所以 i>=i+1 都return false
            //  if (list.get(i) > list.get(i + 1)) {
            if (list.get(i) >= list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static void in(Node head, List<Integer> list) {
        if (head == null) {
            return;
        }
        in(head.left, list);
        list.add(head.value);
        in(head.right, list);
    }

    public static class Info {
        boolean isBST;
        int max;
        int min;

        public Info(boolean _isBST, int _max, int _min) {
            isBST = _isBST;
            max = _max;
            min = _min;
        }
    }


    public static boolean isBST2(Node root) {
        if (root == null) {
            return true;
        }
        return process(root).isBST;
    }

    public static Info process(Node head) {
        if (head.left == null && head.right == null) {
            return new Info(true, head.value, head.value);
        }

        Info left = null;
        Info right = null;

        if (head.left != null) {
            left = process(head.left);
        }

        if (head.right != null) {
            right = process(head.right);
        }

        boolean isBST = false;
        int max;
        int min;

        if (left == null && right != null) {

            if (right.isBST && head.value < right.min) {
                isBST = true;
            }
            max = Math.max(head.value, right.max);
            min = Math.min(head.value, right.min);
            return new Info(isBST, max, min);
        }

        if (left != null && right == null) {

            if (left.isBST && head.value > left.max) {
                isBST = true;
            }
            max = Math.max(head.value, left.max);
            min = Math.min(head.value, left.min);
            return new Info(isBST, max, min);
        }

        // 第三种情况， left != null && right != null
        if (left.isBST && right.isBST && left.max < head.value && right.min > head.value) {
            isBST = true;
        }

        max = Math.max(head.value, Math.max(left.max, right.max));
        min = Math.min(head.value, Math.min(left.min, right.min));
        return new Info(isBST, max, min);
    }


    public static boolean isBST3(Node root) {
        if (root == null) {
            return true;
        }
        return process2(root).isBST;
    }

    public static Info process2(Node cur) {
        if (cur == null) {
            return null;
        }

        Info left = process2(cur.left);
        Info right = process2(cur.right);

        Boolean isBST = false;
        int max;
        int min;


        if (left == null && right == null) {
            return new Info(true, cur.value, cur.value);
        }
        if (left != null && right == null) {
            if (left.isBST && cur.value > left.max) {
                isBST = true;
            }
            max = Math.max(cur.value, left.max);
            min = Math.min(cur.value, left.min);
            return new Info(isBST, max, min);
        }

        if (left == null && right != null) {
            if (right.isBST && cur.value < right.min) {
                isBST = true;
            }
            max = Math.max(cur.value, right.max);
            min = Math.min(cur.value, right.min);
            return new Info(isBST, max, min);
        }

        if (left.isBST && right.isBST && cur.value < right.min && cur.value > left.max) {
            isBST = true;
        }
        max = Math.max(Math.max(cur.value, left.max), right.max);
        min = Math.min(Math.min(cur.value, left.min), right.min);
        return new Info(isBST, max, min);
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
            if (isBST1(head) != isBST2(head)) {
                System.out.println(isBST1(head) + " : " + isBST2(head));
                System.out.println("Oops!, isBST2 is wrong");
                break;
            }
            if (isBST1(head) != isBST3(head)) {
                System.out.println(isBST1(head) + " : " + isBST3(head));
                System.out.println("isBST3 is wrong!");
                break;
            }
        }
        System.out.println("finish!");
    }

}
