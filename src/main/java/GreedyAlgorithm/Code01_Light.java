package GreedyAlgorithm;

import java.util.HashSet;

/**
 * 重点看方法2：一个左神写的 一个我写的
 *
 * TODO：这道题虽然是个贪心。 暴力递归是个 子序列 ->  '.'的位置选择放/不放。
 *
 * TODO：但是贪心解法下的 分类讨论，非常值得 review和反思，第一次没想明白，并且 都想好了是i跳跃，竟然还错误使用了for循环（最后会固定i++），应该使用while的。
 * TODO：【非常重要】贪心解法下的分类讨论，包含了 不同子数组长度(i,i+1,i+2)（if 到达边界/else 未到达边界）的情况讨论（else里面到底包含哪些情况？ else内部再if-else的话 一定要把情况分清楚），如果把 想法变成if-else实现，缜密实现，需要回味 以及 重做。
 * TODO：【实战TIPS】所以，一旦感觉if-else不清晰，就写注释吧，把else的情况都列出来，不要再脑子里面乱想，越想越错。
 * TODO: 脑海中要有分类讨论的 具像图。不要害怕多层if-else。先把题解出来再说。
 *
 **/

public class Code01_Light {

    public static int minLight1(String road) {
        if (road == null || road.length() == 0) {
            return 0;
        }
        return process(road.toCharArray(), 0, new HashSet<>());
    }

    // str[index....]位置，自由选择放灯还是不放灯
    // str[0..index-1]位置呢？已经做完决定了，那些放了灯的位置，存在lights里
    // 要求选出能照亮所有.的方案，并且在这些有效的方案中，返回最少需要几个灯
    public static int process(char[] str, int index, HashSet<Integer> lights) {
        if (index == str.length) { // 结束的时候
            for (int i = 0; i < str.length; i++) {
                if (str[i] != 'X') { // 当前位置是点的话
                    if (!lights.contains(i - 1) && !lights.contains(i) && !lights.contains(i + 1)) {
                        return Integer.MAX_VALUE;
                    }
                }
            }
            return lights.size();
        } else { // str还没结束
            // i X .
            int no = process(str, index + 1, lights);
            int yes = Integer.MAX_VALUE;
            if (str[index] == '.') {
                lights.add(index);
                yes = process(str, index + 1, lights);
                lights.remove(index);
            }
            return Math.min(no, yes);
        }
    }

    // 左神版本!!!
    public static int minLight2_zuo(String road) {
        char[] str = road.toCharArray();
        int i = 0;
        int light = 0;
        while (i < str.length) {
            if (str[i] == 'X') {
                i++;
            } else { //包含三种情况 .边界  |   .. （需要继续讨论）    |   .X
                light++;
                if (i + 1 == str.length) {
                    break;
                } else { // 有i位置 i+ 1 X .
                    if (str[i + 1] == 'X') {
                        i = i + 2;
                    } else {     // 对应 『.. （需要继续讨论）』
                        // TODO: 此时有三种情况： ..边界 ... ..X ==》 发现 这三类情况，执行逻辑可以是一样的 =》 都是 放一个灯，i=i+3
                        //  注意，尤其是边界这个情况 合在一起了，很骚。 因为按上面正常是break的，但是实际上 i=i+3 会退出while，实现一样的效果。
                        i = i + 3;
                    }
                }
            }
        }
        return light;
    }

    // 其实就是列清楚所有的if情况
    public static int minLight2(String road) {
        char[] chs = road.toCharArray();
        /* 是不是很像一颗二叉树，甚至有点霍夫曼二叉树的感觉，只有叶子节点需要考虑。
            然而，不是的，主要是卡在了边界条件上。
            X
            .
                .X
                ..
                    ...
                    ..X
         */

        // 下标i跳跃
        int ans = 0;
        // TODO: 【错误】 因为循环体内部涉及到 i的跳跃了， 不能使用for循环，因为for循环会在所有的执行体 最后加上i++，这个是与我们违背的
        //      for (int i = 0; i < chs.length; i++) {
        int i = 0;
        while (i < chs.length) {
            if (chs[i] == 'X') {
                i = i + 1;
            } else {
                ans++;
                if (i == chs.length - 1) {
                    break;
                } else {
                    if (chs[i + 1] == 'X') {
                        i = i + 2;
                    } else {
                        if (i + 1 == chs.length - 1) {
                            break;
                        } else {
                            i = i + 3;
                        }
                    }
                }
            }
        }
        return ans;
    }


    // 更简洁的解法
    // 两个X之间，数一下.的数量，然后除以3，向上取整
    // 把灯数累加
    public static int minLight3(String road) {
        char[] str = road.toCharArray();
        int cur = 0;
        int light = 0;
        for (char c : str) {
            if (c == 'X') {
                light += (cur + 2) / 3;
                cur = 0;
            } else {
                cur++;
            }
        }
        light += (cur + 2) / 3;
        return light;
    }

    // for test
    public static String randomString(int len) {
        char[] res = new char[(int) (Math.random() * len) + 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = Math.random() < 0.5 ? 'X' : '.';
        }
        return String.valueOf(res);
    }

    public static void main(String[] args) {
        int len = 20;
        int testTime = 100000;
        for (int i = 0; i < testTime; i++) {
            String test = randomString(len);
            int ans1 = minLight1(test);
            int ans2 = minLight2(test);
            int ans3 = minLight3(test);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("oops!");
            }
        }
        System.out.println("finish!");
    }
}
