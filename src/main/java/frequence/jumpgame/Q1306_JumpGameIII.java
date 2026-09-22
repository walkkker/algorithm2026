package frequence.jumpgame;

import java.util.HashMap;

/**
 * 1306. 跳跃游戏III。
 *
 * <p><b>【重点建模】典型的图DFS：图使用数组隐式表示。</b>
 * 用户理解：“这道题算是图DFS，只不过形态是在数组上。”这个理解正确，专业术语为隐式图
 * （Implicit Graph）：不显式存储邻接表，而是在访问节点时根据规则计算邻居。
 *
 * <ul>
 * <li>顶点：数组下标i，不是arr[i]；两个位置即使值相同，也是不同顶点。</li>
 * <li>有向边：从i到i-arr[i]或i+arr[i]，只保留范围内的落点。</li>
 * <li>起点：start；目标顶点：满足arr[i]==0的位置。</li>
 * <li>访问标记：visited[i]，避免环和重复探索。</li>
 * </ul>
 *
 * <p>普通邻接表DFS通过adj.get(i)枚举邻居，本题直接计算两个落点，探索模型相同。
 * 向左或向右跳不代表无向图：能从i跳到j，不保证能从j直接跳回i。
 * 数据存储在数组中，不代表必须用数组贪心或DP；应看状态之间的连接关系。
 * 岛屿数量是二维网格上的图搜索，本题是一维数组上的图搜索。
 * 每个节点至多两条出边，所以图DFS的O(V+E)在这里为O(N)。
 * 专题详解见同目录《隐式图DFS_数组也是图.md》。
 *
 * <p>扩展题：每个下标是图节点，只能跳到i-arr[i]或i+arr[i]，判断从start能否到达值为0的节点。
 * 落点不连续，不能套用Q55/Q45的最远可达区间模型。
 * 本文件置顶保留用户错误版本，故意不修正其方法体，供错误复盘，不作为正确答案使用；
 * 下方SolutionReviewed20260921是用户修正后的DFS版本。
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

    /**
     * 用户修正版本（2026-09-21）：访问标记数组boolean[] visited。
     * 保留先计算p1、再计算p2的原写法，不修改输入数组。
     */
    public static class SolutionReviewed20260921 {
        /**
         * 从start开始做图DFS，判断能否到达任意值为0的节点。
         * 每次入口调用创建独立visited，不需要HashMap结果缓存。
         * 时间O(N)，额外空间O(N)，包含访问数组和最坏O(N)的递归栈。
         * TODO: 【工程边界】很长的路径可能导致Java递归栈溢出；可改为显式栈DFS或队列BFS。
         */
        public boolean canReach(int[] arr, int start) {
            return dfs(arr, start, new boolean[arr.length]);
        }

        /**
         * <p><b>用户理解：</b>重点是避免重复探索，因此返回false不对当前节点结果产生影响。
         *
         * <p><b>精确修正：</b>不影响的是“最初start能否到达0”的最终结论，
         * 不保证每个中间调用都返回“从该节点单独出发”的独立可达性答案。
         * visited[i]表示已经开始探索，不是该节点的答案为false，也不要求它已经探索结束。
         * 标记在两个递归调用之前设置，因此再次遇到它时，第一次调用可能还在递归栈中。
         *
         * <p><b>返回false的含义：</b>跳过重复分支，不在这条分支重复报告成功；
         * 不是宣告该节点全局不可达。第一次访问它的调用已承担探索其出边的责任，
         * 找到的成功结果会沿调用链以逻辑或向上传递。
         * 图的出边仅由下标和数组决定，与到达历史无关，重复访问不会增加新路径选择。
         * 所以visited无需恢复为false，不同分支共享访问记录也不会影响起点的最终判断。
         *
         * <p><b>反例说明“已访问不等于之前返回false”：</b>arr=[1,1,0]、start=1。
         * 首次访问1并标记，然后访问0；0的右跳再次遇到1，此时1仍在递归栈中，尚无答案。
         * 重复分支返回false后，首次访问1的调用继续向右访问2，发现0，最终返回true。
         * 其中位置0的调用返回false，但单独从0出发其实也能经1到2；不能把中间结果当全局缓存。
         *
         * <p><b>步骤：</b>边界及去重检查；检查是否命中0；标记已访问；搜索左右出边；合并结果。
         *
         * <p>TODO: 【可优化，不是错误】当前先计算p1和p2，即使p1=true仍会执行p2。
         * 所以“还在搜索就说明之前没有true”也不成立。
         * 可以直接写{@code return dfs(arr, i - arr[i], visited) || dfs(arr, i + arr[i], visited);}
         * 利用短路或在左侧成功后跳过右侧。保留用户原代码方便复盘。
         */
        private boolean dfs(int[] arr, int i, boolean[] visited) {
            // 步骤1：先判越界，再读取visited；已访问时false只是跳过信号，不是不可达缓存。
            if (i < 0 || i >= arr.length || visited[i]) {
                return false;
            }
            // 步骤2：成功出口，目标是元素值为0，不是下标为0。
            if (arr[i] == 0) {
                return true;
            }
            // 步骤3：先标记再递归，阻断环；不修改arr，所以跳跃距离不会被覆盖。
            visited[i] = true;
            // 步骤4：保留用户分别计算两条分支的实现。
            boolean p1 = dfs(arr, i - arr[i], visited);
            boolean p2 = dfs(arr, i + arr[i], visited);
            // 不撤销visited，重复到达没有新探索价值。
            return p1 || p2;
        }
    }
}
