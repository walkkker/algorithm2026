package frequence.Backtracking;

import java.util.*;

/**
 * 39. 组合总和
 *
 * TODO: 【错误】本题需要恢复现场！！！ 递归树没有画全，不要只有一条从上往下。一个顶点的每一个分支，都对应着一棵树。 因为拿着tmp做路径容器，所以2层回到1层后，再去往2层兄弟节点的时候，此时tmp里面的路径必须统一。（因此必须 在第一个二层返回前 恢复现场）
 *
 * <p>给定一个无重复元素的正整数数组{@code candidates}和一个目标整数{@code target}，
 * 返回所有元素和等于{@code target}的不同组合。数组中的同一个数字可以被无限次重复选取。
 *
 * <p><b>核心【每层选择下一个加入路径的候选数，并保证候选下标不下降】。</b>
 * 递归继续传当前下标，允许当前数字重复选择；下一层禁止回到当前下标之前，从生成阶段直接
 * 消除[2,2,3]、[2,3,2]这种顺序不同但元素相同的重复组合。
 *
 * <p><b>当前实现的递归模型：</b>{@code index}表示正在决定候选数字{@code nums[index]}
 * 使用多少次。当前层依次枚举使用{@code 0..target / nums[index]}次，然后进入下一种候选数字。
 * 因为每种数字只在固定层决定使用次数，所以不会生成元素顺序不同的重复组合。
 *
 * <p><b>共享路径与结果快照：</b>{@code tmp}是整棵递归树共享的可变ArrayList。进入不同兄弟
 * 分支前必须恢复到相同父状态；收集答案时必须执行{@code new ArrayList<>(tmp)}保存当前快照，
 * 不能直接把共享的{@code tmp}引用加入答案。
 *
 * <p><b>可优化点：</b>
 * <ul>
 *     <li>{@code target == 0}时可以立即收集并返回，不需要继续走完剩余候选数字；</li>
 *     <li>可以记录进入方法时的{@code oldSize}，最后恢复到该长度，避免恢复逻辑依赖循环次数；</li>
 *     <li>通用代码可使用{@code count <= target / nums[index]}，避免乘法判断可能产生溢出；</li>
 *     <li>排序后使用{@code start + remaining}模型，可以在候选数字大于剩余目标时直接停止循环。</li>
 * </ul>
 *
 * <p>组合数量本身可能是指数级，因此回溯最坏时间复杂度也是指数级；递归深度最多约为
 * {@code target / min(candidates)}。复制并保存答案还需要与结果总元素数量成正比的空间。
 */
public class Q39_CombinationSum {

    /**
     * 2026-09-12 复盘版本一：每层确定一种候选数使用多少次。
     *
     * <p><b>我的第一轮思路与错误：</b>
     * <ol>
     *     <li>枚举整个candidates，并使用target不断缩小来驱动递归；</li>
     *     <li>错误地认为对candidates去重可以消除重复答案；</li>
     *     <li>后来发现即使输入元素互不相同，仍会同时产生[2,2,3]和[2,3,2]；</li>
     *     <li>进一步改为从左到右处理候选数，每层枚举当前数字使用多少次。</li>
     * </ol>
     *
     * <p>TODO: 【AI修正-重复根因】第一版的问题不是输入存在重复数字，而是递归路径允许候选
     * 下标任意回退，因此同一组合会按不同顺序重复生成。组合去重必须约束递归路径中的选择
     * 顺序，不能只对输入数组做deduplicate。
     *
     * <p>TODO: 【AI修正-枚举范围】“无限次使用”是题目权限，不是当前层真的无限循环。
     * 因为所有候选数均为正数，当前数字实际枚举次数为{@code 0..target / candidate}。
     */
    class SolutionCountReviewed20260912 {

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            List<Integer> path = new ArrayList<>();
            List<List<Integer>> ans = new ArrayList<>();

            // TODO: 【冗余】题目保证candidates元素互不相同，而且arr没有参与后续递归。
            // 即使使用arr，也无法解决[2,2,3]与[2,3,2]这种“选择顺序重复”。
            int[] arr = deduplicate(candidates);
            // process(arr, path, ans, target);

            process(candidates, 0, path, ans, target);
            return ans;
        }

