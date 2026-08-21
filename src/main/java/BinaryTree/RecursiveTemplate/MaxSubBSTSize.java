package BinaryTree.RecursiveTemplate;

import java.util.ArrayList;

// LeetCode 333
// 事实证明，就是用左神的模板完全够用。
// 左神写的对数器版本也很好。 先写主递归，看当前节点是不是BST，否的话取左右孩子最大。  判断当前节点是不是BST时，使用中序遍历放到ArrayList里面。这样可以判断 1) 是否是BST 2） 该subBST的节点个数
public class MaxSubBSTSize {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int data) {
            this.val = data;
        }
    }

    public static class Info {
        boolean isBST;
        int max;
        int min;
        int maxSubBSTSize;

        public Info(boolean isBST, int max, int min, int maxSubBSTSize) {
            this.isBST = isBST;
            this.max = max;
            this.min = min;
            this.maxSubBSTSize = maxSubBSTSize;
        }
    }

    public static Info process(TreeNode head) {
        if (head == null) {
            return null;
        }

        Info left = process(head.left);
        Info right = process(head.right);

        boolean isBST = false;
        int max;
        int min;
        int maxSubBSTSize;

        if (left == null && right == null) {
            return new Info(true, head.val, head.val, 1);
        }

        if (left != null && right == null) {
            if (left.isBST && left.max < head.val) {
                isBST = true;
                maxSubBSTSize = left.maxSubBSTSize + 1;
            } else {
                isBST = false;
                maxSubBSTSize = left.maxSubBSTSize;
            }
            max = Math.max(left.max, head.val);
            min = Math.min(left.min, head.val);
            return new Info(isBST, max, min, maxSubBSTSize);
        }

        if (left == null && right != null) {
            if (right.isBST && head.val < right.min) {
                isBST = true;
                maxSubBSTSize = right.maxSubBSTSize + 1;
            } else {
                isBST = false;
                maxSubBSTSize = right.maxSubBSTSize;
            }
            // TODO: 【错误NPE!】直接复制上面没事，但是你review的时候，要注意 修改所有的变量！！！  -》 注意if条件！！！
            //            max = Math.max(left.max, head.val);
            //            min = Math.min(left.min, head.val);
            max = Math.max(right.max, head.val);
            min = Math.min(right.min, head.val);
            return new Info(isBST, max, min, maxSubBSTSize);
        }


        if (left.isBST && right.isBST && head.val > left.max && head.val < right.min) {
            isBST = true;
            maxSubBSTSize = left.maxSubBSTSize + right.maxSubBSTSize + 1;
        } else {
            isBST = false;
            maxSubBSTSize = Math.max(left.maxSubBSTSize, right.maxSubBSTSize);
        }
        max = Math.max(Math.max(left.max, head.val), right.max);
        min = Math.min(Math.min(left.min, head.val), right.min);
        return new Info(isBST, max, min, maxSubBSTSize);
    }

    public static int maxSubBSTSize1(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return process(head).maxSubBSTSize;
    }


    // 为了验证
    // 对数器方法
    public static int right(TreeNode head) {
        if (head == null) {
            return 0;
        }
        int h = getBSTSize(head);
        if (h != 0) {
            return h;
        }
        return Math.max(right(head.left), right(head.right));
    }

    // 为了验证
    // 对数器方法
    public static int getBSTSize(TreeNode head) {
        if (head == null) {
            return 0;
        }
        ArrayList<TreeNode> arr = new ArrayList<>();
        in(head, arr);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).val <= arr.get(i - 1).val) {
                return 0;
            }
        }
        return arr.size();
    }

    // 为了验证
    // 对数器方法
    public static void in(TreeNode head, ArrayList<TreeNode> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    // 为了验证
    // 对数器方法
    public static TreeNode generateRandomBST(int maxLevel, int maxValue) {
        return generate(1, maxLevel, maxValue);
    }

    // 为了验证
    // 对数器方法
    public static TreeNode generate(int level, int maxLevel, int maxValue) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) (Math.random() * maxValue));
        head.left = generate(level + 1, maxLevel, maxValue);
        head.right = generate(level + 1, maxLevel, maxValue);
        return head;
    }

    // 为了验证
    // 对数器方法
    public static void main(String[] args) {
        int maxLevel = 4;
        int maxValue = 100;
        int testTimes = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = generateRandomBST(maxLevel, maxValue);
            if (maxSubBSTSize1(head) != right(head)) {
                System.out.println("出错了！");
            }
            if (maxSubBSTSizeTest(head) != right(head)) {
                System.out.println("maxSubBSTSizeTest 出错了");
            }
        }
        System.out.println("测试结束");
    }


    public static int maxSubBSTSizeTest(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return process1(root).maxSubBSTSize;
    }

    public static class Info1 {
        boolean isBST;
        int maxSubBSTSize;
        int max;
        int min;

        public Info1(Boolean _isBST, int _maxSubBSTSize, int _max, int _min) {
            isBST = _isBST;
            maxSubBSTSize = _maxSubBSTSize;
            max = _max;
            min = _min;
        }
    }

    public static Info1 process1(TreeNode cur) {
        if (cur == null) {
            return null;
        }

        Info1 l = process1(cur.left);
        Info1 r = process1(cur.right);

        boolean isBST;
        int maxSubBSTSize;
        int max;
        int min;

        if (l == null && r == null) {
            isBST = true;
            maxSubBSTSize = 1;
            max = cur.val;
            min = cur.val;
        } else if (l != null && r == null) {
            if (l.isBST && l.max < cur.val) {
                isBST = true;
                maxSubBSTSize = l.maxSubBSTSize + 1;
                max = Math.max(l.max, cur.val);
                min = Math.min(l.min, cur.val);
            } else {
                isBST = false;
                maxSubBSTSize = l.maxSubBSTSize;
                max = Math.max(l.max, cur.val);
                min = Math.min(l.min, cur.val);
            }
        } else if (l == null && r != null) {
            if (r.isBST && r.min > cur.val) {
                isBST = true;
                maxSubBSTSize = r.maxSubBSTSize + 1;
                max = Math.max(r.max, cur.val);
                min = Math.min(r.min, cur.val);
            } else {
                isBST = false;
                maxSubBSTSize = r.maxSubBSTSize;
                max = Math.max(r.max, cur.val);
                min = Math.min(r.min, cur.val);
            }
        } else {
            if (l.isBST && r.isBST && cur.val > l.max && cur.val < r.min) {
                isBST = true;
                maxSubBSTSize = l.maxSubBSTSize + r.maxSubBSTSize + 1;
                max = Math.max(Math.max(l.max, r.max), cur.val);
                min = Math.min(Math.min(l.min, r.min), cur.val);
            } else {
                // TODO: 【错误点-也不算错误】对数器的答案是 左右subBST大小相同时，选左head；而下面注释的语句是选择右head，所以报错了。
                //  单纯想要左右subBSTSize相同时，选择左侧head，则只需把 l.subSize > r.subSize 改为 l.subSize >= r.subSize ? l.subHead : r.subHead;
//                maxSubBSTHead = l.maxSubBSTSize > r.maxSubBSTSize ? l.maxSubBSTHead : r.maxSubBSTHead;
                isBST = false;
                maxSubBSTSize = Math.max(l.maxSubBSTSize, r.maxSubBSTSize);
                max = Math.max(Math.max(l.max, r.max), cur.val);
                min = Math.min(Math.min(l.min, r.min), cur.val);
            }
        }
        return new Info1(isBST, maxSubBSTSize, max, min);
    }


}
