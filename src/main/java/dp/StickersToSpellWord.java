package dp;

import java.util.HashMap;

/**
 * TODO：超级多小细节上面的错误！！！需要反复做，直到15min内一次性做对！！！
 *   当前没做对！！！
 *   注意：本题注释的TODO全部特别有价值，一定要看，自己做一遍
 *
 *
 * 即便是这种题，也是可以使用 从左往右的尝试模型！！！ 所以本题很重要。   虽然跑分不高，但是可以用模板。
 * <p>
 * 该题的review:
 * （1）这道题很重要的点在于：告诉你 当变动的参数不全是int时，则记忆化搜索就是最优解。 无法转成dp。本题就是，参数包含了String
 * （1.1） 大技巧，对于key为(int, String)的情况，其中String比如只包含字母。  那么寻找一个int和string都不存在的字符（比如'|'当作分隔符），三者拼接起来 当作Key。 就可以使用HashMap<Key, Value>做记忆化搜索了！！！
 * （2）任何涉及到String的比较，无脑使用 s1.equals(s2)!!!
 * (3) 你可以使用全局变量，但是 【确保】 在主方法里面初始化。   必须避免在不同测试样例之间，全局变量的值有残留，【互相干扰】。 因为leetcode 只new一次Solution，然后遍历所有测试用例。
 *
 * <p>
 * 本题测试链接：https://leetcode.com/problems/stickers-to-spell-word
 */
public class StickersToSpellWord {

    // 这个方法很巧啊，AI教的。 =》  Integer + String 想作为 联合Key
    // 方法1： 自定义一个对象，包含这两个 字段。  然后自己实现 hashCode 和 equals方法
    // TODO： 方法2： 这个很巧妙 =》  拼接成一个字符串 ${int} + "|" + ${string}  =》 因为 左跟右 都没有'|'字符。所以完全可以用它来做分割。 组合之后的字符串绝对能够代表 (int, string)
    //  进一步思考，即便是 (int, int) ， 也可以使用这个组合呀！！！  只要 左跟右 都不包含分隔符，就可以这么组合！！！   作为key，从而只需要一个HashMap<Key, Value>就可以了

    // TODO：【错误】对于leetcode而言，这个map的值会在不同的案例之间持续保留，导致数据错乱！！！
    //  解决方案：你可以在外面写一个 public static变量，这样的话，可以减少方法参数的数量。
    //  续：但是，你必须在 主方法内，初始化！！！   这样才可以清空之前的状态。！！
    public static HashMap<String, Integer> map;

