package BackTrack;

import java.io.*;
import java.util.*;

/**
 * 暴力递归 - 回溯
 *
 * 当前位置要 / 不要
 *
 * 📦 全部子集 / 子序列（Subsets / Power Set）
 * 78. Subsets（子集）​ — 给定无重复元素数组，返回所有可能的子集（幂集，含空集），也就是"保持原顺序选取的所有子序列"
 * https://leetcode.cn/problems/subsets/
 * 90. Subsets II（子集 II）​ — 数组含重复元素，返回所有不重复的子集 TODO：这里我做错了，题目是子集，与子序列有一点区别：子集不关心顺序。 例如[4,4,1,4,4] 子序列441differ from 144,但是子集中441==144。 做法上除了HashSet收集外，list添加前，执行list.sort(lambda)
 * https://leetcode.cn/problems/subsets-ii/
 *
 * TODO: 【重要】恢复现场 -> 更准确应该叫做 撤销修改（因为做过修改，对全局共享变量（比如具有可变性的全局共享变量 List<Character> path））。 不撤销修改的话，会影响到其他递归节点。
 *  所以，套路直接记，使用回溯时，如果针对某个记录递归沿途信息的变量，同时满足：
 *      （1）可变性的全局变量（List, Set这种，排除String,Integer） （2）进行了add修改操作
 *  此时，既然是回溯递归，那么必然要实现顺序为：！！！  修改 -> 递归(传递修改结果) -> 撤销修改 （有修改，就必须撤销修改）
 *
 * TODO: 注意对比 我的第一个版本(List<Character> path) 和 左神版本(String path)
 *  我的需要恢复现场，左神的不用。  这个核心是因为变量的存储特性：
 *      （1）List 具有可变性: 【可变性，改变后其他（兄弟）递归节点都可见】堆内，所有递归节点共享相同的引用地址（这与下面的String不同）。 所以需要 回溯+ **恢复现场**
 *      （2）String具有不可变性（不对但是可以理解为 【栈帧内复制了一份，所以随便改】）：【隔离性，改变后其他（兄弟）递归节点不可见。】String也在堆内。 但是当 修改String后（e.g. s = s+"123"），String的不可变性会使得 s赋予一个新的内存地址 （指向s+"123"的地址空间）。
 *      上面说的有点玄乎。可以简单理解为 List共享，基础类型Integer/String 隔离。 所以List需要回溯，而基础类型不需要。 当然本质是因为其可变性/不可变性。  不可变性导致修改后，引用地址会发生改变，所以不需要恢复现场（不对但是可以理解为 【栈帧内复制了一份，所以随便改】）。
 *    总结：
 *      （1）参数的设置，可以将 判断标准放在：
 *              a. 不恢复现场的话，兄弟节点会不会受影响。
 *              b. 具体而言，cur搞完孩子节点 -> 回到parent -> 递归到兄弟时，【兄弟看到的 是不是 parent原本要传递过来的值！！！，是不是包含了 cur/cur.child的一些东西？？？？】
 *          举例：  可变性 vs. 不可变性（不可变性可以理解为值传递，也就是复制了一份，所以对其他节点完全不受影响）
 *              1.List<> path 如果不恢复现场list.remove。 回到父节点 -> 递归兄弟节点 =》 兄弟会看到 错误的 List path (！！！【已经不是parent 原来要传过来的值了】)
 *              2.String path: cur给child 选择 要/不要后。 回到父节点 -> 递归兄弟节点， 兄弟节点内的path完全不受影响 ===》》》 ！！！【依然是parent传递过来的值】
 */
public class PrintAllSubsequences {

    public static void subs(String s) {
        char[] chs = s.toCharArray();
        List<String> ans = new ArrayList<>();
        List<Character> path = new ArrayList<>();
        process(0, path, chs, ans);
        System.out.println(ans);
        System.out.println(ans.size());
    }


    public static void process(int i, List<Character> path, char[] chs, List<String> ans) {
        if (i == chs.length) {
            StringBuilder sb = new StringBuilder();
            for (char c : path) {
                sb.append(c);
            }
            ans.add(sb.toString());
            return;
        }
        // 不选
        process(i + 1, path, chs, ans);

        // 选 - 注意 回溯 要恢复现场
        path.add(chs[i]);
        process(i + 1, path, chs, ans);
        // TODO：【错误】犯晕了 ——》 我要移除的是最后一个元素，而i只是对应chs的位置。 不是path的最后位置
        path.remove(path.size() -1);
    }



    public static void subsNoRepeat(String s) {
        char[] chs = s.toCharArray();
        HashSet<String> ans = new HashSet<>();
        List<Character> path = new ArrayList<>();
        process(0, path, chs, ans);
        System.out.println(ans);
        System.out.println(ans.size());
    }


    public static void process(int i, List<Character> path, char[] chs, HashSet<String> ans) {
        if (i == chs.length) {
            StringBuilder sb = new StringBuilder();
            for (char c : path) {
                sb.append(c);
            }
            ans.add(sb.toString());
            return;
        }
        // 不选
        process(i + 1, path, chs, ans);

        // 选 - 注意 回溯 要恢复现场
        path.add(chs[i]);
        process(i + 1, path, chs, ans);
        // TODO：【错误】犯晕了 ——》 我要移除的是最后一个元素，而i只是对应chs的位置。 不是path的最后位置
        path.remove(path.size() -1);
    }

    public static void main(String[] args) {
        String s = "bbcc";
        subs(s);
        subsNoRepeat(s);
    }


}
