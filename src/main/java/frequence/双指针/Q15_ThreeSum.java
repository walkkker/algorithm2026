package frequence.双指针;

import java.util.*;

/**
 *
 * 三数之和:依然很要命
 *
 * TODO：【核心错误】数组去重时，通常保留第一个重复值，因此应该“和前一个比较后跳过”，不能“和后一个比较后跳过”；当前元素可能还需要使用后面的重复值组成答案。
 *
 * 难就难在思路和去重实现。
 *
 * TODO：如果寻求简单实现，那就用Set。  寻求思考缜密，那就加上去重逻辑，优化常数项。 但是一定注意，第一个相同值必须计算，后面的去重跳过。 绝对不能反过来，会漏答案。
 *
 * <p><b>2026-08-27 错误复盘：</b>不能在判断当前sum之前，无条件同时压缩L和R两侧的重复值。
 * 反例{@code [0,0,0,0,1,2,3]}中，固定第一个0以后，初始sum大于0，本轮只能移动R；
 * 错误代码却先把L从第二个0移动到最后一个0。随后R不断左移，等R到达0区间时已经没有两个不同
 * 下标可供L、R使用，于是遗漏合法答案{@code [0,0,0]}。
 *
 * <p><b>修正原则：</b>先缓存当前三数之和，再由大小关系决定本轮允许移动的指针：sum小于0只移动L，
 * sum大于0只移动R；只有sum等于0并收集答案后，L、R才都可以移动并分别跳过重复值。
 * “重复值不会产生新的值组合”不代表可以提前移动没有被当前大小关系选中的另一侧指针，因为三数之和
 * 仍然要求使用三个不同下标，提前跨过重复区间可能破坏后续答案所需的下标数量。
 */
