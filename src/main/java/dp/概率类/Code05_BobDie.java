package dp.概率类;

/**
 * 给定5个参数,N,M,row,col,k表示在N*M的区域上，
 * 醉汉Bob初始在(row,col)位置Bob一共要迈出k步，
 * 且每步都会等概率向上下左右四个方向走一个单位任何时候Bob只要离开N*M的区域，
 * 就直接死亡返回k步之后，Bob还在N*M的区域的概率
 */
public class Code05_BobDie {

    /**
     * @param row 起点位置
     * @param col 起点位置
     * @param k   走k步
     * @param N   矩阵行数
     * @param M   矩阵列数
     * @return
     */
    public static double mydp(int row, int col, int k, int N, int M) {
        /*
        首先总的可能性数量为： Math.pow(4, k)  ->  因为每次有4个方向走

        dp[k][i][j]

        [k+1](i-1, j)
        (i+1, j)
        (i, j-1)
        (i, j+1)

        base case => z=k时，dp[x][y]全为1

        其实这个没用：
        出界时，dp语义为 START2END （走完k步后）留在矩阵里的个数。 1）语义层面：无效值代表 没有走法=0 2）递归方面：累加，所以0没有影响
        出界问题：第k1步走出边界，那么还剩 k-k1步没走，也就是对应Math.pow(4, 剩余步数)种可能性，对应 pow(4, k - k1)
         */
        int[][][] dp = new int[N][M][k + 1];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < M; y++) {
                dp[x][y][k] = 1;
            }
        }

        for (int z = k - 1; z >= 0; z--) {
            for (int x = 0; x < N; x++) {
                for (int y = 0; y < M; y++) {
                    dp[x][y][z] += pick(x - 1, y, z + 1, dp, k);
                    dp[x][y][z] += pick(x + 1, y, z + 1, dp, k);
                    dp[x][y][z] += pick(x, y - 1, z + 1, dp, k);
                    dp[x][y][z] += pick(x, y + 1, z + 1, dp, k);
                }
            }
        }
        return dp[row][col][0] / Math.pow(4, k);   // TODO: 因为Math.pow 返回值为double类型。 所以自动转了
    }


    public static int pick(int x, int y, int z, int[][][] dp, int k) {
        int rows = dp.length;
        int cols = dp[0].length;
        if (x < 0 || x >= rows || y < 0 || y >= cols) {
            int remaining = k - z;
            return 0;
        }
        return dp[x][y][z];
    }


    public static double livePosibility1(int row, int col, int k, int N, int M) {
        return (double) process(row, col, k, N, M) / Math.pow(4, k);
    }

    // 目前在row，col位置，还有rest步要走，走完了如果还在棋盘中就获得1个生存点，返回总的生存点数
    public static long process(int row, int col, int rest, int N, int M) {
        if (row < 0 || row == N || col < 0 || col == M) {
            return 0;
        }
        // 还在棋盘中！
        if (rest == 0) {
            return 1;
        }
        // 还在棋盘中！还有步数要走
        long up = process(row - 1, col, rest - 1, N, M);
        long down = process(row + 1, col, rest - 1, N, M);
        long left = process(row, col - 1, rest - 1, N, M);
        long right = process(row, col + 1, rest - 1, N, M);
        return up + down + left + right;
    }

    public static double livePosibility2(int row, int col, int k, int N, int M) {
        long[][][] dp = new long[N][M][k + 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                dp[i][j][0] = 1;
            }
        }
        for (int rest = 1; rest <= k; rest++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    dp[r][c][rest] = pick(dp, N, M, r - 1, c, rest - 1);
                    dp[r][c][rest] += pick(dp, N, M, r + 1, c, rest - 1);
                    dp[r][c][rest] += pick(dp, N, M, r, c - 1, rest - 1);
                    dp[r][c][rest] += pick(dp, N, M, r, c + 1, rest - 1);
                }
            }
        }
        return (double) dp[row][col][k] / Math.pow(4, k);
    }

    public static long pick(long[][][] dp, int N, int M, int r, int c, int rest) {
        if (r < 0 || r == N || c < 0 || c == M) {
            return 0;
        }
        return dp[r][c][rest];
    }

    public static void main(String[] args) {
        System.out.println(livePosibility1(6, 6, 10, 50, 50));
        System.out.println(livePosibility2(6, 6, 10, 50, 50));
        System.out.println(mydp(6, 6, 10, 50, 50));
        System.out.println(dpTest(6, 6, 10, 50, 50));
    }



    public static double dpTest(int row, int col, int k, int N, int M) {
        int[][][] dp = new int[k + 1][N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                dp[k][i][j] = 1;
            }
        }

        // TODO: 【错误点】一定要注意 标量不能动，本体k作为k步 && x轴。 因为我错误的先使用了k作为变量，所以for循环触发了k--。 后面的赋值就完全只给固定的step层赋值了， 并且这是完全错的！（1. k值无论如何不能改； 2. 一定要主要区分哪些变量作为x,y,z 或者 xx, i,j ）。
        //   就你不想写step，你用x也可以， x,i,j 不寒蝉！！！
//        for (int step = k - 1; k >= 0; k--) {
        for (int step = k - 1; step >= 0; step--) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    int ans = 0;
                    ans = (i - 1 >= 0 ? dp[step + 1][i - 1][j] : 0)
                            +
                            (i + 1 < N ? dp[step + 1][i + 1][j] : 0)
                            +
                            (j - 1 >= 0 ? dp[step + 1][i][j - 1] : 0)
                            +
                            (j + 1 < M ? dp[step + 1][i][j + 1] : 0);
                    dp[step][i][j] = ans;
                }
            }
        }
        double total = Math.pow(4, k);
        return dp[0][row][col] / total;
    }
}


