package SlidingWindow;

import java.util.*;

//  https://leetcode-cn.com/problems/gas-station/

/**
 *
 * 一、实现步骤：
 *    1. diff
 *    2. diff 倍长
 *    3. preSum -》 其实此时就可以求出，从任何一个起点出发，中间节点的前缀和了 （相对于preSum数组是累加和）
 *    4. slidingWindow 求窗口内最小值。-> 对应从任何一个起点出发，中间节点的最小前缀和 -> 判断是否>=0
 *
 * 二、核心记忆：长度为 n 的滑动窗口，最小值相对起点不小于 0。
 *    拆解：
 *    ## 加油站 · 单调队列解法（速记）
 *
 *    1. **差分**：`diff[i] = gas[i] - cost[i]`
 *    2. **倍长**：数组拉长至 `2n`，消除环形边界
 *    3. **前缀和**：`diff[i] += diff[i-1]`，将油量变化转为累计值
 *    4. **滑窗**：维护长度固定为 `n` 的窗口 `[L, R]`
 *    5. **单调队列**：队头维护窗口内前缀和的最小值
 *    6. **判定**：若 `min(diff[L..R]) - diff[L-1] >= 0`，则起点 `L` 可行。 => 业务含义：任一起点开始的前缀和都>=0，那么目标就变成了求 [L,R]范围内的最小前缀和
 *
 *
 * 本题难点在于 求diff数组 和 求前缀和。
 *      最难点在于，要求一环中的任一前缀和>=0 =》 在x >=0 意味着 假设当前在x， 如果从x开到y后 油量剩余0，也就是说 在当前start下， x->y是支持的。 不会没油，
 *      重点就是 == 0 也可以，就是刚好能够开往下一站。
 *
 *
 * 本题包含了两个解法：
 * （1）单调队列 -> 滑动窗口内最小值更新结构 -> 重点在于预处理 以及对 下标转换的理解
 *      - 但是这个方法不是最优解
 *      - 这个方法可以扩展题意：返回boolean[] 标记每个加油站作为起点的情况下，是否可以完成一周骑行
 * （2）最优解：贪心算法   忘了可以问ai,元宝回答的就很好。  原理就是 minS 的下一个作为start，就会把minS抬的最高。 如果此时遍历完前缀和，发现最终s>=0.那也就意味着，从start开始，最低点就是之前的minS对应的点位 它的值会等同于最终s，即 >= 0.
 *
 */
public class GasStation {

    /**
     * 20260709 看了思路，一遍过，思路虽然复杂，但是很清晰，很巧妙，值得背诵！！！
     实现步骤：
     1. 求diff
     2. diff 倍长
     3. preSum -》 其实此时就可以求出，从任何一个起点出发，中间节点的前缀和了 （相对于preSum数组是累加和）
     4. slidingWindow 求窗口内最小值。-> 对应从任何一个起点出发，中间节点的最小前缀和 -> 判断是否>=0
     */
    class Solution20260709 {
        public int canCompleteCircuit(int[] gas, int[] cost) {
            int len = gas.length;
            int[] diff = new int[len * 2];
            for (int i = 0; i < diff.length; i++) {
                diff[i] = gas[i % len] - cost[i % len];
            }
            int[] preSum = new int[len * 2];
            preSum[0] = diff[0];
            for (int i = 1; i < preSum.length; i++) {
                preSum[i] = preSum[i - 1] + diff[i];
            }

            Deque<Integer> minDeque = new LinkedList<>();
            int L = 0;
            boolean[] ans = new boolean[len];
            int i = 0;
            for (int R = 0; R < preSum.length; R++) {
                while (!minDeque.isEmpty() && preSum[R] <= preSum[minDeque.peekLast()]) {
                    minDeque.pollLast();
                }
                minDeque.offerLast(R);

                if (R - L + 1 > len) {
                    if (minDeque.peekFirst() == L) {
                        minDeque.pollFirst();
                    }
                    L++;
                }

                if (R - L + 1 == len) {
                    int min = preSum[minDeque.peekFirst()];
                    int minus = L - 1 >= 0 ? preSum[L - 1] : 0;
                    if (min - minus >= 0) {
                        ans[i++] = true;
                    } else {
                        ans[i++] = false;
                    }
                }

                if (i == len) {
                    break;
                }
            }

            for (i = 0; i < ans.length; i++) {
                if (ans[i]) {
                    return i;
                }
            }
            return -1;
        }
    }


