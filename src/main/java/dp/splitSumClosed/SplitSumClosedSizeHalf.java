package dp.splitSumClosed;

/**
 * 题意：给定一个正数数组arr，请把arr中所有的数分成两个集合如果arr长度为偶数，
 * 两个集合包含数的个数要一样多如果arr长度为奇数，
 * 两个集合包含数的个数必须只差一个请尽量让两个集合的累加和接近
 * 返回:
 * 最接近的情况下，较小集合的累加和
 * <p>
 * TODO： 核心：1）因为指定了数量=>cap。所以 Process多加一个参数 =》 dp多一维
 *            2）其实本质递归是没变的（相较于版本1），因为递归本来就是考虑了所有的子序列组合（要/不要）。只不过我们加入cap来 把一些 子序列 变成无效值，因为不满足cap。（递归树 从头选到尾就是一个完整子序列）
 *            3) 小细节就是 arr.length==奇数的情况，注意一下。最后要比较 两个cap下的dp谁更大。 （因为dp意味着 <=sum的最大）
 */
public class SplitSumClosedSizeHalf {

    public static int mydp(int[] arr) {
        int n = arr.length;
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum = sum / 2;
        int cap = (n + 1) / 2;
        int[][][] dp = new int[n + 1][sum + 1][cap + 1];
        for (int y = 0; y <= sum; y++) {
            for (int z = 0; z <= cap; z++) {
                if (z == 0) {
                    dp[n][y][z] = 0;
                } else {
                    dp[n][y][z] = -1;
                }
            }
        }

        for (int x = n - 1; x >= 0; x--) {
            for (int y = 0; y <= sum; y++) {
                for (int z = 0; z <= cap; z++) {
                    // TODO: 因为存在无效值 + 聚合方式不是累加， 所以不能忽略。 =》 要检查依赖状态 是否是 -1。 只有当不是-1的时候，才能参与 状态转移方程。
                    //  下面的代码，没有完全检查-1。 但是实现的逻辑就是 只有有效的依赖项才能够参与状态转移计算。 如果依赖项都是-1，那么该dp就也是 -1.
                    // if (y - arr[x] < 0 || z - 1 < 0) {
                    if (y - arr[x] < 0 || z - 1 < 0 || dp[x + 1][y - arr[x]][z - 1] == -1) {   // 这个就是 越界是无效状态， 不越界但是==-1也是无效状态。 此时只考虑 不要的情况=>dp[x + 1][y][z]。 进而，该状态其实也要检查是否为-1无效值， 我这么写是因为 缩写等效于 -1检查。（依赖==-1 ？ -1 ： 依赖）
                        dp[x][y][z] = dp[x + 1][y][z];
                    } else {  // 此时 dp[x + 1][y - arr[x]][z - 1] 一定有效。 所以dp[x + 1][y][z]==-1 那结果就是 左子。  !=-1，那就二者比大小。 所以我这个代码缩写了。
                        dp[x][y][z] = Math.max(arr[x] + dp[x + 1][y - arr[x]][z - 1], dp[x + 1][y][z]);
                    }
                }
            }
        }

        int ans;
        if (n % 2 == 0) {  // 偶数
            ans = dp[0][sum][cap];
        } else {  // 奇数   ==> 奇数时因为不知道是 less cap的部分是较小和 还是 more cap是较小和，// 所以求dp的时候，cap = more cap。 然后最后算结果的时候，Math.max 两个dp值。 就能得出到底是 less cap 还是 more cap能够求出【小于等于sum/2的最大和了】。
            ans = Math.max(dp[0][sum][cap], dp[0][sum][cap - 1]);
        }
        return ans;
    }

    public static int right(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        if ((arr.length & 1) == 0) {
            return process(arr, 0, arr.length / 2, sum / 2);
        } else {
            return Math.max(process(arr, 0, arr.length / 2, sum / 2), process(arr, 0, arr.length / 2 + 1, sum / 2));
        }
    }

