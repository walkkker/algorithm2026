package treemap.leetcodeMine;

public class CountOfRangeSum {
    /**
     * 套路就是：
     * 1）本题属于【数组插入有序表 && 需要包含相同元素】。 所以构造Node(value,index) 允许相同value的元素同时存在有序表中。每一个（value,index）不相同，所以所有元素必包含，且有序。 -> 相同元素排在一起，按照index升序排列
     * <p>
     * 2) lessThanKey()时：实现功能为 <value的节点计算总数。 此时要传入Node(value, -1)  因为-1<任何的index。 本质是要避免value相同时，SBTNode<=传参Node。所以value相同时，Node最小就好了，所以index=-1.
     * <p>
     * 3） 要理解，这个套路下，是可以计算 某一个value值的数量的。 搭配lessThanKey（）
     * lessThanKey(Node(value+1, -1)) - lessThanKey(Node(value, -1))
     * =推出=>  【<=value的数量】 - 【< value的数量】 == 【value的数量】
     */


// TODO: 【错误1】 add方法是递归！！！ 同时必须有put方法 root = add(root, k)!!!
// TODO: 【错误2】preSum 无脑使用 long吧 。
//          特别注意：不能只声明long[]， 对于计算过程时，也要小心不要出现int+int的情况。把每个参数都强转成long
// TODO: 【提示3】本题我改写的方法与左神不一样。 我采取了左神在「滑动窗口中位数」中的套路，一模一样，用来解决存在重复元素的问题。变成Node(value, index)来避免重复 =》 SizeBalancedTree<K>, 自定义一个class Node为K. class Node包含两个属性（value, index），实现Comparable<Node> -> value升序，index升序。
// TODO: 但是对应的，要注意主函数中lessThanKey的传参。 我需要的是完全<value的数量，那么我构造的Node，就需要实现在 value相同的情况下，Node小于有序表的SBTNode。 所以，我不能传Node（value, index）这样会导致value相同，但是我index大，错误包含了。
//      所以，解决方案：构造value相同时，一定小的Node。-> 所以要从index下手，所有的index都>0，所以构造 Node(value, -1)，传入LessThanKey时，只有SBTNode.value<Node.value的节点才会小于，从而计算到数量中。
    class Solution {
        public int countRangeSum(int[] nums, int lower, int upper) {
            int n = nums.length;
            // 先创建前缀和
            long[] preSum = new long[n];
            preSum[0] = nums[0];
            for (int i = 1; i < n; i++) {
                preSum[i] = preSum[i - 1] + nums[i];
            }
            // System.out.println(Arrays.toString(preSum));
            int ans = 0;
            SizeBalancedTreeMap<Node> sbt = new SizeBalancedTreeMap<>();
            for (int i = 0; i < n; i++) {
                long pre = preSum[i];
                if (pre >= lower && pre <= upper) {
                    ans++;
                }
                // 下面这些新边界我也全变成long类型了
                long newLower = pre - upper;
                long newUpper = pre - lower;
                long lessThanLower = sbt.lessThanK(new Node(newLower, -1));
                long lessThanUpper = sbt.lessThanK(new Node(newUpper + 1, -1));
                // System.out.println("--------");
                // System.out.println(newLower + " : " + newUpper);
                // System.out.println(lessThanLower + " : " + lessThanUpper);
                // System.out.println("--------");
                ans += lessThanUpper - lessThanLower;
                // sbt.add(pre);
                sbt.put(new Node(pre, i));
            }
            return ans;
        }

        public class Node implements Comparable<Node> {
            long value;
            int index;

            public Node(long _v, int _i) {
                value = _v;
                index = _i;
            }

