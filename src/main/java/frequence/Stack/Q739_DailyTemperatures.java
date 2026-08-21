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
