package frequence.Graph;

import java.util.*;

/**
 * 994. 腐烂的橘子 - 第一个是我背的，第二个gpt有详细注释
 * <p>
 * TODO： 【难-好题】多源 BFS + 按层遍历 （按层的核心是 while(!queue.isEmpty())里面用 【queue.size()】 去决定每层弹出的元素个数）
 *
 * <p>给定一个二维网格{@code grid}，其中{@code 0}表示空单元格，{@code 1}表示新鲜橘子，
 * {@code 2}表示腐烂橘子。每分钟，腐烂橘子会使其上、下、左、右四个方向相邻的新鲜橘子腐烂。
 * 返回所有新鲜橘子全部腐烂所需的最少分钟数；如果无法全部腐烂，返回{@code -1}。
 *
 * <p><b>问题模型：</b>网格可以看成边权全部为1的无权图。本题不是简单判断两个位置是否连通，
 * 而是要求每个新鲜橘子被任意初始腐烂橘子最早到达的时间。最终答案是所有新鲜橘子的最早
 * 到达时间中的最大值，因此应使用多源BFS求最短传播时间。
 *
 * <p><b>为什么是多源BFS：</b>所有初始腐烂橘子在第0分钟同时开始扩散，必须在初始化时全部
 * 加入同一个队列。不能从每个腐烂橘子分别执行DFS或BFS，否则会产生重复遍历，而且普通DFS
 * 第一次到达某个位置时不能保证使用的是最短路径。
 *
 * <p><b>关键数据结构和状态：</b>
 * <ul>
 *     <li>{@code queue}：保存当前和后续分钟需要继续扩散的腐烂橘子位置；</li>
 *     <li>{@code fresh}：当前仍未腐烂的新鲜橘子数量，用于提前结束和判断是否不可达；</li>
 *     <li>{@code directions}：统一表示上、下、左、右四个相邻方向；</li>
 *     <li>{@code minutes}：已经完整处理的BFS层数，即经过的分钟数。</li>
 * </ul>
 *
 * <p><b>按层遍历：</b>进入每轮外层循环时，先固定{@code size = queue.size()}。这{@code size}
 * 个节点属于同一分钟；处理它们时新加入队列的橘子只能在下一轮继续扩散，因此每处理完一层
 * 才执行一次{@code minutes++}。
 *
 * <p><b>入队即标记：</b>发现新鲜橘子后，必须立即将其从{@code 1}改为{@code 2}，再加入队列。
 * {@code grid}在这里同时承担visited数组的作用。这样同一层的其他腐烂橘子再次检查该位置时，
 * 会发现它已经不是新鲜橘子，从而避免重复入队和重复执行{@code fresh--}。
 *
 * <p><b>推荐计时方式：</b>
 * <pre>
 * while (!queue.isEmpty() && fresh > 0) {
 *     处理当前层;
 *     minutes++;
 * }
 * return fresh == 0 ? minutes : -1;
 * </pre>
 * {@code fresh > 0}可以在最后一个新鲜橘子刚腐烂时立即结束，避免继续处理一个不会产生新腐烂
 * 橘子的末尾层，也就不需要对结果执行{@code minutes - 1}的特殊补偿。
 *
 * <p><b>边界情况：</b>
 * <ul>
 *     <li>初始没有新鲜橘子，答案为0；</li>
 *     <li>存在新鲜橘子但没有任何腐烂源，答案为-1；</li>
 *     <li>BFS结束后{@code fresh > 0}，说明存在被空格或边界隔离的新鲜橘子，答案为-1。</li>
 * </ul>
 *
 * <p><b>复杂度：</b>设网格大小为{@code M x N}。每个位置最多有效入队一次，时间复杂度为
 * {@code O(MN)}；队列最坏保存{@code O(MN)}个位置，空间复杂度为{@code O(MN)}。
 * 当前实现会直接把新鲜橘子改为腐烂状态，因此会修改输入矩阵。
 *
 * <p>关于DFS连通性与多源BFS最短传播的详细区别，参见同目录：
 * {@code DFS扩散与多源BFS 注意事项.md}。
 */
public class Q994_RottingOranges {

