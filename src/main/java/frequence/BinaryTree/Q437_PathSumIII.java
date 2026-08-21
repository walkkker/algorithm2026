package frequence.BinaryTree;

import java.util.*;

/**
 * 437. 路径总和 III
 *
 * <p>给定一棵二叉树的根节点 {@code root} 和一个整数 {@code targetSum}，返回该树中
 * 路径节点值之和等于 {@code targetSum} 的路径数量。
 *
 * <p>路径不需要从根节点开始，也不需要在叶子节点结束，但路径方向必须从父节点指向子节点。
 */
public class Q437_PathSumIII {

    HashMap<Integer, Integer> map = new HashMap<>();

    public int pathSum(TreeNode root, int targetSum) {
        map.put(0, 1);
        return process(root, 0, targetSum);
    }


    public int process(TreeNode cur, int preSum, int targetSum) {
        if (cur == null) {
            return 0;
        }
        preSum += cur.val;
        int ans = map.getOrDefault(preSum - targetSum, 0);
        ans += process(cur.left, preSum, targetSum) + process(cur.right, preSum, targetSum);
        map.put(preSum, map.get(preSum) - 1);
        if (map.get(preSum) == 0) {
            map.remove(preSum);
        }
        return ans;
    }
}
