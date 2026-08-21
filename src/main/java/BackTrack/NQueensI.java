package BackTrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verification: 直接做leetcode
 *
 *
 * 要点：检查时间复杂度
 * 1）二维数组扫描 char[][] board 循环检查已放置的每个皇后 O(n)
 * 2）数组标记法 boolean[] col, mainDiag, subDiag 查三个布尔数组 O(1)
 * 这个数组标记法， 对角线的逻辑是 主对角线: r-c的值区分一条对角线 [-(n-1), n-1] (这个为了匹配到数组 要平均每次 +(n-1)让它变成 [0, 2n-2]范围中的一个)；  副对角线: r+c的值对应一条副对角线 [0, 2n - 2]
 * 3) TODO: 采用左神方式，使用 int[] record 数组，index对应行号，record[index]对应列号。 每次放置棋子时，检查[0,i-1] isValid()函数。 检查是否存在同一列，同一主副对角线（abs(r1-r2) == abs(c1-c2)）
 * 因为 对角线都是 45度角。  所以point1和point2在一条对角线上（不管是平行于主对角线还是副对角线），都是一个45°等腰直角三角形=》根据“等角对等边”，它也有两条相等的边=》行宽=列宽。
 * <p>
 * 返回 List<List<String>>， 返回成功的棋盘布局
 * https://leetcode-cn.com/problems/n-queens/
 * 回溯法经典题 暴力枚举 + 方法二： 单个数组记载已选中点 下标为row,值为 col；
 */
public class NQueensI {

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        int[] record = new int[n];
        process(0, record, ans);
       //分别对应 (起始行, 路径记录数组(Queen pos array), 收集答案容器)
        return ans;
    }


    public static void process(int x, int[] record, List<List<String>> ans) {
        if (x == record.length) {
            List<String> board = new ArrayList<>();
            char[] chs = new char[record.length];
            Arrays.fill(chs, '.');
            for (int i = 0; i < record.length; i++) {
                int col  = record[i];
                chs[col] = 'Q';
                board.add(String.valueOf(chs));   // String.valueOf(char[] chs)
                chs[col] = '.';
            }
            ans.add(board);
            // 千万别忘了 void 类型的递归 -》 一定要手动return base case。
            return;
        }

        for (int j = 0 ; j < record.length; j++) {
            if (isValid(x, j, record)) {
                // TODO：回溯一定不能忘记 -> 设置修改（因为回溯 就是基于 之前的递归信息做判断， 所以不能用dp/记忆化搜索（傻缓存是不需要管前面信息的，是一个独立的START状态））
                record[x] = j;    // 这里回溯的修改动作（aka.记录当前信息动作） ，会直接覆盖上一个迭代，所以不需要 【撤销修改】的动作了。 如果是Set, List这种，就必须要 撤销修改了。（不然不能还原现场）
                process(x + 1, record, ans);
            }
        }
    }

    public static boolean isValid(int x, int y, int[] record) {
        for (int i = 0; i < x; i++) {
            if (y == record[i] || (Math.abs(i - x) == Math.abs(record[i] - y))) {
                return false;
            }
        }
        return true;
    }
}
