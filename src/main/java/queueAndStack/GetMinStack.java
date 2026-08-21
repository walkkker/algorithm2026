package queueAndStack;

import java.util.Stack;

/**
 * 核心思想就是，空间换时间。
 * 多一个最小栈，每次push记录全局最小值
 * https://leetcode.cn/problems/min-stack/description/
 */
public class GetMinStack {

    public static class MinStack {

        Stack<Integer> stackData;
        Stack<Integer> stackMin;

        public MinStack() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(int val) {
            if (stackData.size() == 0) {
                stackData.push(val);
                stackMin.push(val);
            } else {    // >0
                stackData.push(val);
                int min = stackMin.peek();
                stackMin.push(Math.min(val, min));
            }
        }

        public int pop() {
            if (stackData.size() == 0) {
                throw new RuntimeException("The stack is empty!");
            }

            int ans = stackData.pop();
            stackMin.pop();
            return ans;
        }

        public int getMin() {
            if (stackData.size() == 0) {
                throw new RuntimeException("The stack is empty!");
            }
            return stackMin.peek();
        }

        public int top() {
            if (stackData.size() == 0) {
                throw new RuntimeException("The stack is empty!");
            }
            return stackData.peek();
        }


    }

}
