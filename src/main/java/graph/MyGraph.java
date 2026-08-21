package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * (1)标准的图结构 涉及的类： Node, Edge, Graph
 * 以及 (2)从matrix[n][3] (weight, from ,to)结构 生成图的方法 GraphGenerator.createGraph
 */
public class MyGraph {
}

class Node {
    int value;
    int in;
    int out;
    List<Node> nexts;
    List<Edge> edges;

    // TODO: 这里写错了。 虽然只接受_value，但是其他成员变量 也需要初始化。
    public Node(int _value) {
        value = _value;
        // 下面这些也需要补充
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}

class Edge {
    int weight;
    Node from;
    Node to;

    public Edge(int _weight, Node _from, Node _to) {
        weight = _weight;
        from = _from;
        to = _to;
    }
}


class Graph {
    HashMap<Integer, Node> nodes;
    HashSet<Edge> edges;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }
}

/**
 * 功能：用于将任何结构 转化为自己熟悉的图结构。
 * <p>
 * 示例：以下给出一个常见的图表示方法，然后展示如何编写代码转化为MyGraph：
 * <p>
 * 给定一个描述图的边的矩阵。每一行对应一个边。
 * N*3 的矩阵
 * [weight, from节点上面的值，to节点上面的值]  注意：其实这里from,to应该是理解为节点的序号，而不是值。 因为值与node 是一一对应关系，所以值对应的应该是Node的序号。
 * <p>
 * [[ 5 , 0 , 7],
 * <p>
 * [ 3 , 0,  1]]
 */
class GraphGenerator {

    public static Graph createGraph(int[][] edges) {
        Graph graph = new Graph();
        for (int i = 0; i < edges.length; i++) {
            int weight = edges[i][0];
            int from = edges[i][1];
            int to = edges[i][2];

            // S1: 先创建节点
            if (!graph.nodes.containsKey(from)) {
                graph.nodes.put(from , new Node(from));
            }
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node(to));
            }

            // TODO: 检查完是否要创建Node后，此时便可以把fromNode 和 toNode 提取出来
            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);

            // S2: 创建边
            Edge edge = new Edge(weight, fromNode, toNode);
            graph.edges.add(edge);

            // TODO: 【错误-遗漏】Node其他的属性也都要补充上
            fromNode.out++;
            fromNode.nexts.add(toNode);
            fromNode.edges.add(edge);
            toNode.in++;
        }
        return graph;
    }

}
