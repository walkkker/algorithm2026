package frequence.BinaryTree;

import java.util.Stack;

/**
 * 230. 二叉搜索树中第 K 小的元素
 *
 * <p>给定一棵二叉搜索树的根节点 {@code root} 和一个整数 {@code k}，
 * 返回该二叉搜索树中第 {@code k} 小的节点值。
 *
 * <p><b>普通问题答案：</b>
 * BST的中序遍历严格升序，迭代中序遍历弹出的第k个节点就是第k小。
 * 找到后立即返回，时间复杂度为 {@code O(H + k)}，最坏为 {@code O(N)}；
 * 显式栈空间为 {@code O(H)}。
 *
 * <p><b>进阶问题：</b>
 * 如果二叉搜索树经常被修改（插入/删除），并且需要频繁查询第k小，应该如何优化？
 *
 * <p><b>进阶答案：</b>
 * 将BST改造成维护子树节点数量size的顺序统计树（Order Statistic Tree）。
 * 每个节点额外维护：
 * <pre>
 * size = size(left) + size(right) + 1
 * </pre>
 * 插入、删除以及旋转完成后必须调用 {@code pull(cur)} 更新size；所有修改都必须经过
 * 这棵树提供的API，否则size会失效。
 *
 * <p>查询第k小时，令 {@code leftSize = size(cur.left)}：
 * <pre>
 * k &lt;= leftSize      ：第k小在左子树
 * k == leftSize + 1 ：当前节点就是第k小
 * k &gt; leftSize + 1  ：进入右子树，并令k = k - leftSize - 1
 * </pre>
 * 每次可以跳过一整棵子树，查询复杂度由遍历节点数降为 {@code O(H)}。
 *
 * <p>为了防止普通BST退化成链表，需要使用带size增强属性的平衡搜索二叉树，例如
 * SBT、AVL、红黑树或Treap。平衡后插入、删除和查询第k小均为 {@code O(log N)}。
 *
 * <p><b>与当前SBT模板的对应关系：</b>
 * Q230的BST不允许重复值，只需要 {@code SBTSet<Integer>}；如果SBT的
 * {@code getIndex(index)} 使用0-based下标，则：
 * <pre>
 * 第k小 = sbtSet.getIndex(k - 1)
 * </pre>
 * 这正是SBT节点维护size的核心用途。若业务允许重复值，则需要增加count，或者使用
 * {@code (value, id)} 复合键区分重复元素。
 *
 * <p><b>面试选择：</b>
 * 只查询一次时直接使用当前中序遍历；动态插入/删除并频繁查询排名时，使用维护size的
 * SBTSet/Order Statistic Tree。不要为了单次查询重新构建一棵SBT。
 */
public class Q230_KthSmallestElementInABST {

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */
    class Solution {
        public int kthSmallest(TreeNode root, int k) {
            Stack<TreeNode> stack = new Stack<>();
            TreeNode cur  = root;
            while (cur != null || !stack.isEmpty()) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    if (--k == 0) {
                        return cur.val;
                    }
                    cur = cur.right;
                }
            }
            return 0;
        }
    }}
