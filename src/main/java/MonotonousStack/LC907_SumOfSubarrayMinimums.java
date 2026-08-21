 package MonotonousStack;
import java.util.*;

// TODO： 直接看下面的注释： 20260708，精炼有效
// TODO：【核心错误】这道题有个核心点，就是模板使用时，  要保留 == 情况下，参与计算的问题！！  才能覆盖所有的子数组
// TODO： 对于每个元素作为最小值，包含了哪些数组？ 假设区间L,R。每个元素包含的数组为 [L, cur]作为start， [cur, R]作为end => start数量 * end数量

// TODO: 本题非常值得一看，求子数组的最小值之和。 当涉及到每个元素作为最小值时，怎么就求对应的 子数组数量。！！！
//       虽然我老说这道题跟 Code02很像，但是你会发现不一样。02是求所有子数组的最小值 * 区间累加和，【核心：只关心cur最大子数组的长度】，所以==的情况可以被忽略。 因为最后一个重复元素代表的就是 整个区间
//       而本题是求所有子数组的最小值的累加和，需要覆盖到包含cur的每一个子数组（核心与上面不一样）。 而包含cur的每一个子数组，就是 左区间作为start * 右区间作为end。
//              但是这个在计算子数组时，你会发现，如果要求左右两侧较小区间，重复值会有覆盖！！！
//              那一段？每一个重复值的左侧 和 每一个重复值的右侧有覆盖？（就是每个元素都同样为start和end）
//              解决方式很简单：左侧不变，右侧变为重复值1到重复值2的区间，然后最后一个重复值 使用全区间。 这样就完成了
//              =》【超核心】这对应的就是我们重复值无list模板， 只需去掉 if-continue检查，让相同元素弹出的情况也参与计算就可以了。
//              => 【超核心】这样计算的 总子数组数量不会重复！！！ 对应代码(leftLess, cur] * [cur, rightLess)  此时rightLess=相同元素坐标/较小元素坐标
// 子数组的最小值之和
// https://leetcode.cn/problems/sum-of-subarray-minimums/description/
// 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。

 /**
  * TODO： 20260708一遍过
  *  核心：
  *   1. 依然是 arr[i]<=arr[stack.peek()]时弹出，但是此时 <和==的情况下，都参与计算 （不同于柱状图，只考虑<的时候）
  *   2. 续1：此时每个元素 都计算以自己为最小值的所有子数组。  子数组计算公式：【 (l,popI]*[popI,r]代表所有以popI为最小值的子数组个数（同样也是子数组区间）】 （原理，(l,popI]代表起点区间 => [popI,r]代表终点区间 ）
  *
  * TODO：通过1（<=所有情况都算，因为考虑到每一个元素为最小值时）+ 2（子数组起点区间）*(子数组终点区间) 即可包含 所有的子数组情况。无需复杂或者很细节的代码逻辑
  *
  *
  */
 public class LC907_SumOfSubarrayMinimums {
    /**
     * 20260708一遍过
     */
    class Solution20260708 {
        public int sumSubarrayMins(int[] arr) {
            Stack<Integer> stack = new Stack<>();
            long ans = 0;
            for (int i = 0; i < arr.length; i++) {
                while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
                    int popI = stack.pop();
                    int l = stack.isEmpty() ? -1 : stack.peek();
                    int r = i;
                    ans += (long)(popI - l) * (r - popI) * arr[popI];
                }
                stack.push(i);
            }
            while (!stack.isEmpty()) {
                int popI = stack.pop();
                int l = stack.isEmpty() ? -1 : stack.peek();
                int r = arr.length;
                ans += (long)(popI - l) * (r - popI) * arr[popI];
            }
            return (int) (ans % 1000000007);
        }
    }


    // 以后一旦涉及到long类型，preSum, 单调栈求最小之和呀这些，除了long类型外，一定注意把涉及到的所有代码部分：包括其他变量 + 单纯的计算公式 =》 都要变成Long类型！！！
// 【大错误1】这里我犯了一个大错！！！ 本题是要求 以每个元素为为最小值的子数组。那么你求完后一定能够得到 所有子数组的最小值之和。 =》 但是依据模板，==情况不能忽略，因为就是要 包含每个元素作为最小值的情况。 而且就是在这个模板下，不忽略 是可以直接得到正确答案的。 如果你用list模板还不好搞。
// 【大错误2】因为本题 length=10^4 那么对应的子数组数量是 N^2 即 10^8。 而我们在计算时，存在 (当前区间子数组数量) * min 的行为， ！！！所以只把ans变成long类型是不够的！！！ ， 见下方代码
    class Solution {
        public int sumSubarrayMins(int[] arr) {
            int n = arr.length;
            Stack<Integer> stack = new Stack<>();
            long ans = 0;
            for (int i = 0; i < n; i++) {
                // min
                while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                    int cur = stack.pop();
                    // TODO: 【超级错误】本题是要保留 ==情况的，不能排除！！！! 需要把下面这个去掉！！！  必须每个元素作为最小值的情况都考虑到。 尤其是重复元素的情况。而且本题的逻辑，在重复元素下，是可以不遗漏的求出所有子数组情况的。
                    // if (arr[cur] == arr[i]) {
                    //     continue;
                    // }

                    int leftLess = stack.isEmpty() ? -1 : stack.peek();
                    int rightLess = i;
                    // TODO: 这就是我在 Code02_AllTimesMinToMax 里面说的，当前index作为最小值的所有子数组的求法
                    // TODO：【大错误】。Int * int 乘法结果溢出， 光改long ans是不够的！！！
                    // ans += ((cur - leftLess) * (rightLess - cur)) * arr[cur];
                    ans += (long)((cur - leftLess) * (rightLess - cur)) * arr[cur];
                }
                stack.push(i);
            }

            // 把上面复制下来，只需要做两件事情：
            // 1) 把 if(arr[cur] == arr[i]) continue的检查条件删除。因为二阶段，不存在右侧元素。栈内全部都是不重复索引下标元素
            // 2) rightLess 要修改为 n => 因为右侧没有元素
            while (!stack.isEmpty()) {
                int cur = stack.pop();
                int leftLess = stack.isEmpty() ? -1 : stack.peek();
                int rightLess = n;
                // TODO: 这就是我在 Code02_AllTimesMinToMax 里面说的，当前index作为最小值的所有子数组的求法
                // TODO： 这里也改一下，注意，计算公式里面 也必须要 Long。 不然是用 int计算的，直接 相乘完 就溢出了！！！
                //       所以相乘前，直接把一个元素升级为long类型，剩下元素计算时会自动升级。  计算结果就不会溢出了
                ans += (long)((cur - leftLess) * (rightLess - cur)) * arr[cur];
            }
            return (int)(ans % ((long) Math.pow(10,9) + 7));
        }
    }

}

