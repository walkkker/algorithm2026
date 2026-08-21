package BinaryTree.RecursiveTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 重点：该题应该归类为 树形DP。 题意都是 要获取当前树在【某种约束条件】下的【最大累加值】了。 明显是个DP问题，又是在树上 -> 树形DP问题
 * 而且属于 多叉树 的 树形DP问题了。
 * <p>
 * 解法1(纯递归) 和 解法2（二叉树的递归套路） 都很值得看。
 */
public class MaxHappy {

    public static class Employee {
        public int happy;
        public List<Employee> nexts;

        public Employee(int h) {
            happy = h;
            nexts = new ArrayList<>();
        }
    }

    // 从当前节点出发，得到的最大happy值
    // 拆分：选择当前节点 / 不选择当前节点
    // TODO: 这是第一次的版本，其实写的逻辑不是很好，看完左神优化一下。
    public static int processOldVersion(Employee employee, boolean isAvailable) {
        if (employee.nexts.size() == 0) {
            return isAvailable ? employee.happy : 0;
        }
        // 选择当前节点
        int p1 = 0;
        if (isAvailable) {
            p1 = employee.happy;
            for (Employee child : employee.nexts) {
                p1 += process(child, false);
            }
        }
        // 不选择当前节点
        int p2 = 0;
        for (Employee next : employee.nexts) {
            p2 += process(next, true);
        }
        return Math.max(p1, p2);
    }

    // TODO: 下面是优化版本，递归函数语义没变：返回当前节点的maxHappy。 只改动代码逻辑
    // 注意，可选不代表一定要选
    public static int process(Employee employee, boolean isAvailable) {
        // 这个base case可以省略，我习惯一定加了
        if (employee.nexts.size() == 0) {
            return isAvailable ? employee.happy : 0;
        }

        // 基于isAvailable决定最大值。
        // 首先依据 isAvailable 判断当前节点是否可选。  分成两段逻辑：
        // if isAvailable==false, 则当前节点不可选
        // if isAvailable==true，则当前节点可选

        if (isAvailable) {   // 如果该节点你能选，那么你have 2 options: 可选可不选，返回 两个选择中最大的
            int p1 = 0;
            int p2 = employee.happy;
            for (Employee next : employee.nexts) {
                p1 += process(next, true);
                p2 += process(next, false);
            }
            return Math.max(p1, p2);
        } else {          // 但如果该节点你不能选，那么you only have 1 option: 不选
            int p1 = 0;
            for (Employee next : employee.nexts) {
                p1 += process(next, true);
            }
            return p1;
        }
    }

    public static int maxHappy1(Employee head) {
        if (head == null) {
            return 0;
        }
        return process(head, true);
    }


    public static class Info {
        int yes;    // 代表 选择这个节点的获得最大值
        int no;     // 代表 不选择这个节点的获得最大值

        public Info(int _yes, int _no) {
            yes = _yes;
            no = _no;
        }
    }

    // 语义：返回当前节点的这棵树的 maxHappy值 （约束下的最大累加和）
    // 拆解问题：当前节点（选/不选） + nexts 孩子们的  累加和
    // 而且你会发现这种多叉树的Node结构， 他本身的逻辑就包含了base case。 因为遍历nexts数组，不会递归传递Null的。
    public static Info process(Employee employee) {
//        if (employee == null) {
//            return new Info(0, 0);
//        }
        // 跟二叉树递归套路一样，直接拿过来
        int yes = employee.happy;
        int no = 0;
        for (Employee next : employee.nexts) {
            Info info = process(next);
            yes += info.no;
            no += Math.max(info.yes, info.no);
        }
        return new Info(yes, no);
    }

    public static int maxHappy2(Employee employee) {
        if (employee == null) {
            return 0;
        }
        Info info = process(employee);
        return Math.max(info.yes, info.no);
    }


    // for test
    public static Employee genarateBoss(int maxLevel, int maxNexts, int maxHappy) {
        if (Math.random() < 0.02) {
            return null;
        }
        Employee boss = new Employee((int) (Math.random() * (maxHappy + 1)));
        genarateNexts(boss, 1, maxLevel, maxNexts, maxHappy);
        return boss;
    }

    // for test
    public static void genarateNexts(Employee e, int level, int maxLevel, int maxNexts, int maxHappy) {
        if (level > maxLevel) {
            return;
        }
        int nextsSize = (int) (Math.random() * (maxNexts + 1));
        for (int i = 0; i < nextsSize; i++) {
            Employee next = new Employee((int) (Math.random() * (maxHappy + 1)));
            e.nexts.add(next);
            genarateNexts(next, level + 1, maxLevel, maxNexts, maxHappy);
        }
    }

    public static void main(String[] args) {
        int maxLevel = 4;
        int maxNexts = 7;
        int maxHappy = 100;
        int testTimes = 100000;
        for (int i = 0; i < testTimes; i++) {
            Employee boss = genarateBoss(maxLevel, maxNexts, maxHappy);
            if (maxHappy1(boss) != maxHappy2(boss)) {
                System.out.println("Oops!");
            }
            if (maxHappy1(boss) != maxHappy(boss)) {
                System.out.println("Oops! maxHappy is wrong");
            }
        }
        System.out.println("finish!");
    }

    public static int maxHappy(Employee root) {
        if (root == null) {
            return 0;
        }
        Info1 info = process1(root);
        return Math.max(info.yes, info.no);
    }

    public static class Info1 {
        int yes;
        int no;

        public Info1(int _yes, int _no) {
            yes = _yes;
            no = _no;
        }
    }

    public static Info1 process1(Employee cur) {
        if (cur == null) {
            return new Info1(0, 0);
        }

        int yes = cur.happy;
        int no = 0;

        for (Employee e : cur.nexts) {
            Info1 info = process1(e);
            yes += info.no;
            no += Math.max(info.no, info.yes);
//            no += info.yes;   // TODO: 注意，这样写是错误的，你无法保证child yes/no 哪个大。。 cur==no，只是给了child的所有选择权（即可以从child.yes 和 child.no 选）
        }

        return new Info1(yes, no);

    }


}
