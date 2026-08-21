package frequence.Backtracking;

import java.util.*;

/**
 * 51. N 皇后
 *
 * <p>将{@code n}个皇后放置在{@code n x n}棋盘上，并保证任意两个皇后不能位于同一行、
 * 同一列或同一条斜线上。返回所有不同的棋盘放置方案。
 */
public class Q51_NQueens {

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
