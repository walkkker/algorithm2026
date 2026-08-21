package frequence.matrix;

import java.util.*;

/**
 * LeetCode 54：螺旋矩阵。
 *
 * <p><b>核心模型：分圈处理。</b>
 * 使用左上角 {@code (r1,c1)} 和右下角 {@code (r2,c2)}
 * 描述当前尚未访问的矩形区域，每次只收集该区域的最外圈，然后四条边界同时向内收缩：
 * <pre>{@code
 * r1++;
 * c1++;
 * r2--;
 * c2--;
 * }</pre>
 *
 * <p>外层循环不变量：
 * <pre>{@code
 * 每次调用circle之前，[r1...r2][c1...c2]恰好是尚未访问的矩形区域；
 * circle会且只会收集这个矩形的最外圈。
 * }</pre>
 *
 * <p>矩阵不保证是正方形。收缩过程中可能先耗尽行，也可能先耗尽列，因此合法圈必须同时满足：
 * <pre>{@code
 * r1 <= r2 && c1 <= c2
 * }</pre>
 * 只检查行边界或只检查列边界，都会在长方形矩阵中产生不存在的圈。
 *
 * <p>单圈需要区分三种退化情况和一种普通情况：
 * <pre>{@code
 * 1. 只剩一个点：r1 == r2 && c1 == c2
 * 2. 只剩一行：r1 == r2
 * 3. 只剩一列：c1 == c2
 * 4. 普通矩形圈：r1 < r2 && c1 < c2
 * }</pre>
 * 退化情况处理完成后必须立即 return，否则还会继续遍历四条边，导致重复收集。
 *
 * <p>普通圈按“上、右、下、左”顺时针收集。四条边统一采用
 * “包含本边起点、排除本边终点”的规则：
 * <pre>{@code
 * 上边：包含左上角，排除右上角
 * 右边：包含右上角，排除右下角
 * 下边：包含右下角，排除左下角
 * 左边：包含左下角，排除左上角
 * }</pre>
 * 这样四个角各归属于一条边，每个元素只会被访问一次。
 *
 * <p>时间复杂度为 O(MN)，因为每个矩阵元素恰好加入答案一次；
 * 除返回结果外，额外空间复杂度为 O(1)。
 */
public class Q54 {

    // TODO：【错误点】题目不保证是个正方形（左神教的那个是正方形），本题需要更强的拓展。 可能是矩形，错误case：[[6,9,7]] => 输出是6,9,7,9
    // TODO: 【必看：核心错误点】详细说来，就是有可能会退化成 【单行/单列/单点】，此时左上角右下角的分圈结构不适用。 需要分类讨论，单独遍历。

    class Solution {
        /**
         * 外层负责维护尚未访问矩形的边界，每轮调用circle收集一圈。
         *
         * @param matrix 待遍历矩阵
         * @return 顺时针螺旋顺序
         */
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> ans = new ArrayList<>();
            int M = matrix.length;
            int N = matrix[0].length;

            // (r1,c1)是未访问区域左上角，(r2,c2)是未访问区域右下角。
            int r1 = 0;
            int c1 = 0;
            int r2 = M - 1;
            int c2 = N - 1;

            // while (r1 <= r2) {   // TODO: 【必看：超级是一个错误点】他有可能是一个竖着的矩形，比如6*4。你会发现，r1<r2但是c1>c2了。 所以两个条件必须同时满足。
            while (r1 <= r2 && c1 <= c2) {
                // 当前边界合法时，完整收集尚未访问区域的最外圈。
                circle(r1, c1, r2, c2, matrix, ans);

                // 最外圈已经处理完毕，四条边界同时向内收缩一格。
                r1++;
                c1++;
                r2--;
                c2--;
            }
            return ans;
        }

        /**
         * 收集边界为 {@code [r1...r2][c1...c2]} 的单圈。
         * TODO： 【核心错误的来源】一定要各种分类讨论，多层，单行，单列，单点。 还有base case一定要return！！！ 不然会继续执行其他分支，出错！！！
         * <p>TODO: 【建议】circle可以命名为collectRing，并声明为private，
         * 更明确地表达“只负责收集一圈”的职责；当前命名不影响正确性。
         */
        public void circle(int r1, int c1, int r2, int c2, int[][] matrix, List<Integer> list) {
            // 退化情况一：最后只剩一个中心点。
            // TODO: 【说明】该分支可以被“只剩一行”覆盖，但单独保留更容易识别中心点模型。
            if (r1 == r2 && c1 == c2) {
                list.add(matrix[r1][c1]);
                return;
            }

            // 退化情况二：只剩一行，只能从左到右收集一次。
            if (r1 == r2) {
                for (int j = c1; j <= c2; j++) {
                    list.add(matrix[r1][j]);
                }
                return;  // TODO: 【错误-遗漏】【base case一定要return！！！】
            }

            // 退化情况三：只剩一列，只能从上到下收集一次。
            if (c1 == c2) {
                for (int i = r1; i <= r2; i++) {
                    list.add(matrix[i][c1]);
                }
                // TODO: 【错误】【base case一定要return！！！】
                return;
            }


            // TODO: 下面的四边遍历只适用于普通矩形圈：r1 < r2 && c1 < c2。

            // 上边：包含左上角，不包含右上角。
            for (int j = c1; j < c2; j++) {
                list.add(matrix[r1][j]);
            }

            // 右边：包含右上角，不包含右下角。
            for (int i = r1; i < r2; i++) {
                list.add(matrix[i][c2]);
            }

            // 下边：包含右下角，不包含左下角。
            for (int j = c2; j > c1; j--) {
                list.add(matrix[r2][j]);
            }

            // 左边：包含左下角，不包含左上角。
            // TODO: 【错误】倒着的for循环一定要专心啊！！！
            // for (int i = r2; i < r1; i--) {
            for (int i = r2; i > r1; i--) {
                list.add(matrix[i][c1]);
            }
        }
    }
}
