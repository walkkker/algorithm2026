package dp.概率类;


/**
 * 非常经典的一道题：
 * TODO： 说明了一个非常重要的问题：DP中的越界状态不一定是无效状态。也有可能是有效状态。
 * 		（1）判断标准要 结合状态语义 + dp语义。
 * 		（2）无效值的话，依据 聚合操作（累加0，最大最小取最值+主方法if判断）
 * 			【重点】有效值的话，如本题，意味着实现了剪枝，我们要把剪枝的这个子树的
 * 				所有叶子节点算出来（Math.pow(option_num, 剩余)）。 本题就是剪枝的递归节点是杀死怪兽的，
 * 				但是它依赖的节点都超出了dp范围。（我觉得从某个角度也可以理解是业务限制模型，但是我们实现上尝试减小数组长度，aka. 对dp进行剪枝）
 * 			***	所以看似超出dp边界的状态点，实际上是不越界的，也可以从这个角度 诠释为什么越界dp的点其实是 有效点。 因为正规的 列数应该是 M * K长度的（不管你是 定义为从0血积累血砍，还是N血往下减着砍）
 * 			*** 既然越界了，这种有效点的值 可以看作是一种base case，即不需要依赖即可算出来的。 在本题中，计算方式如上述。
 */
public class Code01_KillMonster {

    /**
     * 最后求，多大概率能够把怪兽砍死。
     *
     * @param N 怪兽的总血量
     * @param M 随机砍一刀，伤害值在[0,M]等概率发生。
     * @param K 总共砍K刀
     * @return
     */
    public static double mydp(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {  // 这个复制左神的吧，我其实不是很认同。 或许是想把所有非法条件都定义为 概率为0吧
            return 0;
        }
        // [0,k] [0, N]
        int[][] dp = new int[K + 1][N + 1];  // 定义为，第i步 j滴血的时候 砍死怪兽的组合数
        // i=k 意味着 第K次已经结束 （证明：初始0，第一次1，第二次2. 所以i代表第i次结束的初始状态。）
        // 所以同步到递归Process，i=k代表来到了 终止条件，在该终止条件下：j<=0意味着 1. j>0意味着怪兽没死，所以0
        dp[K][0] = 1;
        for (int i = K - 1; i >= 0; i--) {
            for (int j = 0; j <= N; j++) {
                // 这里封装pick函数 处理边界问题。 但是难点在于，越界为到了副职，而dp[i][-xx]意味着 在第i步怪兽就死了，所以它后续继续砍的话，肯定怪兽都是死。
                // 所以，此时对于 dp[i][-xx]的值 为有效值。 根据语义计算->在这种条件下，怪兽死亡的次数为所有最终的叶子节点数量 Math.pow(M + 1, K - i)
                for (int v = 0; v <= M; v++) {
                    dp[i][j] += pick(i + 1, j - v, dp, M, K);
                }
            }
        }
        int ans = dp[0][N];   // 在第0步，N滴血的时候。 砍死怪兽的组合数
        return ans / Math.pow(M + 1, K);   // 最终求：砍死的组合数 /  总的组合数
    }

    public static int pick(int x, int y, int[][] dp, int M, int K) {
        if (y < 0) {
            return (int) Math.pow(M + 1, K - x);
        }
        return dp[x][y];
    }


    public static double right(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }
        long all = (long) Math.pow(M + 1, K);
        long kill = process(K, M, N);
        return (double) ((double) kill / (double) all);
    }

    // 怪兽还剩hp点血
    // 每次的伤害在[0~M]范围上
    // 还有times次可以砍
    // 返回砍死的情况数！
    public static long process(int times, int M, int hp) {
        if (times == 0) {
            return hp <= 0 ? 1 : 0;
        }
        if (hp <= 0) {
            return (long) Math.pow(M + 1, times);
        }
        long ways = 0;
        for (int i = 0; i <= M; i++) {
            ways += process(times - 1, M, hp - i);
        }
        return ways;
    }

    public static double dp1(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }
        long all = (long) Math.pow(M + 1, K);
        long[][] dp = new long[K + 1][N + 1];
        dp[0][0] = 1;
        for (int times = 1; times <= K; times++) {
            dp[times][0] = (long) Math.pow(M + 1, times);
            for (int hp = 1; hp <= N; hp++) {
                long ways = 0;
                for (int i = 0; i <= M; i++) {
                    if (hp - i >= 0) {
                        ways += dp[times - 1][hp - i];
                    } else {
                        ways += (long) Math.pow(M + 1, times - 1);
                    }
                }
                dp[times][hp] = ways;
            }
        }
        long kill = dp[K][N];
        return (double) ((double) kill / (double) all);
    }

    public static double dp2(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }
        long all = (long) Math.pow(M + 1, K);
        long[][] dp = new long[K + 1][N + 1];
        dp[0][0] = 1;
        for (int times = 1; times <= K; times++) {
            dp[times][0] = (long) Math.pow(M + 1, times);
            for (int hp = 1; hp <= N; hp++) {
                dp[times][hp] = dp[times][hp - 1] + dp[times - 1][hp];
                if (hp - 1 - M >= 0) {
                    dp[times][hp] -= dp[times - 1][hp - 1 - M];
                } else {
                    dp[times][hp] -= Math.pow(M + 1, times - 1);
                }
            }
        }
        long kill = dp[K][N];
        return (double) ((double) kill / (double) all);
    }

    public static void main(String[] args) {
        int NMax = 10;
        int MMax = 10;
        int KMax = 10;
        int testTime = 200;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int N = (int) (Math.random() * NMax);
            int M = (int) (Math.random() * MMax);
            int K = (int) (Math.random() * KMax);
            double ans1 = right(N, M, K);
            double ans2 = dp1(N, M, K);
            double ans3 = dp2(N, M, K);
            double ans4 = mydp(N, M, K);
            double ans5 = dpTest(N, M, K);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4 || ans1 != ans5) {
                System.out.println("Oops!");
                System.out.println(N + " " + M + " " + K);
                System.out.println(ans1);
                System.out.println(ans5);
                break;
            }
        }
        System.out.println("测试结束");
    }

    // TODO: 【反思】这道题体现出来 超出dp但是有效的格子，他们的值不是一样的，也是需要单独计算的。 只不过不需要依赖于状态转移，而是依赖于递归树，可以直接计算得出第n层某个节点的值。
    public static double dpTest(int N, int M, int K) {
        if (N < 1 || M < 1 || K < 1) {
            return 0;
        }

        int[][] dp = new int[K + 1][N + 1];
        dp[K][0] = 1;

        for (int i = K - 1; i >= 0; i--) {
            for (int j = 0; j <= N; j++) {
                int ans = 0;
                for (int k = 0; k <= M; k++) {
                    if (j - k < 0) {   // 这里 j-k<=0 也可以，结果都pass。 但是逻辑严谨一点，特殊情况只考虑 不在dp表格内的情况。 如果能够依赖表格内的各自，还是写状态转移。
//                        ans++;   // TODO: 【错误点】考虑到出界但有效了，但是有效不单纯是1，而是各有各的值
                        ans += Math.pow(M + 1, K - 1 - i);    // TODO: 【重点】我觉得最难的是这行，需要将dp关联到递归树， 最终的分数就是 叶子结点的不断向上汇总。
                    } else {
                        ans += dp[i + 1][j - k];
                    }
                }
                dp[i][j] = ans;
            }
        }

        double total = Math.pow(M + 1, K);
        return dp[0][N] / total;
    }

}
