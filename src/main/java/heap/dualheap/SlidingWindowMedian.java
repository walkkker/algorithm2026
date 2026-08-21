package heap.dualheap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 滑动窗口中位数：双堆 + 延迟删除。
 *
 * <p>本类 main 方法是对数器：暴力复制并排序每个窗口，和双堆结果逐项比较。</p>
 */
public class SlidingWindowMedian {

    public double[] medianSlidingWindow(int[] nums, int k) {
        if (nums == null) {
            throw new IllegalArgumentException("nums must not be null");
        }
        if (k < 1 || k > nums.length) {
            throw new IllegalArgumentException("k must be in [1, nums.length]");
        }

        DualHeap dualHeap = new DualHeap(k);
        for (int i = 0; i < k; i++) {
            dualHeap.add(nums[i]);
        }

        double[] ans = new double[nums.length - k + 1];
        ans[0] = dualHeap.median();
        for (int right = k; right < nums.length; right++) {
            dualHeap.add(nums[right]);
            dualHeap.erase(nums[right - k]);
            ans[right - k + 1] = dualHeap.median();
        }
        return ans;
    }

    /**
     * PriorityQueue 删除堆顶是 O(log K)，但 remove(Object) 需要先线性定位元素，
     * 整体是 O(K)。延迟删除把“窗口已经移除、堆中尚未物理弹出”的值记入 delayed，
     * 等该值到达堆顶时再由 prune 连续清理。
     */
    private static class DualHeap {
        private final PriorityQueue<Integer> small =
                new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        private final PriorityQueue<Integer> large = new PriorityQueue<>();

        /**
         * key：已经失效、等待从某个堆中物理删除的数值。
         * value：该数值还需要物理删除多少次，不是窗口中该数值的有效剩余次数。
         *
         * <p>重复值只按 value 计数是正确的：相同数值的多个副本对中位数不可区分。</p>
         */
        private final Map<Integer, Integer> delayed = new HashMap<>();

        /**
         * 逻辑大小只统计当前窗口中的有效元素；PriorityQueue.size() 还包含延迟删除元素。
         * 平衡和中位数判断必须使用逻辑大小。
         */
        private int smallSize;
        private int largeSize;
        private final int windowSize;

        private DualHeap(int windowSize) {
            this.windowSize = windowSize;
        }

        private void add(int num) {
            if (small.isEmpty() || num <= small.peek()) {
                small.offer(num);
                smallSize++;
            } else {
                large.offer(num);
                largeSize++;
            }
            balance();
        }

        private void erase(int num) {
            delayed.put(num, delayed.getOrDefault(num, 0) + 1);

            // small 的有效堆顶是两个堆的分界线，用它判断被删除值逻辑上属于哪一侧。
            if (num <= small.peek()) {
                smallSize--;
                if (num == small.peek()) {
                    prune(small);
                }
            } else {
                largeSize--;
                if (!large.isEmpty() && num == large.peek()) {
                    prune(large);
                }
            }
            balance();
        }

        private double median() {
            if ((windowSize & 1) == 1) {
                return small.peek();
            }
            return ((long) small.peek() + large.peek()) / 2.0;
        }

        /**
         * 恢复 smallSize == largeSize 或 smallSize == largeSize + 1。
         *
         * <p>add 和 erase 每次只改变一个逻辑元素，且每个操作开始前都是平衡状态，
         * 所以一次跨堆移动即可恢复平衡。移动后 prune 原堆，是因为新的堆顶可能正好是失效值。</p>
         */
        private void balance() {
            if (smallSize > largeSize + 1) {
                large.offer(small.poll());
                smallSize--;
                largeSize++;
                prune(small);
            } else if (smallSize < largeSize) {
                small.offer(large.poll());
                smallSize++;
                largeSize--;
                prune(large);
            }
        }

        /**
         * 只清理堆顶连续出现的失效值。非堆顶元素现在无法 O(log K) 定位，
         * 但它以后若影响答案，必然先成为堆顶，因此届时再删除即可。
         */
        private void prune(PriorityQueue<Integer> heap) {
            while (!heap.isEmpty()) {
                int num = heap.peek();
                Integer count = delayed.get(num);
                if (count == null) {
                    break;
                }
                heap.poll();
                if (count == 1) {
                    delayed.remove(num);
                } else {
                    delayed.put(num, count - 1);
                }
            }
        }
    }

    private static double[] comparator(int[] nums, int k) {
        double[] ans = new double[nums.length - k + 1];
        for (int left = 0; left + k <= nums.length; left++) {
            int[] window = Arrays.copyOfRange(nums, left, left + k);
            Arrays.sort(window);
            if ((k & 1) == 1) {
                ans[left] = window[k / 2];
            } else {
                ans[left] = ((long) window[k / 2 - 1] + window[k / 2]) / 2.0;
            }
        }
        return ans;
    }

    private static void assertWindowMedian(SlidingWindowMedian solution, int[] nums, int k) {
        double[] expected = comparator(nums, k);
        double[] actual = solution.medianSlidingWindow(nums, k);
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("window median mismatch, nums=" + Arrays.toString(nums)
                    + ", k=" + k + ", expected=" + Arrays.toString(expected)
                    + ", actual=" + Arrays.toString(actual));
        }
    }

    public static void main(String[] args) {
        SlidingWindowMedian solution = new SlidingWindowMedian();
        assertWindowMedian(solution, new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
        assertWindowMedian(solution, new int[]{1, 1, 1, 1}, 2);
        assertWindowMedian(solution, new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, 2);
        assertWindowMedian(solution, new int[]{5, -2, 9}, 1);
        assertWindowMedian(solution, new int[]{4, 3, 2, 1}, 4);

        final long seed = 20260716L;
        final int testTimes = 10_000;
        final int maxLength = 30;
        Random random = new Random(seed);
        for (int test = 0; test < testTimes; test++) {
            int length = random.nextInt(maxLength) + 1;
            int[] nums = new int[length];
            for (int i = 0; i < length; i++) {
                nums[i] = random.nextInt(21) - 10;
            }
            int k = random.nextInt(length) + 1;
            assertWindowMedian(solution, nums, k);
        }
        System.out.println("SlidingWindowMedian comparator passed, seed=" + seed);
    }
}
