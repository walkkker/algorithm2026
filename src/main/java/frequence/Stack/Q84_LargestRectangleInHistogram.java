package frequence.Stack;

import java.util.*;

/**
 * 84. 柱状图中最大的矩形
 *
 * <p>给定一个整数数组{@code heights}，其中{@code heights[i]}表示柱状图中第{@code i}根柱子
 * 的高度，每根柱子的宽度为{@code 1}。返回能够在柱状图中勾勒出的最大矩形面积。
 */
public class Q84_LargestRectangleInHistogram {
    /**
     * 单调栈： 找最近较小
     */
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int ans = 0;
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
                int popIndex = stack.pop();
                if (heights[i] < heights[popIndex]) {
                    int l = stack.isEmpty() ? -1 : stack.peek();
                    int r = i;
                    ans = Math.max(ans, (r - l - 1) * heights[popIndex]);
                }
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            int popIndex = stack.pop();
            int l = stack.isEmpty() ? -1 : stack.peek();
            int r = heights.length;
            ans = Math.max(ans, (r - l - 1) * heights[popIndex]);
        }
        return ans;
    }
}
