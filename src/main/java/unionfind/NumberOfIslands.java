package unionfind;

/**
 * TODO: 核心：这道题的重点是：把 并查集 扩展到 二维使用 （二维样本，而不是朋友圈的一维样本）。 对外暴露的接口是 二维的坐标。 但是内部通过index下标转换依然使用一维数组实现。
 * <p>
 * 注意：面试场上，本题使用 infect方法
 *
 * <p>
 * 两个方法，相同时间复杂度：
 * (1) 感染 infect
 * (2) 并查集: 二维转一维 这个事情很重要啊（因为数组实现的并查集是一维的）
 * <p>
 * 本题为leetcode原题
 * 测试链接：https://leetcode.com/problems/number-of-islands/
 * 所有方法都可以直接通过
 */
public class NumberOfIslands {

    // 20260625 版本
    // infect版本，有错题，看注释
    class Solution {
        public int numIslands(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            int ans = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        ans++;
                        process(i, j, grid);
                    }
                }
            }
            return ans;
        }
        // TODO: 因为该process函数定义为，传入一个陆地节点'1'，把联通的所有陆地全部感染0
        public void process(int i, int j, char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            if (i < 0 || i >= m || j < 0 || j >= n) {
                return;
            }
            // TODO: 【错误点-stackOverflow无限循环了】下面这句没写：process自己递归自己，什么能感染？ 只有'1'才执行后续操作。 所以要把越界和非1的情况都设置成base case。
            if (grid[i][j] != '1') {
                return;
            }

            grid[i][j] = 0;
            // TODO：下面的感染必须四个方向，不能并查集只右下，因为底层逻辑不同，特别注意，并查集是全部节点包含，最终汇总答案。 而感染是扫到算一个答案，因此必须把上下左右四个方向都感染到位才行！！！
            //  不然你想一个 上 下 下左，如果你只感染右&下，会算作两次。
            process(i - 1, j, grid);
            process(i + 1, j, grid);
            process(i, j - 1, grid);
            process(i, j + 1, grid);
        }
    }

    // 并查集，针对二维样本
    class Solution20260625 {
        public int numIslands(char[][] grid) {
            int n = grid.length;
            int m = grid[0].length;
            UnionFind uf = new UnionFind(grid);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (grid[i][j] == '1') {  // TODO: 【重点】并查集这里只关注 右下就可以覆盖全图了。  【特别注意】感染必须四个方向！！！
                        if (j + 1 < m && grid[i][j + 1] == '1') {
                            uf.union(i, j, i, j + 1);
                        }
                        if (i + 1 < n && grid[i + 1][j] == '1') {
                            uf.union(i, j, i + 1, j);
                        }
                    }
                }
            }
            return uf.sets();


        }

        // 要点
        // 1. 二维union find。 对外接口为二维参数，对内全部转换成一维
        // 2. 核心就是 （1）2D->1D: index = i * 列数 + j。 (2)1D->2D: (index / 列数， index % 列数)


        public class UnionFind {
            int[] father;
            int[] size;
            int[] stack;   // TODO: 【错误点】 写成了 int stack。 数组形式的栈呀！！！
            int sets;
            int cols;

            public UnionFind(char[][] grid) {
                int n = grid.length;
                int m = grid[0].length;
                cols = m;
                int total = n * m;
                father = new int[total];
                size = new int[total];
                stack = new int[total];
                // TODO: 【超重点】 这里的初始化很重要，是唯一重要的与1D公式的区分度，只处理 grid[i][j]=='1'的点，包括 sets++, father[], size[]。 无效点（海水）不管
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (grid[i][j] == '1') {
                            sets++;
                            int index = convertTo1Dim(i, j);
                            father[index] = index;
                            size[index] = 1;
                        }
                    }
                }
            }

            // TODO: 可以是私有方法，仅内部转换
            public int convertTo1Dim(int i, int j) {
                return i * cols + j;
            }


            // TODO: find方法其实也是内部方法，用来找 某个节点的根节点 同时结合数组栈实现路径压缩！！！
            // 本题，该方法代码一点不用变
            public int find(int a) {
                int i = 0;
                while (a != father[a]) {
                    stack[i++] = a;
                    a = father[a];
                }
                while (i > 0) {
                    father[stack[--i]] = a;
                }
                return a;
            }

            // TODO: 本题处理 入参变了，剩下的逻辑一点也没变，直接背诵
            public void union(int i1, int j1, int i2, int j2) {
                int index1 = convertTo1Dim(i1, j1);
                int index2 = convertTo1Dim(i2, j2);
                int fa = find(index1);
                int fb = find(index2);
                if (fa != fb) {
                    if (size[fa] > size[fb]) {
                        father[fb] = fa;
                        size[fa] += size[fb];
                    } else {
                        father[fa] = fb;
                        size[fb] += father[fa];
                    }
                    sets--;
                }
            }

            public int sets() {
                return sets;
            }
        }
    }





    // 方法一 - 一版本（建议使用2版本）： 并查集
    // TODO: 注意，一版本包含了第一次写代码的错误点。但是二版本好用，同时可以直接复用到 NumberOfIslandII问题。 二版本是用union做检查，更简洁。
    // 题目中都默认 m对应行数  n对应列数
    //        int m = grid.length;
    //        int n = grid[0].length;
    public int numIslands1(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int totalNodes = rows * cols;
        UnionFind uf = new UnionFind(grid);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 每个节点都看自己的【右下】，就能联通完整个矩阵了 -》 什么意思呢？ 就是 从左上角节点，只看右下，可以遍历完整个矩阵
                // 【重要】对于每个节点，如果有右节点，就union右节点；如果右下节点，就Union下节点
                // TODO: 很重要，因为 最下一行 和 最右一行 是边界。 要避免数组越界

                // TODO: 【错误】你在想什么？ 梦游吗？   右下是检查的方向，要满足 x点 和 y点 都是1，再union!!!!!!!
                // 下面代码就是不带脑子写的，算法题必须每一步说清楚再写！！！
                //                if (j + 1 < n) {
                //                    uf.union(index(i, j, n), index(i, j + 1, n));
                //                }
                //                if (i + 1 < m) {
                //                    uf.union(index(i, j, n), index(i + 1, j, n));
                //                }
                if (grid[i][j] == '1') {    // 首先需要当前是1
                    if (j + 1 < cols && grid[i][j + 1] == '1') {   // 右侧存在 且 右侧节点是1
                        uf.union(i, j, i, j + 1);
                    }
                    if (i + 1 < rows && grid[i + 1][j] == '1') {     // 下侧存在 且 下侧节点是1
                        uf.union(i, j, i + 1, j);
                    }
                }

            }
        }
        return uf.sets();
    }


    // 对外暴露二维， 内部实现依然为一维
    public static class UnionFind {

        int[] father;
        int[] size;
        int[] stack;
        // TODO: 【错误】这里还有个坑，这个sets!=totalNodes。 因为我们只看哪些1能够连 成一片，所以sets={1的个数}
        int sets;

        int cols;


        public UnionFind(char[][] M) {
            int rows = M.length;
            cols = M[0].length;
            int totalNodes = rows * cols;
            father = new int[totalNodes];
            size = new int[totalNodes];
            stack = new int[totalNodes];
            // TODO: 【错误点+纠正点】sets要1的数量（而不是二维矩阵的总点数），但是 一维的father因为下标变换，所以长度需要是 二维数组的总长度
            sets = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (M[i][j] == '1') {
                        int index = index(i, j);
                        sets++;

                        // TODO: 【切记！！！】构造器内 【一定！！】 再也不能忘了 初始化 father指向自己
                        father[index] = index;
                        size[index] = 1;
                    }
                }
            }
        }


        // TODO: 【很重要】二维转一维：下标变换 (x,y) -> x*列数+y
        public int index(int x, int y) {
            return x * cols + y;
        }

        private int find(int i) {
            int si = 0;
            while (i != father[i]) {
                // TODO: 这里默写快了，千万不要写成i，int[] stack，入栈是 stack[si++]=i
                stack[si++] = i;
                i = father[i];
            }
            for (si--; si >= 0; si--) {
                father[stack[si]] = i;
            }
            return i;
        }

        public void union(int c1, int r1, int c2, int r2) {
            int x = index(c1, r1);
            int y = index(c2, r2);

            int xf = find(x);
            int yf = find(y);
            if (xf != yf) {
                if (size[xf] >= size[yf]) {
                    father[yf] = xf;
                    size[xf] += size[yf];
                } else {
                    father[xf] = yf;
                    size[yf] += size[xf];
                }
                sets--;
            }
        }

        public int sets() {
            return sets;
        }
    }


    // TODO： 面试用这个：方法2：感染
    public int numIslands2(char[][] grid) {
        int sets = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    sets++;
                    infect(i, j ,grid);
                }
            }
        }
        return sets;
    }


    public static void infect(int i, int j, char[][] grid) {
        // 定义base case。 越界 + 值为0 || '0'
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == 0 || grid[i][j] == '0') {
            return;
        }
        grid[i][j] = 0;
        infect(i - 1, j, grid);
        infect(i + 1, j, grid);
        infect(i, j - 1, grid);
        infect(i, j + 1, grid);
    }



    // TODO: 并查集-二版本 （这个可以直接复用到 NumberOfIslandII问题中，复用性很强，背这个）
    // 题目中都默认 m对应行数  n对应列数
    //        int m = grid.length;
    //        int n = grid[0].length;
    public int numIslands3(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int totalNodes = rows * cols;
        UnionFind2 uf = new UnionFind2(grid);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 二版本下，这里就无脑管【右下】方向就行了。 点1 和 点2 的合并有效性全部在union方法里面检查了（越界 + 都是有效节点aka.点值为'1'）
                uf.union(i, j, i , j+ 1);
                uf.union(i, j, i + 1, j);
            }
        }
        return uf.sets();
    }


    // 对外暴露二维， 内部实现依然为一维
    public static class UnionFind2 {

        int[] father;
        int[] size;
        int[] stack;
        // TODO: 【错误】这里还有个坑，这个sets!=totalNodes。 因为我们只看哪些1能够连 成一片，所以sets={1的个数}
        int sets;

        int cols;
        int rows;


        public UnionFind2(char[][] M) {
            rows = M.length;
            cols = M[0].length;
            int totalNodes = rows * cols;
            father = new int[totalNodes];
            size = new int[totalNodes];
            stack = new int[totalNodes];
            // TODO: 【错误点+纠正点】sets要1的数量（而不是二维矩阵的总点数），但是 一维的father因为下标变换，所以长度需要是 二维数组的总长度
            sets = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (M[i][j] == '1') {
                        int index = index(i, j);
                        sets++;

                        // TODO: 【切记！！！】构造器内 【一定！！】 再也不能忘了 初始化 father指向自己
                        father[index] = index;
                        size[index] = 1;
                    }
                }
            }
        }


        // TODO: 【很重要】二维转一维：下标变换 (x,y) -> x*列数+y
        public int index(int x, int y) {
            return x * cols + y;
        }

        private int find(int i) {
            int si = 0;
            while (i != father[i]) {
                // TODO: 这里默写快了，千万不要写成i，int[] stack，入栈是 stack[si++]=i
                stack[si++] = i;
                i = father[i];
            }
            for (si--; si >= 0; si--) {
                father[stack[si]] = i;
            }
            return i;
        }

        // TODO: 二版本的重点改造。
        //    这样写，直接把union的检查放到 并查集class里面。 调用方只需要 右下无脑调就行了。边界都不用关了。
        //    union方法检查 (c1, r1) (c2,r2)的有效性，必须都有效，才进行union操作，否则直接返回。
        public void union(int c1, int r1, int c2, int r2) {
            // TODO：边界检查放到这里做
            if (c1 < 0 || c1 >= rows || r1 < 0 || r1 >= cols || c2 < 0 || c2 >= rows || r2 < 0 || r2 >= cols ) {
                return;
            }

            int x = index(c1, r1);
            int y = index(c2, r2);

            // TODO：size[x]==0 代表 对应的二维点 不是'1'（所以没有被初始化）
            //  x或者y 不是 '1' ，直接返回
            if (size[x] == 0 || size[y] == 0) {
                return;
            }

            int xf = find(x);
            int yf = find(y);
            if (xf != yf) {
                if (size[xf] >= size[yf]) {
                    father[yf] = xf;
                    size[xf] += size[yf];
                } else {
                    father[xf] = yf;
                    size[yf] += size[xf];
                }
                sets--;
            }
        }

        public int sets() {
            return sets;
        }

    }

}
