package IndexTree.test;

/**
 * TODO: 【错误点】for二重循环的内循环需要单独设置变量，不能复用。不然第二轮外循环就错了。
 *
 * TODO： 20260710 自己看下面的注释，codex很强
 */
public class IndexTree2D_20260710 {

    int[][] nums;
    int[][] tree;
    int M;
    int N;

    public IndexTree2D_20260710(int[][] matrix) {
        M = matrix.length;
        N = matrix[0].length;
        nums = new int[M][N];
        tree = new int[M + 1][N + 1];
        // TODO: 【特别注意点】本题的特别注意点就是 构造器里使用了类里面的方法。 所以coding时顺序不是特别顺，1. 写构造器写到一半去写update/add，2. 等方法全写完了，回来补充构造器
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                update(i, j, matrix[i][j]);
            }
        }
    }


    public void add(int r, int c, int val) {
        r = r + 1;
        c = c + 1;
        for (; r <= M; r += r & (-r)) {
            // TODO: 错误原因：内层循环直接修改 c，外层 r 进入下一轮时 c 已经不是原始列坐标；修改意见：使用局部变量 col，每一轮外层循环都从原始 c 开始更新列方向路径。
            // for (; c <= N; c += c & (-c)) {
            //     tree[r][c] += val;
            // }
            for (int col = c; col <= N; col += col & (-col)) {
                tree[r][col] += val;
            }
        }
    }

    public void update(int r, int c, int newVal) {
        // Step1: 计算diff差值，更新nums
        int diff = newVal - nums[r][c];
        nums[r][c] = newVal;
        // Step2: 更新indexTree
        add(r, c, diff);
    }

    public int preSum(int r, int c) {
        r = r + 1;
        c = c + 1;
        int ans = 0;
        for (; r > 0; r -= r & (-r)) {
            // TODO: 错误原因：内层循环直接修改 c，外层 r 进入下一轮时 c 已经被减到 0；修改意见：使用局部变量 col，每一轮外层循环都从原始 c 开始查询列方向路径。
            // for (; c > 0; c -= c & (-c)) {
            //     ans += tree[r][c];
            // }
            for (int col = c; col > 0; col -= col & (-col)) {
                ans += tree[r][col];
            }
        }
        return ans;
    }

    // TODO: 这里一定要理顺了。 二维数组，每个位置是一个格子，不要理解成点。
    //      理解成格子，你就知道为什么有这么多 r1-1, c1-1 了
    public int regionSum(int r1, int c1, int r2, int c2) {
        return preSum(r2, c2) - preSum(r1 - 1, c2) - preSum(r2, c1 - 1) + preSum(r1 - 1, c1 - 1);
    }

}
