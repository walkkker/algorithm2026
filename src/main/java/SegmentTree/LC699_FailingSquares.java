package SegmentTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;

/**
 * 这是一道好题，必看！！！
 *
 *
 * TODO：本题给了一个提示，可以通过本题的模型去 理解add+max 和 update+max的区别。（add会落到底，而update相当于俄罗斯方块，实际上是直接填充了大矩形
 * TODO: 【超级错误点】你这段线段树主体逻辑 update + max 思路是对的，错在坐标压缩后的 size（压缩坐标系最右侧的位置） 和方块数量（由positions压缩后的数组）不是同一个概念。
 *
 * TODO: 本题需要 线段树update而不是add。 因为目标是max。   你可以想象add，会填满下方空白，而不是俄罗斯方块的样子悬在空中。 因此这种情况下，max会错误。
 *          所以顺序是：
 *              先query(落区间)的高度
 *              newH = 高度 + length
 *              update(落区间，newH)
 *              query(0, N-1) 得到最新的max
 *
 * TODO：除此之外，还有2个点：1. 擦边落下的实现：坐标系下，要看块的位置，而不是点。  2. 稀疏转稠密，因为原题给的变量范围非常大
 * 【很难】
 * 在二维平面上的 x 轴上，放置着一些方块。 TODO：注意题目说到了 第i个，所以[1,xxx]
 * <p>
 * 给你一个二维整数数组 positions ，其中 positions[i] = [lefti, sideLengthi] 表示：第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上坐标点 lefti 对齐。
 * <p>
 * 每个方块都从一个比目前所有的落地方块更高的高度掉落而下。方块沿 y 轴负方向下落，直到着陆到 另一个正方形的顶边 或者是 x 轴上 。一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。一旦着陆，它就会固定在原地，无法移动。
 * <p>
 * 在每个方块掉落后，你必须记录目前所有已经落稳的 方块堆叠的最高高度 。
 * <p>
 * 返回一个整数数组 ans ，其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。
 * <p>
 * TODO: 本题难点， 具体往下看代码开头注释
 *   （1）position范围过大 10^8，需要想办法缩小！
 *      If positions = [[10, 20], [20, 30]], this is the same as [[1, 2], [2, 3]]. Currently, the values of positions are very large. Can you generalize this approach so as to make the values in positions manageable?
 *   （2）算柱子不要算边 -> 因为边与边会直接落下去，而如果l+1,r-1的逻辑也不好，因为宽度为1的时候，l>r更不合理了。
 *      - 所以，最直观的方式就是 算柱子，把柱子当作点(比如 柱子范围为[2,3]，那么以每一个柱子的左边界为柱子的点，所以[2,3]对应[2,2]， [l,r]对应[l,r-1])。 这样的话，计算范围的时候也非常直观，只要new pos的柱子范围内有高度，那么就是基础高度。
 *      - 你会发现，这样转化后([l,r] -> [l,r-1]  x轴范围->将柱子看作是一个点)，柱子与柱子之间 【就避免了边界重合问题】，完美符合线段树的玩法了
 *    (3) 【超核心】回到第一个问题，范围很大，但是 positions.length数量很小，稀疏。 左神方法， 从小到大排序，收缩成连续的。
 *         左神跟我的区别是，直接在index()函数内，就全部转换成最终的柱子序号了。 我是在main主函数里面，先得到原始的坐标范围，再转换成[l, r-1]的。
 */
public class LC699_FailingSquares {


    /**
     * 20260711 做到崩溃
     TODO:  本题给了一个提示，可以通过本题的模型去 理解add+max 和 update+max的区别。（add会落到底，而update相当于俄罗斯方块，实际上是直接填充了大矩形）
     1. 本题需要 update+max，不能add+max =》add方法相当于在区间上加，下面悬空的会补上。 不适合本题。
     续1： 本题用update，相当于 把下面悬空的部分填充了，变成了 大矩形！！！
     2. 如何实现擦到不影响。 那就是把坐标系中的点的位置，转化为块的位置。 就会发现，擦到是不影响 线段树的区间更新的。
     3. 因为positions给到的位置很大，所以需要稀疏转稠密。 =》 就是把所有的位置拎出来，重新排序号就可以了。该重叠的部分还是会重叠。


     */
    class Solution20260711 {
        public List<Integer> fallingSquares(int[][] positions) {
            int[][] arr = index(positions);  // TODO：【超级错误】不能使用arr.length作为SegmentTree的传参，因为这是方块的数量，不是最右的节点。

            int mostRight = 0;
            for (int[] p : arr) {
                mostRight = Math.max(p[1],mostRight);
            }
            int N = mostRight + 1;  // 这才是真正的 坐标系转换为数组的长度

            SegmentTree tree = new SegmentTree(N);
            List<Integer> ans = new ArrayList<>();
            for (int[] p : arr) {
                int l = p[0];
                int r = p[1];
                int len = p[2];   // TODO: 【超级错误点】压缩的时候，不能省略len。 只是压缩了l,r。但是Len不能通过r-l+1求，这是不对的
                int curH = tree.query(l, r);
                tree.update(l, r, curH + len);
                ans.add(tree.query(0, N - 1));
            }

            return ans;
        }

