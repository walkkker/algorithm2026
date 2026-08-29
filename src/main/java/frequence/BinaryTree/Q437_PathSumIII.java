package frequence.BinaryTree;

import java.util.*;

/**
 * 437. 路径总和 III
 *
 * <p>给定一棵二叉树的根节点 {@code root} 和一个整数 {@code targetSum}，返回该树中
 * 路径节点值之和等于 {@code targetSum} 的路径数量。
 *
 * <p>路径不需要从根节点开始，也不需要在叶子节点结束，但路径方向必须从父节点指向子节点。
 *
 * <p><b>专题归类：</b>自顶向下路径前缀和、HashMap频次和回溯恢复。
 * 当前文件保留了错误版本用于复盘；参见同目录《二叉树错题本.md》和
 * 《二叉树递归套路 易犯错误注意事项.md》的Q437条目。
 */
public class Q437_PathSumIII {

    HashMap<Integer, Integer> map = new HashMap<>();

    public int pathSum(TreeNode root, int targetSum) {
        // TODO: 【遗漏】同一个Q437_PathSumIII对象重复调用时，应先map.clear()，避免上次状态残留。
        // TODO: 【类型风险】路径前缀和应使用long，对应HashMap<Long, Integer>，避免累加溢出。
        map.put(0, 1);
        return process(root, 0, targetSum);
    }


    public int process(TreeNode cur, int preSum, int targetSum) {
        if (cur == null) {
            return 0;
        }
        preSum += cur.val;
        int ans = map.getOrDefault(preSum - targetSum, 0);
        // TODO: 【致命遗漏】递归子树前必须登记当前前缀和：map.put(preSum, map.getOrDefault(preSum, 0) + 1)。
        // 当前代码随后直接map.get(preSum) - 1；如果key不存在，还会因null自动拆箱触发NPE。
        ans += process(cur.left, preSum, targetSum) + process(cur.right, preSum, targetSum);
        map.put(preSum, map.get(preSum) - 1);
        if (map.get(preSum) == 0) {
            map.remove(preSum);
        }
        return ans;
    }
}