    public int minStickers(String[] stickers, String target) {
        int n = stickers.length;
        map = new HashMap<>();
        int ans = process(0, stickers, target);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public static int process(int i, String[] stickers, String target) {
        // 写什么comp，直接叫key不好吗？？？
        String key = i + "|" + target;
        if (map.containsKey(key)) {
            return map.get(key);
        }

        if (i == stickers.length) {
            return target.equals("") ? 0 : Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        int count = 0;

        // 不要当前
        min = Math.min(min, process(i + 1, stickers, target));


        // 要当前
        while (true) {
            count++;
            Info info = minus(stickers[i], target);
            boolean isChanged = info.isChanged;
            String after = info.s;
            if (isChanged) {
                int tmp = process(i + 1, stickers, after);
                if (tmp != Integer.MAX_VALUE) {
                    min = Math.min(min, count + tmp);
                }
                target = after;
            } else {
                break;
            }
        }
        map.put(key, min);
        return min;
    }


    public static class Info {
        Boolean isChanged;
        String s;

        public Info(Boolean _isAvailable, String _s) {
            isChanged = _isAvailable;
            s = _s;
        }
    }

    // 变化了的话，isChanged=true s为减完的字符串; else isChanged=false s与target 一模一样
    public static Info minus(String s, String target) {
        int[] sCount = new int[26];
        int[] targetCount = new int[26];
        char[] chs1 = s.toCharArray();
        char[] chs2 = target.toCharArray();

        for (char c : chs1) {
            sCount[c - 'a']++;
        }
        for (char c : chs2) {
            targetCount[c - 'a']++;
        }

        boolean isChanged = false;
        for (int i = 0; i < 26; i++) {
            if (sCount[i] != 0 && targetCount[i] != 0) {
//                int tmp = targetCount[i] - sCount[i];
//                targetCount[i] = tmp < 0 ? 0 : tmp;
                // TODO: 【错误】设计都设计好了，结果写的时候漏掉了？  !=0 意味着 target 和 s拥有相同的字母。此时一定会减成功。
                //   【更重要的是】，我们基于 isChanged 判断 i位置的 无限张卡片 什么时候停止。
                isChanged = true;
                targetCount[i] = targetCount[i] < sCount[i] ? 0 : targetCount[i] - sCount[i];
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (targetCount[i] > 0) {
                while (targetCount[i]-- > 0) {
                    // TODO: 【错误】只要涉及到 算数运算法则，就会触发 【类型提升规则】 -> 自动转化为 4字节int类型。
                    //  所以， 下面的写法都是错的，都会存 数字进去， 进而导致后续的数组出现 负数下标 （数字 - 'a' 得到了负数）
                    //   这是最容易错的，也是最开始写的：sb.append('a' + i);   ！！【特别注意】这样 其实 'a'提升为 int类型的数值， 然后 与 i相加，最终得到的 数值存入 StringBuilder
                    //   剩下的错误示例：sb.append('a' + (char) i))  ===> 注意，这样也是不行的， char + char 最终也会得到 int。 因为我说了，本质是 算数运算符。
                    //   所以 =》 解决方案只有 (char) (? + ?)  算出整数后，强转 char ， 这样的话就能够得到 最终的字符了
                    sb.append((char) ('a' + i));
                }
            }
        }
        return new Info(isChanged, sb.toString());
    }


    /***************    这个是不用构建自定义对象的版本，直接minus返回String             ************************/
    // TODO： 注意点：
    //  (1) 将Solution里面所有static去掉，效果一样。 因为测试时调用的是，Solution s = new Solution() -> s.主方法   => 所以你只需要在主方法里面初始化所有变量就可以了（注意，是所有变量，包括全局变量，必须在主方法里面初始化。  不然会在不同测试示例之间共享全局变量值，就『串行』了，就错了！！！）
    //  (2) 【非常重要】字符串比较，无论何时都使用 s1.equals(s2)!!!!
    class Solution2 {
        public HashMap<String, Integer> map;

        public int minStickers(String[] stickers, String target) {
            int n = stickers.length;
            map = new HashMap<>();
            int ans = process(0, stickers, target);
            return ans == Integer.MAX_VALUE ? -1 : ans;
        }

        public int process(int i, String[] stickers, String target) {
            // 写什么comp，直接叫key不好吗？？？
            String key = i + "|" + target;
            if (map.containsKey(key)) {
                return map.get(key);
            }

            if (i == stickers.length) {
                return target.equals("") ? 0 : Integer.MAX_VALUE;
            }

            int min = Integer.MAX_VALUE;
            int count = 0;

            // 不要当前
            min = Math.min(min, process(i + 1, stickers, target));

            // 要当前
            while (true) {
                count++;
                String after = minus(stickers[i], target);

                // TODO：【错误！！！】字符串比较，要使用 equals方法！！！ 不然比较的是内存地址，不一样
                // if (after == target) {
                if (after.equals(target)) {
                    break;
                } else {
                    target = after;
                    int tmp = process(i + 1, stickers, target);
                    if (tmp != Integer.MAX_VALUE) {
                        min = Math.min(min, count + tmp);
                    }
                }
            }
            map.put(key, min);
            return min;
        }

        // 变化了的话，isChanged=true s为减完的字符串; else isChanged=false s与target 一模一样
        public String minus(String s, String target) {
            int[] sCount = new int[26];
            int[] targetCount = new int[26];
            char[] chs1 = s.toCharArray();
            char[] chs2 = target.toCharArray();

            for (char c : chs1) {
                sCount[c - 'a']++;
            }
            for (char c : chs2) {
                targetCount[c - 'a']++;
            }

            boolean isChanged = false;
            for (int i = 0; i < 26; i++) {
                if (sCount[i] != 0 && targetCount[i] != 0) {
                    //                int tmp = targetCount[i] - sCount[i];
                    //                targetCount[i] = tmp < 0 ? 0 : tmp;
                    // TODO: 【错误】设计都设计好了，结果写的时候漏掉了？  !=0 意味着 target 和 s拥有相同的字母。此时一定会减成功。
                    //   【更重要的是】，我们基于 isChanged 判断 i位置的 无限张卡片 什么时候停止。
                    isChanged = true;
                    targetCount[i] = targetCount[i] < sCount[i] ? 0 : targetCount[i] - sCount[i];
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (targetCount[i] > 0) {
                    while (targetCount[i]-- > 0) {
                        // TODO: 只要涉及到 算数运算法则，就会触发 【类型提升规则】 -> 自动转化为 4字节int类型。
                        //  所以， 下面的写法都是错的，都会存 数字进去， 进而导致后续的数组出现 负数下标 （数字 - 'a' 得到了负数）
                        //   这是最容易错的，也是最开始写的：sb.append('a' + i);   ！！【特别注意】这样 其实 'a'提升为 int类型的数值， 然后 与 i相加，最终得到的 数值存入 StringBuilder
                        //   剩下的错误示例：sb.append('a' + (char) i))  ===> 注意，这样也是不行的， char + char 最终也会得到 int。 因为我说了，本质是 算数运算符。
                        //   所以 =》 解决方案只有 (char) (? + ?)  算出整数后，强转 char ， 这样的话就能够得到 最终的字符了
                        sb.append((char) ('a' + i));
                    }
                }
            }
            return sb.toString();
        }
    }


    public static void main(String[] args) {
        String a = "abc";
        String b = "a" + "b" + "c";
        System.out.println(a == b);
    }

    class Solution {
        public int minStickers(String[] stickers, String target) {
            HashMap<String, Integer> map = new HashMap<>();
            return process(0, target, stickers, map);
        }

        public int process(int i, String target, String[] stickers, HashMap<String, Integer> map) {
            String key = i + "|" + target;
            if (map.containsKey(key)) {
                return map.get(key);
            }

            if (i == stickers.length) {
                if (target.equals("")) {
                    return 0;
                } else {
                    return -1;
                }
            }

            int min = process(i + 1, target, stickers, map);
            min = min == -1 ? Integer.MAX_VALUE : min;   // TODO: 【重要错误点】这里特别容易出错，如果yes一个循环也没有，那么min=Integer.MAX_VALUE了！！！ 而不是正常返回无效值-1了。 所以方法最后要收回来return min == Integer.MAX_VALUE ? -1 : min;

            int count = 0;
            String sticker = stickers[i];
            while (true) {
                count++;
                String after = minus(sticker, target);
                if (after.equals(target)) {
                    break;
                } else {

                    // TODO: 【超级错误点】不要省代码！！！ 因为本题存在无效值，所以 你应该先单独判断Process是否有效，然后再 做 count+process()！！！
                    // int yes = count + process(i + 1, after, stickers, map);

                    // if (yes != -1) {
                    //     min = Math.min(min, yes);
                    // }

                    int p1 = process(i + 1, after, stickers, map);
                    if (p1 != -1) {
                        int yes = count + p1;
                        min = Math.min(min, yes);
                    }

                    // TODO: 【错误点】；漏了下面这句，导致after!=target永远，死循环，跑程序 超时
                    target = after;
                }
            }
            // map.put(key, min);
            // return min;   // TODO: 【错误点】此处对应上面 min == -1 ? Integer.MAX_VALUE : min，为了方便写代码而留下的隐患


            // return min == Integer.MAX_VALUE ? -1 : min;
            // TODO: 【错误点】上面这样写是错误的。 既然有了map做记忆化搜索，返回的值要先存map，然后再返回。你这样写了一个三目返回，那么map存储的就是错误的值了！！！！

            // TODO: 只有下面这样写的逻辑才是正确的！！！
            min = min == Integer.MAX_VALUE ? -1 : min;
            map.put(key, min);
            return min;
        }

        public String minus(String sticker, String target) {
            char[] chs1 = sticker.toCharArray();
            char[] chs2 = target.toCharArray();
            int[] chs1Count = new int[26];
            int[] chs2Count = new int[26];
            for (char c : chs1) {
                chs1Count[c - 'a']++;
            }
            for (char c : chs2) {
                chs2Count[c - 'a']++;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 26; i++) {
                if (chs1Count[i] != 0 && chs2Count[i] != 0) {
                    chs2Count[i] = Math.max(0, chs2Count[i] - chs1Count[i]);
                }
                for (int k = 0; k < chs2Count[i]; k++) {
                    sb.append((char) ('a' + i));
                }
            }
            return sb.toString();
        }
    }


}
