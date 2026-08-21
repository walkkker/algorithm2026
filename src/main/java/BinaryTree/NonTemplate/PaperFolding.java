package BinaryTree.NonTemplate;

/**
 * （1）尝试1 （失败）
 * 做实验，举例子：
 * 凹:0 凸:1
 * 0
 * 1 0 0
 * 1 1 0 0 1 0   好，彻底错了。如果这么看，啥都看不出来
 * <p>
 *
 * （2）尝试2 （成功的尝试） -> 因为既然有次数，你就不要只拿结果看整体only once；也要把每次标记出来，看下 次与次 之间的关系。
 * 在每次对折的地方用笔标记出来F1 F2 F3
 * 发现特别神奇的现象。 用Fn代表第N次的对折
 * -> only 1 count: F1：上凸F2 下凹F2   （order is from top to bottom）
 * -> 2 elements each: F2：上凸F3 下凹F3
 * -> 有理由相信这是一颗 二叉树 =》 node.left:凸  node.right:凹
 * -> 纸张的对折情况就是 **中序遍历**
 * <p>
 * 所以，递归实现， 做一个**中序遍历**就可以。 sout 替换为 list.add? not necessary!!!
 * - 然而！！！看了左神，发现不是的。 你既然可以list.add 按顺序添加就是 paperFolding结果。那么就直接sout呀，一样的！！！
 */
public class PaperFolding {

    // 备注一下：我写的这个，是从上往下看的顺序。  左神那个是从下往上看的顺序。
    // 所以，我俩的根本区别在于 递归时，左右孩子 传的 凹/凸 反过来了。    答案本质没区别。

    // N 从1开始
    public static void printAllFolds(int N) {
        process(1, N, true);   // 当前是第一层，总共N层，1层对应的节点是凹(down = true)
    }


    // level从1开始
    public static void process(int level, int N, boolean down) {
        if (level > N) {
            return;
        }
        // 否则就是中序遍历

        // 上凸下凹  ==> 左子凸 右子凹
        process(level + 1, N, false);
        System.out.print(down ? "凹 " : "凸 ");
        process(level + 1, N, true);
    }

    public static void main(String[] args) {
        printAllFolds(3);
    }

}
