package graph.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 流程如下：
 * 1. class Node, Edge, Graph   (Node 和 Edge 互相依赖对方的类)
 * 2. generateGraph步骤:
 *      a. 生成Node, 加入 graph.nodes
 *      b. 生成Edge(weight, fromNode, toNode), 加入 graph.edges
 *      c. 更新fromNode(.out, .nexts, .edges)  以及 toNode(.in)
 */
public class MyGraph {

    public static class Node {
        int value;
        int in;
        int out;
        List<Node> nexts;
        List<Edge>  edges;

        public Node(int _value) {
            value = _value;
            in = 0;
            out = 0;
            nexts = new ArrayList<>();
            edges = new ArrayList<>();
        }
    }

    public static class Edge {
        int weight;
        Node from;
        Node to;
        public Edge(int _weight, Node _from, Node _to) {
            weight = _weight;
            from = _from;
            to = _to;
        }
    }

    public static class Graph {
        HashMap<Integer, Node> nodes;    // 点集
        HashSet<Edge> edges;     // 边集

        public Graph() {
            nodes = new HashMap<>();
            edges = new HashSet<>();
        }
    }

    // 本次输入为 边集 (weight, from, to)
    public static Graph generateGraph(int[][] edges) {
        Graph graph = new Graph();
        for (int[] e : edges) {
            int w = e[0];
            int from = e[1];
            int to = e[2];

            if (!graph.nodes.containsKey(from)) {
                graph.nodes.put(from, new Node(from));
            }
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node(to));
            }

            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);


            Edge edge = new Edge(w, fromNode, toNode);
            graph.edges.add(edge);

            fromNode.out++;
            fromNode.nexts.add(toNode);
            fromNode.edges.add(edge);
            toNode.in++;
        }
        return graph;
    }


}
