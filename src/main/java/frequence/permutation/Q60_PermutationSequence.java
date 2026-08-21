package frequence.permutation;

import java.util.ArrayList;
import java.util.List;

/**
 * 60. 排列序列
 *
 * <p>DONE: 【已独立完成】当前实现已通过排列家族对数器。状态见
 * {@code frequence/待独立完成题目清单.md}。
 *
 * <p>集合{@code [1,2,...,n]}共有{@code n!}个排列。将全部排列按字典序排列，
 * 返回第{@code k}个排列。题目保证{@code 1 <= n <= 9}且{@code 1 <= k <= n!}。
 *
 * <p><b>字典序排列家族定位：</b>Q31是“当前排列 -> 直接后继”；Q60是
 * “排名 -> 排列”，也叫排列的反排名（unranking）。如果从第1个排列连续执行
 * {@code k - 1}次Q31，也能得到第k个排列，但时间复杂度会与k成正比。
 * Q60利用阶乘分块直接跳过整块排列。
 *
 * <p><b>阶乘分块：</b>当剩余{@code remaining}个数字时，固定当前第一个数字后，
 * 后面共有{@code (remaining - 1)!}种排列。因此，字典序排列可以按首元素分成
 * 若干个大小完全相同的块。
 *
 * <p>例如{@code n=4, k=9}，先把k转为0-based排名8：
 * <pre>
 * 首位每块3! = 6个：8 / 6 = 1，选剩余[1,2,3,4]的下标1 -> 2，块内排名2。
 * 第二位每块2! = 2个：2 / 2 = 1，选剩余[1,3,4]的下标1 -> 3，块内排名0。
 * 后续依次选1、4，答案为2314。
 * </pre>
 *
 * <p><b>为什么要执行{@code k--}：</b>题目的k从1开始，但“商表示第几块，
 * 余数表示块内排名”的计算需要使用0-based排名。如果不先减1，每个阶乘块的
 * 最后一项都会被错误分到下一块。
 *
 * <p>当前实现使用{@link ArrayList#remove(int)}删除已选数字，单次删除为
 * {@code O(N)}，总时间复杂度{@code O(N^2)}，额外空间{@code O(N)}。
 * 在本题{@code n <= 9}的范围内这是面试最合适的实现；没有必要为了将删除优化到
 * {@code O(log N)}而引入Fenwick树。
 *
 * <p>这个阶乘编码直接适用于互异元素。如果元素可重复，每个首元素对应的块大小不再是
 * 简单的阶乘，而需要用多重集排列数公式重新计算。
 */
public class Q60_PermutationSequence {

    /**
     对于n个数字，核心思想：分块， 每块数量是 (n-1)!

     那么对于k （1-based），找出它对应哪个块的第哪个？
     - 要转化成 0-based。 k--
     - 转化成0-based之后，算法类似于 二维矩阵映射一维数字。
     - 块也是0-based， k也是0-based
     - k/(n-1)! 就是第几个块，  k%(n-1)! 就是 第几个块的第几个位置 （全是0-based，算法跟 二维矩阵映射一维数字 的计算逻辑是一样的）

     - 特别注意，确定头元素后，剩余的(n-1)进行重复流程，但是要保证n-1的元素还是从小到大排列。因为我们要的是(n-1)元素里面的第k%(n-1)!个 0-based。 => 所以这里要使用 list.remove(index) O(N)的方法。
     - 续：list.remove() 因为要保证剩余元素的有序性

     */
    class MySolution {
        public String getPermutation(int n, int k) {
            StringBuilder sb = new StringBuilder();
            List<Integer>  list = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                list.add(i);
            }
            k--;
            for (int i = n; i >= 2; i--) {
                // TODO: 【错误】核心错误只有一个：每一轮剩余元素数量在减少，但你始终计算的是固定的 (n - 1)!。
                // int tmp = cal(n - 1);
                int tmp = cal(i - 1);   // TODO：【核心重点】下面这三行是本题的 核心公式。
                int p1 = k / tmp;
                int p2 = k % tmp;
                sb.append(list.get(p1));   // 把块对应的首元素 加到sb里面
                list.remove(p1);           // 删除块对应的首元素。  list剩余元素保持有序，可进一步迭代。
                // list.remove(index)会返回被删除的元素，因此可以合并 get + remove
                // 这个是可以的：sb.append(list.remove(p1));
                k = p2;
            }
            sb.append(list.get(0));
            return sb.toString();
        }

        private int cal(int n) {
            int ans = 1;

            // TODO: 【错误】不要想当然写for循环：for (int i = n; i >= 0; i--) {
            //  这是阶乘， n*(n-1)*...*1
            for (int i = n; i >= 1; i--) {
                ans *= i;
            }
            return ans;
        }

    }


    public static class Solution {

        public String getPermutation(int n, int k) {
            int[] factorial = new int[n + 1];
            factorial[0] = 1;
            List<Integer> available = new ArrayList<Integer>(n);
            for (int value = 1; value <= n; value++) {
                factorial[value] = factorial[value - 1] * value;
                available.add(value);
            }

            int rank = k - 1;
            StringBuilder ans = new StringBuilder(n);
            for (int remaining = n; remaining >= 1; remaining--) {
                int blockSize = factorial[remaining - 1];
                int selectedIndex = rank / blockSize;
                rank %= blockSize;
                ans.append(available.remove(selectedIndex));
            }
            return ans.toString();
        }
    }
}
