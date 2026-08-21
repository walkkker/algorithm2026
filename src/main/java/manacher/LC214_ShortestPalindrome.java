package manacher;

/**
 * TODO：20260704 再做了一次，好多了 =》 核心思路是求 manacherString的最左/最右侧的回文子串，这样可以直接映射到*原字符串*的最左/最右回文子串。  从而就能够组装*最短回文子串*了
 *  主要思路如下 - class Solution1：
 * TODO: 本题为重要变型题 + manacher一些注意事项
 *  实验方法1 - 验证正确：
 *     1. 转化为manacher string后，可以求出 最左侧和最右侧的最长回文子串，那么可以通过len-1求出原始字符串最左侧或最右侧的最长回文子串（或者长度）。 即解决本题的核心问题
 *
 * TODO: 这是一道难题呀！！！
 *       但是依然好多错误都非常不应该！！！ 思维太混乱了！！！ 相信自己，不要犹豫想太多，想太多代码很容易错在细枝末节！
 *
 *  TODO：  本题 核心是 找到第一个R的范围覆盖最右侧元素，那么这个C就是补全后的中点。
 *         左神那个是补end，这是模板！！！
 *         LC是补前面，一样的。 把字符串逆序，就是左神的版本了。
 * https://leetcode.cn/problems/shortest-palindrome/description/
 */
public class LC214_ShortestPalindrome {

    // TODO: 重要变型题 + manacher一些注意事项
    //  实验方法1 - 验证正确：
    //  1. 转化为manacher string后，可以求出 最左侧和最右侧的最长回文子串，那么可以通过len-1求出原始字符串最左侧或最右侧的最长回文子串（或者长度）。 即解决本题的核心问题
    class Solution1 {
        public String shortestPalindrome(String s) {
            if (s.length() == 0) {
                return "";
            }
            int maxLeftLen = manacherLen(s);
            char[] chs = s.toCharArray();
            char[] ans = new char[chs.length - maxLeftLen];
            for (int i = 0; i < ans.length; i++) {
                ans[i] = chs[chs.length - 1 - i];
            }
            // return ans.toString();
            // TODO: 【错误】ans是char[]，转换成String只能用String.valueOf（）
            //   toString() 适用的是 StringBuilder！！！

            // TODO: 【错误】本题要求 “找到并返回可以用这种方式转换的最短回文串。”，要的是最终满足条件的回文串全量
            // return String.valueOf(ans);

            return String.valueOf(ans) + s;
        }

        public char[] manacherString(String oldStr) {
            char[] oldChs = oldStr.toCharArray();
            char[] newChs = new char[oldChs.length * 2 + 1];
            int index = 0;
            for (int i = 0; i < newChs.length; i++) {
                newChs[i] = (i & 1) == 0 ? '#' : oldChs[index++];
            }
            return newChs;
        }


        // TODO：【特别注意】这是manacher主方法： 1. 应该包含manacherString方法，对外暴露唯一接口是这个方法
        //  2.返回的结果应该对应的是*原字符串*的答案，而不是manacherString的答案。 （也就是说直接得到manacherString的答案后，要转换一下，变成原字符串的答案）
        public int manacherLen(String s) {
            char[] chs = manacherString(s);
            int len = chs.length;
            int[] rArr = new int[len];
            int C = -1;
            int R = 0;
            int maxLeftLen = -1;
            for (int i = 0; i < len; i++) {
                rArr[i] = i < R ? Math.min(R - i, rArr[2 * C - i]) : 1;
                while (i + rArr[i] < len && i - rArr[i] > -1) {
                    if (chs[i - rArr[i]] == chs[i + rArr[i]]) {
                        rArr[i]++;
                    } else {
                        break;
                    }
                }
                if (i + rArr[i] > R) {
                    C = i;
                    R = i + rArr[i];
                }
                if (i - rArr[i] + 1 == 0) {
                    maxLeftLen = Math.max(maxLeftLen, rArr[i]);
                }
            }
            return maxLeftLen - 1;
        }


    }


    class Solution {
        public String shortestPalindrome(String s) {
            // 把s倒过来，然后 寻找第一个覆盖最右边界的C。 此时剩余的左区域就是答案。  （因为最大区域对于正反都是相同大小的， 而因为我们已经反过来了，所以直接取答案就可以）
            char[] chs = s.toCharArray();
            // TODO: 【错误-不要使用关键字做变量！！！】
            // char[] new = new char[chs.length];
            char[] newChs = new char[chs.length];  // 这个变量名叫做 reverseString 也很好！
            int newi = 0;
            // TODO: 【错误】脑子进水了？？？
            // for (int i = chs.length - 1; i >= 0; i++) {
            for (int i = chs.length - 1; i >= 0; i--) {
                newChs[newi++] = chs[i];
            }
            // 【错误】最后要检查题目要求输出！！！
            // return manacher(String.valueOf(newChs));
            String preString = manacher(String.valueOf(newChs));
            return preString + s;
        }

        public char[] manacherString(String s) {
            // TODO：【错误-巨大错误！！！排查了十分钟！！！笨蛋啊！！】错在这里了，你敢信吗？马失前蹄！！！怎么想的！！！ s转换为char[] 然后用newChs变成manacherString呀！！！
            //   char[] chs = new char[s.length()];
            char[] chs = s.toCharArray();
            char[] ans = new char[2 * chs.length + 1];
            for (int i = 0; i < ans.length; i++) {
                if ((i & 1) == 1) {
                    ans[i] = chs[i/2];
                } else {
                    ans[i] = '#';
                }
            }
            return ans;
        }

        public String manacher(String s) {
            char[] chs = manacherString(s);
            int[] pArr = new int[chs.length];
            int C = 0;
            int R = 0;
            int containsEnd = 0;
            for (int i = 0; i < chs.length; i++) {
                pArr[i] = i < R ? Math.min(R - i, pArr[2 * C - i]) : 1;

                while (i + pArr[i] < chs.length && i - pArr[i] >= 0 && chs[i + pArr[i]] == chs[i - pArr[i]]) {
                    pArr[i]++;
                }

                if (i + pArr[i] > R) {
                    R = i + pArr[i];
                    C = i;
                }

                if (i + pArr[i] == chs.length) {
                    containsEnd = i;
                    break;
                }

            }
            int bound = containsEnd - pArr[containsEnd] + 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bound; i++) {
                if ((i & 1) == 1) {
                    sb.append(chs[i]);
                }
            }
            return sb.toString();
        }
    }
}
