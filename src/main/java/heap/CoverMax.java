package heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Method1: 暴力解 每个0.5的点计算线段数量。
 * Method2: 系统提供：数组排序 + 堆。  -> 把线段想象成一个一个加进去的，计算每个新线段的重合数量
 * Method3: 我写的这个：左神直接复用的 数组。先彻底搞明白Method2再说。
 *
 *
 * 题意：
 * 给定很多线段，每个线段都有两个数[start, end]；表示线段的开始位置和结束位置，左右都是闭区间。
 * 规定：         1）线段的开始和结束位置一定都是整数值
 *               2）线段重合区域的长度必须 >= 1;    ===> 也就是说 o1.end == o2.start 的场景是不算做重合的
 * 返回线段最多重合的区域中，包含了几条线段？
 *
 * 入参： int[][] lines  -> 行n 列2 -> 代表n个线段（start & end）
 */
public class CoverMax {

    public static int maxCover1(int[][] lines) {

        int start = Integer.MAX_VALUE;
        int end = Integer.MIN_VALUE;
        // S1: 先遍历一遍，找到 最早点 和 最晚点，确定全范围  O(N)
        for (int[] line : lines) {
            start = Math.min(line[0], start);
            end = Math.max(line[1], end);
        }

        int maxCover = -1;
        // S2: start->end, 每0.5必可以计算出 当前0.5上的线段数 O(N^2)
        for (int i = start; i < end; i++) {
            double mid = i + 0.5;
            int count = 0;
            for (int[] line : lines) {
                if (mid > line[0] && mid < line[1]) {
                    count++;
                }
            }
            maxCover = Math.max(maxCover, count);
        }

        return maxCover;
    }


    // 看下能否直接复用数组
    public static int maxCover2(int[][] lines) {

        // 按照 line[0] 升序
        Arrays.sort(lines, (o1, o2) -> (o1[0] - o2[0]));

        // 系统提供： 数组排序，  堆(默认就是int小根堆)
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        int count = 0;
        for (int[] line : lines) {
            while (!heap.isEmpty() && heap.peek() <= line[0]) {
                heap.poll();
            }
            heap.offer(line[1]);    // 先把自己的end加进去，这样计算heap.size() 时，一定包含了当前的线段
            count = Math.max(heap.size(), count);
        }
        return count;
    }



    // for test 左神代码
    public static int maxCover3(int[][] lines) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lines.length; i++) {
            min = Math.min(min, lines[i][0]);
            max = Math.max(max, lines[i][1]);
        }
        int cover = 0;
        for (double p = min + 0.5; p < max; p += 1) {
            int cur = 0;
            for (int i = 0; i < lines.length; i++) {
                if (lines[i][0] < p && lines[i][1] > p) {
                    cur++;
                }
            }
            cover = Math.max(cover, cur);
        }
        return cover;
    }

    public static int[][] generateLines(int N, int L, int R) {
        int size = (int) (Math.random() * N) + 1;
        int[][] ans = new int[size][2];
        for (int i = 0; i < size; i++) {
            int a = L + (int) (Math.random() * (R - L + 1));
            int b = L + (int) (Math.random() * (R - L + 1));
            if (a == b) {
                b = a + 1;
            }
            ans[i][0] = Math.min(a, b);
            ans[i][1] = Math.max(a, b);
        }
        return ans;
    }

//    public static class StartComparator implements Comparator<Line> {
//
//        @Override
//        public int compare(Line o1, Line o2) {
//            return o1.start - o2.start;
//        }
//
//    }

    public static void main(String[] args) {

//        Line l1 = new Line(4, 9);
//        Line l2 = new Line(1, 4);
//        Line l3 = new Line(7, 15);
//        Line l4 = new Line(2, 4);
//        Line l5 = new Line(4, 6);
//        Line l6 = new Line(3, 7);
//
//        // 底层堆结构，heap
//        PriorityQueue<Line> heap = new PriorityQueue<>(new StartComparator());
//        heap.add(l1);
//        heap.add(l2);
//        heap.add(l3);
//        heap.add(l4);
//        heap.add(l5);
//        heap.add(l6);
//
//        while (!heap.isEmpty()) {
//            Line cur = heap.poll();
//            System.out.println(cur.start + "," + cur.end);
//        }

        System.out.println("test begin");
        int N = 100;
        int L = 0;
        int R = 200;
        int testTimes = 200000;
        for (int i = 0; i < testTimes; i++) {
            int[][] lines = generateLines(N, L, R);
            int ans1 = maxCover1(lines);
            int ans2 = maxCover2(lines);
            int ans3 = maxCover3(lines);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test end");
    }


}
