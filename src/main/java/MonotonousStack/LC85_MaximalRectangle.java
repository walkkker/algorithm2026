package MonotonousStack;

import java.util.Stack;

// LC 85 -> 测试链接：https://leetcode.com/problems/maximal-rectangle/

/**
 * 	本题本质就是：以每一个行为底，算最大的矩形。
 * 	1) 而每一行看作是一个柱状图，计算时注意 当前grid为'0'时要清空。
 * 	2) 然后每一行调用 LC84答案就可以：https://leetcode.cn/problems/largest-rectangle-in-histogram/
 */
public class LC85_MaximalRectangle {
	// lucky，一次过，核心是【核心点】注释，每行为底->不重复计算
	class Solution20260707 {
		public int maximalRectangle(char[][] matrix) {
			// TODO: Step2: 以matrix每一行为底，构建int[] arr。 具体要看代码：
			int M = matrix.length;
			int N = matrix[0].length;
			int[] arr = new int[N];
			int max = -1;
			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					// TODO: 【最核心点】 如果底为1，则累加之前的arr[i]；否则，清0（因为将该行为底，'0'的矩形不应该被计算）
					if (matrix[i][j] == '1') {
						arr[j] += 1;
					} else {
						arr[j] = 0;
					}
				}
				max = Math.max(max, getMax(arr));
			}
			return max;
		}

		// TODO: Step1: 单个柱形图（对应int[] arr），求最大矩形面积
		public int getMax(int[] arr) {
			Stack<Integer> stack = new Stack<>();
			int max = -1;
			for (int i = 0; i < arr.length; i++) {
				while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
					int popI = stack.pop();
					if (arr[i] < arr[popI]) {
						int l = stack.isEmpty() ? -1 : stack.peek();
						int r = i;
						int w = r - l - 1;
						max = Math.max(max, arr[popI] * w);
					}
				}
				stack.push(i);
			}
			while (!stack.isEmpty()) {
				int popI = stack.pop();
				int l = stack.isEmpty() ? -1 : stack.peek();
				int r = arr.length;
				int w = r - l - 1;
				max = Math.max(max, w * arr[popI]);
			}
			return max;
		}
	}


	class Solution {
		public int maximalRectangle(char[][] matrix) {
			int rows = matrix.length;
			int cols = matrix[0].length;
			int[] heights = new int[cols];
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					// TODO: 【错误】这里heights[j]的计算写错了！！！ 如果当前为'0'的话，要清空的！！！
					// heights[j] += matrix[i][j] == '1' ? 1 : 0;
					heights[j] = matrix[i][j] == '1' ? 1 + heights[j] : 0;
				}
				max = Math.max(max, largestRectangleForOneArray(heights));
			}
			return max;
		}
		// 直接复用LC84 答案！！！ 所以 84题很重要！！！ 别忘了也有个地方弄错了，就是 子数组长度！！ (L, R) => R - L - 1
		public int largestRectangleForOneArray(int[] heights) {
			int n = heights.length;
			Stack<Integer> stack = new Stack<>();
			int ansMax = Integer.MIN_VALUE;
			for (int i = 0; i < heights.length; i++) {
				// min
				while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
					int cur = stack.pop();
					int leftLess = stack.isEmpty() ? -1 : stack.peek();
					int rightLess = i;
					// 因为是求  底部长度 * 子数组最小值  =》 下面这个if没有 也是对的。 因为重复算的那些区间都是小的，最后都会被最后一个元素算出来的值 替代掉
					if (heights[cur] > heights[i]) {
						// TODO: 写错了！！！ 慢点写，觉得太乱了你就多个变量也行呀！！！
						// ansMax = Math.max(ansMax, heights[index] * ((leftLess + 1) * (rightLess - 1)));
						ansMax = Math.max(ansMax, heights[cur] * (rightLess - leftLess - 1));
					}
				}
				stack.push(i);
			}

			while (!stack.isEmpty()) {
				int index = stack.pop();
				int leftLess = stack.isEmpty() ? -1 : stack.peek();
				int rightLess = n;
				ansMax = Math.max(ansMax, heights[index] * (rightLess - leftLess - 1));
			}
			return ansMax;
		}
	}

	public static int maximalRectangle(char[][] map) {
		if (map == null || map.length == 0 || map[0].length == 0) {
			return 0;
		}
		int maxArea = 0;
		int[] height = new int[map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				height[j] = map[i][j] == '0' ? 0 : height[j] + 1;
			}
			maxArea = Math.max(maxRecFromBottom(height), maxArea);
		}
		return maxArea;
	}

	// height是正方图数组
	public static int maxRecFromBottom(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int maxArea = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < height.length; i++) {
			while (!stack.isEmpty() && height[i] <= height[stack.peek()]) {
				int j = stack.pop();
				int k = stack.isEmpty() ? -1 : stack.peek();
				int curArea = (i - k - 1) * height[j];
				maxArea = Math.max(maxArea, curArea);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			int k = stack.isEmpty() ? -1 : stack.peek();
			int curArea = (height.length - k - 1) * height[j];
			maxArea = Math.max(maxArea, curArea);
		}
		return maxArea;
	}

}