    public int orangesRotting(int[][] grid) {
        // TODO: 【可优化】LinkedList作为队列没有错误；ArrayDeque通常具有更低的常数开销。
        Queue<int[]> queue = new LinkedList<>();
        int M = grid.length;
        int N = grid[0].length;
        int fresh = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1) {
                    fresh++;
                } else if (grid[i][j] == 2) {
                    queue.add(new int[]{i, j});
                }
            }
        }

        int[][] directions = new int[][]{
                        {-1, 0},
                        {1, 0},
                        {0, -1},
                        {0, 1}};

        int minutes = 0;
        // TODO: 【计时方式较绕】当前版本只判断queue，会把最后一批新腐烂的橘子再处理一层。
        // 因此成功时需要返回minutes - 1，并额外处理初始队列为空的边界。
        // 更稳定的模板见下面Solution：while (!queue.isEmpty() && fresh > 0)。
        while (!queue.isEmpty()) {
            // 固定当前层节点数。循环中新加入队列的节点属于下一分钟，不能在本层继续扩散。
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pos = queue.poll();

                // 方向数组把四份重复的边界判断和扩散代码统一成一次循环。
                for (int[] dir : directions) {
                    int row = pos[0] + dir[0];
                    int col = pos[1] + dir[1];

                    if (row < 0 || row >= M || col < 0 || col >= N) {
                        continue;
                    }

                    if (grid[row][col] != 1) {
                        continue;
                    }

                    // 入队前立即改为2，相当于标记visited，防止同一位置被其他橘子重复入队。
                    fresh--;
                    grid[row][col] = 2;
                    queue.add(new int[]{row, col});
                }
            }
            // 当前写法统计“处理过的队列层数”。最后一层不会再感染新橘子，所以成功时需要减1。
            minutes++;
        }
        // 推荐版本（搭配while中的fresh > 0）：return fresh == 0 ? minutes : -1;
        // 第一次写错： return fresh == 0 ? minutes - 1 : -1;    反例：[[0]]
        // 第二次写错： return minutes == 0 ? 0 : (fresh == 0 ? minutes - 1 : -1);   反例：[[1]]

        return fresh != 0 ? -1 : (minutes > 0 ? minutes - 1 : 0);
        // 当前计时方式的边界解释：
        /**
         * 1. fresh > 0：存在永远无法腐烂的新鲜橘子，返回-1。
         * 2. fresh == 0且minutes > 0：处理过队列，并额外处理了最后一个无有效扩散的层，
         *    因此返回minutes - 1。
         * 3. fresh == 0且minutes == 0：初始队列为空且没有新鲜橘子，例如[[0]]，返回0。
         */
    }


    /**
     * 推荐版本：多源BFS + 分层遍历。
     *
     * <p>核心不变量：
     * <pre>
     * fresh = 当前尚未腐烂的新鲜橘子数量
     * queue当前层 = 同一分钟已经腐烂、即将向外扩散的所有橘子
     * grid[row][col] == 2 = 该位置已经腐烂或已经加入队列
     * </pre>
     *
     * <p>外层循环增加{@code fresh > 0}，最后一个新鲜橘子腐烂后立即停止，不处理多余层，
     * 因而{@code minutes}可以直接作为答案。
     */
    class Solution {

        public int orangesRotting(int[][] grid) {
            int rows = grid.length;
            int cols = grid[0].length;

            Queue<int[]> queue = new ArrayDeque<>();
            int fresh = 0;

            // 1. 收集所有初始腐烂橘子，并统计新鲜橘子数量
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (grid[row][col] == 2) {
                        // 所有初始腐烂橘子都是BFS起点
                        queue.offer(new int[]{row, col});
                    } else if (grid[row][col] == 1) {
                        fresh++;
                    }
                }
            }

            // 上、下、左、右
            int[][] directions = {
                    {-1, 0},
                    {1, 0},
                    {0, -1},
                    {0, 1}
            };

            int minutes = 0;

            // 2. 按层执行多源BFS
            // fresh > 0可以防止所有橘子已经腐烂后，分钟数继续增加
            while (!queue.isEmpty() && fresh > 0) {
                // 当前队列中的节点，都是同一分钟已经腐烂的橘子
                int size = queue.size();

                // 处理当前这一分钟的全部腐烂橘子
                for (int i = 0; i < size; i++) {
                    int[] cur = queue.poll();
                    int row = cur[0];
                    int col = cur[1];

                    for (int[] direction : directions) {
                        int nextRow = row + direction[0];
                        int nextCol = col + direction[1];

                        // 越界
                        if (nextRow < 0 || nextRow >= rows
                                || nextCol < 0 || nextCol >= cols) {
                            continue;
                        }

                        // 只有新鲜橘子才能被腐烂
                        if (grid[nextRow][nextCol] != 1) {
                            continue;
                        }

                        // 新鲜橘子第一次腐烂
                        // 必须立即修改为2，【防止被其他橘子重复加入队列】
                        grid[nextRow][nextCol] = 2;
                        fresh--;

                        // 新腐烂的橘子下一分钟继续向外扩散
                        queue.offer(new int[]{nextRow, nextCol});
                    }
                }

                // 当前这一层处理完成，代表经过了一分钟
                minutes++;
            }

            // 3. 如果还有新鲜橘子，说明它们无法被腐烂橘子到达
            return fresh == 0 ? minutes : -1;
        }
    }
}
