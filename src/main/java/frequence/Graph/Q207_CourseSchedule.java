package frequence.Graph;
import java.util.*;
/**
 * 207. 课程表
 * TODO: 【拓扑序】 必定与 【图结构】 相挂钩。
 * TODO： 而图结构除了class Node构建节点外，还有三种表示方法：1. 邻接矩阵；2. 邻接表 3. 边集 （其实本题给的就是边集）
 * TODO： 但是对于图的表示方法，除了K算法并查集适合边集外，其他算法 都需要 把【边集】转化为 邻接矩阵/邻接表
 *
 * TODO：【超级重要！！！】除此之外，必须有 inMap这个结构，用来记录每个节点的入度。
 *
 * <p>共有{@code numCourses}门课程，课程编号为{@code 0}到{@code numCourses - 1}。
 * 数组{@code prerequisites}中的每个元素{@code [a, b]}表示学习课程{@code a}之前必须先
 * 完成课程{@code b}。判断是否可以完成所有课程。
 *
 * <p><b>问题模型：</b>课程是有向图的节点，先修关系{@code [a, b]}必须建成
 * {@code b -> a}。如果图中存在有向环，环上的课程互相依赖，无法完成全部课程；因此本题
 * 等价于判断有向图是否可以完成拓扑排序。
 *
 * <p><b>关键数据结构：</b>
 * <ul>
 *     <li>邻接表{@code graph}：{@code graph.get(from)}保存所有由{@code from}指向的后续课程；</li>
 *     <li>入度数组{@code inMap}：{@code inMap[to]}保存课程{@code to}尚未消除的先修依赖数量；</li>
 *     <li>零入度队列{@code zeroQueue}：保存当前已经没有先修依赖、可以直接学习的课程。</li>
 * </ul>
 *
 * <p><b>Kahn BFS拓扑排序：</b>
 * <ol>
 *     <li>根据先修课程对构建邻接表和入度数组；</li>
 *     <li>将所有入度为0的课程加入队列；</li>
 *     <li>依次学习队首课程，并将它指向的所有后续课程入度减1；</li>
 *     <li>某门后续课程的入度降为0时，将其加入队列；</li>
 *     <li>如果最终处理课程数等于{@code numCourses}，说明不存在环，否则存在环。</li>
 * </ol>
 *
 * <p><b>复杂度：</b>设课程数为{@code V}，先修关系数为{@code E}。每个节点入队、出队一次，
 * 每条边只处理一次，时间复杂度为{@code O(V + E)}；邻接表、入度数组和队列的空间复杂度为
 * {@code O(V + E)}。
 *
 * <p><b>易错点：</b>
 * <ul>
 *     <li>{@code [a, b]}表示{@code b -> a}，不要把建图方向写反；</li>
 *     <li>{@code graph.get(i).size()}是节点{@code i}的出度，不能用它寻找入度为0的节点；</li>
 *     <li>最终统计的是完成拓扑排序的节点数量，必须与{@code numCourses}比较，而不是
 *     {@code numCourses - 1}；</li>
 *     <li>课程编号连续，可以使用{@code int[]}维护入度，不需要使用HashMap。</li>
 * </ul>
 */
public class Q207_CourseSchedule {

    // TODO: 【错误版本，正确看下面的版本】
    //  【可以通过本题，因为本题只是检测是否有环，正向图无环则反向图也无环】但是下面代码 建图反了，因为对于 课程对理解反了[a,b]实际上是b->a。
    //   同时两个优化点：1. 因为节点范围确定[0, numCourses-1]，所以可以用int[] inMap 做入度统计。
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 1. 构建邻接表 + inMap
        List<List<Integer>> graph = new ArrayList<>();
        HashMap<Integer, Integer> inMap = new HashMap<>();

        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
            inMap.put(i, 0);
        }

        for (int[] p : prerequisites) {
            // TODO: 【错误点-建图方向反了】题目中的p=[a,b]表示学习a之前必须先完成b，
            // 因此依赖关系应当是b -> a，即from=p[1]、to=p[0]，并增加课程a的入度。
            // 当前代码构建的是反向图a -> b。本题只判断能否完成全部课程，本质是判断有向图
            // 是否存在环；原图与反向图是否有环的结论相同，所以当前代码可能仍然通过Q207。
            // 但这不代表建图语义正确：如果要求返回真实选课顺序，当前拓扑序会完全反向。
            int from = p[0];
            int to = p[1];
            graph.get(from).add(to);
            inMap.put(to, inMap.get(to) + 1);
        }

        // 2. BFS 拓扑序 -> inMap + zeroQueue

        Queue<Integer> zeroQueue = new LinkedList<>();

        // TODO: 下面这段是错误的， 邻接表nexts存的是出度，不是我们要找的入度为0。  入度必须由inMap确定。
