package org.mb.tedd.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class Graph<T>
{
    private Map<T, Set<T>> graph;

    public Graph(List<T> nodes)
    {
        graph = new HashMap<>();
        for (final T node : nodes) graph.put(node, new HashSet<>());
    }

    public void addEdge(T u, T v)
    {
        graph.get(u).add(v);
    }

    public void removeEdge(T u, T v)
    {
        graph.get(u).remove(v);
    }

    public Set<T> getDependencies(T u)
    {
        final HashSet<T> dep = new HashSet<>();
        final HashSet<T> visited = new HashSet<>();
        final Stack<T> s = new Stack<>();

        s.push(u);

        while (!s.empty()) {
            T v = s.pop();
            for (final T node : graph.get(u)) {
                if (!visited.contains(node)) {
                    s.push(node);
                    dep.add(node);
                } else if (node.equals(u)) dep.add(node);
            }

            visited.add(v);
        }

        return dep;
    }

    public void transitiveReduction()
    {
        Map<T, Set<T>> min_graph = new HashMap<>();
        for (final Map.Entry<T, Set<T>> entry : graph.entrySet()) {
            Set<T> min_edges = new HashSet<>(entry.getValue());

            for (final T v : entry.getValue()) {
                Set<T> deps  = getDependencies(v);
                for (final T u : entry.getValue()) {
                    if (deps.contains(u) && min_edges.contains(u))
                        min_edges.remove(u);
                }
            }
            min_graph.put(entry.getKey(), min_edges);
        }
        graph = min_graph;
    }

    public String toString()
    {
        String res = "digraph G {\n";

        for (final Map.Entry<T, Set<T>> entry : graph.entrySet())
            res += "    " + entry.getKey().toString() + ";\n";

        for (final Map.Entry<T, Set<T>> entry : graph.entrySet())
            for (final T target : entry.getValue())
                res += "    " + entry.getKey().toString() + " -> " + target.toString() + ";\n";

        res += "}\n";

        return res;
    }
}
