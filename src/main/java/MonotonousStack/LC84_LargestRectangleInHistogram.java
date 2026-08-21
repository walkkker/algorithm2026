package MonotonousStack;

import java.util.Stack;

// LC 84 -> 测试链接：https://leetcode.com/problems/largest-rectangle-in-histogram
// 答案： 子数组的min问题
// TODO: 本题非常重要 一维数组柱状图求最大矩形，很好解释单调栈的使用（求子数组最小 以及 忽略重复元素的设计）
// 		 同时本题是 LC85的强依赖，直接复用本题答案！！！
// TODO: 【关注点】唯一的关注点是"相同时弹出，但是不处理"+"要相信前面写的是对的，后面大胆写就行，比如我们说的你都做好了底部为小的单调栈，要相信弹出来的就是最小值！！！"
// TODO: 【错误点】原题入参是 heights，平时写arr写手顺了，一定不要写串了。 觉得heights不好拼写，可以直接复制，coding时候直接复制上去。
public class LC84_LargestRectangleInHistogram {
	// 20260707 新版本，二次过。 也是求最小值
	class Solution1 {
		public int largestRectangleArea(int[] heights) {
			int max = -1;
			Stack<Integer> stack = new Stack<>();
			for (int i = 0; i < heights.length; i++) {
				while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
					int popI = stack.pop();
					if (heights[i] < heights[popI]) {
						int l = stack.isEmpty() ? -1 : stack.peek();
						int r = i;
						int w = r - l - 1;
						max = Math.max(max, w * heights[popI]);
					}
				}
				stack.push(i);
			}
			while (!stack.isEmpty()) {
				int popI = stack.pop();
				int l = stack.isEmpty() ? -1 : stack.peek();
				int r = heights.length;
				int w = r - l - 1;
				max = Math.max(max, w * heights[popI]);
			}
			return max;
		}


	}



	// MyVersion
	class Solution {
		public int largestRectangleArea(int[] heights) {
			int n = heights.length;
			Stack<Integer> stack = new Stack<>();
			int ansMax = Integer.MIN_VALUE;
			for (int i = 0; i < heights.length; i++) {
				// min
				while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
					int cur = stack.pop();
					int leftLess = stack.isEmpty() ? -1 : stack.peek();
					int rightLess = i;
					// TODO: 【下面这个别忘了】因为是求  底部长度 * 子数组最小值  =》 下面这个if没有 也是对的。 因为重复算的那些区间都是小的，最后都会被最后一个元素算出来的值 替代掉
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

	public static int largestRectangleArea1(int[] height) {
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

	public static int largestRectangleArea2(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int N = height.length;
		int[] stack = new int[N];
		int si = -1;
		int maxArea = 0;
		for (int i = 0; i < height.length; i++) {
			while (si != -1 && height[i] <= height[stack[si]]) {
				int j = stack[si--];
				int k = si == -1 ? -1 : stack[si];
				int curArea = (i - k - 1) * height[j];
				maxArea = Math.max(maxArea, curArea);
			}
			stack[++si] = i;
		}
		while (si != -1) {
			int j = stack[si--];
			int k = si == -1 ? -1 : stack[si];
			int curArea = (height.length - k - 1) * height[j];
			maxArea = Math.max(maxArea, curArea);
		}
		return maxArea;
	}

}
