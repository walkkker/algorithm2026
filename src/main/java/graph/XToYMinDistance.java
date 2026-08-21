package graph;

import java.util.*;

/**
 * 城市1~n
 * 随机生成m条道路
 * 每一条路的距离，在1~v之间
 * <p>
 * 写对数器的生成器时候的一些知识：
 * /* 关于HashSet 去重 自定义对象（需要重写equals和hashcode方法） 和 int[] （转成 Arrays.asList()）
 * // 数组：去重失败 -> new int[]{1,2,3}
 * HashSet<int[]> arraySet = new HashSet<>();
 * arraySet.add(new int[]{1, 2, 3});
 * arraySet.add(new int[]{1, 2, 3});
 * System.out.println(arraySet.size()); // 2 - 不同对象，不同哈希
 * <p>
 * // List：去重成功 -> List接口 （Arrays.asList() 或者 ArrayList）
 * HashSet<List<Integer>> listSet = new HashSet<>();
 * listSet.add(Arrays.asList(1, 2, 3));
 * listSet.add(Arrays.asList(1, 2, 3));
 * System.out.println(listSet.size()); // 1 - 内容相同，哈希相同
 **/
public class XToYMinDistance {


    // 转化为邻接表
    public static int minDistance1(int n, int m, int[][] roads, int x, int y) {

        // 提供的是edge list。 dijkstra 需要转换成 邻接表或者邻接矩阵
        List<List<int[]>> graph = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] edge : roads) {
            int from = edge[0], to = edge[1], w = edge[2];
            graph.get(from).add(new int[]{to, w});
            // TODO: 注意题目： 最短距离->dij ， 两点之间画线 -> 无向边 -> 双向边！！！ -> 不管是邻接表还是邻接矩阵，都要 一个edge 转换为 双向边
            graph.get(to).add(new int[]{from, w});
        }

        PriorityQueue<int[]> heap = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        HashMap<Integer, Integer> map = new HashMap<>();
        // start from x
        map.put(x, 0);
        for (int[] edge : graph.get(x)) {
            heap.add(edge);
        }

        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int node = record[0];
            int distance = record[1];

            if (map.containsKey(node)) {
                continue;
            } else {
                map.put(node, distance);
                for (int[] edge : graph.get(node)) {
                    heap.add(new int[]{edge[0], edge[1] + distance});
                }
            }
        }

        return map.get(y) == null ? Integer.MAX_VALUE : map.get(y);
    }

    public static class NodeRecord {
        int node;
        int distance;

        public NodeRecord(int _node, int _distance) {
            node = _node;
            distance = _distance;
        }
    }


    // 转化为邻接矩阵
    // TODO: 本方法写错了，检查也没看出来问题
    public static int minDistance2(int n, int m, int[][] roads, int x, int y) {
        int[][] matrix = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                matrix[i][j] = i == j ? 0 : Integer.MAX_VALUE;
            }
        }

        for (int[] edge : roads) {
            matrix[edge[0]][edge[1]] = edge[2];
            matrix[edge[1]][edge[0]] = edge[2];
        }

        //   邻接矩阵实现
        int[] distances = new int[n + 1];
        boolean[] visited = new boolean[n + 1];

        visited[x] = true;
        for (int j = 1; j <= n; j++) {
            distances[j] = matrix[x][j];
        }

        for (int t = 1; t <= n - 1; t++) {
            int minIndex = getMinIndex(distances, visited);
            if (minIndex == -1) {
                break;
            }

            visited[minIndex] = true;
            for (int j = 1; j <= n; j++) {
                // TODO：下面这两个条件 必须都有，非常重要
                if (!visited[j] && matrix[minIndex][j] != Integer.MAX_VALUE) {
                    distances[j] = Math.min(distances[j], distances[minIndex] + matrix[minIndex][j]);
                }
            }
        }
        return distances[y];
    }


    public static int getMinIndex(int[] distances, boolean[] visited) {
        int minValue = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 1; i < distances.length; i++) {
            if (!visited[i] && distances[i] < minValue) {
                minValue = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }


    // 方法1：暴力递归：得到所有x->y的可能性的pathSum,返回最短的pathSum
    public static int minDistance3(int n, int m, int[][] roads, int x, int y) {
        // 转邻接矩阵
        // 因为邻接矩阵为 n*n，其中n为直接映射的 节点范围。 图中节点范围为1-n, 所以数组应该设置成如下形式int[0-n][0-n]
        int[][] matrix = new int[n + 1][n + 1];

        // 这里要注意，因为给定的 数组中 只记载了 连接线段的信息，所以我们要将邻接矩阵matrix初始化为 Integer.MAX_VALUE,表示无边
        // 从而，没有被 roads 涉及到的边，就自动变成了 系统最大值，表示无边
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n + 1; j++) {
                matrix[i][j] = Integer.MAX_VALUE;
            }
        }
        // 根据 roads 对 matrix 赋值
        for (int[] road : roads) {
            int from = road[0];
            int to = road[1];
            int weight = road[2];
            matrix[from][to] = Math.min(matrix[from][to], weight);
            matrix[to][from] = Math.min(matrix[to][from], weight);
        }
        // 为了防止 x->y 的过程中，y又考虑回到X的路径，所以设置 visited数组，将X置为true表示y->x无边，从而不考虑。不然的话会x-y死循环。
        // visited 表示哪些节点 已经包含，从而后续节点不去计算 下一个节点为这些已包含节点的 数值
        boolean[] visited = new boolean[n + 1];
        // 初始化完毕
        return process(x, y, new HashSet<>(), matrix);
    }

    // Set用来记录路径中的节点- > 回溯 （TODO： 特别重要的本方法，回溯不是 不能有返回值 || 必须在叶子结点收集结果，回溯重要的是恢复现场）
    public static int process(int cur, int y, HashSet<Integer> set, int[][] matrix) {
        if (cur == y) {
            return 0;
        }

//        if (set.contains(cur)) {  // 说明在这一个路径搜索中， cur节点已经来过一次了。 这次来就成环了 =》 所以该节点应该拒绝到达，返回 无边值->Integer.MAX_VALUE
//            return Integer.MAX_VALUE;
//        }
        set.add(cur);
        // 注意我这里用了long，因为后面 不管是 检查(minIndex,j)是否有边，还是后面的process是否能达到y（可能返回MAX_VALUE），情况太多了，if的话太麻烦。直接使用long了
        int minPath = Integer.MAX_VALUE;   // 不管是出于比较min的目的 ，还是表示默认状态下 无边 =》 设置成 Integer.MAX_VALUE都是合理的
        for (int j = 1; j < matrix.length; j++) {
            // 下面这句特别重要，必须是 1)有边 才会进行下面的操作。2) 本次搜索没来过j节点，再去搜索  =》 这是为了 1) 剪枝，肯定有边才递归 2) 回溯战法，避免出现环
            if (matrix[cur][j] != Integer.MAX_VALUE && !set.contains(j)) {
//                set.add(j);   // 防环
                int nextPath = process(j, y, set, matrix); // TODO： 即便上面做了规避，这里process可能返回无边值（Integer.MAX_VALUE）（表示永远到达不了y）
                if (nextPath != Integer.MAX_VALUE) {     // TODO: 这个if也很重要
                    minPath = Math.min(minPath, matrix[cur][j] + nextPath);
                }
//                set.remove(j);  // 回溯 -》 需要恢复现场
            }
        }
        set.remove(cur);
        return minPath;
    }


    // TODO: 重写了生成器，之前的生成器 有可能造成 from,to,d  和 to,from,d 不同，导致 邻接矩阵下有问题
    // 为了测试
    // 城市1~n
    // 随机生成m条道路
    // 每一条路的距离，在1~v之间

    // 因为是 无向边 ——》 所以，最大数量是  (n*(n-1)) / 2 ，这个在 传入的 n,m 上面已经做了值的保证
    public static int[][] randomRoads(int n, int m, int v) {
        HashSet<List<Integer>> set = new HashSet<>();
        while (m-- > 0) {
            // 实现不重复 的 from, to 集合   && 保证 from,to 和 to,from一样
            int from = (int) (Math.random() * n) + 1;
            int to;
            do {
                to = (int) (Math.random() * n) + 1;   // [1, n]
            } while (to == from);

            // 为了保证from,to  to,from 一样 =》 统一成 <小，大>
            int max = Math.max(from, to);
            int min = Math.min(from, to);
            from = min;
            to = max;
//            int distance = (int) (Math.random() * v) + 1;
            List<Integer> list = Arrays.asList(from, to);
            set.add(list);
        }

        int[][] roads = new int[set.size()][3];
        int index = 0;
        for (List<Integer> fromTo : set) {
            int from = fromTo.get(0);
            int to = fromTo.get(1);
            int distance = (int) (Math.random() * v) + 1;   // [1, v]
            roads[index++] = new int[]{from, to, distance};
        }
        return roads;
    }


    // 为了测试
    public static void main(String[] args) {
        // TODO: 城市数量n，下标从1开始，不从0开始
        int n = 4;
        // 边的数量m，m的值不能大于n * (n-1) / 2
        int m = 4;
        // 所的路有m条
        // [a,b,c]表示a和b之间有路，距离为3，根据题意，本题中的边都是无向边
        // 假设有两条路
        // [1,3,7]，这条路是从1到3，距离是7
        // [1,3,4]，这条路是从1到3，距离是4
        // 那么应该忽略[1,3,7]，因为[1,3,4]比它好
        int[][] roads = new int[m][3];
        roads[0] = new int[]{1, 2, 4};
        roads[1] = new int[]{1, 3, 1};
        roads[2] = new int[]{1, 4, 1};
        roads[3] = new int[]{2, 3, 1};
        // 求从x到y的最短距离是多少，x和y应该在[1,n]之间
        int x = 2;
        int y = 4;

        // 暴力方法的解
        System.out.println(minDistance1(n, m, roads, x, y));

        // Dijkstra的解
        System.out.println(minDistance2(n, m, roads, x, y));

        // 解法3
        System.out.println(minDistance3(n, m, roads, x, y));

        // 解法4 20260629
        System.out.println(minDistanceTest(n, m, roads, x, y));

        // 下面开始随机验证
        int cityMaxSize = 15;
        int pathMax = 30;
        int testTimes = 20000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            n = (int) (Math.random() * cityMaxSize) + 1;      // [1, n]
//            m = (int) (Math.random() * n * (n - 1) / 2) + 1;    // m次 正常情况下，无向边数量 -> [1, n * (n - 1) / 2]
            m = (int) (Math.random() * n * (n - 1) / 2);    // 修改为生成m次，最终生成的数组不确定大小，等返回后 再修改m为正确的值
            roads = randomRoads(n, m, pathMax);
            m = roads.length;
            x = (int) (Math.random() * n) + 1;
            y = (int) (Math.random() * n) + 1;
            int ans1 = minDistance1(n, m, roads, x, y);
            int ans2 = minDistance2(n, m, roads, x, y);
            int ans3 = minDistance3(n, m, roads, x, y);
            int ans4 = minDistanceTest(n, m, roads, x, y);
            if (ans1 != ans2 || ans1 != ans3 || ans2 != ans3 || ans1 != ans4) {
                System.out.print(m + " " + x + " " + y + " ");
                System.out.println("ans1:" + ans1 + " : ans2: " + ans2 + " : ans3:" + ans3 + " : ans4:" + ans4);
                System.out.println("出错了！");
                return;
            }
        }
        System.out.println("测试结束");
    }

    public static int minDistanceTest(int n, int m, int[][] roads, int x, int y) {

        // Step1: djkstra需要使用邻接矩阵或者邻接表，所以入参是一个边集（实现不了点解锁边，边解锁点），所以需要将 【边集 转换为 邻接矩阵】
        int[][] matrix = new int[n + 1][n + 1];
        // Step1.1: 这一步初始化，所有各自全部变成 MAX_VALUE表示 没有距离很重要（因为有的题目中 两点距离可以为0，代表有边）
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                matrix[i][j] = Integer.MAX_VALUE;
            }
        }


        // TODO: Step2: 特别重要，无向边集 转化为 邻接矩阵/邻接表，注意 m[i][j]和m[j][i] 都要赋值！！！
        //   无向边本质上就是双向边
        for (int[] road : roads) {
            int from = road[0];
            int to = road[1];
            int w = road[2];
            matrix[from][to] = Math.min(matrix[from][to], w);
            matrix[to][from] = Math.min(matrix[to][from], w);
        }

        // Step3: djkstra + 邻接矩阵 公式 （PriorityQueue<Record> + HashMap）
        //   初始化：解锁点，点解锁边
        //   循环： heap弹出边（解锁边），边解锁点，点扩边  -> 进入下一轮循环（下一轮选边选点循环）
        PriorityQueue<Record> heap = new PriorityQueue<>((a, b) -> a.distance - b.distance);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(x, 0);
        for (int j = 1; j <= n; j++) {
            if (matrix[x][j] != Integer.MAX_VALUE) {
                heap.add(new Record(j, matrix[x][j]));
            }
        }
        while (!heap.isEmpty()) {
            Record record = heap.poll();
            int toNode = record.toNode;
            int distance = record.distance;
            if (!map.containsKey(toNode)) {
                map.put(toNode, distance);
                for (int j = 1; j <= n; j++) {
                    if (matrix[toNode][j] != Integer.MAX_VALUE) {
                        heap.add(new Record(j, distance + matrix[toNode][j]));
                    }
                }
            }
        }
        // TODO: 错误点： 当debug时，发现错误总是发生在m=0时，m为roads.length。 所以可以得出，此时x->y没有路径，
        //  其他方法都返回Integer.MAX_VALUE，而我们设置返回-1，所以错误
        // 修正：只需要修改为没边时，返回Integer.MAX_VALUE即可
//        return map.get(y) == null ? -1 : map.get(y);
        return map.get(y) == null ? Integer.MAX_VALUE : map.get(y);
    }

    public static class Record {
        int toNode;
        int distance;

        public Record(int _toNode, int _distance) {
            toNode = _toNode;
            distance = _distance;
        }
    }

}