    // arr[i....]自由选择，挑选的个数一定要是picks个，累加和<=rest, 离rest最近的返回
    public static int process(int[] arr, int i, int picks, int rest) {
        if (i == arr.length) {
            return picks == 0 ? 0 : -1;
        } else {
            int p1 = process(arr, i + 1, picks, rest);
            // 就是要使用arr[i]这个数
            int p2 = -1;
            int next = -1;
            if (arr[i] <= rest) {
                next = process(arr, i + 1, picks - 1, rest - arr[i]);
            }
            if (next != -1) {
                p2 = arr[i] + next;
            }
            return Math.max(p1, p2);
        }
    }

    public static int dp(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum /= 2;
        int N = arr.length;
        int M = (N + 1) / 2;
        int[][][] dp = new int[N + 1][M + 1][sum + 1];
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= M; j++) {
                for (int k = 0; k <= sum; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        for (int rest = 0; rest <= sum; rest++) {
            dp[N][0][rest] = 0;
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int picks = 0; picks <= M; picks++) {
                for (int rest = 0; rest <= sum; rest++) {
                    int p1 = dp[i + 1][picks][rest];
                    // 就是要使用arr[i]这个数
                    int p2 = -1;
                    int next = -1;
                    if (picks - 1 >= 0 && arr[i] <= rest) {
                        next = dp[i + 1][picks - 1][rest - arr[i]];
                    }
                    if (next != -1) {
                        p2 = arr[i] + next;
                    }
                    dp[i][picks][rest] = Math.max(p1, p2);
                }
            }
        }
        if ((arr.length & 1) == 0) {
            return dp[0][arr.length / 2][sum];
        } else {
            return Math.max(dp[0][arr.length / 2][sum], dp[0][(arr.length / 2) + 1][sum]);
        }
    }

//	public static int right(int[] arr) {
//		if (arr == null || arr.length < 2) {
//			return 0;
//		}
//		int sum = 0;
//		for (int num : arr) {
//			sum += num;
//		}
//		return process(arr, 0, 0, sum >> 1);
//	}
//
//	public static int process(int[] arr, int i, int picks, int rest) {
//		if (i == arr.length) {
//			if ((arr.length & 1) == 0) {
//				return picks == (arr.length >> 1) ? 0 : -1;
//			} else {
//				return (picks == (arr.length >> 1) || picks == (arr.length >> 1) + 1) ? 0 : -1;
//			}
//		}
//		int p1 = process(arr, i + 1, picks, rest);
//		int p2 = -1;
//		int next2 = -1;
//		if (arr[i] <= rest) {
//			next2 = process(arr, i + 1, picks + 1, rest - arr[i]);
//		}
//		if (next2 != -1) {
//			p2 = arr[i] + next2;
//		}
//		return Math.max(p1, p2);
//	}
//
//	public static int dp1(int[] arr) {
//		if (arr == null || arr.length < 2) {
//			return 0;
//		}
//		int sum = 0;
//		for (int num : arr) {
//			sum += num;
//		}
//		sum >>= 1;
//		int N = arr.length;
//		int M = (arr.length + 1) >> 1;
//		int[][][] dp = new int[N + 1][M + 1][sum + 1];
//		for (int i = 0; i <= N; i++) {
//			for (int j = 0; j <= M; j++) {
//				for (int k = 0; k <= sum; k++) {
//					dp[i][j][k] = -1;
//				}
//			}
//		}
//		for (int k = 0; k <= sum; k++) {
//			dp[N][M][k] = 0;
//		}
//		if ((arr.length & 1) != 0) {
//			for (int k = 0; k <= sum; k++) {
//				dp[N][M - 1][k] = 0;
//			}
//		}
//		for (int i = N - 1; i >= 0; i--) {
//			for (int picks = 0; picks <= M; picks++) {
//				for (int rest = 0; rest <= sum; rest++) {
//					int p1 = dp[i + 1][picks][rest];
//					int p2 = -1;
//					int next2 = -1;
//					if (picks + 1 <= M && arr[i] <= rest) {
//						next2 = dp[i + 1][picks + 1][rest - arr[i]];
//					}
//					if (next2 != -1) {
//						p2 = arr[i] + next2;
//					}
//					dp[i][picks][rest] = Math.max(p1, p2);
//				}
//			}
//		}
//		return dp[0][0][sum];
//	}