        /**
         * 错误版本：每层重新扫描整个candidates。
         *
         * <p>它允许候选下标从1回到0，因此[2,3,2]和[3,2,2]会与[2,2,3]重复。
         */
        /*
        private void process(
                int[] candidates,
                List<Integer> path,
                List<List<Integer>> ans,
                int target) {

            if (target == 0) {
                ans.add(new ArrayList<>(path));
                return;
            }

            for (int candidate : candidates) {
                if (target - candidate >= 0) {
                    path.add(candidate);
                    process(candidates, path, ans, target - candidate);
                    path.remove(path.size() - 1);
                }
            }
        }
        */

        /**
         * 递归定义：
         * candidates[0, i)中每个数字的使用次数已经确定；当前方法负责枚举
         * candidates[i, n)的所有使用次数，使新增元素之和等于target，并把完整组合加入ans。
         */
        private void process(
                int[] candidates,
                int i,
                List<Integer> path,
                List<List<Integer>> ans,
                int target) {

            if (i == candidates.length) {
                if (target == 0) {
                    ans.add(new ArrayList<>(path));
                }
                return;
            }

            int candidate = candidates[i];

            // TODO: 【错误-遗漏】当前候选数使用0次也是一条必要分支。
            // base case只能判断何时结束，不能代替缺失的状态转移。
            // 例如candidates=[2,3], target=3，只有跳过2，才能得到答案[3]。
            process(candidates, i + 1, path, ans, target);

            int count = 0;
            // TODO: 【通用性】题目约束下乘法安全；通用写法可使用times<=target/candidate。
            for (int times = 1; candidate * times <= target; times++) {
                count = times;
                path.add(candidate);
                process(
                        candidates,
                        i + 1,
                        path,
                        ans,
                        target - candidate * times
                );
            }

            // 本层累计加入了count个candidate，统一恢复到进入方法时的父路径。
            while (count-- > 0) {
                path.remove(path.size() - 1);
            }
        }

