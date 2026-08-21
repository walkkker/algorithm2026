package IndexTree;
// LeetCode 308（Range Sum Query 2D - Mutable）
/**
 * TODO: 【特别重要的纠错点】我们每次聊坐标，但是一定要清楚->在一/二维数组中，坐标对应的是【格子】，而不是线交叉的点。！！！
 * TODO： 【特别-错误点】codex检查出来了！！！ 二维循环时，一定要注意外层循环后，内层循环要从头开始，所以不能直接 for(; c <= N; c += c&(-c)), 这样的话第一次循环结束后，c已经超出了N，第二次循环的时候是没有内层循环的！！！！
 *
 *
 * 测试链接：https://leetcode.com/problems/range-sum-query-2d-mutable
 * <p>
 * 这是一个实现二维树状数组 (Fenwick Tree / Binary Indexed Tree 2D）的代码，用于高效解决 LeetCode 308 题 "Range Sum Query 2D - Mutable"。
 * <p>
 * 问题核心：
 * 设计一个数据结构，能够对一个二维整数矩阵（可变）高效地执行以下两种操作：
 * 更新 (Update)： 更新矩阵中某个指定单元格的值。
 * 区域和查询 (SumRegion)： 快速计算并返回矩阵中某个矩形区域内所有值的总和。
 * <p>
 * 代码解析：
 * 核心数据结构 (tree)：
 * 这是一个维护“前缀和信息”的辅助二维数组，尺寸为 (N+1) * (M+1)。
 * 它的构建逻辑基于“树状数组”思想，每个 tree[i][j]存储原矩阵中一个特定矩形区域的和，而非简单的 (0,0) -> (i-1, j-1)的总和。这种组织方式使得更新和查询的时间复杂度都能达到 O(logN * logM)。
 **/

// TODO: update和sum方法都是必须接收matrix原坐标，在内部实现的时候转换为index tree的+1坐标。
//      ps: 这个是很好理解的，你可以看做index tree是一个数据结构。 那么对外暴露的接口的参数，一定是 用户所理解的matrix的坐标参数，而不是你index tree逻辑下的+1参数。！！！
//      所以，简而言之，update传入原始位置与更新值，【重要】sum也是传入原始位置，即可返回累加和（是在内部实现的+1逻辑后，二重循环累加logN*logM得到累加和）


public class IndexTree2D {



    // TODO: 但凡你把index tree扩展到单点更新，你就需要多一个跟原始数组一样大小的辅助数组，用来记录旧值。
    private int[][] nums;  // TODO：因为indexTree只支持单点add，所以拓展到update，需要建立一个拷贝数组，每次将update转化成add。 addValue = newValue - nums[i][j] (增量=新-旧)
    private int[][] tree; // 这个rows & cols都要+1的
    int N;  // 这两个拿出来，是为了 update ,sum方法内部 可以 直接引用。 因为都涉及到了二重循环
    int M;

    public IndexTree2D(int[][] matrix) {
        // TODO: 左神代码，我漏了边界检查
        if (matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        N = matrix.length;
        M = matrix[0].length;
        nums = new int[N][M];
        tree = new int[N + 1][M + 1];
        // TODO: 这个要记住，很重要。 初始化的时候，nums[][]是初始化的空数组。然后依次调用update，把原数组的值更新到 nums和tree中。
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                update(i, j, matrix[i][j]);
            }
        }
    }


    public void update(int row, int col, int val) {
        int addValue = val - nums[row][col];
        // TODO: 【错误】漏掉了
        nums[row][col] = val;
        for (int i = row + 1; i <= N; i += (i & (-i))) {
            for (int j = col + 1; j <= M; j += (j & (-j))) {
                tree[i][j] += addValue;
            }
        }
    }

    // TODO: 函数定义：**传入原始位置**，返回前缀和（左上角[0][0],右下角[i][j]的区域累加和）
    public int sum(int row, int col) {
        row = row + 1;
        col = col + 1;
        int ans = 0;
        for (; row > 0; row -= (row & -row)) {
            // TODO: 【错误-codex】错误原因：内层循环直接修改 col，外层 row 进入下一轮时 col 已经被减到 0；修改意见：使用局部变量 j，每一轮外层循环都从原始 col 开始查询列方向路径。
            // for (; col > 0; col -= (col & -col)) {
            //     ans += tree[row][col];
            // }
            for (int j = col; j > 0; j -= (j & -j)) {
                ans += tree[row][j];
            }
        }
        return ans;
    }

    // 这是目标函数： 传入matrix的 左上角和右下角坐标，返回 矩形的累加和。 因为我们借助sum（sum和update都是接收的原坐标，在内部转换为index tree的+1坐标），所以直接把原坐标传给sum。
    public int sumRegion(int row1, int col1, int row2, int col2) {
        // TODO: 【错误！！！后面这句话你先想一下（我一开始想错了），我哪里说错了？】首先这道题好像就不太对劲，这里传入的是 tree数组的对应坐标，不是matrix的坐标。 =》 不是的，你完全错了。 这个之所以可以这么做是因为 我们调用的是sum函数，而sum函数的入参是 matrix的原坐标。！！！！
        //       【特别重要的纠错点】我们每次聊坐标，但是一定要清楚->在一/二维数组中，坐标对应的是【格子】，而不是线交叉的点。！！！
        //        所以下列代码是错误的：
        // return sum(row2, col2) - sum(row1, col2) - sum(row2, col1) + sum(row1, col1);
        // TODO: 【解决步骤】1)要画图 2)坐标/下标对应格子！ 3) 分成四块，每一块的定位方式都是 左上角->【右下角】定位一个矩形，所以只需要确认右下角的坐标！
        return sum(row2, col2) - sum(row1 - 1, col2) - sum(row2, col1 - 1) + sum(row1 - 1, col1 - 1);
        // 补充：为什么有这么多-1的操作？就是因为 方格图的每一个方格对应下标(i, j)。 所以当你找需要被处理的其他子矩形的时候，
        // 他们的右下角节点，一定都会在 行/列上 -1。 画图便知，直接在纸上写好最终的计算公式，然后写代码的时候抄到代码框里就可以了。
    }

    public int regionSum(int row1, int col1, int row2, int col2) {
        // TODO: 【错误！！！后面这句话你先想一下（我一开始想错了），我哪里说错了？】首先这道题好像就不太对劲，这里传入的是 tree数组的对应坐标，不是matrix的坐标。 =》 不是的，你完全错了。 这个之所以可以这么做是因为 我们调用的是sum函数，而sum函数的入参是 matrix的原坐标。！！！！
        //       【特别重要的纠错点】我们每次聊坐标，但是一定要清楚->在一/二维数组中，坐标对应的是【格子】，而不是线交叉的点。！！！
        //        所以下列代码是错误的：
        // return sum(row2, col2) - sum(row1, col2) - sum(row2, col1) + sum(row1, col1);
        // TODO: 【解决步骤】1)要画图 2)坐标/下标对应格子！ 3) 分成四块，每一块的定位方式都是 左上角->【右下角】定位一个矩形，所以只需要确认右下角的坐标！
        return sum(row2, col2) - sum(row1 - 1, col2) - sum(row2, col1 - 1) + sum(row1 - 1, col1 - 1);
        // 补充：为什么有这么多-1的操作？就是因为 方格图的每一个方格对应下标(i, j)。 所以当你找需要被处理的其他子矩形的时候，
        // 他们的右下角节点，一定都会在 行/列上 -1。 画图便知，直接在纸上写好最终的计算公式，然后写代码的时候抄到代码框里就可以了。
    }

}
