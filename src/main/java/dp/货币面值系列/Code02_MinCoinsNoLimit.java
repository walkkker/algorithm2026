package dp.货币面值系列;


import java.util.Arrays;

/**
 * int[] arr -> coins数组，代表不同种coin。 每种coin的数量是无限的
 * 求： 组合出aim的最小coins数量
 * 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。  这道题，失效状态不是简单的组合数0了，而是需要人为定义 一个值来表明当前状态无法得到答案，有点类似于null的感觉。
 *
 * TODO: 这题也很有代表性，大体上跟 CoinsWayNoLimit枚举的逻辑是一样的！！！
 * 		但是，有很多细节不同：目标语义不同，还有就是base case行要更新一下，以及最终要的  把 聚合方式： 累加 -> 最小
 *
 */
public class Code02_MinCoinsNoLimit {

	// 已通过leetcode验证。   coins[i]给的很大，要考虑溢出问题。溢出后，while检查条件失效，导致 arr[index]中Index为负数
	public static int mydp(int[] arr, int aim) {
		// TODO: 边界条件=-= . arr.length==0，没有纸张 就没有组合数！！！
		if (arr == null || arr.length == 0 || aim < 0) {
			return -1; // // TODO: 【错误】因为直接复制的 coinWaysNoLimit ，没改。 这里要看题目，拼不出来就返回-1
		}
		int n = arr.length;
		int[][] dp = new int[n + 1][aim + 1];
		// 第n行，能拼出来的话为0（表示当前位置需要0个coin），拼不出来需要为-1
		Arrays.fill(dp[n], -1);
		dp[n][aim] = 0;

		for (int i = n - 1; i >= 0; i--) {
			for (int j = aim; j >= 0; j--) {
				int count = 0;
				int min = Integer.MAX_VALUE;
				// TODO: 【错误】为了防溢出，在这里做了 类型升级。 因为 这三个变量都是默认给的int类型，只能手动升级了
				while ((long)j + arr[i] * count <= aim) {
					// TODO: 【错误】1 <= coins[i] <= 2^31 - 1 题目给的值很大，出现溢出 -》 Index -2147483647 out of bounds for length 3
					int dpValue = dp[i + 1][j + count * arr[i]];
					if (dpValue != -1) {
						min = Math.min(min, count + dpValue);
					}
					count++;
				}
				//TODO: 这个检查也很重要！！！存在 所有依赖节点 都是 -1，此时 min=初始值。要考虑这种情况，这意味着当前(i,j)状态也是无效的
				dp[i][j] = min == Integer.MAX_VALUE ? -1 : min;
			}
		}
		return dp[0][0];
	}


	public static int minCoins(int[] arr, int aim) {
		return process(arr, 0, aim);
	}

	// arr[index...]面值，每种面值张数自由选择，
	// 搞出rest正好这么多钱，返回最小张数
	// 拿Integer.MAX_VALUE标记怎么都搞定不了
	public static int process(int[] arr, int index, int rest) {
		if (index == arr.length) {
			return rest == 0 ? 0 : Integer.MAX_VALUE;
		} else {
			int ans = Integer.MAX_VALUE;
			for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
				int next = process(arr, index + 1, rest - zhang * arr[index]);
				if (next != Integer.MAX_VALUE) {
					ans = Math.min(ans, zhang + next);
				}
			}
			return ans;
		}
	}

	public static int dp1(int[] arr, int aim) {
		if (aim == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 0;
		for (int j = 1; j <= aim; j++) {
			dp[N][j] = Integer.MAX_VALUE;
		}
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				int ans = Integer.MAX_VALUE;
				for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
					int next = dp[index + 1][rest - zhang * arr[index]];
					if (next != Integer.MAX_VALUE) {
						ans = Math.min(ans, zhang + next);
					}
				}
				dp[index][rest] = ans;
			}
		}
		return dp[0][aim];
	}

	public static int dp2(int[] arr, int aim) {
		if (aim == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 0;
		for (int j = 1; j <= aim; j++) {
			dp[N][j] = Integer.MAX_VALUE;
		}
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				dp[index][rest] = dp[index + 1][rest];
				if (rest - arr[index] >= 0
						&& dp[index][rest - arr[index]] != Integer.MAX_VALUE) {
					dp[index][rest] = Math.min(dp[index][rest], dp[index][rest - arr[index]] + 1);
				}
			}
		}
		return dp[0][aim];
	}

	// 为了测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int N = (int) (Math.random() * maxLen);
		int[] arr = new int[N];
		boolean[] has = new boolean[maxValue + 1];
		for (int i = 0; i < N; i++) {
			do {
				arr[i] = (int) (Math.random() * maxValue) + 1;
			} while (has[arr[i]]);
			has[arr[i]] = true;
		}
		return arr;
	}

	// 为了测试
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// 为了测试
	public static void main(String[] args) {
		int maxLen = 20;
		int maxValue = 30;
		int testTime = 300000;
		System.out.println("功能测试开始");
		for (int i = 0; i < testTime; i++) {
			int N = (int) (Math.random() * maxLen);
			int[] arr = randomArray(N, maxValue);
			int aim = (int) (Math.random() * maxValue);
			int ans1 = minCoins(arr, aim);
			int ans2 = dp1(arr, aim);
			int ans3 = dp2(arr, aim);
			int ans4 = mydp(arr, aim);
			int ans5 = dpTest(arr, aim);
			if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println(ans4);
				System.out.println(ans5);
				break;
			}
		}
		System.out.println("功能测试结束");
	}


	public static int dpTest(int[] arr, int aim) {
		int len = arr.length;
		int[][] dp = new int[len + 1][aim + 1];
		for (int j = 0; j <= aim; j++) {
			dp[len][j] = j == 0 ? 0 : -1;
		}
		for (int i = len - 1; i >= 0; i--) {
			for (int j = 0; j <= aim; j++) {
				int ans = Integer.MAX_VALUE;
				for (int k = 0; j - k * arr[i] >= 0; k++) {
					if (dp[i + 1][j - k * arr[i]] != -1) {
						ans = Math.min(ans, dp[i + 1][j - k * arr[i]] + k);
					}
				}
				ans = ans == Integer.MAX_VALUE ? -1 : ans;
				dp[i][j] = ans;
			}
		}
		return dp[0][aim];
	}

}
