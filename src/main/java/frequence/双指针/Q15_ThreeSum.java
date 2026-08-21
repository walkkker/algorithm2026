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