//        for (int i = 0; i < numCourses; i++) {
//            if (graph.get(i).size() == 0) {
//                zeroQueue.add(i);
//            }
//        }
        // TODO: 不叫做【map.entries()】 ，应该是 【map.entrySet()】
        // 查找入度为0 的 所有节点。
        for (Map.Entry<Integer, Integer> entry : inMap.entrySet()) {
            int node = entry.getKey();
            int in = entry.getValue();
            if (in == 0) {
                zeroQueue.add(node);
            }
        }

        // zeroQueue开始BFS。 初始化全部入度为0的节点加入，然后每次弹出，把nexts的所有to节点入度-1，如果to的入度为0，则再加入zeroQueue。
        int ans = 0;
        while (!zeroQueue.isEmpty()) {
            int cur = zeroQueue.poll();
            ans++;

            List<Integer> nexts = graph.get(cur);
            for (int i = 0; i < nexts.size(); i++) {
                int to = nexts.get(i);
                inMap.put(to, inMap.get(to) - 1);
                if (inMap.get(to) == 0) {
                    zeroQueue.add(to);
                }
            }
        }

        // TODO: 【错误】你是不是沙雕，ans统计的是所有可以被拓扑序的节点数量。  numCourse是所有的课程数，只是说下标范围对应[0, numCourses - 1]
        //     错误行：return ans == numCourses - 1;
        return ans == numCourses;
    }



    /**
     * 正确版本：邻接表 + 入度数组 + Kahn BFS拓扑排序。
     *
     * <p>建图方向必须服从题意：{@code prerequisites[i] = [a, b]}表示学习课程{@code a}
     * 之前必须先完成课程{@code b}，因此建立有向边{@code b -> a}：
     * <pre>
     * from = p[1];
     * to = p[0];
     * </pre>
     *
     * <p>递归或BFS过程中需要始终维持以下不变量：
     * <pre>
     * inMap[i] = 课程i当前尚未消除的先修依赖数量
     * zeroQueue = 当前所有inMap[i] == 0且尚未处理的课程
     * ans = 已经从zeroQueue弹出并完成拓扑排序的课程数量
     * </pre>
     *
     * <p>如果图中无环，所有课程最终都会进入零入度队列，满足
     * {@code ans == numCourses}；如果图中有环，环中节点的入度无法降为0，队列会提前为空。
     *
     * <p>时间复杂度为{@code O(V + E)}，空间复杂度为{@code O(V + E)}。
     */
    class Solution {
        public boolean canFinish(int numCourses, int[][] prerequisites) {
            // 1. 构建邻接表和入度数组。课程编号连续，所以int[]比HashMap更直接。
            List<List<Integer>> graph = new ArrayList<>();
            // TODO: 【命名建议】当前实现正确；inDegree比inMap更准确，因为这里使用的是数组。
            int[] inMap = new int[numCourses];

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            for (int[] p : prerequisites) {
                // TODO: 【历史错误】曾经把课程对理解反了，错误地构建成p[0] -> p[1]。
                // int from = p[0];
                // int to = p[1];
                // 正确方向：先修课程p[1]指向学完它之后才能学习的课程p[0]。
                int from = p[1];
                int to = p[0];
                graph.get(from).add(to);
                inMap[to]++;
            }

            // 2. 初始化零入度队列。LinkedList可以使用，ArrayDeque通常更适合作为FIFO队列。
            Queue<Integer> zeroQueue = new LinkedList<>();

            // TODO: 下面这段是错误的， 邻接表nexts存的是出度，不是我们要找的入度为0。  入度必须由inMap确定。
            //        for (int i = 0; i < numCourses; i++) {
            //            if (graph.get(i).size() == 0) {
            //                zeroQueue.add(i);
            //            }
            //        }
            // TODO: 【历史语法错误】HashMap遍历应使用map.entrySet()，不存在map.entries()；
            // 当前正确版本已经改成int[]，直接遍历课程编号即可。
            for (int i = 0; i < numCourses; i++) {
                if (inMap[i] == 0) {
                    zeroQueue.add(i);
                }
            }

            // 3. 执行Kahn BFS。每弹出一门课程，就消除它对所有后续课程贡献的一条入边。
            int ans = 0;
            while (!zeroQueue.isEmpty()) {
                int cur = zeroQueue.poll();
                // ans统计已经进入拓扑序的课程数量。
                ans++;

                List<Integer> nexts = graph.get(cur);
                for (int i = 0; i < nexts.size(); i++) {
                    int to = nexts.get(i);
                    // 入度减为0，说明to的全部先修课程均已处理，此时才能入队。
                    if (--inMap[to] == 0) {
                        zeroQueue.add(to);
                    }
                }
            }
            // 无环时所有课程都能进入拓扑序；有环时环中节点始终无法变成零入度节点。
            return ans == numCourses;
        }
    }
}
