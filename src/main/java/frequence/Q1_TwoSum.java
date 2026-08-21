package frequence;

import java.util.*;

/**
 * 这道题虽然很简单。 但是跟 【子数组累加和三连-第二连】是一样的框架。
 * 但是它：
 *  1. HashMap
 *  2. 在for循环中，每一个 后面元素 【检查】 map是否存有'前面满足target-arr[i]的元素'
 */
public class Q1_TwoSum {
    // 题目保证唯一解
    public static int[] twoSum(int[] arr, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(target - arr[i])) {
                return new int[]{map.get(target - arr[i]), i};
            } else {
                map.put(arr[i], i);
            }
        }
        return new int[]{-1, -1};
    }
}
