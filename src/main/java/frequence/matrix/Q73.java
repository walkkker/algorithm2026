package frequence.matrix;

/**
 * LeetCode 73：矩阵置零。
 *
 * <p>如果矩阵中某个元素为 0，就将该元素所在的整行和整列全部置为 0。
 *
 * <p><b>最优模型：借用第一行和第一列作为标记数组。</b>
 * <pre>{@code
 * matrix[i][0] == 0：第i行最终需要清零
 * matrix[0][j] == 0：第j列最终需要清零
 * }</pre>
 *
 * <p>第一行和第一列既是原始数据，又被借用为标记区域，因此必须额外保存：
 * <pre>{@code
 * firstRowZero：第一行原来是否包含0
 * firstColumnZero：第一列原来是否包含0
 * }</pre>
 *
 * <p>算法分为四个阶段：
 * <pre>{@code
 * 1. 保存第一行、第一列的原始状态。
 * 2. 扫描内部区域，把清零信息写入第一行、第一列。
 * 3. 根据标记清零内部区域。
 * 4. 标记使用完毕后，最后处理第一行和第一列。
 * }</pre>
 *
 * <p>核心不变量：
 * <pre>{@code
 * 内部区域处理完成前，第一行和第一列始终保存各行、各列是否需要清零的信息。
 * }</pre>
 *
 * <p>TODO: 【核心难点】boolean 解决“第一行、第一列的原始状态丢失”；
 * 处理顺序解决“标记被提前破坏”。两者缺一不可。
 *
 * <p>时间复杂度为 O(MN)，额外空间复杂度为 O(1)。多个顺序执行的 for 循环
 * 不会相乘，所有阶段的总访问次数仍然是常数倍的 MN。
 */
public class Q73 {

    /**
     * 正确版本：使用两个 boolean 保存第一行、第一列的原始状态，
     * 第一行和第一列则用于记录内部区域的行列标记。
     *
     * @param matrix 待原地修改的矩阵
     */
    public void setZeroes(int[][] matrix) {
        boolean firstRowZero = false;
        boolean firstColumnZero = false;

        int M = matrix.length;
        int N = matrix[0].length;

        // 第一列后续会被用作“行标记”，必须先保存它原来是否包含0。
        for (int i = 0; i < M; i++) {
            if (matrix[i][0] == 0) {
                firstColumnZero = true;
            }
        }

        // 第一行后续会被用作“列标记”，必须先保存它原来是否包含0。
        for (int j = 0; j < N; j++) {
            if (matrix[0][j] == 0) {
                firstRowZero = true;
            }
        }

        // 只扫描内部区域：发现0后，分别在该行首位和该列首位写入标记。
        for (int i = 1; i < M; i++) {
            for (int j = 1; j < N; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        // 根据第一列中的行标记，清零对应行。
        for (int i = 1; i < M; i++) {
            if (matrix[i][0] == 0) {
                for (int j = 0; j < N; j++) {
                    matrix[i][j] = 0;
                }
            }
        }

        // 根据第一行中的列标记，清零对应列。
        for (int j = 1; j < N; j++) {
            if (matrix[0][j] == 0) {
                for (int i = 0; i < M; i++) {
                    matrix[i][j] = 0;
                }
            }
        }

        // TODO: 【建议】上面“按行清零”和“按列清零”可以合并为一次内部区域遍历：
        // if (matrix[i][0] == 0 || matrix[0][j] == 0) matrix[i][j] = 0;
        // 当前分开书写同样正确，逻辑更直观，时间复杂度仍然是O(MN)。

        // 行列标记已经使用完毕，现在才能根据原始状态处理第一行。
        if (firstRowZero) {
            for (int j = 0; j < N; j++) {
                matrix[0][j] = 0;
            }
        }

        // 最后根据原始状态处理第一列。
        if (firstColumnZero) {
            for (int i = 0; i < M; i++) {
                matrix[i][0] = 0;
            }
        }
    }




    /**
     * 错误版本：没有保存第一行、第一列的原始状态，并且提前破坏了标记区域。
     *
     * <p>TODO: 【错误原因一】{@code matrix[0][0]} 同时被当作“第一行标记”和
     * “第一列标记”，一个位置无法区分究竟是第一行原来有 0，还是第一列原来有 0。
     *
     * <p>TODO: 【错误原因二】按行清零时包含第一行。如果 {@code matrix[0][0] == 0}，
     * 第一行会被全部清零；后续按列检查第一行时，就会误以为所有列都需要清零。
     * 这属于标记被提前破坏并继续传播。
     *
     * <p>最小反例：
     * <pre>{@code
     * 输入：
     * [1, 0]
     * [1, 1]
     *
     * 正确结果：
     * [0, 0]
     * [1, 0]
     *
     * 错误版本会先令matrix[0][0]=0，再清空第一行，
     * 最后把第一行的所有0当作列标记，错误地清空整个矩阵。
     * }</pre>
     *
     * <p>TODO: 【修改建议】使用两个 boolean 分别保存第一行、第一列原来是否有 0；
     * 只使用内部区域生成标记；先消费标记处理内部区域，最后再处理第一行和第一列。
     */
    class Solution1 {
        public void setZeroes(int[][] matrix) {
            int M = matrix.length;
            int N = matrix[0].length;
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    if (matrix[i][j] == 0) {
                        // TODO: 【错误】这里可能把matrix[0][0]改成0，但没有记录这个0
                        // 来自第一行还是第一列，导致两个含义混在同一个标记中。
                        matrix[i][0] = 0;
                        matrix[0][j] = 0;
                    }
                }
            }

            for (int i = 0; i < M; i++) {
                if (matrix[i][0] == 0) {
                    // TODO: 【错误】i从0开始。一旦matrix[0][0]为0，这里会提前清空
                    // 整个第一行，从而破坏第一行中尚未消费的列标记。
                    for (int j = 0; j < N; j++) {
                        matrix[i][j] = 0;
                    }
                }
            }

            for (int j = 0; j < N; j++) {
                if (matrix[0][j] == 0) {
                    // TODO: 【错误结果】如果第一行已被上一步全部清零，这里会把每一列
                    // 都判断为需要清零，最终可能错误地把整个矩阵置零。
                    for (int i = 0; i < M; i++) {
                        matrix[i][j] = 0;
                    }
                }
            }
        }
    }
}
