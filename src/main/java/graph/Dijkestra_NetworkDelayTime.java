package graph;

import java.util.*;

/**
 * Dijkstra练习题
 * https://leetcode.cn/problems/network-delay-time
 *
 * 其实邻接矩阵对应的方法2 对应的是 dijkstra最初的算法设计。
 *
 *
 * Dijkstra: 接收的是 edgeList
 *
 * 方法1：转化为 邻接表 -> 堆优化  ->
 *      Q: int[] distances vs. HashMap<节点,距离>
 *      A：我觉得HashMap更好。 因为可以基于hashmap 直接 算 map.size()表示当前已经固定/访问过的节点 ，不用单独计数了。
 *
 * 方法2：转化为 邻接矩阵 -> 朴素  ->
 *      Q：int[] distances vs. long[] distances
 *      A：我觉的使用 int[] distances + 检查边是否存在 这个实现更好。 单纯因为更符合 dijkstra最初算法设计，语义上解释更合理。
 *
 *
 * TODO：都有很多坑，要多次重复做。
 */
public class Dijkestra_NetworkDelayTime {
    // 方法一: 边列表 转化为 邻接表 （左神用的List<List<int[]>>, ai用的是 List<int[]>[]）
    // 方法一 : 输入参数：邻接表   =》   普通堆 + 屏蔽已经计算过的点
    class Solution1 {
        public int networkDelayTime(int[][] times, int n, int k) {
            if (n < 1) {
                return 0;
            }

            List<int[]>[] graph = transformToGraphList(times, n);
            PriorityQueue<NodeRecord> heap = new PriorityQueue<>((o1, o2) -> o1.distance - o2.distance);
            HashMap<Integer, Integer> map = new HashMap<>();  // 其实这里你看，Key, Value全部都是Integer了，Key又是连续且有范围的，完全可以使用 int[] distance代替。


            map.put(k, 0);
            for (int[] edge : graph[k]) {
                int to = edge[0], weight = edge[1];
                heap.add(new NodeRecord(to, weight));
            }

            while (!heap.isEmpty() && map.size() < n) {
                NodeRecord cur = heap.poll();
                int to = cur.to, distance = cur.distance;

                if (map.containsKey(to)) {
                    continue;
                } else {
                    map.put(to, distance);
                    for (int[] nextEdge : graph[to]) {
                        int nextTo = nextEdge[0], nextWeight = nextEdge[1];
                        heap.offer(new NodeRecord(nextTo, distance + nextWeight));
                    }
                }
            }

            if (map.size() < n) {
                return -1;
            } else {
                // 从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？ -> 最大值
                int max = Integer.MIN_VALUE;
                for (int time : map.values()) {
                    max = Math.max(max, time);
                }
                return max;
            }
        }

        public class NodeRecord {
            int to;
            int distance;

            public NodeRecord(int _to, int _distance) {
                to = _to;
                distance = _distance;
            }
        }

        public List<int[]>[] transformToGraphList(int[][] times, int n) {
            List<int[]>[] graph = new ArrayList[n + 1];
            for (int i = 1; i <= n; i++) {
                graph[i] = new ArrayList<>();
            }

            // 题目明确写明：是有向边。 因此此处只加一个有向边 到 Graph[from].add([to, edgeWeight])
            for (int[] edge : times) {
                int from = edge[0], to = edge[1], weight = edge[2];
                graph[from].add(new int[]{to, weight});
            }
            return graph;
        }
    }


