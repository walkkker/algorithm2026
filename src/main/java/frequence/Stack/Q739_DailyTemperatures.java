package frequence.Stack;

import java.util.*;

/**
 * 739. 每日温度
 *
 * <p>给定一个整数数组{@code temperatures}表示每天的温度，返回数组{@code answer}，其中
 * {@code answer[i]}表示第{@code i}天之后需要等待多少天才能出现更高温度。如果之后不会出现
 * 更高温度，则该位置返回{@code 0}。
 */
public class Q739_DailyTemperatures {

    /**
     * 2026-09-16复盘版本。
     *
     * <p><b>你的原始记忆：</b>“最近最大，相等不弹出”。其中“相等不弹出”完全正确；
     * “最近最大”应精确修正为“右侧最近的严格更大值”，不是寻找右侧范围内的最大值。
     *
     * <p>TODO: 【概念修正】本题不是寻找“右侧最大值”，而是寻找“右侧第一个严格大于当前值
     * 的位置”，并返回两个下标之间的距离。
     *
     * <p><b>栈不变量：</b>栈中保存尚未找到右侧更高温度的日期下标；从栈底到栈顶，下标递增，
     * 对应温度单调不升。相等温度不能弹栈，因为题目要求的是严格更高温度。
     *
     * <p>当{@code temperatures[i] > temperatures[stack.peek()]}时，当前i就是栈顶日期右侧
     * 第一个更高温度的位置。因为数组从左向右扫描；如果更早的位置已经更高，该下标早就会
     * 在更早的一轮弹出，不可能保留到当前。
     *
     * <p>TODO: 【错误-返回含义】{@code answer[index]}保存的不是目标日期下标，而是需要等待
     * 的天数，因此必须写成{@code currentIndex - index}。
     *
     * <p>TODO: 【可精简】循环结束后栈内下标都不存在右侧更高温度，答案应为0；Java新建
     * {@code int[]}默认值就是0，因此无需专门清栈赋值。保留该循环也正确，只是冗余。
     */
    class SolutionReviewed20260916 {
        public int[] dailyTemperatures(int[] temperatures) {
            int len = temperatures.length;
            Stack<Integer> stack = new Stack<>();
            int[] ans = new int[len];

            for (int i = 0; i < len; i++) {
                // TODO: 【重点】相等不弹出。题目要求严格更高，所以必须使用>，不能使用>=。
                while (!stack.isEmpty()
                        && temperatures[i] > temperatures[stack.peek()]) {
                    int popI = stack.pop();
                    // TODO: 【错误】题目要求answer[popI]表示几天后，而不是目标下标。
                    // 错误行：ans[popI] = i;
                    ans[popI] = i - popI;
                }
                // 单调栈必须保存下标，才能同时比较温度并计算日期距离。
                stack.push(i);
            }

            // TODO: 【可精简】下列循环不影响正确性，但int[]默认已经全部初始化为0，可以删除。
            while (!stack.isEmpty()) {
                int popI = stack.pop();
                ans[popI] = 0;
            }
            return ans;
        }
    }

    /**
     * 寻找右侧较大。
     * @param temperatures
     * @return
     */

    /**
     找右侧最近最大。
     单调栈。
     */
    class Solution {
        public int[] dailyTemperatures(int[] temperatures) {
            int len = temperatures.length;
            int[] ans = new int[len];
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < len; i++) {
                while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                    int popIndex = stack.pop();
                    ans[popIndex] = i - popIndex;
                }
                // TODO: 【错误】单调栈/滑动窗口双端队列 里面存的都是下标
                // 错误行： stack.push(temperatures[i]);
                stack.push(i);
            }
            return ans;
        }
    }
}
