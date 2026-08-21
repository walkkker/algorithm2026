package queueAndStack;

import java.util.Stack;

/*
https://leetcode.cn/problems/implement-queue-using-stacks/
 */
public class TwoStacksImplementQueue {

    public static class MyQueue {

        Stack<Integer> pushStack;
        Stack<Integer> popStack;


        public MyQueue() {
            pushStack = new Stack<>();
            popStack = new Stack<>();
        }

        public void push(int x) {
            pushStack.push(x);
        }

        public int pop() {
            if (empty()) {
                throw new RuntimeException("The queue is empty!");
            }
            pushStackToPopStack();
            return popStack.pop();
        }

        public int peek() {
            if (empty()) {
                throw new RuntimeException("The queue is empty!");
            }
            pushStackToPopStack();
            return popStack.peek();
        }

        public boolean empty() {
            return pushStack.size() == 0 && popStack.size() == 0;
        }

        public void pushStackToPopStack() {
            if (popStack.size() == 0 && pushStack.size() > 0) {
                while (pushStack.size() > 0) {    // 此处也可写成   while (!pushStack.empty()) {
                    popStack.push(pushStack.pop());
                }
            }
        }
    }


}
