package frequence.双指针;

/**
 * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
 *
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 * 返回容器可以储存的最大水量。
 *
 * 说明：你不能倾斜容器。
 */
public class Q11_ContainerWithMostWater {
    /**
     TODO: 【错误点】X轴距离计算。错误版：R-L+1； 正确版：R-L。 因为本题index对应的是端点，而不是常规的格子。
     TODO: 【原因】1. [L, R]长度为R-L+1是基于，index对应着格子。
     TODO:          2. 而本题L,R对应的是端点。 你会发现[1,2]的距离是1，所以对应的长度其实是R-L

     */
    class Solution {
        public int maxArea(int[] height) {
            int ans = 0;
            int L = 0;
            int R = height.length - 1;
            while (L <= R) {
                if (height[L] <= height[R]) {
                    // TODO: 【错误】之前计算长度是 R - L + 1。 但是本题index对应的是端点，不是格子。所以不能用常规的R-L+1来计算。
                    ans = Math.max(ans, height[L] * (R - L));
                    L++;
                } else {
                    ans = Math.max(ans, height[R] * (R - L));
                    R--;
                }
            }
            return ans;
        }
    }
}
