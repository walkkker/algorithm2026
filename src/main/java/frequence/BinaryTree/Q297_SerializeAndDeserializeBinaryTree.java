package frequence.BinaryTree;

/**
 * 297. 二叉树的序列化与反序列化
 *
 * <p>设计一种算法，把二叉树转换为字符串，并能根据该字符串恢复出结构和值完全相同的二叉树。
 * 序列化格式可以自行设计，但序列化与反序列化必须严格使用同一套协议。
 *
 * <p><b>标准协议：前序遍历 + 空节点标记。</b>
 * <pre>
 * 非空节点：写入节点值
 * 空节点  ：写入#
 * 分隔符  ：逗号
 * 遍历顺序：根 -> 左子树 -> 右子树
 * </pre>
 *
 * <p>例如：
 * <pre>
 *       1
 *      / \
 *     2   3
 *
 * 序列化结果：1,2,#,#,3,#,#,
 * </pre>
 *
 * <p><b>为什么必须记录空节点：</b>只记录非空节点值会丢失结构。例如“根1的左孩子是2”和
 * “根1的右孩子是2”的非空前序结果都是{@code 1,2}。写入{@code #}以后，{@code #}既表示
 * 对应孩子不存在，也是反序列化递归返回的结构边界。
 *
 * <p><b>反序列化递归契约：</b>{@code build(tokens)}从当前index开始消费一棵完整子树的编码，
 * 返回新建子树的根节点，并把index推进到下一段尚未消费的位置：
 * <ol>
 *     <li>读到{@code #}，当前子树为空，返回{@code null}。</li>
 *     <li>否则创建当前根节点。</li>
 *     <li>按照序列化协议，依次构造左子树和右子树。</li>
 * </ol>
 *
 * <p>本题不属于底向上Info汇总。树在反序列化开始时尚不存在，算法根据遍历协议自顶向下创建
 * 节点并返回新子树根。它与Q105、Q108同属“建树”，区别是本题通过空节点标记确定递归边界。
 *
 * <p>序列化和反序列化时间复杂度均为{@code O(N)}；递归栈空间为{@code O(H)}；序列化结果
 * 本身占用{@code O(N)}空间，其中N为节点数、H为树高。
 */
public class Q297_SerializeAndDeserializeBinaryTree {

    public static class Codec {
        private static final String NULL = "#";
        private static final String SEPARATOR = ",";

        private int index;

        /**
         * 按照“根、左、右”顺序写入整棵树，并显式记录每个空孩子。
         */
        public String serialize(TreeNode root) {
            StringBuilder builder = new StringBuilder();
            serialize(root, builder);
            return builder.toString();
        }

        private void serialize(TreeNode cur, StringBuilder builder) {
            if (cur == null) {
                builder.append(NULL).append(SEPARATOR);
                return; // TODO: 收集空节点标记后必须返回，否则会继续访问cur.val并触发NPE。
            }

            builder.append(cur.val).append(SEPARATOR);
            serialize(cur.left, builder);
            serialize(cur.right, builder);
        }

        /**
         * 按照与serialize完全相同的前序协议消费token并重建二叉树。
         */
        public TreeNode deserialize(String data) {
            String[] tokens = data.split(SEPARATOR);
            index = 0; // 同一个Codec对象可能被重复调用，每次反序列化都必须重新开始。
            return build(tokens);
        }

        /**
         * 从tokens[index]开始重建一棵完整子树，返回新子树根节点。
         */
        private TreeNode build(String[] tokens) {
            String token = tokens[index++];
            if (NULL.equals(token)) {
                return null;
            }

            TreeNode cur = new TreeNode(Integer.parseInt(token));
            cur.left = build(tokens);
            cur.right = build(tokens);
            return cur;
        }
    }
}
