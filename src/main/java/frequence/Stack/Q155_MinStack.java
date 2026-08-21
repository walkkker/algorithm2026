package frequence.Stack;

import java.util.Stack;

/**
 * 155. 最小栈
 *
 * <p>设计一个支持{@code push、pop、top}操作，并能在常数时间内检索最小元素的栈。
 *
 * <p>{@code push、pop、top、getMin}操作都必须具有{@code O(1)}时间复杂度。
 *
 * <p><b>核心思路：</b>{@code stack1}保存完整数据，{@code stack2}只保存各阶段的最小值。
 * 新值小于或等于当前最小值时才进入{@code stack2}；弹出值等于当前最小值时，两个栈同步弹出。
 *
 * <p><b>注意：</b>新值与最小值相等时也必须压入辅助栈，否则弹出一个重复最小值后会过早丢失
 * 当前最小值。
 */
public class Q155_MinStack {

    public static class MinStack {

        private final Stack<Integer> stack1;
        private final Stack<Integer> stack2;

        public MinStack() {
            stack1 = new Stack<>();
            stack2 = new Stack<>();
        }

        public void push(int value) {
            stack1.push(value);
            if (stack2.isEmpty()) {
                stack2.push(value);
            } else {
                if (value <= stack2.peek()) {
                    stack2.push(value);
                }
            }
        }

        public void pop() {
            int popValue = stack1.pop();
            if (popValue == stack2.peek()) {
                stack2.pop();
            }
        }

        public int top() {
            return stack1.peek();
        }

        public int getMin() {
            return stack2.peek();
        }
    }
}
