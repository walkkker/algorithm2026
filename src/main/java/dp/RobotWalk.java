package dp;

/**
 * N - (1, N)个位置
 * start 起始位置
 * aim 目标位置
 * K 总共给予的步数
 * <p>
 * TODO：判断： 从左往右的尝试模型 -> 哪里从左往右了？  总共给K步，这个步数是从左往右走  => 在本题的实现中，从左往右的尝试模型，process的起点 和 最终dp[][]的返回值，都是指代起点位置。 因为语义都是 从【起点】 到【目标】的 答案。
 * TODO：-> 步数i代表当前走的步数 （从小到大 遇到K终止），i代表递归的深度。 而另一个pos 仅仅代表 递归的宽度。 所以i才能作为 base case。
 * TODO：-> 由此我们得出，能够影响递归深度的参数，才能作为 base case。
 * <p>
 * 返回总共有多少种走法？
 */
public class RobotWalk {
    // 二维
    // （1）一个点依赖 上下左右
    // （2）i 依赖 i+1
    public static int ways1(int N, int start, int aim, int K) {
        if (K == 0 || N <= 1) {
            return 0;
        }
        // TODO: 总共走K步，那么第一步 step应为1！！！ 这个理解是没错的！！！！  但是初始状态是还没开始走呀 -》 所以是 第0步 站在start上！！！而不是第一步站在start上，走出去（aka.递归）才算步数+1  =》 解决方案：脑海中想象 举个最小例子
//        return process(0, start, N, aim, K);
        // TODO：不对！！！ 下面这个是错的。 当前处于 0 步。 不是1步！！！ 这是状态设置的问题！！！
        // TODO：【错误】后面这个是错的！！return process(1, start, N, aim, K);
        return process(0, start, N, aim, K);
    }

    public static int process(int step, int pos, int N, int aim, int K) {
        // TODO: 这个终止条件写错了！！！ 真正的base case只有一个：step=K。 至于pos=aim? 这只是影响了 base case的返回值。
        //  但是pos与【是否达成base case无关】， base case只与 结束状态有关 -> 也就是 step ==K
// 下面这个写法导致 java.lang.StackOverflowError  =》 因为base case写错了 =》存在某个递归 ，永远无法到达base case，那么就无限递归下去了 （比如左右反复横跳）
//        if (step == K && pos == aim) {
//            return 1;
//        }
        if (step == K) {
            if (pos == aim) {
                return 1;
            } else {
                return 0;
            }
        }

        int total = 0;
        // 依赖 前一个位置 或 后一个位置
        if (pos == 1) {
            total = process(step + 1, pos + 1, N, aim, K);
        } else if (pos == N) {
            total = process(step + 1, pos - 1, N, aim, K);
        } else {
            int p1 = process(step + 1, pos + 1, N, aim, K);
            int p2 = process(step + 1, pos - 1, N, aim, K);
            total = p1 + p2;
        }
        return total;
    }


    // 方法2： 记忆化搜索
    public static int ways2(int N, int start, int aim, int K) {
        if (K == 0 || N <= 1) {
            return 0;
        }

        // S1: 首先要确定int[step][pos] dp大小
        // step [0, K]   pos [1, N]
        int[][] dp = new int[K + 1][N + 1];
        // S2: 我要初始化int[][] dp的元素。 因为是缓存法，所以要能够判断 是否 命中缓存。
        // 所以，初始化时，dp值要能够被判断出 已经缓存/未被缓存。  结合本题dp值为方式数，所以-1代表无效值，即未被缓存。
        for (int i = 0; i <= K; i++) {
            for (int j = 0; j <= N; j++) {
                dp[i][j] = -1;
            }
        }

        return process2(0, start, N, aim, K, dp);
    }