        public class SegmentTree {
            int[] max;
            int[] update;
            boolean[] isChanged;
            int N;

            public SegmentTree(int n) {
                N = n;
                int maxN = N + 1;
                max = new int[maxN * 4];
                update = new int[maxN * 4];
                isChanged = new boolean[maxN * 4];
            }

            private void pushUp(int i) {
                max[i] = Math.max(max[i * 2], max[i * 2 + 1]);
            }

            private void pushDown(int i) {
                if (isChanged[i]) {
                    update[i * 2] = update[i];
                    update[i * 2 + 1] = update[i];
                    isChanged[i * 2] = isChanged[i * 2 + 1] = true;
                    // TODO: 【错误】pushDown 你的 目标数组 更新呀！！！
                    max[i * 2] = max[i * 2 + 1] = update[i];
                    isChanged[i] = false;
                }
            }

            public void update(int i, int l, int r, int L, int R, int val) {
                if (l >= L && r <= R) {
                    update[i] = val;
                    isChanged[i] = true;
                    max[i] = val;
                    return;
                }
                pushDown(i);
                int mid = (l + r) / 2;
                if (L <= mid) {
                    update(i * 2, l , mid, L ,R, val);
                }
                if (R > mid) {
                    update(i * 2 + 1, mid + 1, r, L, R, val);
                }
                pushUp(i);
            }

            public void update(int L, int R, int val) {
                update(1, 1, N, L + 1, R + 1, val);
            }

            public int query(int i, int l, int r, int L, int R) {
                if (l >= L && r <= R) {
                    return max[i];
                }
                pushDown(i);
                int mid = (l + r) / 2;
                int max = Integer.MIN_VALUE;
                if (L <= mid) {
                    max = Math.max(max, query(i * 2, l, mid, L, R));
                }
                if (R > mid) {
                    max = Math.max(max, query(i * 2 + 1, mid + 1, r, L, R));
                }
                return max;
            }

            public int query(int L, int R) {
                return query(1, 1, N, L + 1, R + 1);
            }
        }

        public int[][] index(int[][] positions) {
            TreeSet<Integer> set = new TreeSet<>();
            for (int[] p : positions) {
                int l = p[0];
                int len = p[1];
                int r = l + len - 1;
                set.add(l);
                set.add(r);
            }

            HashMap<Integer, Integer> map = new HashMap<>();
            int count = 0;
            for (Integer num : set) {
                map.put(num, count++);
            }

            int[][] ans = new int[positions.length][3];
            int index = 0;
            for (int[] p : positions) {
                int l = p[0];
                int len = p[1];
                int r = l + len - 1;
                ans[index][0] = map.get(l);
                ans[index][1] = map.get(r);
                ans[index][2] = len;
                index++;
            }
            return ans;
        }
    }






    /**
     本题使用update线段树。构造器依然是传入 int size。 如果默认MAXN为10^8，会太大。题目直接报错-超出内存限制。
     【核心难点-超难！】：pos.length<1000但是范围<10^8，如何化稀疏为稠密？这是本题的核心难点。
     【解决方案】: 把涉及的坐标都拎出来，排序。 使用HashMap映射，最小到最大，依次对应1-N。后续用线段树的时候，先把坐标转换成map value，然后传入线段树。【很神奇，是等效的！！！！】但是这个设计是很合理的。
     【关于线段树】：没改变任何逻辑。构造器传入size。 没有变动。
     【其余难点】方块与方块的边界重合问题需要特别注意，不能简单使用线段树的区间更新和区间查询。因为边界贴缝时，是不叠起来的，但是区间查询会查到。
     【续-解决方案+通用方案-这类问题都可以这么搞呀！！】所以方案很简单，把点的坐标 转换为 矩形柱子的下标 -> 每个柱子左下角的坐标点对应该柱子的序号。这样的话，你用区间查询和区间更新，贴缝时，不会重合。 转换[l,r] -> [l, r-1]。
     【易错点-这类问题都要注意这个错误！】关于position [l位置，矩阵长度]  r的位置不是l+len-1，因为这不是数组，数组中len代表元素的个数，可以这么算。 这里不对，矩阵长度包含的点数是len+1，或者可以看作每个线段的终点对应一个新的坐标点，所以r=l+len。
     */
    class Solution {
        public  class SegmentTree {
            int MAXN;
            int[] max;
            int[] change;
            boolean[] update;

