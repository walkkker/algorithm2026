package frequence.Graph;

import java.util.HashMap;

/**
 * 1306. 跳跃游戏III。
 *
 * <p>扩展题：每个下标是图节点，只能跳到i-arr[i]或i+arr[i]，判断从start能否到达值为0的节点。
 * 落点不连续，不能套用Q55/Q45的最远可达区间模型。
 * 本文件置顶保留用户错误版本，故意不修正其方法体，供错误复盘，不作为正确答案使用。
 */
public class Q1306_JumpGameIII {

    /**
     * 【错误版本，必须review】用户2026-09-21的递归+HashMap实现，保留原始执行逻辑。
     */
    public static class Solution_superMistake_20260921 {
        HashMap<Integer, Boolean> map;

        /**
         * 【非常重要：这是错误代码，不是标准答案】
         *
         * <p><b>用户原理解：</b>III限定跳跃范围，只支持i+arr[i]或i-arr[i]。
         * 有点像dp？i是下标，dp[i]=？使用记忆化搜索，但是需要附带访问标记。
         *
         * <p><b>错误1：忽略题目给定的start。</b>原代码调用process(arr, 0)，总从0开始。
         * 正确语句应为{@code return process(arr, start);}。
         * 独立反例：arr=[1,1,0]、start=2，起点已经为0，应立即成功；从0却只能在0和1之间循环。
         *
         * <p><b>错误2：缺少成功的base case。</b>越界、访问标记只处理失败，
         * 没有任何入口直接返回true。应在边界检查后添加：
         * {@code if (arr[i] == 0) return true;}。
         * 最小反例arr=[0]、start=0应返回true，原代码返回false。
         *
         * <p><b>错误3：保存了旧值，却仍然使用覆盖后的新值！</b>
         * {@code int tmp = arr[i]; arr[i] = arr.length;}之后，arr[i]已变成访问哨兵，
         * 不再是跳跃距离。原代码实际跳到i-arr.length或i+arr.length，对任何合法i都越界。
         * 应改为{@code process(arr, i - tmp)}和{@code process(arr, i + tmp)}。
         * 这是“先覆盖，后续却误用新值”的典型错误，与指针重连、原地修改中的旧值丢失同类。
         *
         * <p><b>访问标记本身：</b>题目原值满足0 <= arr[i] < arr.length，
         * 所以arr.length可以作为哨兵；真正错误是覆盖后没有用tmp计算落点。
         * 末尾恢复arr[i]意味着它是“当前递归路径标记”，不是永久访问标记。
         *
         * <p><b>额外警惕：路径限制下的false不等于该节点全局不可达。</b>
         * 存在环时，某个节点可能经由当前路径上的祖先到达目标，却暂时被路径标记挡住。
         * 例如有向图A->B、B->A、A->目标，先搜B时会因A在路径上而得到false，
         * 但B实际上可以经A到达目标。不能把这种缓存解释为每个节点独立的可达性答案。
         * 这不等于断言修复前三处后的单起点搜索必然错误：祖先仍可能继续探索并找到目标；
         * 需要区别根调用的可达性判断与缓存中每个布尔值的语义。
         *
         * <p><b>推荐思路：</b>这是有环有向图上的DFS。只判断一个起点能否到达任意0，
         * 使用全局visited即可，无需额外HashMap，也无需退出时撤销visited。
         * 每个下标的出边固定，与到达它的历史无关，因此重复到达不会增加新的探索能力。
         * visited只表示已经开始探索，遇到已访问节点返回false是跳过重复分支，
         * 不是宣布这个节点全局不可达。每个节点至多展开一次，时间O(N)，含递归栈空间O(N)。
         *
         * <p><b>概念区分：</b>递归是实现机制，DFS是探索顺序；本题递归沿出边深入，属于DFS。
         * 记忆化保存完成计算的答案，而visited保存访问状态，两者不能混为一谈。
         */
        public boolean canReach(int[] arr, int start) {
            map = new HashMap<>();
            // TODO: 【错误1】忽略start。正确语句：return process(arr, start);
            return process(arr, 0);
        }

        /**
         * 【错误递归，原样保留】完整错误分析见canReach方法Javadoc。
         * 特别牢记：缺少arr[i]==0成功出口，以及覆盖arr[i]后仍用它计算跳跃位置。
         */
        private boolean process(int[] arr, int i) {
            if (i < 0 || i >= arr.length) {
                return false;
            }
            // TODO: 【错误2-遗漏】这里应增加：if (arr[i] == 0) return true;
            if (arr[i] == arr.length) {
                return false;
            }
            if (map.containsKey(i)) {
                return map.get(i);
            }

            int tmp = arr[i];
            arr[i] = arr.length;
            // TODO: 【致命错误3】已覆盖arr[i]！正确：boolean p1 = process(arr, i - tmp);
            boolean p1 = process(arr, i - arr[i]);
            // TODO: 正确：boolean p2 = process(arr, i + tmp);
            boolean p2 = process(arr, i + arr[i]);
            map.put(i, p1 || p2);
            arr[i] = tmp;
            return p1 || p2;
        }
    }
}
