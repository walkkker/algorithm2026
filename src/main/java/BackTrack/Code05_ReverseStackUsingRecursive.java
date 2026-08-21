package BackTrack;

import java.util.Stack;

/**
 * Q：如何不申请额外空间的方式，将栈内的元素倒转过来
 * <p>
 * 本质就是使用系统栈。 但是这道题对于理解递归很好：
 * TODO： 只要拆解步骤时 发现能缩小问题规模进行递归 && 拥有base case（直接就能确定答案的最小问题规模就是base case），那么就能使用递归！
 * TODO： 即便像本题，从stack取最底部元素的 递归那么奇怪（pop,递归stack,push 即完成步骤得到地层元素），但是它就是满足了上面的条件，缩小stack规模&&递归下去。 至于拆解步骤中的其他步骤，多呆都不用管。 就是能成功实现函数语义！！！
 * <p>
 * 怎么想的： 递归本质 能缩小递归子问题的问题规模（本题就是栈规模），那么递归就可行！！
 * <p>
 * (1) 直接逆序的话，构造不出来 ——》 a=stack.pop  剩余递归逆序  a的位置不对
 * (2) 所以想 逆序的话 -> 得到最底部元素a， 剩余递归 ，push(a) 可行    ==》 baseCase: stack.size==1 return; 直接满足逆序答案
 * -> 进而那么转为 求 最底部元素a(也可行): stack.pop -> 剩余递归 + 返回底部元素 -> stack.push + 返回底部元素    ==》 baseCase: stack.size==1时， 直接return stack.pop()  -> 完成函数语义：抽出底层元素&&返回该元素
 */
public class Code05_ReverseStackUsingRecursive {

    // 先使用系统栈获取底层元素。  再使用系统栈实现逆序
    // 想不明白没关系：先宏观设计实现 ， 再看base case 怎么写。 TODO： 其实base case很好写： （1）首先看下主体 递归下 最小会到什么地方(比如无子节点还是Null) （2）base case实际上就是 不需要递归了，直接可以获取答案的节点位置
    public static void myReverse(Stack<Integer> stack) {
        if (stack.size() == 1) {    // 此处1 || 0 都可以，已验证
            return;
        }

        // TODO：步骤（围绕减少问题规模，传给递归节点）: 取出底层元素 ——》 剩余递归完成逆序 -》stack.push

        int bottom = g(stack);
        myReverse(stack);   // 这就是递归函数， 判断这个情况下 会到什么样的base case?  其实 stack.size=1 || 0 都可以
        stack.push(bottom);

    }

    // 递归1： 返回栈底层元素 ， 剩余不变
    // TODO: 脑海绘图：其实你脑海想象这个递归图 ，其实就是 使用系统栈 一层一层stack.pop， 到了base case后返回底层元素，然后 系统栈依次完成 stack.push 接住返回值返回parent节点。 最终恢复stack原样，并返回地层元素
    public static int g(Stack<Integer> stack) {
        if (stack.size() == 1) {     // 但是此处必须是1了。 size=1的时候，可以明确 返回 底层的唯一元素
            return stack.pop();
        }

        int a = stack.pop();
        int ans = g(stack);
        stack.push(a);
        return ans;
    }


    public static void reverse(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        int i = f(stack);
        reverse(stack);
        stack.push(i);
    }

    // 栈底元素移除掉
    // 上面的元素盖下来
    // 返回移除掉的栈底元素
    public static int f(Stack<Integer> stack) {
        int result = stack.pop();
        if (stack.isEmpty()) {
            return result;
        } else {
            int last = f(stack);
            stack.push(result);
            return last;
        }
    }

    public static void main(String[] args) {
        Stack<Integer> test = new Stack<Integer>();
        test.push(1);
        test.push(2);
        test.push(3);
        test.push(4);
        test.push(5);
        reverse(test);
        while (!test.isEmpty()) {
            System.out.println(test.pop());
        }

        System.out.println("-------------");

        Stack<Integer> test2 = new Stack<Integer>();
        test2.push(1);
        test2.push(2);
        test2.push(3);
        test2.push(4);
        test2.push(5);
        testVersion(test2);
        while (!test2.isEmpty()) {
            System.out.println(test2.pop());
        }

    }

    public static void testVersion(Stack<Integer> stack) {
        process2(stack);
    }

    // 1. 把底部元素拿到stack top
    public static void process1(Stack<Integer> stack) {
        if (stack.size() <= 1) {
            return;
        }

        Integer top = stack.pop();
        process1(stack);
        Integer bottom = stack.pop();
        stack.push(top);
        stack.push(bottom);
    }

    public static void process2(Stack<Integer> stack) {
        if (stack.size() <= 1) {
            return;
        }
        process1(stack);
        Integer bottom = stack.pop();
        process2(stack);
        stack.push(bottom);
    }

}
