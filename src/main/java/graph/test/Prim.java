package graph.test;


import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * 点解锁边，边解锁点
 * <p>
 * solution: PriorityQueue + boolean visited(或者set，核心目的是记录解锁的点)
 */
public class Prim {

    // 我觉得可以统一起来，全部使用 PriorityQueue来实现
    // 那么核心就是 构建 Record(toNode, distance)
    public static class Record {
        int toNode;
        int weight;

        public Record(int _toNode, int _weight) {
            toNode = _toNode;
            weight = _weight;
        }
    }


    public static int prim1(int[][] m) {
        int n = m.length;
        PriorityQueue<Record> heap = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        HashSet<Integer> set = new HashSet<>();
        int ans = 0;
        for (int i = 0; i < n; i++) {    // for循环是为了防森林
            if (!set.contains(i)) {
                set.add(i);
                for (int j = 0; j < n; j++) {
                    if (m[i][j] != Integer.MAX_VALUE) {
                        heap.add(new Record(j, m[i][j]));
                    }
                }

                while (!heap.isEmpty()) {
                    Record record = heap.poll();
                    int to = record.toNode;
                    int weight = record.weight;
                    if (!set.contains(to)) {
                        ans += weight;  // 解锁边
                        set.add(to);    // 解锁点
                        for (int j = 0; j < n; j++) {   // 增加新的候选边
                            if (m[to][j] != Integer.MAX_VALUE) {
                                heap.add(new Record(j, m[to][j]));
                            }
                        }
                    }
                }

            }
        }
        return ans;  // TODO: 别忘了 return ans;
    }

}
