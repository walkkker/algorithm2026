package dp;

/**
 * 这道题很难的，虽然代码短。 我理解这是一个 样本对应模型+业务限制推m/n大小。  样本对应模型就没有从左到右这一说了。
 *
 * <p>
 * Q: 给定一个正数1，裂开的方法有一种，(1)给定一个正数2,裂开的方法有两种，(1和1)、(2)
 * 给定一个正数3，裂开的方法有三种，(1、1、1)、(1、2)、(3)给定一个正数4,裂开的方法有五种,(1、1、1、1)、(1、1、2)、(1、3)、(2、2)、(4)
 * 给定一个正数n，求裂开的方法数。动态规划优化状态依赖的技巧
 *
 * <p>
 * A：核心：我觉得这道题的核心就是 1）按由小到大的顺序splitNumber 2)具体而言，后一个>=前一个 （后一个不能小于前一个的原因是，这样会有重复）
 */
public class SplitNumber {

    /*
        状态定义: dp[pre][rest] TODO: 每一个dp的含义都看作是一个全新的初始状态。 你定义的时候，不要说前一个prexxx。 他就表示一个新的start状态。
                                   所以，这个状态的定义就是：【第一个数字最小从pre开始，分裂rest 的 组合数。】
        状态转移： TODO: 这个难点在于 画图的时候有点晕。 他是一个 不确定的左平行点，然后平行逆对角线一直到 x轴
          i < j:
            dp[i][j] = 1
            for (int cur = pre; cur <= (rest + 1) / 2; cur++) {   // 为什么定义cur名字，因为在当前节点，对我而言第一个分裂的数字是cur
                dp+=dp[cur][rest - cur];
            }
            所以，方向是： 从下往上， 从左往右

        base case:
        i==j dp[i][j]=1
        i > j dp[i][j]=0

     */
    public static int mydp(int n) {  // 给定一个正数n
        // 起始的边界条件也非常重要啊！左神
//        if (n <= 0) {
//            return 0;
//        }
//        if (n == 1) {
//            return 1;
//        }

        int[][] dp = new int[n + 1][n + 1];  // pre[0,n] rest[0,n]
        for (int i = 0; i <= n; i++) {
            dp[i][i] = 1;
        }
        // 推：下上  左右      ！局限于上半区
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j <= n; j++) {
                dp[i][j] = 1;   // 这是直接把后半区域全要了， 因为循环保证了 i<j
                for (int cur = i; cur <= (j + 1) / 2; cur++) {   // 为什么定义cur名字，因为在当前节点，对我而言第一个分裂的数字是cur
                    dp[i][j] += dp[cur][j - cur];
                    // 这里有个判断，j-cur < 0 ? 发现不会，因为cur<=j的上中，所以j-cur>=0 (j==1的话 是0)
                }
            }
        }
        return dp[1][n]; // 第一个分裂数必须>=1的情况下，分裂n的 组合数
    }

    // n为正数
    public static int ways(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return process(1, n);
    }

    // 上一个拆出来的数是pre
    // 还剩rest需要去拆
    // 返回拆解的方法数
    public static int process(int pre, int rest) {
        if (rest == 0) {
            return 1;
        }
        if (pre > rest) {
            return 0;
        }
        int ways = 0;
        for (int first = pre; first <= rest; first++) {
            ways += process(first, rest - first);
        }
        return ways;
    }

    public static int dp1(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
            dp[pre][pre] = 1;
        }
        for (int pre = n - 1; pre >= 1; pre--) {
            for (int rest = pre + 1; rest <= n; rest++) {
                int ways = 0;
                for (int first = pre; first <= rest; first++) {
                    ways += dp[first][rest - first];
                }
                dp[pre][rest] = ways;
            }
        }
        return dp[1][n];
    }

    public static int dp2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
            dp[pre][pre] = 1;
        }
        for (int pre = n - 1; pre >= 1; pre--) {
            for (int rest = pre + 1; rest <= n; rest++) {
                dp[pre][rest] = dp[pre + 1][rest];
                dp[pre][rest] += dp[pre][rest - pre];
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {


        for (int test = 1; test <= 100; test++) {
            int ans1 = ways(test);
            int ans2 = dp1(test);
            int ans3 = dp2(test);
            int ans4 = mydp(test);
            int ans5 = dpTest(test);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4 || ans1 != ans5) {
                System.out.println("opps");
                System.out.println(test);
                System.out.println(ans4);
                System.out.println(ans5);
                break;
            }
        }
        System.out.println("Test Finished!");

    }


    // TODO: 【错题+难题】 主要是本题的思路不好搞，容易为了从小到大排列，遗漏了 剩余num 直接使用num 组成的情况（也就是不拆了）
    public static int dpTest(int n) {

        int[][] dp = new int[n + 1][n + 1];
        // TODO: 【易错点】这里也是一个易错点，实际上因为我们要dp[a][b>=a]，所以本质上只需要处理 正对角线+右上区域
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 1;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j <= n; j++) {
                int ans = 1;   // TODO: 【超级错误点！！！】你设计的是强制从小到大拆分来保证唯一性，最差停在 dp[pre][pre]。 但是这里忽略了一种情况，就是dp[pre][remain](remain>pre)时，因为你追求的是 依赖于 dp[a][b>a]，但是忽略了 此时可以直接不拆分，直接取remain，而这种情况被我们 remain>pre的约束排除在外了，所以初始值要设置为1.
                for (int k = i; k <= j && j - k >= k; k++) {
                    ans += dp[k][j - k];
                }
                dp[i][j] = ans;
            }
        }
        return dp[1][n];
    }

}