public class Q15_ThreeSum {
    /**
     思路：三数之和 降为 两数之和。
     步骤：
     1. 排序：很重要（1. 去重； 2. L,R知道是需要更大/更小，从而移动L,R）
     2. 固定i，右侧L，R指针定边界
     3. 注意去重
     最终时间复杂度： O(N^2)

     注意点：
     1. 可以不考虑去重，只用HashSet。但是这样时间会很久。 HashSet 对 Arrays.asList() 也是支持去重的。
     - 这是因为：List 的 equals() 和 hashCode() 按【元素内容计算】

     错误点（用Set可以不管，但是写上说明你真懂）：
     1. 数组去重时，通常保留第一个重复值，因此应该“和前一个比较后跳过”，不能“和后一个比较后跳过”；当前元素可能还需要使用后面的重复值组成答案。  但是最左侧相同值计算的结果 一定 包含后面相同值的答案。
     2. 一旦涉及到【数组去重】：你要做很多：（1）固定的i元素要去重 (2)右侧的L,R要去重
     3. 【去重这里有太多的错误了！！！】核心就是，相同值时，第一个必须先计算，让后面的元素检查。  绝对不能让前面的元素检查，这样的话 （1）对于i而言，右侧可选元素不全，会遗漏  （2）对于L,R而言。例子:  [-2,0,1,1,2] -> 遗漏 [-2,1,1]。   结论就一个：一定要先算，让后面的相同值去重(continue或者L++/R--),这样才不漏。 因为只要先算，一定不会漏掉元素；而后算 相当于你直接跳过了一些元素去算和，会错误

     */
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);   // 千万不能忘了排序
            List<List<Integer>> ans = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {

                // TODO: 【错误】这里写反了。 必须跟前一个比较，javadoc有解释。 最左侧的计算包含右边的，繁反之不行
                // if (i <= nums.length - 2 && nums[i] == nums[i + 1]) {
                //     continue;
                // }
                if (i >= 1 && nums[i] == nums[i - 1]) {
                    continue;
                }

                int L = i + 1;
                int R = nums.length - 1;
                while (L < R) {
                    int tmp = nums[L] + nums[R] + nums[i];
                    if (tmp < 0) {
                        L++;
                    } else if (tmp > 0) {
                        R--;
                    } else {
                        ans.add(Arrays.asList(nums[i], nums[L], nums[R]));  // TODO: 【这个语法很重要！！！】 Arrays.asList() 支持入参写初始化元素！！！  可以使用new ArrayList<>(Arrays.asList(1,2,3)) 实现元素初始化
                        // TODO: 【错误点】你收集完L和R。 你要移动呀！！！ 要不然就 死循环了 -》 超出时间限制
                        L++;
                        R--;


                        // TODO: 【错误+遗漏-代码位置错误！】不使用HashSet接的话，L,R也要去重
                        // TODO: 【继续的错误】依然只能 跟前一个比较，才去重。  必须让第一个相同值先计算！！！例子： [-2,0,1,1,2]
                        // while (L < R && nums[L] == nums[L + 1]) {
                        //     L++;
                        // }

                        // while (L < R && nums[R] == nums[R - 1]) {
                        //     R--;
                        // }
                        while (L < R && nums[L] == nums[L - 1]) {
                            L++;
                        }

                        while (L < R && nums[R] == nums[R + 1]) {
                            R--;
                        }
                    }


                }

            }

            return ans;

        }
    }

    /**
     * 2026-08-27 当前实现复盘：错误代码以注释形式保留，下面执行修正后的分支移动与去重逻辑。
     */
    public static class Solution20260827 {

        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            List<List<Integer>> ans = new ArrayList<>();

            for (int i = 0; i < nums.length - 2; i++) {
                // 固定位置i去重：保留相同值第一次出现的位置，后续相同固定值直接跳过。
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                if (nums[i] > 0) {
                    break;
                }

                int left = i + 1;
                int right = nums.length - 1;

                while (left < right) {
                    long sum = (long) nums[i] + nums[left] + nums[right];

                    // TODO: 【2026-08-27 致命错误】不能在判断sum之前，无条件同时压缩左右重复区间。
                    // 错误原因：sum > 0时本轮只能移动right，但下面的错误代码会先移动left。
                    // 在[0,0,0,0,1,2,3]中，这会把left移动到最后一个0，最终遗漏[0,0,0]。
                    // 错误代码：
                    // while (left + 1 < nums.length && nums[left + 1] == nums[left]) {
                    //     left++;
                    // }
                    // while (right - 1 >= 0 && nums[right - 1] == nums[right]) {
                    //     right--;
                    // }

                    if (sum < 0) {
                        // 当前和偏小，只能通过增大左值寻找答案；right仍可能参与后续答案。
                        left++;
                    } else if (sum > 0) {
                        // 当前和偏大，只能通过减小右值寻找答案；left仍可能参与后续答案。
                        right--;
                    } else {
                        ans.add(Arrays.asList(nums[i], nums[left], nums[right]));

                        // 找到答案后，当前左右值对应的三元组已经收集，可以同时移动两个指针。
                        left++;
                        right--;

                        // 跳过刚才已经使用过的左值；left-1是已收集答案中的旧左值。
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++;
                        }

                        // 跳过刚才已经使用过的右值；right+1是已收集答案中的旧右值。
                        while (left < right && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    }

                    // TODO: 【2026-08-27 旧写法错误】不要用两个会重新计算表达式的独立if。
                    // 第一个if修改left后，第二个if判断的已经不是同一个候选三元组。
                    // 错误代码：
                    // if (nums[left] + nums[right] <= -nums[i]) {
                    //     left++;
                    // }
                    // if (nums[left] + nums[right] >= -nums[i]) {
                    //     right--;
                    // }
                    // 修正：使用本轮移动前缓存的sum，并采用if / else if / else互斥分类。
                }
            }
            return ans;
        }
    }

    /**
     * 2026-08-27 我的正确修改版。
     *
     * <p>本题是求三数之和为0，可以作为“排序 + 固定一个数 + 双指针”模板。
     * 这道题的难点不在双指针本身，而在去重时机和指针移动规则。
     *
     * <p><b>本次错误：</b>在尚未根据当前sum判断应该移动哪个指针时，
     * 就同时跳过L、R两侧的重复值。这可能提前丢掉后续答案需要的下标，
     * 反例是{@code [1,2,0,1,0,0,0,0]}排序后应收集{@code [0,0,0]}。
     *
     * <p><b>修正步骤：</b>
     * <ol>
     *     <li>sum小于0：只执行{@code L++}。</li>
     *     <li>sum大于0：只执行{@code R--}。</li>
     *     <li>sum等于0：先收集答案，再跳过L、R当前值的重复区间，最后各移动一步。</li>
     * </ol>
     *
     * <p>真正必须显式去重的是“收集结果之后”，否则相同的左右值会重复收集同一个三元组。
     * 当sum不等于0时不必显式跳过重复值：单指针逐步移动仍然正确，只是可能多执行几次比较。
     */
    public static class MyCorrectedSolution20260827 {

        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            List<List<Integer>> ans = new ArrayList<>();

            for (int i = 0; i < nums.length - 2; i++) {
                // 固定值去重：保留第一个相同值，后续相同值不再重复枚举。
                if (i >= 1 && nums[i] == nums[i - 1]) {
                    continue;
                }

                int left = i + 1;
                int right = nums.length - 1;
                while (left < right) {
                    // 缓存本轮候选三元组的sum：后面的指针移动不得改变本轮分类依据。
                    // 题目当前取值范围下int不会溢出；作为通用模板可改成long sum。
                    int sum = nums[i] + nums[left] + nums[right];

                    if (sum < 0) {
                        left++;
                    } else if (sum > 0) {
                        right--;
                    } else {
                        // TODO: 【2026-08-27 遗忘点】找到答案时必须先收集，不能只移动去重指针。
                        ans.add(Arrays.asList(nums[i], nums[left], nums[right]));

                        // 跳过与当前left、right相同的值，避免重复收集同一个三元组。
                        while (left < right && nums[left + 1] == nums[left]) {
                            left++;
                        }
                        while (left < right && nums[right - 1] == nums[right]) {
                            right--;
                        }

                        left++;
                        right--;
                    }
                }
            }
            return ans;
        }
    }



    // ai 答案
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {

            // 固定位置去重：相同的nums[i]只处理一次
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // 排序后nums[i]已经大于0，后面不可能再得到和为0
            if (nums[i] > 0) {
                break;
            }

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                long sum = (long) nums[i] + nums[left] + nums[right];

                if (sum < 0) {
                    left++;
                } else if (sum > 0) {
                    right--;
                } else {
                    ans.add(Arrays.asList(
                            nums[i],
                            nums[left],
                            nums[right]
                    ));

                    left++;
                    right--;

                    // left去重
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }

                    // right去重
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                }
            }
        }

        return ans;
    }
}
