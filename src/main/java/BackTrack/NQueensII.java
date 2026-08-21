package BackTrack;

/**
 *  Verification: 直接做leetcode
 *
 * TODO: 采用左神方式，使用 int[] record 数组，index对应行号，record[index]对应列号。 每次放置棋子时，检查[0,i-1] isValid()函数。 检查是否存在同一列，同一主副对角线（abs(r1-r2) == abs(c1-c2)）
 *      因为 对角线都是 45度角。  所以point1和point2在一条对角线上（不管是平行于主对角线还是副对角线），都是一个45°等腰直角三角形=》根据“等角对等边”，它也有两条相等的边=》行宽=列宽。
 *
 *
 *
 * 要点：检查时间复杂度
 * 1） 二维数组扫描 char[][] board 循环检查已放置的每个皇后 O(n)
 * 2）数组标记法 boolean[] col, mainDiag, subDiag 查三个布尔数组 O(1)
 * <p>
 * [N皇后 - 返回成功的棋盘布局数目](src/main/java/backtracking/NQueens2.java):
 * https://leetcode-cn.com/problems/n-queens-ii/
 * 暴力回溯是以每个被选中的节点为中心，看是否与其他已经选中的节点冲突；   一维数组表示方式，是以 已经选中的点为中心，来判断 待检测点 是否有效。
 * 注意Solution2中的注解：  在 isValid函数中，对于 (row,col)节点，只遍历到 selected 数组的 **[0, row)**，因为对于每个 row 行的检查，只检查 上面的区域，因为只有上面的区域为已经选中的点，当到达 row行时，只检查上面已选中的点 是否与 当前点 冲突。所以一定要控制好遍历的范围。
 */
public class NQueensII {

    public int totalNQueens(int n) {
        // n * n board

        int[] record = new int[n];
        // TODO:【错误】 Process语义：当前在第x行，基于前面queen摆放的信息（record），有多少种方法数摆皇后
        //        int ans = process(n, record);
        int ans = process(0, record);
        return ans;
    }

    public static int process(int x, int[] record) {
        if (x == record.length) {     // 越过最后一行，标志base case已经结束。 此时递归一条完整的方案，因此方案数返回1
            return 1;
        }
        int ans = 0;
        for (int j = 0; j < record.length; j++) {
            if (isValid(x, j, record)) {
                record[x] = j;
                ans += process(x + 1, record);
            }
        }
        return ans;
    }

    public static boolean isValid(int x, int y, int[] record) {
        // [0, x-1] 只有上半区域放置了皇后
        for (int i = 0; i < x; i++) {
            // (i, record[i])    (x, y)
            if (record[i] == y || Math.abs(i - x) == Math.abs(record[i] - y)) {
                return false;
            }
        }
        return true;
    }


    public static int num1(int n) {
        if (n < 1) {
            return 0;
        }
        int[] record = new int[n];
        return process1(0, record, n);
    }

    // 当前来到i行，一共是0~N-1行
    // 在i行上放皇后，所有列都尝试
    // 必须要保证跟之前所有的皇后不打架
    // int[] record record[x] = y 之前的第x行的皇后，放在了y列上
    // 返回：不关心i以上发生了什么，i.... 后续有多少合法的方法数
    public static int process1(int i, int[] record, int n) {
        if (i == n) {    // TODO：【重要】-> DP也是，递归也是 =》 这个条件理解，不用非要理解为到达了第n行（总的行只有0-(n-1)），就是理解为 到达结束状态了。是否要依据结束状态进行分类讨论还是直接就能够得到值。
                        //   aka. 意思就是一条完整的递归路径完成了，所以此时返回1 ——》 代表成功计算出一种方法
            return 1;
        }
        int res = 0;
        // i行的皇后，放哪一列呢？j列，
        for (int j = 0; j < n; j++) {
            if (isValid(record, i, j)) {
                record[i] = j;   // 这里其实是隐式的进行了 撤销修改。
                res += process1(i + 1, record, n);
            }
        }
        return res;
    }

    public static boolean isValid(int[] record, int i, int j) {
        // 0..i-1  只看[0,i-1] 即之前防止了棋子的行
        for (int k = 0; k < i; k++) {
            // 主对角线 和 副对角线
            if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) {
                return false;
            }
        }
        return true;
    }

    // 请不要超过32皇后问题 =》 元宝说 如果[32,64]，那就使用long类型
    public static int num2(int n) {
        if (n < 1 || n > 32) {
            return 0;
        }
        // 如果你是13皇后问题，limit 最右13个1，其他都是0
        int limit = n == 32 ? -1 : (1 << n) - 1;     // 原码代表原始值（这是逻辑层面）；在java中 参与代码计算时 是补码形式。 此处32queens，需要的数是（java中补码形式）11111111 =》 通过公式（取反符号位不变） ~a + 1 = 补码（111111） 可以得到 a=1000001，所以a=-1。 也就是说 -1的补码是1111111
        return process2(limit, 0, 0, 0);
    }

    // 7皇后问题
    // limit : 0....0 1 1 1 1 1 1 1
    // 之前皇后的列影响：colLim
    // 之前皇后的左下对角线影响：leftDiaLim
    // 之前皇后的右下对角线影响：rightDiaLim
    public static int process2(int limit, int colLim, int leftDiaLim, int rightDiaLim) {
        if (colLim == limit) {
            return 1;
        }
        // pos中所有是1的位置，是你可以去尝试皇后的位置
        int pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
        int mostRightOne = 0;
        int res = 0;
        while (pos != 0) {
            mostRightOne = pos & (~pos + 1);
            pos = pos - mostRightOne;
            res += process2(limit, colLim | mostRightOne, (leftDiaLim | mostRightOne) << 1,
                    (rightDiaLim | mostRightOne) >>> 1);
        }
        return res;
    }

    public static void main(String[] args) {
        int n = 32;

        long start = System.currentTimeMillis();
        System.out.println(num2(n));
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        System.out.println(num1(n));
        end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + "ms");

    }
}


