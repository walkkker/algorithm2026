package bit_calculation;

import java.util.HashMap;
import java.util.HashSet;

// 输入一定能够保证，数组中所有的数都出现了M次，只有一种数出现了K次
// 1 <= K < M
// 返回这种数
// 【solution】相比于哈希表统计，位运算不改变时间复杂度，但是可以优化空间复杂度到O(1)
public class KM {
    public static int km(int[] arr, int k, int m) {
        // 【易错点】注意 出现K次0的情况
        // 思路：new int[32] 对应32bit。 统计每个bit位的1数量
        // 因为k,m次的原因，所以 有两种1的积累情况：m * n + k(有k的数字), m * n（没有K的数字） (n >= 0)
        int[] bits = new int[32];
        for (int i = 0; i < 32; i++) {
            int bit = 1 << i;
            for (int num : arr) {
                bits[i] += (num & bit) != 0 ? 1 : 0;
            }
        }
        int target = 0;    // 这样写，直接包含了 0出现K次的情况。因为0出现K次时，所有的bits都是被m整除的。
        for (int i = 0; i < 32; i++) {
            if (bits[i] % m == k) {
                // 此处 += 或者 |= 答案相同。 |=好理解，主要说下 +=
                // 对于 +=，其实就是进行二进制相加。 单个位置上的1 相互加起来。所以效果跟 |= 一样
                // 不用担心正负数的问题(尤其对于1 << 31)，因为 二进制相加本身就是基于 【补码】。
                // 不要想着是把1<<n位的数字转成十进制然后相加，这样无法跟 |= 对齐。【其实就是二进制相加就完事了。】
                // 然后担心正负数问题的话，没必要，因为计算机的运算都是基于补码。这使得二进制运算（无论正负）均可使用加法。
                /*
                    int a = 12; // 0000 1100
                    System.out.println((1<<31) | a);  // -2147483636
                    System.out.println((1<<31) + a);  // 2147483636
                 */
                target |= 1 << i;
            }
        }
        return target;
    }


    public static int test(int[] arr, int k, int m) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        int ans = 0;
        for (int num : map.keySet()) {
            if (map.get(num) == k) {
                ans = num;
                break;
            }
        }
        return ans;
    }

    public static HashMap<Integer, Integer> map = new HashMap<>();


    // 为了测试
    public static int[] randomArray(int maxKinds, int range, int k, int m) {
        int ktimeNum = randomNumber(range);
        // 真命天子出现的次数
        int times = k;
        // 2
        int numKinds = (int) (Math.random() * maxKinds) + 2;
        // k * 1 + (numKinds - 1) * m
        int[] arr = new int[times + (numKinds - 1) * m];
        int index = 0;
        for (; index < times; index++) {
            arr[index] = ktimeNum;
        }
        numKinds--;
        HashSet<Integer> set = new HashSet<>();
        set.add(ktimeNum);
        while (numKinds != 0) {
            int curNum = 0;
            do {
                curNum = randomNumber(range);
            } while (set.contains(curNum));
            set.add(curNum);
            numKinds--;
            for (int i = 0; i < m; i++) {
                arr[index++] = curNum;
            }
        }
        // arr 填好了
        for (int i = 0; i < arr.length; i++) {
            // i 位置的数，我想随机和j位置的数做交换
            int j = (int) (Math.random() * arr.length);// 0 ~ N-1
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }

    // 为了测试
    // [-range, +range]
    public static int randomNumber(int range) {
        return (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
    }

    // 为了测试
    public static void main(String[] args) {
        int kinds = 5;
        int range = 30;
        int testTime = 100000;
        int max = 9;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int a = (int) (Math.random() * max) + 1; // a 1 ~ 9
            int b = (int) (Math.random() * max) + 1; // b 1 ~ 9
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            // k < m
            if (k == m) {
                m++;
            }
            int[] arr = randomArray(kinds, range, k, m);
            int ans1 = test(arr, k, m);
            int ans2 = km(arr, k, m);
            int ans3 = km(arr, k, m);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println(ans1);
                System.out.println(ans3);
                System.out.println("出错了！");
            }
        }
        System.out.println("测试结束");
    }

}