    // 方法1 改造版 -> HashMap 变成 int[] distance。  distance[i]代表源点到i的最短路径距离
    class Solution1Optimize {
        public int networkDelayTime(int[][] times, int n, int k) {
            if (n < 1) {
                return 0;
            }

            List<int[]>[] graph = transformToGraphList(times, n);
            PriorityQueue<NodeRecord> heap = new PriorityQueue<>((o1, o2) -> o1.distance - o2.distance);
//            HashMap<Integer, Integer> map  = new  HashMap<>();  // 其实这里你看，Key, Value全部都是Integer了，Key又是连续且有范围的，完全可以使用 int[] distance代替。
            int[] minDistances = new int[n + 1];   // 默认都是 1-n的下标范围
            Arrays.fill(minDistances, -1);
            int count = 1;
            minDistances[k] = 0;
            for (int[] edge : graph[k]) {
                int to = edge[0], weight = edge[1];
                heap.add(new NodeRecord(to, weight));
            }

            while (!heap.isEmpty() && count < n) {
                NodeRecord cur = heap.poll();
                int to = cur.to, distance = cur.distance;

                if (minDistances[to] != -1) {
                    continue;
                } else {
                    count++;
                    minDistances[to] = distance;
                    for (int[] nextEdge : graph[to]) {
                        int nextTo = nextEdge[0], nextWeight = nextEdge[1];
                        heap.offer(new NodeRecord(nextTo, distance + nextWeight));
                    }
                }
            }

            if (count < n) {
                return -1;
            } else {
                // 从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？ -> 最大值
                int max = Integer.MIN_VALUE;
                for (int d : minDistances) {
                    max = Math.max(max, d);
                }
                return max;
            }
        }

        public class NodeRecord {
            int to;
            int distance;

            public NodeRecord(int _to, int _distance) {
                to = _to;
                distance = _distance;
            }
        }

        public List<int[]>[] transformToGraphList(int[][] times, int n) {
            List<int[]>[] graph = new ArrayList[n + 1];
            for (int i = 1; i <= n; i++) {
                graph[i] = new ArrayList<>();
            }

            // 题目明确写明：是有向边。 因此此处只加一个有向边 到 Graph[from].add([to, edgeWeight])
            for (int[] edge : times) {
                int from = edge[0], to = edge[1], weight = edge[2];
                graph[from].add(new int[]{to, weight});
            }
            return graph;
        }
    }


    // 方法2：邻接矩阵 =》 不使用堆 + int[] distance + boolean[] visited
    // 感觉还是要统一一下，邻接矩阵的话 叫做m。  邻接表的话叫做 graph。 边列表的话叫做edges。
    // 这个方法使用的是 long[] distances
    class Solution2 {
        public int networkDelayTime(int[][] times, int n, int k) {
            int[][] graph = new int[n + 1][n + 1]; // TODO：节点对应[1,n],所以长度要是n+1
            // TODO: 下面这种写法是错误的, Arrays.fill只能填充一维的数组。 所以要遍历一维，然后给每个子数组 调用 Arrays.fill
            //  Arrays.fill(graph, Integer.MAX_VALUE);
            // 初始化矩阵这一步也很重要，最大值表示无边

            // TODO: 【进一步错误！！！】下面的语法是对的。但是 graph[i][i]=0呀。实践证明，不设置出问题。因为k->k对应distance[k]会锁住，而初始化后distances[k]=Integer.MAX_VALUE。最终distances里记录的结果 k位置是错的 =》 应该是0，而不是MAX_VALUE。罪魁祸首就是你建立m[][]邻接矩阵时，初始化没做对！！！！
            // for (int i = 1; i <= n; i++) {
            //     Arrays.fill(graph[i], Integer.MAX_VALUE);
            // }
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (i == j) {
                        graph[i][j] = 0;
                    } else {
                        graph[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
            for (int[] edge : times) {
                int from = edge[0], to = edge[1], weight = edge[2];
                graph[from][to] = weight;
                // 因为是有向边，所以 graph[to][from]不赋值
            }
            /******   上面就完成了 edgeList->邻接矩阵 的转换   ******/
            // TODO: 【错误】这是重中之重的错误。 当然还有一个办法是在下面的if里加条件，有边才比较
            long[] distances = new long[n + 1];
            boolean[] visited = new boolean[n + 1];
            visited[k] = true;
            for (int j = 1; j <= n; j++) {
                distances[j] = graph[k][j];
            }

            // TODO: 这个执行次数你都写错了！！！
            // for (int t = n - 1; t >= 0; t++) {
            int t = n - 1;
            while (t-- > 0) {
                int minIndex = getMinIndex(distances, visited);

                if (minIndex == -1) {
                    return -1; // 说明不是连通图
                }

                visited[minIndex] = true;
                for (int j = 1; j <= n; j++) {
                    // if (!visited[j] && (distances[minIndex] + graph[minIndex][j]) < distances[j]) {
                    //     distances[j] = distances[minIndex] + graph[minIndex][j];
                    // }
                    if (!visited[j]) {
                        distances[j] = Math.min(distances[j], distances[minIndex] + graph[minIndex][j]);
                    }
                }
            }

            System.out.println(Arrays.toString(distances));
            long max = Integer.MIN_VALUE;
            for (long d : distances) {
                max = Math.max(max, d);
            }
            return (int) max;
        }

        public int getMinIndex(long[] distances, boolean[] visited) {
            long min = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int i = 1; i < distances.length; i++) {
                if (!visited[i] && distances[i] < min) {
                    min = distances[i];
                    minIndex = i;
                }
            }
            return minIndex;
        }
    }



