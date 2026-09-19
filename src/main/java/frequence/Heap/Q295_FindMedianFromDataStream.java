package frequence.Heap;

import java.util.Comparator;
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

    /**
     * 2026-09-19用户复盘版：纯寻找中位数，无删除，使用双堆。
     *
     * <p><b>你的原始理解与命名：</b>线性顺序是upper | lower，左侧upper为大根堆，
     * 弹出左侧最大值；右侧lower为小根堆，弹出右侧最小值。当前代码正确。
     *
     * <p>TODO: 【原错误记录】“固定三步骤反了：1.进lower；2.upper.offer(lower.poll());
     * 3.视size情况是否要lower.add(upper.poll())。”
     * <p>【限定说明】上述步骤与本版本“左侧upper多一个”的约定不一致；但变量名本身没有
     * 固定算法含义。下方FixedStepMedianFinder把lower定义为左侧大根堆，所以它先入lower正确。
     * 应记住职责：先入左侧大根堆，左侧最大值移到右侧，右侧数量多时将右侧最小值移回左侧。
     *
     * <p>TODO: 【概念误区】“左侧是大根堆，所以左侧所有元素 >= 右侧所有元素”是错误理解。
     * 大根堆只保证堆顶是本堆的最大值，不表示整堆的值大于另一堆。
     * 左侧保存较小的一半，右侧保存较大的一半，因此跨堆关系仍是左侧所有元素 <= 右侧所有元素。
     * <pre>
     * 左upper（大根堆）：{1,2,3} | 右lower（小根堆）：{4,5}
     * 左堆顶3 <= 右堆顶4；中位数为3。
     * upper.size() == lower.size()
     * 或upper.size() == lower.size() + 1
     * </pre>
     *
     * <p>插入O(log N)，查询O(1)，空间O(N)。findMedian沿用题目保证：查询前至少插入一个数。
     * 复盘关联：同目录《Hot100堆逐题详解.md》的Q295命名与跨堆不变量章节。
     */
    public static class MedianFinderReviewed20260919 {
        PriorityQueue<Integer> lower;
        PriorityQueue<Integer> upper;

        public MedianFinderReviewed20260919() {
            lower = new PriorityQueue<>();
            upper = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        }

        public void addNum(int num) {
            // TODO: 一定不要写反：本版本upper为左侧大根堆，lower为右侧小根堆。
            upper.offer(num);
            lower.offer(upper.poll());
            if (lower.size() > upper.size()) { // 右.size > 左.size
                upper.offer(lower.poll());
            }
        }

        public double findMedian() {
            int size = lower.size() + upper.size();
            if (size % 2 == 1) {
                return upper.peek();
            } else {
                // 先转double再相加，避免两个int的和溢出。
                return ((double) upper.peek() + lower.peek()) / 2;
            }
        }
    }

    /**
     * 2026-09-14推荐复盘版本：使用固定三步插入，不需要先判断新元素应该属于哪一侧。
     *
     * <p><b>双堆结构：</b>{@code lower}保存较小的一半，是大根堆；{@code upper}保存较大的
     * 一半，是小根堆。始终维护：
     * <pre>
     * lower中的所有元素 <= upper中的所有元素
     * lower.size() == upper.size()
     * 或lower.size() == upper.size() + 1
     * </pre>
     *
     * <p><b>固定插入步骤：</b>
     * <ol>
     *     <li>新元素先进入lower；</li>
     *     <li>弹出lower最大值并放入upper，修复左右值域关系；</li>
     *     <li>如果upper数量超过lower，弹出upper最小值并移回lower，修复数量关系。</li>
     * </ol>
     * 第2步维护顺序不变量，第3步维护数量不变量。
     *
     * <p>TODO: 【命名建议】原实现的more、less容易被理解为“数量更多/更少”。lower、upper
     * 能直接表达数值区间，记忆负担更低。
     *
     * <p>TODO: 【比较器易错点】不要使用{@code (a,b) -> b-a}构造大根堆，极值相减可能
     * 整数溢出。应使用{@link Comparator#reverseOrder()}或{@link Integer#compare(int, int)}。
     */
    public static class FixedStepMedianFinder {

        private final PriorityQueue<Integer> lower;
        private final PriorityQueue<Integer> upper;

        public FixedStepMedianFinder() {
            lower = new PriorityQueue<>(Comparator.reverseOrder());
            upper = new PriorityQueue<>();
        }

        public void addNum(int num) {
            // 第1、2步：无条件先入lower，再把lower最大值转给upper，保证lower <= upper。
            lower.offer(num);
            upper.offer(lower.poll());

            // 第3步：规定奇数时由lower多保存一个元素。
            if (upper.size() > lower.size()) {
                lower.offer(upper.poll());
            }
        }

        public double findMedian() {
            if (lower.size() > upper.size()) {
                return lower.peek();
            }
            // 先转换为double再相加，避免两个int直接相加溢出。
            return ((double) lower.peek() + upper.peek()) / 2.0;
        }
    }

    public static class MedianFinder {

        private final PriorityQueue<Integer> more;
        private final PriorityQueue<Integer> less;
        private int size;

        public MedianFinder() {
            // TODO: 【可优化】b-a在整数极值下可能溢出，推荐Comparator.reverseOrder()。
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
