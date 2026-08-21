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
     * 1. 对应本题是：HashMap + 小根堆（门槛堆）
     * 2. 最优解是 桶（后面TODO）
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
