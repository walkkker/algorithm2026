package frequence.dp;

import java.util.ArrayList;
import java.util.List;

/**
 * 118. 杨辉三角
 *
 * <p>生成杨辉三角的前numRows行。每行首尾为1，中间元素等于上一行相邻两个元素之和。
 *
 * <p><b>DP类型：</b>状态拓扑属于“分层递推构造”，状态值语义属于“结果构造”。题目要求返回
 * 全部行，而不是只求某个最优值或方案数。
 *
 * <p><b>原错误：</b>{@code new ArrayList<>(i)}只设置底层数组初始容量，不会创建i个逻辑元素，
 * 因此不能立即使用{@code set}。改为逐个{@code add}后逻辑正确。合并“预填充1”和“覆盖计算”
 * 时，循环次数也不能继续依赖初始为0的{@code tmp.size()}。
 *
 * <p>本题更准确地说是递推结果构造，而不是求最优值的典型DP。题型归纳参见同目录
 * 《动态规划题型共性总结.md》的“递推构造型”章节。
 *
 * <p><b>一维DP复盘：</b>本题不要为了章节分类而强行定义成普通{@code dp[i]}。状态天然包含“行、列”，
 * 当前行的每一列依赖上一行相邻两列；而且题目要求返回全部行，不能像只求最终值的线性DP那样丢弃
 * 历史结果。原错误属于{@code ArrayList}容量与逻辑长度混淆，不是状态转移错误。与真正的一维模型的
 * 边界说明参见同目录《一维DP核心总结.md》。
 */
public class Q118_PascalsTriangle {

    public static class OriginalSolution {

        public List<List<Integer>> generate(int numRows) {
            List<List<Integer>> ans = new ArrayList<>();
            for (int i = 1; i <= numRows; i++) {
                /*
                 * 1. new ArrayList(n)指定底层数组初始容量，不是指定列表逻辑长度。
                 * 2. 如果后续要使用set，必须先通过add建立对应数量的元素。
                 */
                // TODO: 【原错误】只有下面一句时，tmp.size()仍然为0，不能直接tmp.set(j, value)。
                List<Integer> tmp = new ArrayList<>(i);

                // TODO: 【常数优化】把预填充和计算合并成一次循环是正确的，但循环上界必须使用i。
                // 错误写法：for (int j = 0; j < tmp.size(); j++)，此时tmp.size()等于0。
                for (int j = 0; j < i; j++) {
                    if (j == 0 || j == i - 1) {
                        tmp.add(1);
                    } else {
                        // 当前(row,col)读取上一层(row-1,col-1)与(row-1,col)，属于分层递推构造。
                        tmp.add(ans.get(ans.size() - 1).get(j - 1)
                                + ans.get(ans.size() - 1).get(j));
                    }
                }
                ans.add(tmp);
            }
            return ans;
        }
    }

    public static class RecommendedSolution {

        public List<List<Integer>> generate(int numRows) {
            List<List<Integer>> ans = new ArrayList<>(numRows);
            for (int row = 0; row < numRows; row++) {
                List<Integer> current = new ArrayList<>(row + 1);
                List<Integer> previous = row == 0 ? null : ans.get(row - 1);

                for (int col = 0; col <= row; col++) {
                    if (col == 0 || col == row) {
                        current.add(1);
                    } else {
                        current.add(previous.get(col - 1) + previous.get(col));
                    }
                }
                ans.add(current);
            }
            return ans;
        }
    }
}
