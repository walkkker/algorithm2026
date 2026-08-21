package frequence.Heap;

import java.util.PriorityQueue;

/**
 * 295. 数据流的中位数
 *
 * <p>中位数是有序整数列表中间位置的值。如果列表长度为偶数，中位数是中间两个值的平均值。
 *
 * <p>设计一个支持以下操作的数据结构：
 * <ul>
 *     <li>{@code addNum(num)}：将整数加入数据流；</li>
 *     <li>{@code findMedian()}：返回当前所有元素的中位数。</li>
 * </ul>
 *
 * <p><b>核心思路：</b>{@code more}是保存左半区的大根堆，{@code less}是保存右半区的小根堆。
 * 始终维持{@code more.size() == less.size()}或{@code more.size() == less.size() + 1}，并保证
 * {@code more.peek() <= less.peek()}。因此奇数个元素时中位数是{@code more.peek()}，偶数个元素时
 * 是两个堆顶的平均值。
 *
 * <p>{@code addNum}不能只按堆大小直接加入某个堆，因为这只能维持容量，不能保证左右区间有序。
 * 当前实现先加入一侧，再把该侧堆顶转移到另一侧，一次操作同时维护两个不变量。
 */
public class Q295_FindMedianFromDataStream {

    public static class MedianFinder {

        private final PriorityQueue<Integer> more;
        private final PriorityQueue<Integer> less;
        private int size;

        public MedianFinder() {
            more = new PriorityQueue<>((a, b) -> b - a);
            less = new PriorityQueue<>();
            size = 0;
        }

        public void addNum(int num) {
            if (more.size() == less.size() + 1) {
                more.offer(num);
                less.offer(more.poll());
            } else {
                less.offer(num);
                more.offer(less.poll());
            }
            size++;
        }

        public double findMedian() {
            if (size % 2 == 1) {
                return (double) more.peek();
            } else {
                return ((double) more.peek() + less.peek()) / 2;
            }
        }
    }
}
