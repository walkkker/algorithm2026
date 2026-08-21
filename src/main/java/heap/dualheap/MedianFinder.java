package heap.dualheap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 普通双堆：只支持数据流持续插入，不涉及删除。
 *
 * <p>学习顺序：</p>
 * <ol>
 *     <li>先掌握本类的“插入 + 平衡 + 查询中位数”。</li>
 *     <li>再学习 {@link SlidingWindowMedian} 增加的“删除 + 延迟删除”。</li>
 * </ol>
 *
 * <p>本类 main 方法是对数器：用排序后的 List 作为正确但较慢的参照实现。</p>
 */
public class MedianFinder {

    /**
     * small 保存较小的一半，是大根堆，堆顶是较小一半中的最大值。
     * large 保存较大的一半，是小根堆，堆顶是较大一半中的最小值。
     *
     * <p>注意：PriorityQueue 只保证堆顶最优，不保证迭代或底层数组整体有序。</p>
     */
    private final PriorityQueue<Integer> small =
            new PriorityQueue<>((a, b) -> Integer.compare(b, a));
    private final PriorityQueue<Integer> large = new PriorityQueue<>();

    public void addNum(int num) {
        // 分界线是 small.peek()。放入任意一侧后，再恢复两个堆的大小不变量。
        if (small.isEmpty() || num <= small.peek()) {
            small.offer(num);
        } else {
            large.offer(num);
        }
        balance();
    }

    public double findMedian() {
        if (small.isEmpty()) {
            throw new IllegalStateException("cannot query median before adding numbers");
        }
        if (small.size() > large.size()) {
            return small.peek();
        }
        // 先转 long 再相加，防止两个 int 极值相加发生溢出。
        return ((long) small.peek() + large.peek()) / 2.0;
    }

    /**
     * 维护两个不变量：
     * 1. small 中的每个有效元素都不大于 large 中的每个有效元素；
     * 2. small.size == large.size，或 small.size == large.size + 1。
     *
     * <p>每次只插入一个数，插入前又是平衡状态，因此大小差最多只会偏离目标 1。
     * 移动一次堆顶就能恢复平衡，不需要 while。</p>
     */
    private void balance() {
        if (small.size() > large.size() + 1) {
            large.offer(small.poll());
        } else if (small.size() < large.size()) {
            small.offer(large.poll());
        }
    }

    private static double comparatorMedian(List<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int size = sorted.size();
        if ((size & 1) == 1) {
            return sorted.get(size / 2);
        }
        return ((long) sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
    }

    private static void assertMedian(MedianFinder finder, List<Integer> values) {
        double expected = comparatorMedian(values);
        double actual = finder.findMedian();
        if (Double.compare(expected, actual) != 0) {
            throw new AssertionError("median mismatch, values=" + values
                    + ", expected=" + expected + ", actual=" + actual);
        }
    }

    public static void main(String[] args) {
        final long seed = 20260716L;
        final int testTimes = 1_000;
        final int maxOperations = 100;
        Random random = new Random(seed);

        for (int test = 0; test < testTimes; test++) {
            MedianFinder finder = new MedianFinder();
            List<Integer> values = new ArrayList<>();
            int operations = random.nextInt(maxOperations) + 1;
            for (int operation = 0; operation < operations; operation++) {
                int value;
                if (operation == 0 && test % 2 == 0) {
                    value = Integer.MIN_VALUE;
                } else if (operation == 1 && test % 2 == 0) {
                    value = Integer.MAX_VALUE;
                } else {
                    value = random.nextInt(201) - 100;
                }
                finder.addNum(value);
                values.add(value);
                assertMedian(finder, values);
            }
        }
        System.out.println("MedianFinder comparator passed, seed=" + seed);
    }
}
