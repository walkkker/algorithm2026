package frequence.dp;

/**
 * 673. 最长递增子序列的个数
 *
 * <p><b>DONE: 【已独立完成】</b>当前实现已通过计数DP对数器。
 * 状态总表参见{@code frequence/待独立完成题目清单.md}。
 *
 * <p>给定整数数组，返回最长严格递增子序列的数量。不同子序列按照选择的数组下标区分。
 *
 * <p><b>DP类型：</b>状态拓扑属于“一维位置结尾型DP”，状态值语义属于“最优值+最优方案计数”。
 * 本题的独立状态参数仍然只有位置i，只是每个状态同时保存两个属性：
 *
 * <pre>
 * length[i] = 必须以nums[i]结尾的最长递增子序列长度
 * count[i]  = 必须以nums[i]结尾，并且长度为length[i]的子序列数量
 * </pre>
 *
 * <p>发现更长候选时，旧方案已经不是最优方案，因此同时覆盖长度和计数；发现同样长的新候选时，
 * 保持最优长度并累加计数；更短候选直接忽略。最后需要汇总所有{@code length[i]}等于全局最长长度
 * 的{@code count[i]}，因为全局LIS不一定以最后一个数组元素结尾。
 *
 * <p>详细分类参见同目录《动态规划题型共性总结.md》《一维位置结尾型DP.md》，以及专题
 * {@code frequence/substringandsubsequence/子串与子序列区别.md}。
 */
public class Q673_NumberOfLongestIncreasingSubsequence {

    /**
     * 1-dimensiond dp  + 多状态 （因为多状态只有两个状态，所以拆分成两个 一维数组dp）
     * <p>
     * 每次一维数组 多状态，拆成多个一维数组时。 【你要知道，他俩是相伴相生的。 因为本质上是 二维dp dp[i][状态]】
     * - 只是说可能 状态求值的时候 会有先后。 => 比如本题，在length[i]的确认过程中， 会计算 count[i]的值。 => 换句话说，count[i] 依赖于 length[i]的先行计算
     *
     *
     * => TODO： 【错误】这是第一个版本，错误版本。 思路没错，代码写错了。 错在哪里？变量串用了！！！ 一定要注意，不要变量用混了！！！
     *           你自己找下错误！ 有两处。
     *             1. 错误一：使用了 nums[j]，应该使用 length[j]
     *             2. 错误二：遗漏 count[i] = 1 【这是超级大错误，必须初始化！！！】 必须在每个i循环中，初始化length[i]&count[i]。 不然如果if一点不进的话，
     *                  这两个值就会变成0.！！！
     *
     *    TODO: 【错误汇总】你的整体 DP 框架和最后的汇总逻辑是正确的，错误集中在“读取错状态”和“方案数初始化遗漏”。
     *
     * @param nums
     * @return
     */
    public int myWrongFindNumberOfLIS(int[] nums) {
        int len = nums.length;
        int[] length = new int[len];
        int[] count = new int[len];

        length[0] = 1;
        count[0] = 1;
        int maxLength = 1;

        for (int i = 1; i < len; i++) {
            length[i] = 1;   // TODO: 【严重错误】严重遗漏 length[i]/count[i]=1的初始化。 语义上应该初始化，代码逻辑上必须初始化，不然 两个就是0了，超级错误！！！
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {    // 严格递增
                    if (nums[j] + 1 > length[i]) {
                        length[i] = nums[j] + 1;
                        count[i] = count[j];
                    } else if (nums[j] + 1 == length[i]) {
                        count[i] += count[j];
                    } else {
                        continue;
                    }
                }
            }
            maxLength = Math.max(maxLength, length[i]);
        }

        int ans = 0;
        for (int i = 0; i < len; i++) {
            if (length[i] == maxLength) {
                ans += count[i];
            }
        }

        return ans;
    }

    /**
     * 正确版本
     */
    public int myCorrectFindNumberOfLIS(int[] nums) {
        int len = nums.length;
        int[] length = new int[len];
        int[] count = new int[len];

        length[0] = 1;
        count[0] = 1;
        int maxLength = 1;

        for (int i = 1; i < len; i++) {
            length[i] = 1;   // TODO: 【错误】必须初始化
            count[i] = 1;    // TODO: 【错误】必须初始化
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {    // 严格递增
                    if (length[j] + 1 > length[i]) {   // TODO: 【错误】length[] count[] 都是dp呀！！ 尤其是length[i] 依赖于 更小规模的dp length[j]
                        length[i] = length[j] + 1;
                        count[i] = count[j];
                    } else if (length[j] + 1 == length[i]) {
                        count[i] += count[j];
                    } else {
                        continue;     // 【优化】这一段也可以直接删除
                    }
                }
            }
            maxLength = Math.max(maxLength, length[i]);
        }

        int ans = 0;
        for (int i = 0; i < len; i++) {
            if (length[i] == maxLength) {
                ans += count[i];
            }
        }

        return ans;
    }


    /**
     * 时间复杂度O(N^2)，额外空间O(N)。
     */
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        int[] length = new int[n];
        int[] count = new int[n];
        int maxLength = 0;

        for (int i = 0; i < n; i++) {
            // 单独选择nums[i]：长度为1，方案数为1。
            length[i] = 1;
            count[i] = 1;

            for (int j = 0; j < i; j++) {
                if (nums[j] >= nums[i]) {
                    continue;
                }

                int candidateLength = length[j] + 1;
                if (candidateLength > length[i]) {
                    // 发现更优值：旧方案失去最优资格，长度和计数都必须覆盖。
                    length[i] = candidateLength;
                    count[i] = count[j];
                } else if (candidateLength == length[i]) {
                    // 发现达到同一最优值的新来源：累加对应方案数量。
                    count[i] += count[j];
                }
            }
            maxLength = Math.max(maxLength, length[i]);
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (length[i] == maxLength) {
                ans += count[i];
            }
        }
        return ans;
    }
}
