package frequence.Heap;

import java.util.*;

/**
 * 347. 前K个高频元素
 *
 * <p>给定一个整数数组{@code nums}和一个整数{@code k}，返回其中出现频率最高的{@code k}个
 * 元素。答案可以按任意顺序返回。
 *
 * <p>要求算法的时间复杂度优于{@code O(N log N)}。
 */
public class Q347_TopKFrequentElements {

    /**
     * 2026-09-19复盘：HashMap统计频次 + 分桶法。当前实现正确。
     *
     * <p><b>建模思想：</b>桶下标表示频次，桶内列表保存具有该频次的不同数字；
     * 从高频向低频收集，达到k个立即返回。数字值域可能很大，但频次一定在[1,N]，
     * 因此可以用频次直接索引桶，无需比较排序；桶内的数字也不需要排序。
     *
     * <p><b>复杂度与最优性：</b>设N为输入长度、U为不同数字数。哈希表操作期望O(1)时，
     * 统计期望O(N)，分桶O(U)，初始化和扫描桶O(N)，桶内合计最多U个元素。
     * 总时间期望O(N)，达到读取输入的线性时间下界；额外空间O(N)。这是时间渐进最优解之一，
     * 不代表额外空间一定最少。前提是题目保证1 <= k <= U。
     *
     * <p>TODO: 【泛型数组】new List<>[N]是编译错误；new List[N]可编译但会产生
     * unchecked conversion警告。显式转换配合局部SuppressWarnings仍是未经检查的转换；
     * 若需完全避免它，可使用List<List<Integer>>。
     *
     * <p>TODO: 【退出双层循环】break语法合法，但只退出最内层循环；达到k后必须结束收集，
     * 此处直接return ans最清晰。否则后续非空桶可能导致ans[k]越界。
     *
     * <p>复盘文档：同目录《Hot100堆逐题详解.md》的Q347分桶复盘章节。
     */
    class SolutionReviewed20260919 {
        public int[] topKFrequent(int[] nums, int k) {
            HashMap<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                if (!map.containsKey(num)) {
                    map.put(num, 1);
                } else {
                    map.put(num, map.get(num) + 1);
                }
            }
            // 分桶 -> 每个桶对应List<Integer>，桶的含义对应下标，下标即表示频率。
            // TODO: 【原语法错误记录】List<Integer>[] buckets = new List<>[nums.length + 1]; 右侧不能有<>
            // 【准确原因】不能直接创建参数化类型的数组；new List<Integer>[N]同样不合法。
            // 下列原写法正确可用，但会产生unchecked conversion警告，保留以供复盘。
            // 【可选改写】@SuppressWarnings("unchecked")
            // List<Integer>[] buckets = (List<Integer>[]) new List<?>[nums.length + 1];
            // 上述转换仅在始终只存List<Integer>且不经其他可写别名污染数组时保持类型约束。
            List<Integer>[] buckets = new List[nums.length + 1];
            // 最大频次是N，必须有buckets[N]，所以长度为N+1；桶按需创建列表。
            for (int key : map.keySet()) {
                if (buckets[map.get(key)] == null) {
                    buckets[map.get(key)] = new ArrayList<>();
                }
                buckets[map.get(key)].add(key);
            }