    public static int process2(int step, int pos, int N, int aim, int K, int[][] dp) {

        if (dp[step][pos] != -1) {
            return dp[step][pos];
        }

        if (step == K) {
            int ans;
            if (pos == aim) {
                ans = 1;
            } else {
                ans = 0;
            }
            dp[step][pos] = ans;
            return ans;
        }

        int total = 0;
        // 依赖 前一个位置 或 后一个位置
        if (pos == 1) {
            total = process2(step + 1, pos + 1, N, aim, K, dp);
        } else if (pos == N) {
            total = process2(step + 1, pos - 1, N, aim, K, dp);
        } else {
            int p1 = process2(step + 1, pos + 1, N, aim, K, dp);
            int p2 = process2(step + 1, pos - 1, N, aim, K, dp);
            total = p1 + p2;
        }
        dp[step][pos] = total;
        return total;
    }


    // 方法3： 转dp
    // TODO: 【错误点】dp语义一定要定义清楚，对于 最终返回dp[?][?]时格外重要！！！
    // 比如本题的dp[i][j] 意思是 当前（位于第i不，位于j位置），到 最终第K步且位于aim位置的方法数。
    // 简而言之，就是 起始位置 到 目标位置的 方法数！！！ 所以最终return dp[0][start]。
    public static int ways3(int N, int start, int aim, int K) {
        if (K == 0 || N <= 1) {
            return 0;
        }

        // S1: 首先要确定int[step][pos] dp大小
        // step [0, K]   pos [1, N]
        int[][] dp = new int[K + 1][N + 1];
        // S2: 与傻缓存不一样 -> 不需要初始化数字，因为这不是缓存。
        // 而是依据依赖关系，逐层填表

        // a -> b , 表示a依赖b。   a => b 表示 a推出b，即b依赖a
        // step -> step+1;
        // base case: (K, aim) = 1, (K, 其他) = 0
        // (i,j) -> (i + 1, j + 1) + (i + 1, j - 1)
        // 最后要求的位置 dp[0][aim]
        //（1）注意，先判断依赖方向 分别依赖 左下 和 右下
        //（2）如果撞墙了，那就只关心 合法范围内的依赖
        // (3)同时撞墙，往往意味着 可以优先初始化

        for (int j = 1; j <= N; j++) {
            dp[K][j] = j == aim ? 1 : 0;
        }
        // 那这个矩阵很好填，从下往上依次填就可以了， 从左往右还是从右往左都无所谓
        for (int i = K - 1; i >= 0; i--) {
            for (int j = 1; j <= N; j++) {
                // 因为 依赖 左下+右下
                // 所以 通过 if检查不越界 即可累加到ans上
                // TODO: 简而言之： 因为依赖 左下+右下，所以代码逻辑为 【有左加左，有右加右】
                int total = 0;
                if (j - 1 >= 1) {    // j - 1 合法 才加左下 。 这种设计 支持 最左和最右点的
                    total += dp[i + 1][j - 1];
                }
                if (j + 1 <= N) {   // j + 1 合法 才加右下
                    total += dp[i + 1][j + 1];
                }
                dp[i][j] = total;
            }
        }
        // TODO: 【错误！！！】还是语义不清晰的问题， 这个走步数的问题，你怎么就一直搞倒了？？？
        //      错误句子：  return dp[K][aim];
        // 释义：-》 总共K步，目标aim。 我当前处在第0步，位于 start 位置时， 到达 K步且在aim上的 方法数
        // 说白了，这个dp的语义就是 起始位置
        return dp[0][start];
    }


    public static void main(String[] args) {
        System.out.println(ways1(5, 2, 4, 6));
        System.out.println(ways2(5, 2, 4, 6));
        System.out.println(ways3(5, 2, 4, 6));
        System.out.println(dpTest(5, 2, 4, 6));
    }

    public static int dpTest(int N, int start, int aim, int K) {
        int[][] dp = new int[K + 1][N];
        dp[K][aim-1] = 1;
        for (int i = K - 1; i >= 0; i--) {
            for (int j = 0; j < N; j++) {
                if (j == 0) {
                    dp[i][j] = dp[i + 1][j + 1];
                } else if (j == N - 1) {
                    dp[i][j] = dp[i + 1][j - 1];
                } else {
                    dp[i][j] = dp[i + 1][j + 1] + dp[i + 1][j - 1];
                }
            }
        }
        return dp[0][start-1];
    }

}
