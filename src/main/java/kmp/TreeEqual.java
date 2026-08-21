package kmp;


import java.util.*;

/**
 * TODO： 方法1是最简单&&最直接的方法 （面试用这个 递归）：
 *          1. 基于二叉树，写一个 isIdentical(TreeNode a, TreeNode b)方法。 用于比较两个二叉树是否相同
 *          2. 层级遍历root每个节点，对每个节点isIdentical()
 *
 * 用KMP方法其实是要注意大坑的： 具体见leetcode题解
 * TODO：【特别注意】使用Integer[] + KMP 大坑，见 kmp.TestForIntegerGreaterThan127
 * // TODO: 【大错错误】这道题犯了一个非常不应该的错误。 你不能转成String，使用#分割。 12#null#null 与 2#null#null 也会认为是子树！！！！（这里 12是一个节点，  2是一个节点的意思）
 * // TODO: 所以，只能使用 list作为容器去装
 * // TODO: 【超重要-KMP改造-参数由char[]变成String[]】将getIndexOf(string1, string2)转换成 getIndexOf(String[] s1, String[] s2) 逻辑都是一样的！！！！
 * // 错误版本我放下面了，一定要注意看。除此之外，还有很多小错：
 * // (1)String a类型的边界检查，a.length==1!!! 这是类的方法！！不是数组的属性呀！
 *
 *
 * TODO: 本题犯了大错。
 *       简而言之，本题涉及到
 *       （1）序列化选择（应该是List->String[]，而不是List->String(用#分割)），反例见下文 (ps, 只要涉及二叉树序列化，null要记录为"null")
 *       （2）基于（1），KMP算法需要改造，非常微小。 从原始的char[] 变成String[]即可。
 * https://leetcode.cn/problems/subtree-of-another-tree/
 *
 * TODO: 这里当时还有一个大坑，因为我一开始是拼成的字符串String。只有一个节点的时候，y能到9，很惊讶！！！ 其实很正常，因为 123#null#null !!! String->char[]时，length很长的！！！
 */
public class TreeEqual {
    /**
     * 方法1是最简单&&最直接的方法：
     *      1. 基于二叉树，写一个 isIdentical(TreeNode a, TreeNode b)方法。 用于比较两个二叉树是否相同
     *      2. 层级遍历root每个节点，对每个节点isIdentical()
     */
    class Solution {
        public boolean isSubtree(TreeNode root, TreeNode subRoot) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                TreeNode cur = queue.poll();
                if (isIdentical(cur, subRoot)) {
                    return true;
                }
                if (cur.left != null) {
                    queue.add(cur.left);
                }
                if (cur.right != null) {
                    queue.add(cur.right);
                }
            }
            return false;
        }

        public boolean isIdentical(TreeNode a, TreeNode b) {
            if (a == null && b == null) {
                return true;
            }
            if ((a == null && b != null) || (a != null && b == null)) {
                return false;
            }

            return a.val == b.val && isIdentical(a.left, b.left) && isIdentical(a.right, b.right);
        }
    }


    // 20260606 使用的kmp + Integer[] 方法，依然有错误，见开头和本题注释
    // TODO: 写一个麻烦的，结合kmp算法的
