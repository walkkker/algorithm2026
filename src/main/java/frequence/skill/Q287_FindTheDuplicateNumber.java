package frequence.skill;

/**
 * 287. 寻找重复数
 *
 * <p>给定一个包含{@code N + 1}个整数的数组{@code nums}，其中每个整数都位于
 * {@code [1, N]}内。数组中只有一个重复的整数，返回这个重复数。要求不能修改原数组，
 * 并且只使用常数级额外空间。
 *
 * <p>DONE: 【已独立完成】当前实现已通过技巧章节对数器。状态见
 * {@code frequence/待独立完成题目清单.md}。
 *
 * <p><b>核心前提：</b>数组下标范围是{@code [0, N]}，数组值域是{@code [1, N]}，
 * 因此任何{@code nums[index]}都可以继续作为合法下标访问数组。这允许把数组解释成函数图：
 * <pre>
 * 当前节点：index
 * next引用：nums[index]
 * 映射边：  index -> nums[index]
 * </pre>
 * 下标相当于链表节点的引用；本题不关心节点自身的value，只关心它的next指向哪个下标。
 *
 * <p>TODO: 【条件理解错误】“只有一个重复的整数”表示重复值的种类只有一个，并不保证该值
 * 只出现两次，也不能推出数组一定等于完整的{@code [1, N]}再额外添加一个值。例如
 * {@code [2,2,2,2,2]}同样符合N=4时的题目条件。Floyd方法不依赖“恰好重复两次”。
 *
 * <p><b>为什么环入口就是重复数：</b>不同下标如果存放相同的值，就会共同指向同一个下标。
 * 从0开始沿{@code index -> nums[index]}不断跳转，有限节点必然进入环；产生重复入边的位置
 * 正是重复整数对应的下标，也就是这条路径的环入口。
 *
 * <p><b>Floyd两阶段：</b>
 * <ol>
 *     <li>慢指针每次走一步，快指针每次走两步，先在环内相遇。</li>
 *     <li>一个指针回到下标0，两个指针改为每次走一步，再次相遇的位置就是环入口。</li>
 * </ol>
 *
 * <p>时间复杂度{@code O(N)}，额外空间复杂度{@code O(1)}，并且不会修改原数组。
 */
public class Q287_FindTheDuplicateNumber {

    /**
     本题的前提条件非常特殊：
     1. 数组长度nums: n+1
     2. 数组值域: [1, n]
     3. 只存在一个重复的整数，那么也就是说组成数组的元素是 [1,n] + 其中一个值的重复
     4. 于是乎：值域[1,n]，数组下标范围[0,n]。 => 任何一个值都可以作为数组下标

     TODO：【特别注意】每次遇到这种前提条件，就要注意，任何一个nums[index]都可以作为数组下标。（值域[1,n]，数组下标范围[0,n]）。（核心就是可以 通过值 作为下标 在数组上跳来跳去。）
     - 1. 这个时候就产生了很多不一样的玩法：本题为【数组转化为链表， 下标index对应listNode， nums[index]对应ListNode.next (本质都是 【下标】映射【引用】) 】
     - 2. 【待补充】我记得还有一道题，暂时忘了

     1. O(N) 把数组当成「链表」，用 Floyd 判圈法（快慢指针）找环的入口
     - 映射关系： i -> nums[i]，指针指向 下一个index。  这里可以理解index对应引用，nums[index]对应value，同时 next指针指向nums[index]
     - 数值范围[1, n]， 下标范围[0,n]。 因此每个 nums[i]都能对应一个下标。 从而形成一张链表。
     - 【实现重点-映射到链表-每句都是重点】：【下标对应ListNode引用。】 ListNode.value不关心。 【ListNode.next指向的是 另一个下标， next指向的下标值是nums[index]】。

     2. O(N*logN)第二个方法：二分法的本质是：利用“抽屉原理”，通过统计数量来判断重复数在哪一半。 l和r分别对应值的区间， Mid对应的就是中位数的值。
     */
    class MySolution {
        public int findDuplicate(int[] nums) {
            // 数组看做链表，快慢指针找环 （因为有重复值，所以 两个不同位置具有相同值，对应着指向相同的ListNode,这就是环）
            int slow = nums[0];   // 0.next
            int fast = nums[nums[0]];  // 0.next.next
            // 因为已知有环
            while (slow != fast) {
                slow = nums[slow];
                fast = nums[nums[fast]];
            }

            fast = 0;
            while (slow != fast) {
                slow = nums[slow];
                fast = nums[fast];
            }
            return slow;  // 上面每次都走一步，最后相遇时。 过程对应的就是两个不同的下标，指向相同的下标（nums[index1]==nums[index2]）。  因此相遇的节点（入环点）的下标就是 重复数的值。
        }
    }


    public static class Solution {

        public int findDuplicate(int[] nums) {
            int slow = 0;
            int fast = 0;

            do {
                slow = nums[slow];
                fast = nums[nums[fast]];
            } while (slow != fast);

            int fromStart = 0;
            while (fromStart != slow) {
                fromStart = nums[fromStart];
                slow = nums[slow];
            }
            return fromStart;
        }
    }
}