    // 方法2另一个版本：不使用 long[] distances。 用long的原因是 m[i][j]=Integer.MAX_VALUE时，需要参与distances[j]的比较
    // 如果不用long会溢出。    但是还有一个办法，就是比较前检查m[i][j]是否有边，有边才比较(!=MAX_VALUE)，无边不比较。
    // 我是感觉这个版本更 符合算法流程
    class Solution2Optimize {
        public int networkDelayTime(int[][] times, int n, int k) {
            int[][] graph = new int[n + 1][n + 1]; // TODO：节点对应[1,n],所以长度要是n+1
            // TODO: 下面这种写法是错误的, Arrays.fill只能填充一维的数组。 所以要遍历一维，然后给每个子数组 调用 Arrays.fill
            //  Arrays.fill(graph, Integer.MAX_VALUE);
            // 初始化矩阵这一步也很重要，最大值表示无边

            // TODO: 【进一步错误！！！】下面的语法是对的。但是 graph[i][i]=0呀。实践证明，不设置出问题。因为k->k对应distance[k]会锁住，而初始化后distances[k]=Integer.MAX_VALUE。最终distances里记录的结果 k位置是错的 =》 应该是0，而不是MAX_VALUE。罪魁祸首就是你建立m[][]邻接矩阵时，初始化没做对！！！！
            // for (int i = 1; i <= n; i++) {
            //     Arrays.fill(graph[i], Integer.MAX_VALUE);
            // }
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (i == j) {
                        graph[i][j] = 0;
                    } else {
                        graph[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
            for (int[] edge : times) {
                int from = edge[0], to = edge[1], weight = edge[2];
                graph[from][to] = weight;
                // 因为是有向边，所以 graph[to][from]不赋值
            }
            /******   上面就完成了 edgeList->邻接矩阵 的转换   ******/
            // TODO: 【错误】这是重中之重的错误。 当然还有一个办法是在下面的if里加条件，有边才比较
            int[] distances = new int[n + 1];
            boolean[] visited = new boolean[n + 1];
            visited[k] = true;
            for (int j = 1; j <= n; j++) {
                distances[j] = graph[k][j];
            }

            // TODO: 这个执行次数你都写错了！！！
            // for (int t = n - 1; t >= 0; t++) {
            int t = n - 1;
            while (t-- > 0) {
                int minIndex = getMinIndex(distances, visited);

                if (minIndex == -1) {
                    return -1; // 说明不是连通图
                }

                visited[minIndex] = true;
                for (int j = 1; j <= n; j++) {
                    // if (!visited[j] && (distances[minIndex] + graph[minIndex][j]) < distances[j]) {
                    //     distances[j] = distances[minIndex] + graph[minIndex][j];
                    // }
                    // TODO: 方法2优化版本，long[] distances -> int[] distances 优化在这里，m[][]显示有边才进行更新 距离表
                    if (!visited[j] && graph[minIndex][j] != Integer.MAX_VALUE) {
                        distances[j] = Math.min(distances[j], distances[minIndex] + graph[minIndex][j]);
                    }
                }
            }

            System.out.println(Arrays.toString(distances));
            int max = Integer.MIN_VALUE;
            for (int d : distances) {
                max = Math.max(max, d);
            }
            return max;
        }

        public int getMinIndex(int[] distances, boolean[] visited) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int i = 1; i < distances.length; i++) {
                if (!visited[i] && distances[i] < min) {
                    min = distances[i];
                    minIndex = i;
                }
            }
            return minIndex;
        }
    }
}
