package MonotonousStack;

import java.util.*;
// 测试链接：https://leetcode.com/problems/count-submatrices-with-all-ones
// 给你一个 m x n 的二进制矩阵 mat ，请你返回有多少个 子矩形 的元素全部都是 1 。

/**
 * 本题的难点在于数学计算：
 * 假设当前有一个矩阵 m * n，求以最底行为底的矩阵有多少个？
 * <p>
 * 1 1 1 1
 * 1 1 1 1
 * 1 1 1 1   =>！（底层组合数） * （高度组合数）！ 【我这是类比了Code02的 包含cur的子数组数量 -> (左侧start选项数) * (右侧end选项数)】
 * =>   m!  * n 你是傻吗？ 不是m! 是 1+2+3+...m 这是【等差数列！！！】 (1+m) * m / 2 !!!
 * =>  （1有4个，2有3个，3有2个，4有1个 因为必须连着） * (必须以底)
 */
public class LC1504_CountSubmatricesWithAllOnes {

    /**
     * 20260708版本
     * 核心还是 以每行为底计算
     *
     * @param mat
     * @return
     */
    public int numSubmat1(int[][] mat) {
        int ans = 0;
        int M = mat.length;
        int N = mat[0].length;
        int[] arr = new int[N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (mat[i][j] == 1) {
                    arr[j] += 1;
                } else {
                    arr[j] = 0;
                }
            }
            ans += count(arr);
        }
        return ans;
    }

    // TODO：【重点】求单个柱状图中有多少个 全1矩形的方法
    public int count(int[] arr) {
        Stack<Integer> stack = new Stack<>();
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
                int popI = stack.pop();

                if (arr[i] < arr[popI]) {
                    int l = stack.isEmpty() ? -1 : stack.peek();
                    int r = i;
                    // TODO:【重点】！！！本题基于单调栈的拓展核心就是这一段！！！    宽度为w时，组合数为1,2...w => (n * (n + 1)) / 2
                    int w = r - l - 1;
                    int p1 = l >= 0 ? arr[l] : 0;
                    int p2 = r < arr.length ? arr[r] : 0;
                    ans += (arr[popI] - Math.max(p1, p2)) * ((1 + w) * w / 2);
                }
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int popI = stack.pop();
            int l = stack.isEmpty() ? -1 : stack.peek();
            int r = arr.length;
            int w = r - l - 1;
            int p1 = l >= 0 ? arr[l] : 0;
            int p2 = r < arr.length ? arr[r] : 0;
            ans += (arr[popI] - Math.max(p1, p2)) * ((1 + w) * w / 2);
        }
        return ans;
    }


    // TODO: 核心依然是 每行为底求矩阵总数量。  但是这里很细致，做错了很多遍，需要反复看
    //    本题基于LC 85 二维矩阵计算最大全1矩形 -> 一维数组柱状图求最大矩形(这个模型是最重要的)
    //    只是聚合方式发生改变：本题不求最大，求总和
    //    难点在于数学公式:1) 单调栈下，每次不是求完整的柱子，是柱子的差值部分 这样才能不重复的算出所有的矩形数量 2）当有一个m*n的全1矩形，以底为底的 子矩形数量为 (底层组合数) * （高度组合数）=> 注意组合数是 5+4+3+2+1等差数列！！！n*(n+1)/2 ，不是 n!。一开始搞错了=-=
    class Solution {
        public int numSubmat(int[][] mat) {
            // S1： 主函数跟 LC 85一样，构建 每行柱状图
            int rows = mat.length;
            int cols = mat[0].length;
            int[] heights = new int[cols];
            int ans = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // 【错误-排查很久】要特别阅题，这里是int[][] mat， 不是LC 85的 char[][]!!!
                    // heights[j] = mat[i][j] == '1' ? 1 + heights[j] : 0;
                    heights[j] = mat[i][j] == 1 ? 1 + heights[j] : 0;
                    // 【错误！！】你下面这个怎么放在这里了？？？？ 每一行处理完，再调用的 柱状图函数呀！！！
                    // ans += countRectangle(heights);
                }
                ans += countRectangle(heights);
            }
            return ans;
        }

        public int countRectangle(int[] heights) {
            int n = heights.length;
            Stack<Integer> stack = new Stack<>();
            int total = 0;
            for (int i = 0; i < n; i++) {
                // min
                while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    int cur = stack.pop();
                    // TODO: 【错误】这一段绝对不能忘，我甚至认为这已经是一个标准模板了！！！
                    if (heights[cur] == heights[i]) {
                        continue;
                    }
                    int leftLess = stack.isEmpty() ? -1 : stack.peek();
                    int rightLess = i;

                    int p1 = leftLess >= 0 ? heights[leftLess] : 0;
                    int p2 = rightLess < n ? heights[rightLess] : 0;
                    // TODO: 【错误！！！！】每次弹出来的那个index 才是 当前区间最小值呀！！！
                    // 错了！！！int h = heights[i] - Math.max(p1, p2);
                    int h = heights[cur] - Math.max(p1, p2);
                    int w = rightLess - leftLess - 1;
                    // TODO: 【完全搞错了】
                    // total += factorial(w) * h;
                    total += sum(w) * h;
                }
                stack.push(i);
            }

            while (!stack.isEmpty()) {
                int index = stack.pop();
                int leftLess = stack.isEmpty() ? -1 : stack.peek();
                // TODO：复制过来后，只需要修改这里i->n  以及删除 if(heights[index] == heights[i])的检查
                int rightLess = n;
                // TODO: 【错误】这一段绝对不能忘，我甚至认为这已经是一个标准模板了！！！

                int p1 = leftLess >= 0 ? heights[leftLess] : 0;
                int p2 = rightLess < n ? heights[rightLess] : 0;
                // 【TODO】一起错了！！！ index才是当前区间最小值
                // int h = heights[i] - Math.max(p1, p2);
                int h = heights[index] - Math.max(p1, p2);
                int w = rightLess - leftLess - 1;
                total += sum(w) * h;
            }
            return total;
        }

        // 【完全错误】计算都写了是 1+2+3 结果写阶乘，没谁了！！！ 不过挺好的， 发现了一个子错误while(n--)的避雷区
        public int factorial(int n) {
            int ans = 1;
            // 【错误】下面这个只能用做 数次数！！！ 别的什么都不能干！！！ 你自己没想明白，会出错的！！！
            // while (n-- > 0) {    你这样想的话，每次进入循环体用的是n-1！！！ 虽然n=1时是最后一次进入，但是这时 ans *= 0!!!!
            //     ans *= n;
            // }
            // TODO: 除了只数次数+n以后不会再用到外！！！ 全部使用最笨的for循环！！！ 清晰明了不出错
            for (int i = 1; i <= n; i++) {
                ans *= i;
            }

            return ans;
        }

        public int sum(int n) {
            // int ans = 0;
            // for (int i = 1 ; i <=n; i++) {
            //     ans += i;
            // }
            // return ans;
            // TODO: 上面太笨了！！！ 明明 6+5+4+3+2+1 等差数列！！！直接写成 n * (n + 1) / 2
            return n * (n + 1) / 2;
        }
    }


    public static int numSubmat(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return 0;
        }
        int nums = 0;
        int[] height = new int[mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                height[j] = mat[i][j] == 0 ? 0 : height[j] + 1;
            }
            nums += countFromBottom(height);
        }
        return nums;

    }

    // 比如
    //              1
    //              1
    //              1         1
    //    1         1         1
    //    1         1         1
    //    1         1         1
    //
    //    2  ....   6   ....  9
    // 如上图，假设在6位置，1的高度为6
    // 在6位置的左边，离6位置最近、且小于高度6的位置是2，2位置的高度是3
    // 在6位置的右边，离6位置最近、且小于高度6的位置是9，9位置的高度是4
    // 此时我们求什么？
    // 1) 求在3~8范围上，必须以高度6作为高的矩形，有几个？
    // 2) 求在3~8范围上，必须以高度5作为高的矩形，有几个？
    // 也就是说，<=4的高度，一律不求
    // 那么，1) 求必须以位置6的高度6作为高的矩形，有几个？
    // 3..3  3..4  3..5  3..6  3..7  3..8
    // 4..4  4..5  4..6  4..7  4..8
    // 5..5  5..6  5..7  5..8
    // 6..6  6..7  6..8
    // 7..7  7..8
    // 8..8
    // 这么多！= 21 = (9 - 2 - 1) * (9 - 2) / 2
    // 这就是任何一个数字从栈里弹出的时候，计算矩形数量的方式
    public static int countFromBottom(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int nums = 0;
        int[] stack = new int[height.length];
        int si = -1;
        for (int i = 0; i < height.length; i++) {
            while (si != -1 && height[stack[si]] >= height[i]) {
                int cur = stack[si--];
                if (height[cur] > height[i]) {
                    int left = si == -1 ? -1 : stack[si];
                    int n = i - left - 1;
                    int down = Math.max(left == -1 ? 0 : height[left], height[i]);
                    nums += (height[cur] - down) * num(n);
                }

            }
            stack[++si] = i;
        }
        while (si != -1) {
            int cur = stack[si--];
            int left = si == -1 ? -1 : stack[si];
            int n = height.length - left - 1;
            int down = left == -1 ? 0 : height[left];
            nums += (height[cur] - down) * num(n);
        }
        return nums;
    }

    public static int num(int n) {
        return ((n * (1 + n)) >> 1);
    }

}
