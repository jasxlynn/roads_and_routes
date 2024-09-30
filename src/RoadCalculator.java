import java.util.*;

import big.data.DataSource;
/**
 * Builds a graph of nodes and edges by calling the BidData class.
 * Use information from graph to create a minimum spanning tree using Prim's algorithm
 * Uses Djikstra's algorithm to find the shortest path between two nodes
 * @author Jasmine Ngo
 * SB ID: 115632314
 * Homework #7 for CSE 214
 */

public class RoadCalculator {
    private static HashMap<String, Node> graph; // Graph of nodes and edges
    private static LinkedList<Edge> mst; // Minimum spanning tree

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter graph URL");
        String url = input.nextLine();
        buildGraph(url);

        while (true) {
            System.out.print("Enter a starting point for shortest path or Q to quit: ");
            String source = input.nextLine().trim();
            if (source.equalsIgnoreCase("Q")) {
                System.out.println("Goodbye; PSA, there's a cop on the right in 3 miles!");
                break;
            }

            System.out.print("Enter a destination: ");
            String dest = input.nextLine().trim();

            if (!graph.containsKey(source) || !graph.containsKey(dest)) {
                System.out.println("Cannot find the given city.");
                continue;
            }

            int shortestPath = dijkstra(graph, source, dest);
            if (shortestPath == Integer.MAX_VALUE) {
                System.out.println("No path found between " + source + " and " + dest);
            } else {
                System.out.println("Distance: " + shortestPath);
                System.out.println("Path:");
                printShortestPath(graph, source, dest);
            }
        }
    }
    /**
     * A connected graph of Nodes and Edges has been constructed.
     * @param location //Location
     * @return a graph of nodes and edges
     */
    public static HashMap<String, Node> buildGraph(String location) {
        graph = new HashMap<>();

        DataSource ds = DataSource.connectXML("https://www3.cs.stonybrook.edu/~cse214/hw/hw7-files/hw7.xml");
        ds.load();

        String cityNamesStr = ds.fetchString("cities");
        String[] cityNames = cityNamesStr.substring(1, cityNamesStr.length() - 1).replace("\"", "").split(",");
        Arrays.sort(cityNames);

        String roadNamesStr = ds.fetchString("roads");

        String[] roadNames = roadNamesStr.substring(1, roadNamesStr.length() - 1).split("\",\"");
        roadNames[0] = roadNames[0].replaceAll("\"", "");
        roadNames[17] = roadNames[17].replaceAll("\"", "");
        Arrays.sort(roadNames);

        for (String roadName : roadNames) {
            String[] parts = roadName.split(",");
            String cityAName = parts[0].trim();
            String cityBName = parts[1].trim();
            int cost = Integer.parseInt(parts[2].trim());

            Node cityA = graph.computeIfAbsent(cityAName, Node::new);
            Node cityB = graph.computeIfAbsent(cityBName, Node::new);

            Edge edge = new Edge(cityA, cityB, cost);
            cityA.getEdges().add(edge);
            cityB.getEdges().add(edge);
        }

        System.out.println("Cities:");
        for (String cityName : cityNames) {
            System.out.println(cityName);
        }
        System.out.println("Roads:");
        for (String roadName : roadNames) {
            String[] parts = roadName.split(",");
            String cityAName = parts[0].trim();
            String cityBName = parts[1].trim();
            int cost = Integer.parseInt(parts[2].trim());
            System.out.println(cityAName + " to " + cityBName + " " + cost);
        }
        System.out.println("Minimum Spanning Tree:");
        mst = buildMST(graph);
        mst.sort(Comparator.comparing(e -> e.getA().getName()));
        for(Edge e : mst){
            System.out.println(e.getA().getName() + " to " + e.getB().getName() + " " + e.getCost());
        }
        return graph;
    }
    /**
     * @param graph //Graph to build mst from
     * @return A connected Minimum Spanning Tree in the form of a Linked List has been constructed
     */
    public static LinkedList<Edge> buildMST(HashMap<String, Node> graph) {
        HashSet<Node> mstSet = new HashSet<>();
        HashMap<Node, Integer> key = new HashMap<>();
        HashMap<Node, Node> parent = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(key::get));

        for (Node node : graph.values()) {
            key.put(node, Integer.MAX_VALUE);
            parent.put(node, null);
        }

        Node startNode = graph.values().iterator().next();
        key.put(startNode, 0);
        pq.offer(startNode);

        while (!pq.isEmpty()) {
            Node u = pq.poll();
            mstSet.add(u);

            for (Edge edge : u.getEdges()) {
                Node v = (edge.getA() == u) ? edge.getB() : edge.getA();

                if (!mstSet.contains(v) && edge.getCost() < key.get(v)) {
                    key.put(v, edge.getCost());
                    parent.put(v, u);
                    pq.offer(v);
                }
            }
        }

        LinkedList<Edge> mst = new LinkedList<>();
        for (Node node : parent.keySet()) {
            if (parent.get(node) != null) {
                for (Edge edge : node.getEdges()) {
                    if ((edge.getA() == node && edge.getB() == parent.get(node)) ||
                            (edge.getB() == node && edge.getA() == parent.get(node))) {
                        mst.add(edge);
                        break;
                    }
                }
            }
        }
        return mst;
    }
    /**
     * @param graph // Graph to find cheapest cost
     * @param source // First node name
     * @param dest //Final node (destination) name
     * @return The cost of the cheapest path from source to dest.
     */
    public static int dijkstra(HashMap<String, Node> graph, String source, String dest) {
        HashMap<Node, Integer> distances = new HashMap<>();
        HashMap<Node, Boolean> visited = new HashMap<>();
        for (Node node : graph.values()) {
            distances.put(node, Integer.MAX_VALUE);
            visited.put(node, false);
            node.setPath(new LinkedList<>());
        }

        distances.put(graph.get(source), 0);

        for (int i = 0; i < graph.size() - 1; i++) {
            Node u = getMinDistanceNode(distances, visited);

            if (u == null) {
                break;
            }

            visited.put(u, true);

            for (Edge edge : u.getEdges()) {
                Node v = (edge.getA() == u) ? edge.getB() : edge.getA();
                if (!visited.get(v) && distances.get(u) != Integer.MAX_VALUE
                        && distances.get(u) + edge.getCost() < distances.get(v)) {
                    distances.put(v, distances.get(u) + edge.getCost());
                    LinkedList<String> newPath = new LinkedList<>(u.getPath());
                    newPath.add(u.getName());
                    v.setPath(newPath);
                }
            }
        }
        return distances.get(graph.get(dest));
    }

    /**
     * Find node with minimum distance
     * @param distances //Mapping of distances to source node
     * @param visited //A mapping of nodes that have already been visited
     * @return node with minimum distance
     */
    private static Node getMinDistanceNode(HashMap<Node, Integer> distances, HashMap<Node, Boolean> visited) {
        Node minNode = null;
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distances.entrySet()) {
            Node node = entry.getKey();
            int distance = entry.getValue();
            if (!visited.get(node) && distance < minDistance) {
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
    }

    /**
     * Prints the shortest path from the source node to the destination node.
     * @param graph //graph with nodes and edges
     * @param source //source node name
     * @param dest //destination node name
     */
    public static void printShortestPath(HashMap<String, Node> graph, String source, String dest) {
        Node sourceNode = graph.get(source);
        Node destNode = graph.get(dest);

        if (sourceNode != null && destNode != null) {
            LinkedList<String> path = destNode.getPath();
            if (path != null) {
                for (String city : path) {
                    System.out.print(city + ", " );
                }
                System.out.print(destNode.getName());
                System.out.println();
            } else {
                System.out.println("No path found.");
            }
        }
    }

}