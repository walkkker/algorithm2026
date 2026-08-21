package GreedyAlgorithm;

import java.util.Comparator;
import java.util.PriorityQueue;

/*
从给定项目列表中选择 最多 k 个不同项目，以 最大化最终资本 ，并输出最终可获得的最多资本。
w是初始资本。
 */

// https://leetcode.cn/problems/ipo/

// TODO: 自己写的版本。第一次存在错误，自定义的class Program属性按参数写了capital。
//  但是在PriorityQueue的比较器方法中，却按照'记忆'写了o1.cost，导致报错: cannot not find symbol
class Solution {
	public int findMaximizedCapital(int k, int w, int[] profits, int[] capital){
		PriorityQueue<Program> cQ = new PriorityQueue<>((o1, o2) -> o1.capital - o2.capital);   // lambda expression
		PriorityQueue<Program> pQ = new PriorityQueue<>((o1, o2) -> o2.profit - o1.profit);
		for (int i = 0; i < profits.length; i++) {
			cQ.offer(new Program(capital[i], profits[i]));
		}

		while (k > 0) {
			while (!cQ.isEmpty() && cQ.peek().capital <= w) {
				pQ.offer(cQ.poll());
			}

			if (pQ.size() == 0) {
				return w;
			} else {
				w += pQ.poll().profit;
			}
			k--;   // k退出时k==0.  若使用 while(k-->0)，退出循环时k==-1
		}
		return w;
	}

	public static class Program {
		int capital;
		int profit;
		public Program(int _capital, int _profit) {
			capital = _capital;
			profit = _profit;
		}
	}
}


public class Code04_IPO {

	// 最多K个项目
	// W是初始资金
	// Profits[] Capital[] 一定等长。
	// TODO: 这里特别注意，profits是利润，不是收入。就是说当你affordable时，这个项目给你带来的加成/利润 就是 profits[i]
	// 返回最终最大的资金
	public static int findMaximizedCapital(int K, int W, int[] Profits, int[] Capital) {
		PriorityQueue<Program> minCostQ = new PriorityQueue<>(new MinCostComparator());
		PriorityQueue<Program> maxProfitQ = new PriorityQueue<>(new MaxProfitComparator());
		for (int i = 0; i < Profits.length; i++) {
			minCostQ.add(new Program(Profits[i], Capital[i]));
		}
		for (int i = 0; i < K; i++) {
			while (!minCostQ.isEmpty() && minCostQ.peek().c <= W) {
				maxProfitQ.add(minCostQ.poll());
			}
			if (maxProfitQ.isEmpty()) {
				return W;
			}
			W += maxProfitQ.poll().p;
		}
		return W;
	}

	public static class Program {
		public int p;
		public int c;

		public Program(int p, int c) {
			this.p = p;
			this.c = c;
		}
	}

	public static class MinCostComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o1.c - o2.c;
		}

	}

	public static class MaxProfitComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o2.p - o1.p;
		}

	}

}
