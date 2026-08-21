package dp.跳马系列;

/**
 * 当前来到的位置是（x,y）
 * 还剩下rest步需要跳
 * 跳完rest步，正好跳到a，b的方法数是多少？
 * 10 * 9
 * <p>
 * 三维表
 * 题目: 从棋盘(0,0)出发，到指定点(x,y)，在K步走到 的方法数
 */
public class HorseJump {

    public static int jump(int a, int b, int k) {
        int[][][] dp = new int[10][9][k + 1];
        dp[a][b][k] = 1;
        for (int z = k - 1; z >= 0; z--) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    // 无效条件 意味着 这个状态是无效的。 无效状态对应本题意就是 在start(x,y,z)下，到目标没有方法数。
                    // TODO: 【你自己看下面错误在哪里！！！】变量名要专门练习一下，每次现想的话，一点不专心，变量就可能写串了
//                    dp[i][j][k] = getValue(i + 1, j - 2, z + 1, dp)
                    dp[i][j][z] = getValue(i + 1, j - 2, z + 1, dp)
                            + getValue(i + 2, j - 1, z + 1, dp)
                            + getValue(i + 1, j + 2, z + 1, dp)
                            + getValue(i + 2, j + 1, z + 1, dp)
                            + getValue(i - 1, j - 2, z + 1, dp)
                            + getValue(i - 2, j - 1, z + 1, dp)
                            + getValue(i - 1, j + 2, z + 1, dp)
                            + getValue(i - 2, j + 1, z + 1, dp);
                }
            }
        }

        // 打印二维数组
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 9; j++) {
//                System.out.print(dp[i][j][0] + " ");
//            }
//            System.out.println();
//        }

        // 返回 START[第0步时，处于(0,0)位置的] 到 END[(a,b) k步]时的总方法数
        return dp[0][0][0];
    }

    public static int getValue(int x, int y, int z, int[][][] dp) {
        if (x < 0 || x >= 10 || y < 0 || y >= 9) {
            return 0;
        }
        return dp[x][y][z];
    }

    public static void main(String[] args) {
        int x = 7;
        int y = 7;
        int step = 10;       // 515813
//        System.out.println(ways(x, y, step));
//        System.out.println(dp(x, y, step));

        System.out.println(jump(x, y, step));
        System.out.println(dpTest(x, y, step));
    }

    public static int dpTest(int a, int b, int K) {
        int[][][] dp = new int[K + 1][10][9];
        dp[K][a][b] = 1;
//        for (int k = K - 1; k >= 0; k++) {   // TODO: 【错误点】从左到右尝试模型，我们是倒着遍历的，所以一定是 step从大到小，step>=0,step--
        for (int k = K - 1; k >= 0; k--) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    int ans = 0;
                    ans += pick(k + 1, i - 1, j - 2, dp);
                    ans += pick(k + 1, i - 1, j + 2, dp);
                    ans += pick(k + 1, i - 2, j - 1, dp);
                    ans += pick(k + 1, i - 2, j + 1, dp);
                    ans += pick(k + 1, i + 1, j - 2, dp);
                    ans += pick(k + 1, i + 1, j + 2, dp);
                    ans += pick(k + 1, i + 2, j + 1, dp);
                    ans += pick(k + 1, i + 2, j - 1, dp);
                    dp[k][i][j] = ans;
                }
            }
        }
        return dp[0][0][0];
    }

    public static int pick(int step, int x, int y, int[][][] dp) {
        if (x >= 0 && x < 10 && y >= 0 && y < 9) {
            return dp[step][x][y];
        } else {
            return 0;
        }
    }

}