            @Override
            public int compareTo(Node o2) {
                if (value != o2.value) {
                    // Line 44: error: incompatible types: possible lossy conversion from long to int [in Node.java]
                    // TODO: 【错误】你要注意，因为value是long类型，但是compare依旧返回需要是int
                    //  return value - o2.value;
                    // TODO： 这么写也是不对的！！！   ==》 return (int) value - o2.value;
                    return value - o2.value < 0 ? -1 : 1;
                    // TODO: 【超级重要】对于Long类型的 compareTo，后续全部改成这样写！ long转int涉及到溢出问题，所以不能这样操作


                } else {
                    return index - o2.index;
                }
            }

        }

        public class SBTNode<K extends Comparable<K>> {
            K k;
            SBTNode<K> l;
            SBTNode<K> r;
            int size;

            public SBTNode(K _k) {
                k = _k;
                size = 1;
            }
        }

        public class SizeBalancedTreeMap<K extends Comparable<K>> {
            SBTNode<K> root;

            public SizeBalancedTreeMap() {
                root = null;
            }

            private SBTNode<K> leftRotate(SBTNode<K> cur) {
                SBTNode<K> right = cur.r;
                cur.r = right.l;
                right.l = cur;
                right.size = cur.size;
                // TODO: 【错误】size忘调整了
                cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
                return right;
            }

            private SBTNode<K> rightRotate(SBTNode<K> cur) {
                SBTNode<K> left = cur.l;
                cur.l = left.r;
                left.r = cur;
                left.size = cur.size;
                cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
                return left;
            }

            private SBTNode<K> maintain(SBTNode<K> cur) {
                if (cur == null) {
                    return null;
                }
                int ls = cur.l != null ? cur.l.size : 0;
                int rs = cur.r != null ? cur.r.size : 0;
                int lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
                int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
                int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
                int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
                if (lls > rs) {
                    cur = rightRotate(cur);
                } else if (lrs > rs) {
                    cur.l = leftRotate(cur.l);
                    cur = rightRotate(cur);
                } else if (rrs > ls) {
                    cur = leftRotate(cur);
                } else if (rls > ls) {
                    cur.r = rightRotate(cur.r);
                    cur = leftRotate(cur);
                } else {
                    return cur;
                }
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
                return cur;
            }

            // TODO: 【错误】NTMD 完全写错了！！！ 这是递归式！！！ 哪是简单的 向左 向右！？
            // public SBTNode<K> add(SBTNode<K> cur, K k) {
            //     if (cur == null) {
            //         return new SBTNode<K>(k);
            //     }
            //     cur.size++;
            //     if (cur.k.compareTo(k) < 0) {
            //         cur = cur.r;
            //     } else {
            //         cur = cur.l;
            //     }
            //     return maintain(cur);
            // }
            public SBTNode<K> add(SBTNode<K> cur, K k) {
                if (cur == null) {
                    return new SBTNode<K>(k);
                }
                cur.size++;
                if (cur.k.compareTo(k) < 0) {
                    cur.r = add(cur.r, k);    // cur.k < K，那么就要去【右子树添加节点（递归右子树）】，同时因为maintain操作可能换头，所以使用cur.r接住新的右子树头部
                } else {
                    cur.l = add(cur.l, k);
                }
                return maintain(cur);   // 最底层加上了节点，从底至上沿着链路 依次maintain！
            }

            public void put(K k) {
                root = add(root, k);
            }

            public int lessThanK(K key) {
                // System.out.println("--------------");
                // System.out.println(root);  // TODO: 【错误】错误的那段注释，使用这个sout检查，就会发现 第二次add之后， sbt的 root==null。 因为root = maintain(null)了。
                if (root == null) {
                    return 0;
                }
                int ans = 0;
                SBTNode<K> cur = root;
                while (cur != null) {
                    if (key.compareTo(cur.k) == 0) {
                        ans += (cur.l == null ? 0 : cur.l.size);
                        break;
                    } else if (key.compareTo(cur.k) > 0) {
                        ans += (cur.l == null ? 0 : cur.l.size) + 1;
                        cur = cur.r;
                    } else {
                        cur = cur.l;
                    }
                }
                return ans;
            }
        }
    }
}