            public SegmentTree(int n) {
                MAXN = n + 1;
                max = new int[MAXN * 4];
                change = new int[MAXN * 4];
                update = new boolean[MAXN * 4];
            }

            public void pushUp(int i) {
                max[i] = Math.max(max[i * 2], max[i * 2 + 1]);
            }

            public void pushDown(int i, int ln, int rn) {
                if (update[i]) {
                    update[2 * i] = true;
                    update[2 * i + 1] = true;
                    change[2 * i] = change[i];
                    change[2 * i + 1] = change[i];
                    max[2 * i] = change[i];
                    max[2 * i + 1] = change[i];
                    update[i] = false;
                }
            }

            public void update(int i, int l, int r, int L, int R, int C) {
                if (l >= L && r <= R) {
                    update[i] = true;
                    change[i] = C;
                    max[i] = C;
                    return;
                }
                int mid = (l + r) / 2;
                // TODO: 懒不住，那就要下下推。 把之前积压的信息 先更新给子节点。然后再让子节点做更新操作（递归函数）
                pushDown(i, mid - l + 1, r - mid);
                if (L <= mid) {
                    update(i * 2, l, mid, L, R, C);
                }
                if (R > mid) {
                    update(i * 2 + 1, mid + 1, r, L, R, C);
                }
                pushUp(i);
            }

            public int query(int i, int l, int r, int L, int R) {
                if (l >= L && r <= R) {
                    return max[i];
                }
                int mid = (l + r) / 2;
                pushDown(i, mid - l + 1, r - mid);
                int max = Integer.MIN_VALUE;
                if (L <= mid) {
                    max = Math.max(max, query(i * 2, l, mid, L, R));
                }
                if (R > mid) {
                    max = Math.max(max, query(i * 2 + 1, mid + 1, r, L, R));
                }
                return max;
            }
        }

        public List<Integer> fallingSquares(int[][] positions) {
            Map<Integer, Integer> map = index(positions);
            int n = map.size();
            SegmentTree tree = new SegmentTree(n);
            int S = 1;
            int N = n;
            List<Integer> ans = new ArrayList<>();
            for (int[] ele : positions) { // [l,r] -> [l,r-1]
                // 直接对应 SegmentTree 的范围
                int L = map.get(ele[0]);
                int R = map.get(ele[0] + ele[1]);
                int currentHeight = tree.query(1, S, N, L, R - 1);
                // System.out.println(L + ":" + R + ":" + currentHeight);
                int newHeight = currentHeight + ele[1];
                tree.update(1, S, N, L, R - 1, newHeight);
                ans.add(tree.query(1, S, N, S, N));
            }
            return ans;
        }

        // TODO：【超难点】稀疏转稠密
        // TODO:【超级错误】而且对于position -> [1,2] 问end在哪里？ 不是这么算的： 1+2-1!!! 因为这个2不是点的长度，我们说的 [s,e] len 对应的数组问题，这个s e是下标，len是包含的下标长度。
        //      而本题[1,2]代表的2矩阵长度，跟数组不是一个逻辑， 你画图便知。 实际上包含的点数是len+1！！！ 所以 end = ele[0] + ele[1](一个边对应一个点)
        public Map<Integer, Integer> index(int[][] positions) {
            TreeSet<Integer> treeSet = new TreeSet<>();
            for (int[] ele : positions) {
                treeSet.add(ele[0]);
                // treeSet.add(ele[0] + ele[1] - 1);
                treeSet.add(ele[0] + ele[1]);
            }
            HashMap<Integer, Integer> map = new HashMap<>();
            int count = 1;
            for (Integer integer : treeSet) {
                map.put(integer, count++);
            }
            return map;
        }
    }

}
