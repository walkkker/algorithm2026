package BackTrack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 46. Permutations（全排列）​ — 给定无重复数字的数组，返回所有可能的全排列
 * https://leetcode.cn/problems/permutations/
 * 47. Permutations II（全排列 II）​ — 数组含重复数字，返回所有不重复的全排列
 * https://leetcode.cn/problems/permutations-ii/
 *
 * 经典回溯 -> swap + char[] -> char[]具有可变性 -> 撤销修改操作
 *
 * Leetcode里面输入是int[]，在去重时涉及到 ：
 *  Step1: 使用HashSet<List<Integer>>在base case时添加排列  -》 这一步实现对所有排列去重
 *  Step2: 最后List<List<Integer>> ans = new ArrayList<>(hashset);  -》 实现将HashSet<List<Integer>> 转换为 List<List<Integer>>
 *
 */
public class PrintAllPermutations {

    public static void perms(String s) {
        char[] chs = s.toCharArray();
        List<String> ans = new ArrayList<>();
        process(0, chs, ans);
        System.out.println(ans);
    }


    public static void process(int i, char[] chs, List<String> ans) {
        if (i == chs.length) {
            StringBuilder sb = new StringBuilder();
            for (char c : chs) {
                sb.append(c);
            }
            ans.add(sb.toString());
            return;
        }

        // 回溯核心 -> 修改了 chs变量 -> 递归完成后，必须撤销修改
        // 因为 char[] chs 全部递归节点可见
        for (int j = i; j < chs.length; j++) {
            swap(chs, i, j);
            process(i + 1, chs, ans);
            swap(chs, i, j);
        }
    }


    public static void swap(char[] chs, int i, int j) {
        char tmp = chs[i];
        chs[i] = chs[j];
        chs[j] = tmp;
    }


    public static void perms2(String s) {
        char[] chs = s.toCharArray();
        HashSet<String> ans = new HashSet<>();
        process2(0, chs, ans);
        System.out.println(ans);
    }


    public static void process2(int i, char[] chs, HashSet<String> ans) {
        if (i == chs.length) {
            StringBuilder sb = new StringBuilder();
            for (char c : chs) {
                sb.append(c);
            }
            ans.add(sb.toString());
            return;
        }

        // 回溯核心 -> 修改了 chs变量 -> 递归完成后，必须撤销修改
        // 因为 char[] chs 全部递归节点可见
        for (int j = i; j < chs.length; j++) {
            swap(chs, i, j);
            process2(i + 1, chs, ans);
            swap(chs, i, j);
        }
    }




    public static void main(String[] args) {
        String s = "acc";
        perms(s);
        perms2(s);
        testPerm(s);
    }

    public static List<String> testPerm(String s) {
        char[] chs = s.toCharArray();
        List<String> ans = new ArrayList<>();
        process1(0, chs, ans);
        System.out.println(ans);
        return ans;
    }

    public static void process1(int i, char[] chs, List<String> ans) {
        if (i == chs.length) {
            ans.add(String.valueOf(chs));
            return;
        }
        for (int k = i; k < chs.length; k++) {
            swap(chs, i, k);
            process1(i + 1, chs, ans);
            swap(chs, i, k);
        }
    }


}
