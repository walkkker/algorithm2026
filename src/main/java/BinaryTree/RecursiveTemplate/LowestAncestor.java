package BinaryTree.RecursiveTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Leetcode 236 && Leetcode 235 （235是BST，O(logN)，基于BST特性，使用结论，找到第一个>=p&&<=q的节点(TODO: 错误点，具体看代码，因为注意有可能cur为q或者q) 就是公共祖先。 不能再往下找了，下面的都不是，p q不在一颗子树上了）
 *
 * 思路，非常重要：
 * 只考虑 ancestor != null的情况：（就跟isCBT一样，只考虑 isCBT=true的情况）
 * (1) 经过head (节点a,b分布在左右树)
 * TODO: 【错误1/2】少考虑了一种情况。 经过的场景少想了。 不只有左右分布A，B。 还有一种情况A/B是对方的祖先，此时head=A|B。！
 * TODO: 【错误2/2】看测试方法，发现A,B是从 BT中pickRandomNode(head)挑出来的，存在一样的可能性。 而我们之前的递归方法假设的是两个节点不同。还要考虑A==B的情况
 *
 * (2) 不经过head (左.ancestor!=null 或者 右.ancestor!=null)
 * <p>
 * 这样考虑的好处是， ancestor==null的情况，全部放到了 else逻辑里，我不需要关心细节了。
 */
public class LowestAncestor {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    public static class Info {
        Node lowestAncestor;
        boolean containsA;
        boolean containsB;

        public Info(Node _lowestAncestor, boolean _containsA, boolean _containsB) {
            lowestAncestor = _lowestAncestor;
            containsA = _containsA;
            containsB = _containsB;
        }
    }

    public static Info process(Node head, Node A, Node B) {
        if (head == null) {
            return new Info(null, false, false);
        }
        Info left = process(head.left, A, B);
        Info right = process(head.right, A, B);

        Node lowestAncestor;
        boolean containsA;
        boolean containsB;


        // TODO: 【错误2/2】看测试方法，发现A,B是从 BT中pickRandomNode(head)挑出来的，存在一样的可能性。 而我们之前的递归方法假设的是两个节点不同。
        if (A == B && head == A) {
            lowestAncestor = head;
        }
        // TODO: 【错误1/2】少考虑了一种情况。 经过的场景少想了。 不只有左右分布A，B。 还有一种情况A/B是对方的祖先，此时head=A|B。！
        else if (((left.containsA || right.containsA) && head == B)
                ||
                ((left.containsB || right.containsB) && head == A)) {
            lowestAncestor = head;
        } else if ((left.containsA && right.containsB) || (left.containsB && right.containsA)) {
            lowestAncestor = head;
        } else if (left.lowestAncestor != null || right.lowestAncestor != null) {
            lowestAncestor = left.lowestAncestor != null ? left.lowestAncestor : right.lowestAncestor;
        } else {
            lowestAncestor = null;
        }

        containsA = left.containsA || right.containsA || head == A;
        containsB = left.containsB || right.containsB || head == B;
        return new Info(lowestAncestor, containsA, containsB);
    }

    public static Node lowestAncestor2(Node head, Node A, Node B) {
        if (head == null) {
            return null;
        }
        return process(head, A, B).lowestAncestor;
    }


    // 对数器方法
    public static Node lowestAncestor1(Node head, Node o1, Node o2) {
        if (head == null) {
            return null;
        }
        // key的父节点是value
        HashMap<Node, Node> parentMap = new HashMap<>();
        parentMap.put(head, null);
        fillParentMap(head, parentMap);
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
        return cur;
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

    // for test
    public static Node pickRandomOne(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        fillPrelist(head, arr);
        int randomIndex = (int) (Math.random() * arr.size());
        return arr.get(randomIndex);
    }

    // for test
    public static void fillPrelist(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPrelist(head.left, arr);
        fillPrelist(head.right, arr);
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head = generateRandomBST(maxLevel, maxValue);
            Node o1 = pickRandomOne(head);
            Node o2 = pickRandomOne(head);
            if (lowestAncestor1(head, o1, o2) != lowestAncestor2(head, o1, o2)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("finish!");
    }


}




