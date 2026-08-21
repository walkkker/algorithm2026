package frequence.array;

import java.util.*;
/**
 * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
 * 请你合并所有重叠的区间，并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间 。
 */
public class Q56 {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (Integer.compare(a[0], b[0])));
        List<int[]> ans = new ArrayList<>();
        for (int[] cur : intervals) {
            if (ans.size() == 0) {
                ans.add(new int[]{cur[0], cur[1]});
            } else {
                int[] last = ans.get(ans.size() - 1);
                if (cur[0] <= last[1]) {
                    last[1] = Math.max(last[1], cur[1]);
                } else {
                    ans.add(new int[]{cur[0], cur[1]});
                }
            }
        }
        int[][] ret = new int[ans.size()][2];
        int index = 0;
        for (int[] cur : ans) {
            ret[index++] = cur;
        }
        return ret;
    }
}
