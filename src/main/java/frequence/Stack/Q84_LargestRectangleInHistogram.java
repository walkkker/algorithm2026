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
     * 2026-09-16复盘版本：寻找每根柱子左右两侧最近的严格更小值。
     *
     * <p><b>你的原始记忆：</b>“最近最小，相等弹出、不考虑”。更精确地说：某个下标
     * {@code popI}真正因为更小值而弹出时，弹栈后的栈顶是它左侧最近的严格更小下标，当前
     * {@code i}是它右侧最近的严格更小下标。因此以{@code heights[popI]}为高的最大宽度为
     * {@code i - left - 1}。
     *
     * <p><b>栈不变量：</b>栈中保存柱子下标；当前实现遇到相等高度也弹出，因此每轮处理结束后，
     * 栈底到栈顶的高度严格递增。
     *
     * <p>TODO: 【相等弹出、不结算】当{@code heights[i] == heights[popI]}时，i并不是popI
     * 右侧严格更小的边界，所以不能在此时结算。弹出旧下标后保留更靠右的等高下标i作为代表；
     * 它继承弹栈后的更远左边界，并会在以后遇到严格更小值或数组结束时统一计算，从而不会漏掉
     * 等高平台能够形成的最大矩形。
     *
     * <p>TODO: 【致命错误-矩形高度】弹出谁，就在结算谁的最大矩形。矩形高度必须使用
     * {@code heights[popI]}，当前{@code heights[i]}只是触发弹栈的右边界，不能作为被结算
     * 矩形的高度。
     */
    class SolutionReviewed20260916 {
        public int largestRectangleArea(int[] heights) {
            Stack<Integer> stack = new Stack<>();
            int ans = 0;

            for (int i = 0; i < heights.length; i++) {
                while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
                    int popI = stack.pop();

                    // 相等时弹出旧下标，但不结算：相等值不是“右侧严格更小边界”。
                    if (heights[i] < heights[popI]) {
                        int r = i;
                        int l = stack.isEmpty() ? -1 : stack.peek();

                        // TODO: 【致命错误】弹出并结算的是popI，高度必须取heights[popI]。
                        // 错误行：ans = Math.max(ans, (r - l - 1) * heights[i]);
                        ans = Math.max(ans, (r - l - 1) * heights[popI]);
                    }
                }
                stack.push(i);
            }

            // 栈中剩余下标右侧不存在更小值，统一使用虚拟右边界heights.length。
            while (!stack.isEmpty()) {
                int popI = stack.pop();
                int r = heights.length;
                int l = stack.isEmpty() ? -1 : stack.peek();

                // TODO: 【错误】同样必须使用被弹出柱子的高度heights[popI]。
                // 错误行：ans = Math.max(ans, (r - l - 1) * heights[i]);
                ans = Math.max(ans, (r - l - 1) * heights[popI]);
            }
            return ans;
        }
    }

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
