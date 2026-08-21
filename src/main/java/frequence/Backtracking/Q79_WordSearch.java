package frequence.Backtracking;

/**
 * 79. 单词搜索
 *
 * TODO：二维矩阵 递归走路，如何不走回头路？【原地标记+恢复现场】（其实这个跟 感染模型很像， 要修改一下二维矩阵点的，只不过多了恢复现场）
 * ```
 * 1. 进入一个匹配的单元格后：
 * 2. 临时将当前单元格标记为已使用。 -> tmp = board[r][c]; board[r][c] = 0;
 * 3. 向上下左右递归。
 * 4. 递归结束后恢复原字符。 -> board[r][c] = tmp
 * ```
 * 必须恢复的原因是：
 *  1. 当前路径不能重复使用该位置。
 *  2. 其他兄弟路径和其他搜索起点仍然可以使用该位置。
 * 这正是标准回溯。
 *
 *
 *
 * <p>给定一个字符网格{@code board}和一个字符串{@code word}，判断该单词是否存在于网格中。
 * 单词必须按照水平方向或竖直方向相邻的单元格依次组成，同一个单元格在一条搜索路径中不能
 * 被重复使用。
 */
public class Q79_WordSearch {
    /**
     * 正确版本+适当剪枝
     * TODO：重点看 不走回头路 + 剪枝逻辑
     *
     * 后面还有两个重要版本：1.错误版本； 2. 初步正确版本，没有剪枝
     */
    class Solution {

        private final int[][] directions = new int[][] {
                { -1, 0 },
                { 1, 0 },
                { 0, -1 },
                { 0, 1 }
        };

        public boolean exist(char[][] board, String word) {
            char[] chs = word.toCharArray();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == chs[0]) {
                        boolean res = process(board, chs, 0, i, j);
                        if (res) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean process(char[][] board, char[] chs, int index, int r, int c) {
            if (index == chs.length) {
                return true;
            }

            if (r < 0 || r == board.length || c < 0 || c == board[0].length) {
                return false;
            }

            char cur = chs[index];
            if (board[r][c] != cur) {
                return false;
            } else {
                boolean res = false;
                board[r][c] = 0;  // TODO：必须抹掉board[r][c]，避免走回头路
                for (int[] direction : directions) {
                    res = process(board, chs, index + 1, r + direction[0], c + direction[1]);
                    if (res) {     // TODO: 【重点-小错误】提前发现true,剪枝。 避免多余递归
                        break;
                    }
                }
                board[r][c] = cur; // TODO： 必须恢复现场
                return res;
            }
        }
    }


    /**
     * 错误版本，会走回头路！
     * TODO：【错误】下面这个写法有问题，因为在board上，每到一个点，就会往 【上下左右】递归，所以会走回头路。
     * TODO：【错误例子】官网， word="ABCB" 当，ABC走完之后，C回头可以再到B。因此错误输出true
     * @param board
     * @param word
     * @return
     */
    public boolean exist(char[][] board, String word) {
        char[] chs = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == chs[0]) {
                    boolean res = process(board, chs, 0, i, j);
                    if (res) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean process(char[][] board, char[] chs, int index, int r, int c) {
        if (index == chs.length) {
            return true;
        }

        if (r < 0 || r == board.length || c < 0 || c == board[0].length) {
            return false;
        }


        char cur = chs[index];
        if (board[r][c] != cur) {
            return false;
        } else {
            int[][] directions = new int[][]{
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };
            boolean res = false;
            for (int[] direction : directions) {
                res = res || process(board, chs, index + 1, r + direction[0], c + direction[1]);
            }
            return res;
        }
    }


    /**
     * 正确版本： private static final int[][] 第一行应该是这个
     *
     * 解决了走回头路的问题。
     *
     * 但是还有些小问题，主要是剪枝问题：
     * （1）在 四个方向process里面，可以剪枝，一旦发现有一个true，就可以 恢复现场+返回true了。
     */
    class CorrectSolution {

        private final int[][] directions = new int[][] {
                { -1, 0 },
                { 1, 0 },
                { 0, -1 },
                { 0, 1 }
        };

        public boolean exist(char[][] board, String word) {
            char[] chs = word.toCharArray();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == chs[0]) {
                        boolean res = process(board, chs, 0, i, j);
                        if (res) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean process(char[][] board, char[] chs, int index, int r, int c) {
            if (index == chs.length) {
                return true;
            }

            if (r < 0 || r == board.length || c < 0 || c == board[0].length) {
                return false;
            }

            char cur = chs[index];
            if (board[r][c] != cur) {
                return false;
            } else {
                boolean res = false;
                board[r][c] = 0;
                for (int[] direction : directions) {
                    res = res || process(board, chs, index + 1, r + direction[0], c + direction[1]);
                }
                board[r][c] = cur;
                return res;
            }
        }
    }
}
