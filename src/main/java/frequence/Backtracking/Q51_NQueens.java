package frequence.Backtracking;

import java.util.*;

/**
 * 51. N 皇后
 *
 * <p>将{@code n}个皇后放置在{@code n x n}棋盘上，并保证任意两个皇后不能位于同一行、
 * 同一列或同一条斜线上。返回所有不同的棋盘放置方案。
 */
public class Q51_NQueens {

    /**
     * 2026-09-09 复盘版本：逐行放置皇后。
     *
     * <p>{@code board[row] = column}表示第{@code row}行的皇后放在第{@code column}列。
     * 递归层{@code i}就是当前准备放置皇后的行；当前层枚举这一行的所有列{@code j}。
     *
     * <p>这种一维棋盘表示天然保证“每行只有一个皇后”，因此合法性检查只需要排除同列和
     * 两条对角线。两个位置{@code (r1,c1)}和{@code (r2,c2)}位于同一条对角线，当且仅当
     * {@code abs(r1-r2) == abs(c1-c2)}。
     */
    class SolutionReviewed20260909 {

        public List<List<String>> solveNQueens(int n) {
            int[] board = new int[n];
            List<List<String>> ans = new ArrayList<>();
            process(0, board, ans);

            // TODO: 【遗漏】对于void process的回溯类型，千万不要忘了返回承接变量。
            return ans;
        }

        private void process(int i, int[] board, List<List<String>> ans) {
            if (i == board.length) {
                List<String> list = new ArrayList<>();
                for (int k = 0; k < board.length; k++) {
                    char[] tmp = new char[board.length];
                    Arrays.fill(tmp, '.');
                    tmp[board[k]] = 'Q';

                    // TODO: 【错误】list的元素类型是String，不能直接执行list.add(tmp)。
                    // 正确语句：list.add(new String(tmp));
                    list.add(new String(tmp));
                }
                ans.add(list);
                return;
            }

            // 当前递归层固定第i行，枚举皇后可能放置的所有列。
            for (int j = 0; j < board.length; j++) {
                if (isEqual(board, i, j)) {
                    board[i] = j;
                    process(i + 1, board, ans);

                    // TODO: 【AI补充-无需恢复现场】这里不需要把board[i]恢复成旧值：
                    // 1. 下一个兄弟分支会直接用新的j覆盖board[i]；
                    // 2. isEqual只读取已经确定的board[0..i-1]；
                    // 3. 到达base case时，board[0..n-1]已经全部由当前路径写入。
                }
            }
        }

        /**
         * 判断把皇后放在(r,c)是否合法。
         *
         * <p>方法名{@code isEqual}沿用原代码；从语义上看，{@code isValid}或
         * {@code canPlace}会更准确。
         */
        private boolean isEqual(int[] board, int r, int c) {
            for (int i = 0; i < r; i++) {
                if (board[i] == c
                        || Math.abs(i - r) == Math.abs(board[i] - c)) {
                    return false;
                }
            }
            return true;
        }
    }

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        int[] selected = new int[n];
        process(n, 0, selected, ans);
        return ans;
    }

    private void process(int n, int index, int[] selected, List<List<String>> ans) {
        if (index == n) {
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                char[] chs = new char[n];
                Arrays.fill(chs, '.');
                chs[selected[i]] = 'Q';
                tmp.add(new String(chs));
            }
            ans.add(tmp);
            return;
        }
        for (int j = 0; j < n; j++) {
            if (isRight(index, j, selected)) {
                selected[index] = j;
                process(n, index + 1, selected, ans);
            }
        }
    }

    private boolean isRight(int r, int c, int[] selected) {
        for (int i = 0; i < r; i++) {
            int row = i;
            int col = selected[row];
            if (col == c || Math.abs(r - row) == Math.abs(c - col)) {
                return false;
            }
        }
        return true;
    }
}
