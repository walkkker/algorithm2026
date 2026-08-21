package BinaryTree.RecursiveTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MaxDistance {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static class Info {
        int maxDistance;
        int height;

        public Info(int m, int h) {
            maxDistance = m;
            height = h;
        }
    }

    public static Info process(Node head) {
        if (head == null) {
            return null;
        }

        Info left = process(head.left);
        Info right = process(head.right);

        if (left == null && right == null) {
            return new Info(1, 1);
        }

        if (left != null && right == null) {
            return new Info(Math.max(left.maxDistance, left.height + 1), left.height + 1);
        }

        if (left == null && right != null) {
            return new Info(Math.max(right.maxDistance, right.height + 1), right.height + 1);
        }
        int maxDistance;
        int height;

        maxDistance = Math.max(Math.max(left.maxDistance, right.maxDistance), left.height + right.height + 1);
        height = Math.max(left.height, right.height) + 1;
        return new Info(maxDistance, height);

    }

    public static int maxDistance2(Node head) {
        if (head == null) {
            return 0;
        }
        return process(head).maxDistance;
    }


    // 对数器
    public static int maxDistance1(Node head) {
        if (head == null) {
            return 0;
        }
        ArrayList<Node> arr = getPrelist(head);
        HashMap<Node, Node> parentMap = getParentMap(head);
        int max = 0;
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i; j < arr.size(); j++) {
                max = Math.max(max, distance(parentMap, arr.get(i), arr.get(j)));
            }
        }
        return max;
    }

    public static ArrayList<Node> getPrelist(Node head) {
        ArrayList<Node> arr = new ArrayList<>();
        fillPrelist(head, arr);
        return arr;
    }

    public static void fillPrelist(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPrelist(head.left, arr);
        fillPrelist(head.right, arr);
    }

    public static HashMap<Node, Node> getParentMap(Node head) {
        HashMap<Node, Node> map = new HashMap<>();
        map.put(head, null);
        fillParentMap(head, map);
        return map;
    }

    public static void fillParentMap(Node head, HashMap<Node, Node> parentMap) {
        if (head.left != null) {
            parentMap.put(head.left, head);
            fillParentMap(head.left, parentMap);
        }
        if (head.right != null) {
            parentMap.put(head.right, head);
            fillParentMap(head.right, parentMap);
        }
    }

    public static int distance(HashMap<Node, Node> parentMap, Node o1, Node o2) {
        HashSet<Node> o1Set = new HashSet<>();
        Node cur = o1;
        o1Set.add(cur);
        while (parentMap.get(cur) != null) {
            cur = parentMap.get(cur);
            o1Set.add(cur);
        }
        cur = o2;
        while (!o1Set.contains(cur)) {
            cur = parentMap.get(cur);
        }
        Node lowestAncestor = cur;
        cur = o1;
        int distance1 = 1;
        while (cur != lowestAncestor) {
            cur = parentMap.get(cur);
            distance1++;
        }
        cur = o2;
        int distance2 = 1;
        while (cur != lowestAncestor) {
            cur = parentMap.get(cur);
            distance2++;
        }
        return distance1 + distance2 - 1;
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
            if (maxDistance1(head) != maxDistance2(head)) {
                System.out.println("Oops! for maxDistance1&2");
            }
            if (maxDistance1(head) != maxDistance3(head)) {
                System.out.println("Oops! for maxDistance1&3");
            }
        }
        System.out.println("finish!");
    }


    public static int maxDistance3(Node root) {
        if (root == null) {
            return 0;
        }
        return process1(root).maxDistance;
    }

    public static class Info1 {
        int maxDistance;
        int height;

        public Info1(int _maxDistance, int _height) {
            maxDistance = _maxDistance;
            height = _height;
        }
    }

    public static Info1 process1(Node cur) {
        if (cur == null) {
            return new Info1(0, 0);
        }

        Info1 left = process1(cur.left);
        Info1 right = process1(cur.right);

        int maxDistance;
        int height;

        maxDistance = Math.max(Math.max(left.maxDistance, right.maxDistance), left.height + right.height + 1);
        height = Math.max(left.height, right.height) + 1;
        return new Info1(maxDistance, height);
    }



}