    public static int dp2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum >>= 1;
        int N = arr.length;
        int M = (arr.length + 1) >> 1;
        int[][][] dp = new int[N][M + 1][sum + 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= M; j++) {
                for (int k = 0; k <= sum; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int k = 0; k <= sum; k++) {
                dp[i][0][k] = 0;
            }
        }
        for (int k = 0; k <= sum; k++) {
            dp[0][1][k] = arr[0] <= k ? arr[0] : Integer.MIN_VALUE;
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= Math.min(i + 1, M); j++) {
                for (int k = 0; k <= sum; k++) {
                    dp[i][j][k] = dp[i - 1][j][k];
                    if (k - arr[i] >= 0) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i - 1][j - 1][k - arr[i]] + arr[i]);
                    }
                }
            }
        }
        return Math.max(dp[N - 1][M][sum], dp[N - 1][N - M][sum]);
    }

    // for test
    public static int[] randomArray(int len, int value) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * value);
        }
        return arr;
    }

    // for test
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // for test
    public static void main(String[] args) {
        int maxLen = 100;
        int maxValue = 50;
        int testTime = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr = randomArray(len, maxValue);
//            int ans1 = right(arr);
            int ans2 = dp(arr);
            int ans3 = dp2(arr);
            int ans4 = mydp(arr);
            int ans5 = dpTest(arr);
            if (ans2 != ans4) {
                printArray(arr);
//                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                System.out.println(ans4);
                System.out.println("Oops!");
                break;
            }
            if (ans2 != ans5) {
                printArray(arr);
                System.out.println("Oops, you test version is wrong");
                System.out.println(ans2);
                System.out.println(ans5);
            }
        }
        System.out.println("测试结束");
    }

    public static int dpTest(int[] arr) {
        int len = arr.length;
        int count = (len + 1) / 2;
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum /= 2;
        int[][][] dp = new int[arr.length + 1][sum + 1][count + 1];

        // TODO: 【错误-超级重点】无效值，x=arr.length && remain_count != 0， 则初始化时dp=-1。标记无效 （因为是正数数组）
        //      当初始化层是无效值时，整个往上的依赖链（就是依赖这个无效格子的上层格子），直接也要赋值为 无效值。
        for (int y = 0; y < sum + 1; y++) {
            for (int z = 1; z < count + 1; z++) {
                dp[arr.length][y][z] = -1;
            }
        }


        for (int x = arr.length - 1; x >= 0; x--) {
            for (int y = 0; y < sum + 1; y++) {
                for (int z = 0; z < count + 1; z++) {
                    int yes = -1;
                    int no = -1;
                    // TODO: 【错误】这里yes/no 都要检查无效值
                    if (y - arr[x] >= 0 && z - 1 >= 0 && dp[x + 1][y - arr[x]][z - 1] != -1) {
                        yes = arr[x] + dp[x + 1][y - arr[x]][z - 1];
                    }
                    if (dp[x + 1][y][z] != -1) {
                        no = dp[x + 1][y][z];
                    }
                    dp[x][y][z] = Math.max(yes, no);  // TODO: 【错误点】一定不要忘了给dp[x][y][z]赋值！
                }
            }
        }
        if ((arr.length % 2) == 0) {
            return dp[0][sum][count];
        } else {
            return Math.max(dp[0][sum][count - 1], dp[0][sum][count]);
        }
    }


}
