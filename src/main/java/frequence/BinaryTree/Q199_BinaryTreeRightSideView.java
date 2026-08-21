package frequence.BinaryTree;

import java.util.*;

/**
 * 199. 二叉树的右视图
 *
 * TODO：【思路】层级遍历加强版（识别每一层），每一层最后一个节点curEnd 汇总就是答案。
 *
 * <p>给定一个二叉树的根节点 {@code root}，假设从该二叉树的右侧观察，
 * 返回按照从顶部到底部顺序能够看到的节点值。
 */
public class Q199_BinaryTreeRightSideView {

    public List<Integer> rightSideView(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Integer> ans = new ArrayList<>();
        // 层级遍历，收集每一层的最后一个节点（等同于end）
        TreeNode curEnd = root;
        TreeNode nextEnd = null;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur.left != null) {
                queue.add(cur.left);
                nextEnd = cur.left;
            }

            if (cur.right != null) {
                queue.add(cur.right);
                nextEnd = cur.right;
            }

            if (cur == curEnd) {  // TODO：这一步只存在两部分：1.【必做】curEnd=nextEnd 2. 【按题目要求收集信息】
                curEnd = nextEnd;
                ans.add(cur.val);
            }
        }
        return ans;
    }
}
