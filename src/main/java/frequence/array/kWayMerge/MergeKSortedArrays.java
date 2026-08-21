package frequence.array.kWayMerge;

import java.util.*;

/**
 * 合并 K 个升序数组。
 *
 * <p>给定一个二维数组 {@code arrays}，其中每个一维数组都已经按照升序排列。
 * 将这些数组中的全部元素合并为一个升序的一维数组并返回。
 *
 * <p>本题与“合并 K 个升序链表”属于同一个 K 路归并模型：
 * 数组使用下标定位各序列的下一个元素，链表使用 {@code next} 指针定位下一个节点。
 *
 * <p>统一记号：{@code K} 表示有序数组数量，{@code T} 表示所有数组的元素总数。
 */
public class MergeKSortedArrays {

    /**
     * 使用小根堆完成 K 路归并。
     *
     * <p>输入要求：非空的 {@code M * N} 规则矩阵，每一行非空、等长且升序。
     * 该版本不能处理空外层数组、空行和不等长的行。
     *
     * TODO：如果要包含 二维数组每行不等长，看下面的wudi版本方法。
     *
     * <p>时间复杂度：{@code O(T log K)}；
     * 堆的额外空间复杂度：{@code O(K)}，不计算返回数组。
     */
    public int[] mergeByMinHeap(int[][] arrays) {
        int M = arrays.length;
        int N = arrays[0].length;
        int[] ans = new int[M * N];
        int write = 0;
        // TODO: 【边界问题】直接相减可能发生int溢出；数值范围不明确时应改为Integer.compare(a.val, b.val)。
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> (a.val - b.val));
        // 堆中始终只保存每一行当前尚未处理的第一个元素，因此堆大小最多为K。
        for (int i = 0; i < M; i++) {
            heap.add(new Node(arrays[i][0], i, 0));
        }
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            ans[write++] = cur.val;

            // TODO: 这里一定注意 1. 检查【下一个位置】的有效性 2. 新添加节点到heap
            //   错误： if (cur.index < N) {
            if (cur.index + 1 < N) {   // next position is available
                heap.add(new Node(arrays[cur.rowIndex][cur.index + 1], cur.rowIndex, cur.index + 1));
            }
        }
        return ans;
    }

    /**
     * 堆节点必须同时保存元素值及其来源位置。
     * 弹出节点后，通过 {@code rowIndex} 和 {@code index} 才能找到同一行的后继元素。
     */
    public static class Node {
        // 当前参与K路比较的元素值。
        int val;
        // 元素来自二维数组的哪一行。
        int rowIndex;
        // 元素在所属行中的下标。
        int index;

        public Node(int _val, int _rowIndex, int _index) {
            val = _val;
            rowIndex = _rowIndex;
            index = _index;
        }
    }

    /**
     * 下列版本为 数组K路归并无敌覆盖版本，包含二维数组为空，每行不等长。
     *
     * <p>支持外层数组为 {@code null} 或空数组，支持每行不等长以及长度为0的空行；
     * 仍要求每一行本身不是 {@code null}，并且行内元素已经升序排列。
     *
     * <p>时间复杂度：{@code O(T log K)}；
     * 堆的额外空间复杂度：{@code O(K)}，不计算返回数组。
     */
    public int[] mergeByMinHeapWudi(int[][] arrays) {
        if (arrays == null || arrays.length == 0) {
            return new int[]{};
        }
        int total = 0;
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> (Integer.compare(a.val, b.val)));
        for (int i = 0; i < arrays.length; i++) {  // TODO: 【改动1】这里要每行单独累加total
            total += arrays[i].length;
            if (arrays[i].length > 0) {  // TODO: 【改动2】只有有长度的行 才会把头元素 加入到小根堆
                heap.add(new Node(arrays[i][0], i, 0));
            }
        }
        int[] ans = new int[total];
        int write = 0;
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            ans[write++] = cur.val;

            if (cur.index + 1 < arrays[cur.rowIndex].length) {  // TODO: 【改动3】每行检查的时候不能再用N统一检查了。需要使用【每行的长度 arrays[rowIndex].length】来检查 【下一个位置是否可用】
                heap.add(new Node(arrays[cur.rowIndex][cur.index + 1], cur.rowIndex, cur.index + 1));
            }
        }
        return ans;
    }

    /**
     * 使用平衡的两两归并完成 K 路归并。  -》 递归（模仿 归并排序）
     *
     * <p>支持每行不等长以及长度为0的空行，但要求外层数组至少包含一行，
     * 且每一行本身不是 {@code null}。
     *
     * <p>每层归并处理的元素总数为 {@code T}，平衡递归共有 {@code log K} 层，
     * 所以时间复杂度为 {@code O(T log K)}。归并结果的额外空间复杂度为
     * {@code O(T)}，递归栈空间为 {@code O(log K)}。
     */
    public int[] mergeByDivideAndConquer(int[][] arrays) {
        // TODO: 【边界限制】arrays.length == 0时会进入非法递归区间，通用版本应直接返回new int[0]。
        return process(0, arrays.length - 1, arrays);
    }

    /**
     * 将行下标区间 {@code [l, r]} 内的有序数组平衡地合并。
     */
    public int[] process(int l, int r, int[][] matrix) {
        if (l == r) {
            // 递归终点：区间中只剩一个本身已经有序的数组。
            return matrix[l];
        }
        int mid = (l + r) / 2;
        int[] a = process(l, mid, matrix);
        int[] b = process(mid + 1, r, matrix);
        return merge(a, b);
    }

    /**
     * 归并两个升序数组，时间复杂度为 {@code O(a.length + b.length)}。
     */
    public int[] merge(int[] a, int[] b) {
        int len1 = a.length;
        int len2 = b.length;
        int[] ans = new int[len1 + len2];
        int index = 0;
        int p1 = 0;
        int p2 = 0;
        while (p1 < len1 && p2 < len2) {
            ans[index++] = a[p1] <= b[p2] ? a[p1++] : b[p2++];
        }
        while (p1 < len1) {
            ans[index++] = a[p1++];
        }
        while (p2 < len2) {
            ans[index++] = b[p2++];
        }
        return ans;
    }
}
