package dp.货币面值系列;

/**
 * 有重复，但是每张货币认为是不同的货币。求满足aim的组合数
 * <p>
 * 经典的 从左到右的尝试模型 =》就是背包问题
 */
public class Code02_CoinsWayEveryPaperDifferent {


    public static int myCoinWays(int[] arr, int aim) {
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][aim] = 1;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = aim; j >= 0; j--) {
                int p1 = dp[i + 1][j];
                int p2 = 0;
                if (j + arr[i] <= aim) {
                    p2 = dp[i + 1][j + arr[i]];
                }
                dp[i][j] = p1 + p2;
            }
        }
        return dp[0][0];
    }

    public static int coinWays(int[] arr, int aim) {
        return process(arr, 0, aim);
    }

    // arr[index....] 组成正好rest这么多的钱，有几种方法
    public static int process(int[] arr, int index, int rest) {
        if (rest < 0) {
            return 0;
        }
        if (index == arr.length) { // 没钱了！
            return rest == 0 ? 1 : 0;
        } else {
            return process(arr, index + 1, rest) + process(arr, index + 1, rest - arr[index]);
        }
    }

    public static int dp(int[] arr, int aim) {
        if (aim == 0) {
            return 1;
        }
        int N = arr.length;
        int[][] dp = new int[N + 1][aim + 1];
        dp[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                dp[index][rest] = dp[index + 1][rest] + (rest - arr[index] >= 0 ? dp[index + 1][rest - arr[index]] : 0);
            }
        }
        return dp[0][aim];
    }

    // 为了测试
    public static int[] randomArray(int maxLen, int maxValue) {
        int N = (int) (Math.random() * maxLen);
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = (int) (Math.random() * maxValue) + 1;
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
        int testTime = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr = randomArray(maxLen, maxValue);
            int aim = (int) (Math.random() * maxValue);
            int ans1 = coinWays(arr, aim);
            int ans2 = dp(arr, aim);
            int ans3 = myCoinWays(arr, aim);
            int ans4 = dpTest(arr, aim);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4) {
                System.out.println("Oops!");
                printArray(arr);
                System.out.println(aim);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                System.out.println(ans4);
                break;
            }
        }
        System.out.println("测试结束");
    }

    public static int dpTest(int[] arr, int aim) {
        int len = arr.length;
        int[][] dp = new int[len + 1][aim + 1];
        dp[len][0] = 1;

        for (int i = len - 1; i >= 0; i--) {
            for (int j = 0; j <= aim; j++) {
                int ans = 0;
                if (j - arr[i] >= 0) {
                    ans += dp[i + 1][j - arr[i]];
                }
                ans += dp[i + 1][j];
                dp[i][j] = ans;
            }
        }
        return dp[0][aim];
    }

}