            int[] ans = new int[k];
            int index = 0;
            // i>=0正确；频次0不会出现，也可以写i>=1。
            for (int i = buckets.length - 1; i >= 0; i--) {
                if (buckets[i] != null) {
                    for (int num : buckets[i]) {
                        ans[index++] = num;
                        if (index == k) {
                            // TODO: 【原记录：语法错误-忘记上文】这里是双重循环，break只会到外层循环，
                            // 最终导致ArrayIndexOutOfBoundsException。错误行：break;
                            // 【修正分类】这是控制流逻辑错误，不是语法错误：break只退出内层for，
                            // 后续外层若再找到非空桶，就会写ans[k]越界；return直接结束整个方法。
                            return ans;
                        }
                    }
                }
            }
            return ans;
        }
    }

    /**
     * 1. 对应本题是：HashMap + 小根堆（门槛堆）
     * 2. 原TODO“最优解是桶”已完成，参见上方SolutionReviewed20260919。
     *
     * 本答案是简化版本的 门槛堆-小根堆。
     *  1. 先把正确版本写出来并讲清复杂度。如果面试官继续追问常数优化，再说：
     *     - 当前实现为了模板简洁，采用先入堆再淘汰。还可以把堆顶作为准入门槛，只有新元素频率更高时才执行替换，从而【减少不必要的堆调整】。
     */
    class Solution {
        public int[] topKFrequent(int[] nums, int k) {
            HashMap<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }

            // TODO: 这个PriorityQueue的 lambda太屌了。   泛型Integer对应下标（本题是数字本身，对应Map.key），lambda比较时，去map拿value。
            PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> (map.get(a) - map.get(b)));


            // TODO: 这是门槛堆的写法，一定要记住。
            // TODO: 小根堆的堆顶是当前 K 个候选中频率最低的元素。当大小超过 K，就把它淘汰。
            for (int key : map.keySet()) {
                heap.add(key);

                if (heap.size() > k) {
                    heap.poll();
                }
            }

            int[] ans = new int[k];
            int index = 0;
            while (!heap.isEmpty()) {
                ans[index++] = heap.poll();
            }
            return ans;
        }
    }



    /**
     * 下面这个是很早之前我写的版本，门槛堆的常数时间更小。
     */
    /**
     哈希表词频统计<int num, Node > + 封装Node（num + freq）+  门槛堆
     【难点】在于 封装 num + frep 在一起。从而从门槛堆中 既控制了频次，又能够从最终size中获得对应num的值。

     【步骤】
     使用 HashMap 统计完词频后，遍历每一个values。 放入到门槛堆。
     门槛堆是一个小根堆，
     （1）if heap.size() < targetSize，那么就 入堆
     （2）if size满了 && 当前元素 比 堆顶大的话，那么就要 堆顶出 + 新元素进
     通过上面两步，实现了门槛堆（小根堆），最终 只剩 前 size 个 最大的出现频率 的数字
     PS： 但是你看到了，在门槛堆中，我们比较的是频次，但是最终要拿出来的结果 是 该频次对应的值。 这就使得 同一个对象需要具备两个属性： 【1】频次用来比较 【2】对应的值用来获取结果  ==》 所以要封装成Node实例就可以了
     -----------------------------------------------------------------------

     利用一个堆就可以了 -> 门槛堆
     - 门槛堆
     - 就是 小根堆组织（堆顶就是这个门槛）， 当前的数字 能不能将 门槛干掉 自己进来。（有点类似于 堆实现 的 topK）
     - 先进行 词频统计（词频表）
     - 准备一个小根堆 （次数 少的 放在顶部）
     - 看小根堆 有没有满
     */
    class Solution1 {
        public int[] topKFrequent(int[] nums, int k) {
            HashMap<Integer, Node> map = new HashMap<>();
            // Step1 统计词频
            for (int i = 0; i < nums.length; i++) {
                if (!map.containsKey(nums[i])) {
                    map.put(nums[i], new Node(nums[i], 0));
                }
                // 【错误点】这里不是 重新赋值。 而是直接访问对应Node，修改Node里面的值
                // map.put(nums[i], map.get(nums[i]).freq + 1);
                map.get(nums[i]).freq++;
            }


            PriorityQueue<Node> minHeap = new PriorityQueue<>((a,b) -> (a.freq - b.freq)); // 注意，因为对象不可比较，所以要传入比较器
            // 词频统计完成之后，都封装到了Node里面，此时遍历Node， 使用 map.values()
            for (Node node : map.values()) {
                if (minHeap.size() < k) {                    // 门槛堆未满时，直接加入
                    minHeap.add(node);
                } else {
                    if (node.freq > minHeap.peek().freq) {  // 门槛堆满了，只有当堆顶元素<新元素时，才弹堆顶+加入新元素
                        minHeap.poll();
                        minHeap.add(node);
                    }
                }
            }

            // 最后，将门槛堆中的 node 依次弹出，塞入ans数组结果
            int[] ans = new int[k];
            int index = 0;
            while (!minHeap.isEmpty()) {
                ans[index++] = minHeap.poll().val;
            }
            return ans;
        }


        // 【构建Node数组】 包含val + freq两个属性
        public class Node {
            int val;
            int freq;

            public Node(int v, int f) {
                val = v;
                freq = f;
            }
        }

        public class MyComparator implements Comparator<Node> {
            public int compare(Node o1, Node o2) {
                return o1.freq - o2.freq;
            }
        }
    }

}
