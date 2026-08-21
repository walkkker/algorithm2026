package GreedyAlgorithm;

import java.util.PriorityQueue;
import java.util.Stack;

/**
 * 核心本质就是 我每次合两个。 如果有最终目标为n个子块，那么我需要合并n-1次。
 * 局部最优：我每次合并代价最小 -> 选择最小的两个子块
 * 全局最优：是合理的。因为每次都是最小代价，最终n-1次 是全局的最小代价。
 */
public class Code02_LessMoneySplitGold {

	// 纯暴力！
	public static int lessMoney1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		return process(arr, 0);
	}

	// 等待合并的数都在arr里，pre之前的合并行为产生了多少总代价
	// arr中只剩一个数字的时候，停止合并，返回最小的总代价
	public static int process(int[] arr, int pre) {
		if (arr.length == 1) {
			return pre;
		}
		int ans = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				ans = Math.min(ans, process(copyAndMergeTwo(arr, i, j), pre + arr[i] + arr[j]));
			}
		}
		return ans;
	}

	public static int[] copyAndMergeTwo(int[] arr, int i, int j) {
		int[] ans = new int[arr.length - 1];
		int ansi = 0;
		for (int arri = 0; arri < arr.length; arri++) {
			if (arri != i && arri != j) {
				ans[ansi++] = arr[arri];
			}
		}
		ans[ansi] = arr[i] + arr[j];
		return ans;
	}

	// 左神版本
	public static int lessMoney3(int[] arr) {
		PriorityQueue<Integer> pQ = new PriorityQueue<>();
		for (int i = 0; i < arr.length; i++) {
			pQ.add(arr[i]);
		}
		int sum = 0;
		int cur = 0;
		while (pQ.size() > 1) {
			cur = pQ.poll() + pQ.poll();
			sum += cur;
			pQ.add(cur);
		}
		return sum;
	}

	// 我的版本
	// 本质就是 每次弹出最小的两个数，然后合并塞回小根堆 -> until小根堆只有一个数
	public static int lessMoney2(int[] arr) {
		if (arr == null) {
			return 0;
		}
		// TODO: 【错误-base case你要认真想，不要想当然！你要有逻辑的说的通的去给base case返回值】
		//  解释：当只有一个元素时，不需要切割，直接满足目标条件，所以没有代价。
		if (arr.length == 1) {
		//	错误点：	return arr[0];
			return 0;
		}

		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for (int num : arr) {
			heap.offer(num);
		}
		int sum = 0;
		while (heap.size() > 1) {
			int n1 = heap.poll();
			int n2 = heap.poll();
			int cur = n1 + n2;
			heap.offer(cur);
			sum += cur;
		}
		return sum;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTime = 100000;
		int maxSize = 6;
		int maxValue = 1000;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			if (lessMoney1(arr) != lessMoney2(arr)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