// TODO: 【特别注意 + 错误点】Integer -128 ~ 127有缓存，超出可能导致 Integer[] arr中对象使用==，值一样，但是返回false！！！
    /**
     Integer a = 128;
     Integer b = 128;
     System.out.println(a == b);   // false !!!

     a = 127;
     b = 127;
     System.out.println(a == b);  // true

     int c = 129;
     int d = 129;
     System.out.println(c == d); // true

     */
    class Solution2 {
        public boolean isSubtree(TreeNode root, TreeNode subRoot) {
            Integer[] arr1 = getSerArr(root);
            Integer[] arr2 = getSerArr(subRoot);
            System.out.println(Arrays.toString(arr1));
            System.out.println(Arrays.toString(arr2));

            int ans = indexOf(arr1, arr2);
            System.out.println(ans);
            return ans != -1;
        }

        public int[] getNextArray(Integer[] arr) {
            if (arr.length == 1) {
                return new int[] { -1 };
            }
            int len = arr.length;
            int[] next = new int[len];
            next[0] = -1;
            next[1] = 0;
            int c = 0;
            int i = 2;
            while (i < len) {
                // if (arr[c] == arr[i - 1]) {
                if (isEqual(arr[c], arr[i - 1])) {
                    next[i++] = ++c;
                } else if (c != 0) {
                    c = next[c];
                } else {
                    i++;
                }
            }
            return next;
        }

        public int indexOf(Integer[] arr1, Integer[] arr2) {
            int[] next = getNextArray(arr2);
            int x = 0;
            int y = 0;
            while (x < arr1.length && y < arr2.length) {
                if (isEqual(arr1[x], arr2[y])) {
                    x++;
                    y++;
                } else if (y != 0) {
                    y = next[y];
                } else {
                    x++;
                }
            }
            return y == arr2.length ? x - y : -1;
        }

        public Integer[] getSerArr(TreeNode head) {
            List<Integer> list = new ArrayList<>();
            pre(head, list);
            Integer[] ans = new Integer[list.size()];
            int i = 0;
            for (Integer ele : list) {
                ans[i++] = ele;
            }
            return ans;
        }

        // TODO: 【错误点】 这种遍历时加入到List的方法，递归时，千万别漏了加参数
        public void pre(TreeNode cur, List<Integer> list) {
            if (cur == null) {
                list.add(null);
                return;
            }
            list.add(cur.val);
            // TODO: 【错误】遗漏List参数
//            pre(cur.left);
//            pre(cur.right);
            pre(cur.left, list);
            pre(cur.right, list);
        }

        // TODO: 【错误点】因为有null，所有Integer.compare也不能用。 你不设置Integer.MAX_VALUE作为无效值，那就自己实现方法检查相等
        public boolean isEqual(Integer a, Integer b) {
            if (a ==  null && b == null) {
                return true;
            }
            if (a != null && b != null) {
                return a.equals(b);
            }
            return false;
        }
    }


    class Solution3 {
        // 先序遍历/后序遍历/层序遍历 可以 序列化一个二叉树（需要带上Null）。本题依然是把序列化放在list里面， 而不是变成string。
        public boolean isSubtreeWrongVersion(TreeNode root, TreeNode subRoot) {
            String s1 = preSerialWrongVersion(root);
            String s2 = preSerialWrongVersion(subRoot);
            System.out.println(s1);
            System.out.println(s2);
            return getIndexOfWrongVersion(s1, s2) != -1;
        }

        public String preSerialWrongVersion(TreeNode root) {
            List<Integer> list = new ArrayList<>();
            preWrongVersion(root, list);
            StringBuilder sb = new StringBuilder('#');
            for (Integer ele : list) {
                sb.append(ele == null ? "null" : ele);
                // TODO: 【错误】这里不要漏了也要加'#'
                sb.append('#');
            }
            return sb.toString();
        }

        public void preWrongVersion(TreeNode cur, List<Integer> list) {
            if (cur == null) {
                list.add(null);
                return;
            }
            list.add(cur.val);
            preWrongVersion(cur.left, list);
            preWrongVersion(cur.right, list);
        }

        public int[] getNextArrayWrongVersion(char[] chs) {
            if (chs.length == 1) {
                return new int[]{-1};
            }
            int[] next = new int[chs.length];
            next[0] = -1;
            next[1] = 0;
            int i = 2;
            int c = 0;
            while (i < next.length) {
                if (chs[c] == chs[i - 1]) {
                    next[i++] = ++c;
                } else if (c == 0) {
                    next[i++] = 0;
                } else {
                    c = next[c];
                }
            }
            return next;
        }

        public int getIndexOfWrongVersion(String s, String m) {
            if (s.length() < m.length()) {
                return -1;
            }
            char[] chs1 = s.toCharArray();
            char[] chs2 = m.toCharArray();
            int[] next = getNextArrayWrongVersion(chs2);
            int x = 0;
            int y = 0;
            System.out.println(chs1.length);
            System.out.println(chs2.length);

            while (x < chs1.length && y < chs2.length) {
                if (chs1[x] == chs2[y]) {
                    x++;
                    y++;
                } else if (y == 0) {
                    x++;
                } else {
                    y = next[y];
                }
                System.out.println(y);
            }
            return y == chs2.length ? x - y : -1;
        }

        public boolean isSubtree(TreeNode root, TreeNode subRoot) {
            String[] rootArr = preSerial(root);
            String[] subArr = preSerial(subRoot);
            return getIndexOf(rootArr, subArr) != -1;
        }

        public String[] preSerial(TreeNode root) {
            List<TreeNode> list = new ArrayList<>();
            pre(root, list);
            String[] arr = new String[list.size()];
            int i = 0;
            for (TreeNode ele : list) {
                arr[i++] = ele == null ? "null" : String.valueOf(ele.val);
            }
            return arr;
        }

        public void pre(TreeNode cur, List<TreeNode> list) {
            if (cur == null) {
                list.add(null);
                return;
            }
            list.add(cur);
            pre(cur.left, list);
            pre(cur.right, list);
        }

        public int[] getNextArray(String[] arr) {
            if (arr.length == 1) {
                return new int[]{-1};
            }
            int[] next = new int[arr.length];
            next[0] = -1;
            next[1] = 0;
            int i = 2;
            int c = 0;
            while (i < next.length) {
                if (arr[c].equals(arr[i - 1])) {
                    next[i++] = ++c;
                } else if (c == 0) {
                    next[i++] = 0;
                } else {
                    c = next[c];
                }
            }
            return next;
        }

        public int getIndexOf(String[] arr1, String[] arr2) {
            if (arr1.length < arr2.length) {
                return -1;
            }
            int[] next = getNextArray(arr2);
            int x = 0;
            int y = 0;
            while (x < arr1.length && y < arr2.length) {
                if (arr1[x].equals(arr2[y])) {
                    x++;
                    y++;
                } else if (y == 0) {
                    x++;
                } else {
                    y = next[y];
                }
            }
            return y == arr2.length ? x - y : -1;
        }

    }


}

// Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
