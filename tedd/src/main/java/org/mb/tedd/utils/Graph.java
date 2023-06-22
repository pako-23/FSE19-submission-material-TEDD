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
}
