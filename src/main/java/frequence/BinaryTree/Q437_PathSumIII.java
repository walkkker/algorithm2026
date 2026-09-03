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

    /**
     * 2026-09-03 复写版本：DFS/回溯 + 当前根路径前缀和 + HashMap频次统计。
     *
     * <p><b>核心点：</b>先查询答案，再把当前前缀和加入Map；递归结束后，List和Map必须
     * 成对回溯。核心不是维护整棵树的前缀和，而是始终只维护当前递归路径上的前缀和。
     *
     * <p><b>一、解题步骤：</b>
     * <ol>
     *     <li>使用{@code 0L}初始化路径前缀和与频次表，表示根节点之前的虚拟前缀和。</li>
     *     <li>到达节点后，根据父节点前缀和计算当前{@code pre}。</li>
     *     <li>先查询{@code map[pre - targetSum]}，得到以当前节点为终点的合法路径数量。</li>
     *     <li>再把当前{@code pre}加入List和Map，然后递归左右子树。</li>
     *     <li>离开当前节点时，从List和Map中撤销当前{@code pre}，恢复父节点现场。</li>
     * </ol>
     *
     * <p><b>二、遗漏点与易错点：</b>
     * <ol>
     *     <li>List和Map都必须初始化{@code 0L}，否则无法统计从根节点开始的合法路径。</li>
     *     <li>前缀和必须使用long；节点值沿长路径累加时，int可能溢出。</li>
     *     <li>必须先查询答案，再登记当前pre。若targetSum为0，先登记会把当前pre与自身配对，
     *     错误统计一条长度为0的路径。</li>
     *     <li>递归前，List和Map都要登记当前pre；退出时二者也必须成对撤销。</li>
     *     <li>Map只能保存当前递归路径，不能残留已经处理完的兄弟子树状态。</li>
     * </ol>
     *
     * <p>List只用于取得父节点前缀和，因此可以进一步改成递归参数{@code parentPre}；当前写法
     * 虽然不是最精简形式，但路径栈和回溯过程更直观。时间复杂度O(N)，额外空间O(H)。
     */
    class Solution20260903 {

        List<Long> preSum;
        Map<Long, Integer> map;
        int ans;

        public int pathSum(TreeNode root, int targetSum) {
            // 每次入口重新创建状态，避免同一个Solution对象连续调用时保留上一次的数据。
            preSum = new ArrayList<>();
            map = new HashMap<>();
            ans = 0;

            // TODO: 【错误-遗漏】List和Map都要加入虚拟前缀和0。
            // 当根路径之和等于targetSum时，pre - targetSum == 0，依靠它统计答案。
            preSum.add(0L);
            map.put(0L, 1);
            process(root, targetSum);
            return ans;
        }

        private void process(TreeNode cur, int targetSum) {
            if (cur == null) {
                return;
            }

            // TODO: 【类型易错】所有前缀和相关变量和Map的key都必须使用long。
            long pre = preSum.get(preSum.size() - 1) + cur.val;

            // Step1：根据pre[j] - pre[i] == targetSum，统计以cur为终点的路径。
            // TODO: 【顺序易错】必须先查询、后登记当前pre，防止targetSum==0时统计空路径。
            ans += map.get(pre - targetSum) == null ? 0 : map.get(pre - targetSum);

            // Step2：进入节点，List和Map必须同时登记当前前缀和。
            // TODO: 【错误-遗漏】不能只更新Map而忘记preSum List。
            preSum.add(pre);
            if (!map.containsKey(pre)) {
                map.put(pre, 1);
            } else {
                map.put(pre, map.get(pre) + 1);
            }

            process(cur.left, targetSum);
            process(cur.right, targetSum);

            // Step3：退出节点，严格恢复进入当前节点之前的路径现场。
            preSum.remove(preSum.size() - 1);
            if (map.get(pre) == 1) {
                map.remove(pre);
            } else {
                map.put(pre, map.get(pre) - 1);
            }
        }
    }

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