    class Solution {
        public int canCompleteCircuit(int[] gas, int[] cost) {
            int n = gas.length;
            int[] diff = new int[n * 2];
            // TODO: 【错误】赋值完全错了，数组越界
            // for (int i = 0; i < rest.length; i++) {
            //     rest[i] = rest[i + n] = gas[i] - cost[i];
            // }
            for (int i = 0; i < diff.length; i++) {
                diff[i] = i < n ? gas[i] - cost[i] : diff[i - n];
            }
            for (int i = 1; i < diff.length; i++) {
                diff[i] = diff[i - 1] + diff[i];
            }

            boolean[] ans = new boolean[n];
            int index = 0;


            // TODO: 【错误】这里 【Deque】是对的， 我写成了 DeQue！！！ 要记忆好！！！
            Deque<Integer> minDeque = new LinkedList<>();
            int L = 0;
            // 【错误】-> R <diff.length 会导致[n,2n-1]也参与计算，这是不必要的，多算一次。 会导致boolea[]收集时越界  => for (int R = 0; R < diff.length - 1; R++)
            // 续：第二种解决方法，循环体内 加上if检查 if(L == n - 1) then break
            // for (int R = 0; R < diff.length; R++) {
            for (int R = 0; R < diff.length; R++) {
                while (!minDeque.isEmpty() && diff[minDeque.peekLast()] >= diff[R]) {
                    minDeque.pollLast();
                }
                minDeque.offerLast(R);

                if (R - L + 1 > n) {
                    if (minDeque.peekFirst() == L) {
                        minDeque.pollFirst();
                    }
                    L++;
                }

                if (R - L + 1 == n) {
                    int min = diff[minDeque.peekFirst()];
                    int minus = L == 0 ? 0 : diff[L - 1];
                    if (min - minus >= 0) {
                        ans[index++] = true;
                    } else {
                        // 【错误】如果for循环， R<rest.length;这么写是不行的，这个if体会进入 n+1次 （有n+1次 满足大小的窗口），因为最后一次[n,2n-1]这个其实与[0,n-1]是相同的，所以不做这一次
                        // ans[index++] = false;
                        ans[index++] = false;
                    }
                }

                // 避免ans数组越界，在L==n-1时，就把所有加油站作为起点的情况都计算完了。
                // 避免在for循环里控制i<rest.length -1， 直接在尾部添加if控制退出就好了！！！
                if (L == n - 1) {
                    break;
                }
            }

            // System.out.println(Arrays.toString(ans));
            for (int i = 0;  i < ans.length; i++) {
                if (ans[i]) {
                    return i;
                }
            }
            return -1;
        }
    }

   // 这个是针对本题（答案唯一的情况下/返回一个可行的答案）的最优解 =》 贪心算法：空间O(1) => 前缀和最低点minS的下一个start
   /*
    为什么这个算法有效？（正确性证明）
    **如果从 A 出发，第一次在 B 没油了，那么 A+1 到 B 之间的任何一个点作为起点，也会在 B 或更早没油。**

    必要条件：if (s >= 0)判断总油量是否足够绕一圈。如果总油量为负，无论如何都无法完成，直接返回-1。
    充分条件与起点选择：
    minS记录的是从虚拟起点0开始，行驶过程中油量的最低谷。
    我们将起点选在 minS出现的下一个位置 (start = i + 1)。
    这相当于将整个油量变化曲线“抬升”了。因为从 start出发，相当于把 minS之前的那段“消耗最大”的路径移到了最后面。由于总油量 s >= 0，这段最艰难的路段放在最后时，前面积累的油量足以支撑它通过。
    我的备注：这个start就是 把最耗油的路端安排在最后。 这样sum 对应的就是 最后加油站能否 回到 start （>=0说明可以， <0说明不可以）
            进一步证明，如果说s<0，那就是说最耗油的放到最后都不行。那么其余位置更不行，因为放在中间的话，前面抬升的高度更小。
    */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        int s = 0;
        int minS = 0;
        int start = 0;
        for (int i = 0; i < len; i++) {
            s += gas[i] - cost[i];
            if (s < minS) {
                minS = s;
                start = i + 1;
            }
        }
        if (s >= 0) {
            return start;
        } else {
            return -1;
        }
    }


}