        private int[] deduplicate(int[] arr) {
            HashSet<Integer> set = new HashSet<>();
            for (int num : arr) {
                set.add(num);
            }
            int[] ans = new int[set.size()];
            int index = 0;
            for (int num : set) {
                ans[index++] = num;
            }
            return ans;
        }
    }

    /**
     * 2026-09-12 复盘版本二：标准start + remaining回溯。
     *
     * <p>每层选择下一个加入路径的候选数，并保证候选下标不下降。递归传入当前下标
     * {@code index}而不是{@code index + 1}，表示同一个候选数可以继续重复使用。
     */
    class SolutionStartReviewed20260912 {

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            Arrays.sort(candidates);
            List<Integer> path = new ArrayList<>();
            List<List<Integer>> ans = new ArrayList<>();
            process(candidates, 0, target, path, ans);
            return ans;
        }

        /**
         * 递归定义：
         * path中已经选择的候选下标保持非递减；本方法只允许从candidates[start, n)
         * 选择下一个数字，枚举所有新增元素之和为remaining的组合。
         */
        private void process(
                int[] candidates,
                int start,
                int remaining,
                List<Integer> path,
                List<List<Integer>> ans) {

            if (remaining == 0) {
                ans.add(new ArrayList<>(path));
                return;
            }

            for (int index = start; index < candidates.length; index++) {
                if (candidates[index] > remaining) {
                    break;
                }

                path.add(candidates[index]);
                // 继续传index：允许重复选当前数字；禁止回到index之前，避免排列型重复。
                process(
                        candidates,
                        index,
                        remaining - candidates[index],
                        path,
                        ans
                );
                path.remove(path.size() - 1);
            }
        }
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        process(candidates, 0, target, tmp, ans);
        return ans;
    }

    public void process(int[] nums, int index, int target, List<Integer> tmp, List<List<Integer>> ans) {
        // 当前方法定义：决定nums[index]使用多少次；tmp保存前面候选数字已经形成的路径。
        // TODO: 【超级致命错误】下面的写法，在ans里面存储的全部都是 同一个ArrayList对象。 而这个ArrayList因为回溯（恢复现场的原因），在最顶上的process结束后，tmp为空（tmp.size()==0）
        //        这就会导致ans里面所有的元素全部是空List (本身这些ArrayList也都一样，因为是同一个对象)
        //       【举例】int[] nums = [2,3,6,7], target = 7.  预期答案：[[2,2,3],[7]]； 输出答案：[[],[]]     =》 注意这里，因为ans里面存的都是同一个对象。 然后这个对象最终因为恢复现场，变成了空List
        //      【错误代码如下】：
        //        if (index == nums.length) {
        //            if (target == 0) {
        //                ans.add(tmp);
        //            }
        //            return;
        //        }
        if (index == nums.length) {
            if (target == 0) {
                // TODO: 【Java语义修正】Java是引用按值传递，tmp与调用方仍指向同一个
                // 可变ArrayList。问题不是“参数没法更改”，而是ans.add(tmp)会产生引用别名。
                // 必须复制当前路径快照，避免后续回溯修改已经收集的答案。
                ans.add(new ArrayList<>(tmp));
            }
            return;
        }



        // TODO: 【可优化】本题范围不会溢出，但通用写法可改为：
        // for (int i = 0; i <= target / nums[index]; i++)，避免nums[index] * i乘法溢出。
        for (int i = 0; nums[index] * i <= target; i++) {
            if (i == 0) {
                process(nums, index + 1, target, tmp, ans);
            } else {
                tmp.add(nums[index]);
                // TODO: 【错误】必须恢复现场。 下面这种累加才能成立。 不然下一次循环的时候，tmp的末尾元素 都被（后面的process）污染了。
                process(nums, index + 1, target - nums[index] * i, tmp, ans);
            }
        }
        // TODO： 【错误】返回前必须恢复现场。 不然tmp是污染状态。
        // TODO: 【可优化-降低耦合】当前删除数量与循环最大次数绑定。更稳定的写法是进入方法时
        // 记录oldSize，递归结束后一直删除到tmp.size() == oldSize。
        int removed = target / nums[index];
        while (removed-- > 0) {
            tmp.remove(tmp.size() - 1);
        }
    }


    /**
     * 优化版本一：保留“每层决定当前候选数字使用多少次”的递归模型。
     *
     * <p>相比原实现，增加{@code target == 0}提前结束，使用{@code oldSize}恢复路径，
     * 并通过除法计算最大使用次数。
     */
    class SolutionCountOptimized {

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            List<List<Integer>> ans = new ArrayList<>();
            List<Integer> path = new ArrayList<>();
            process(candidates, 0, target, path, ans);
            return ans;
        }

        private void process(
                int[] nums,
                int index,
                int target,
                List<Integer> path,
                List<List<Integer>> ans) {

            // 所有候选数字均为正数，target归零后继续选择只会超过目标，可以立即结束。
            if (target == 0) {
                ans.add(new ArrayList<>(path));
                return;
            }

            if (index == nums.length) {
                return;
            }

            int oldSize = path.size();
            int maxCount = target / nums[index];

            for (int count = 0; count <= maxCount; count++) {
                // count每增加1，只追加一个当前数字，使path累计表示当前数字使用count次。
                if (count > 0) {
                    path.add(nums[index]);
                }

                process(
                        nums,
                        index + 1,
                        target - nums[index] * count,
                        path,
                        ans
                );
            }

            // 恢复到进入当前递归节点时的父路径，不依赖循环具体执行了多少次。
            while (path.size() > oldSize) {
                path.remove(path.size() - 1);
            }
        }
    }


    /**
     * 优化版本二：排序 + start + remaining，是更常用的面试回溯模板。
     *
     * <p>每层直接选择下一个加入路径的数字。递归继续传入{@code i}而不是{@code i + 1}，
     * 表示同一个候选数字可以重复使用；下一层不再考虑{@code i}之前的数字，避免生成顺序不同
     * 的重复组合。
     */
    class SolutionRecommended {

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            // 排序用于单调剪枝；注意这会修改输入数组的顺序。
            Arrays.sort(candidates);

            List<List<Integer>> ans = new ArrayList<>();
            List<Integer> path = new ArrayList<>();
            process(candidates, 0, target, path, ans);
            return ans;
        }

        private void process(
                int[] candidates,
                int start,
                int remaining,
                List<Integer> path,
                List<List<Integer>> ans) {

            if (remaining == 0) {
                ans.add(new ArrayList<>(path));
                return;
            }

            for (int i = start; i < candidates.length; i++) {
                // 数组已经升序，当前数字超过remaining，后续更大的数字也不可能选择。
                if (candidates[i] > remaining) {
                    break;
                }

                path.add(candidates[i]);

                // 继续传i：同一个数字允许重复选择。
                process(
                        candidates,
                        i,
                        remaining - candidates[i],
                        path,
                        ans
                );

                // List是共享路径容器，兄弟分支开始前必须恢复现场。
                path.remove(path.size() - 1);
            }
        }
    }
}
