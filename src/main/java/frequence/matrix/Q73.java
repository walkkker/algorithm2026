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
     * 2026-08-30 我的修正版。
     *
     * <p><b>核心错误：</b>置零阶段破坏了标记数组。只管up和left就可以，
     * 所以对应两个boolean变量。
     *
     * <p>你已经避开了上下左右，单独依靠boolean赋值。那么置0阶段，应该从
     * {@code [1, m - 1]}和{@code [1, n - 1]}检查，然后将对应行、列置0，
     * 避开第0行和第0列。否则，例如先行后列时，一旦{@code matrix[0][0] == 0}，
     * 第0行会被全部提前置0，导致接下来按列置0的阶段把整个矩阵全部置0。
     *
     * <p><b>必须强调的不变量：</b>
     * <pre>{@code
     * matrix[i][0]：第i行是否需要置零的标记
     * matrix[0][j]：第j列是否需要置零的标记
     * }</pre>
     * 在所有依赖这些标记的逻辑执行完成前，第0行和第0列不能被提前覆盖。
     * boolean只解决“第0行、第0列原始状态丢失”的问题；正确的处理顺序才解决
     * “标记被提前覆盖”的问题，这两个条件缺一不可。
     *
     * <p><b>关于列循环能否从j=0开始：</b>按照本实现“先消费全部行标记，再消费列标记”
     * 的固定顺序，列阶段从j=0开始也不会再破坏尚未消费的行标记，因此结果仍然正确。
     * 但是推荐仍从j=1开始：第0行、第0列统一留到最后处理，循环不变量对称、证明简单，
     * 以后即使调整代码顺序也不容易引入标记污染。
     */
    public static class Solution20260830 {

        public void setZeroes(int[][] matrix) {
            int m = matrix.length;
            int n = matrix[0].length;
            boolean up = false;
            boolean left = false;
            for (int j = 0; j < n; j++) {
                if (matrix[0][j] == 0) {
                    up = true;
                }
            }

            for (int i = 0; i < m; i++) {
                if (matrix[i][0] == 0) {
                    left = true;
                }
            }

            for (int i = 1; i < m; i++) {
                for (int j = 1; j < n; j++) {
                    if (matrix[i][j] == 0) {
                        matrix[i][0] = 0;
                        matrix[0][j] = 0;
                    }
                }
            }

            // 置0阶段
            // TODO: 【错误】置0阶段，要避开0行0列。不然如果[0][0]为0，会提前把0行全部置0，
            // 然后到了依据0行按列置0的时候，就完蛋了！！！ ===》0行0列一定不能提前覆盖。
            // 错误行：for (int i = 0; i < m; i++) {
            for (int i = 1; i < m; i++) {
                if (matrix[i][0] == 0) {
                    for (int j = 0; j < n; j++) {
                        matrix[i][j] = 0;
                    }
                }
            }

            // for (int j = 0; j < n; j++) {
            // TODO：上面这行在当前执行顺序下其实也正确，因为行标记已经全部消费完毕，
            // 后面不会再根据第0列中的行标记执行置零操作，不怕第0列此时被覆盖。
            // 但推荐仍从1开始，使第0行和第0列始终延迟到最后处理，逻辑最稳定。
            for (int j = 1; j < n; j++) {
                if (matrix[0][j] == 0) {
                    for (int i = 0; i < m; i++) {
                        matrix[i][j] = 0;
                    }
                }
            }

            // 行列标记已经全部消费完毕，现在才能根据原始状态处理第0行。
            for (int j = 0; j < n; j++) {
                matrix[0][j] = up ? 0 : matrix[0][j];
            }

            // 最后处理第0列。matrix[0][0]即使再次赋值，也只会得到题意要求的最终状态。
            for (int i = 0; i < m; i++) {
                matrix[i][0] = left ? 0 : matrix[i][0];
            }
        }
    }

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
