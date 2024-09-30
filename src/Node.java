import java.util.HashSet;
import java.util.LinkedList;

/**
 * Node that represents a City that will be a part of our graph of connected cities.
 * Each city has a name, a HashSet containing which cities it is adjacent to (represented by an edge), and a boolean value to represent if the node has been visited by the graph traversal.
 * @author Jasmine Ngo
 * SB ID: 115632314
 * Homework #7 for CSE 214
 */
public class Node {
    private String name; // Name of city
    private HashSet<Edge> edges; // Collection of all the nodes that are adjacent to the current node
    private boolean visited; // Useful in MST and Dijkstra's
    private LinkedList<String> path; // Stores the current shortest path from the starting node to this node
    private int distance; // Stores the length of the current known shortest path from the given node to the starting node in Dijkstra's

    /**
     * Constructor
     * @param name // name of City
     */
    public Node(String name){
        this.edges = new HashSet<>();
        this.name = name;
    }

    /**
     * @return the name of the city of the current node
     */
    public String getName() {
        return name;
    }

    /**
     * @return the HashSet of nodes adjacent to the current node
     */
    public HashSet<Edge> getEdges() {
        return edges;
    }

    /**
     * @return whether a node has been visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @return the shortest path
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     * @return the current known shortest path's length
     */
    public int getDistance() {
        return distance;
    }

    /**
     * @param name // Name of city
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param edges // Nodes adjacent to current node
     */
    public void setEdges(HashSet<Edge> edges) {
        this.edges = edges;
    }

    /**
     * @param visited //if a node has been visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @param path // current shortest path
     */
    public void setPath(LinkedList<String> path) {
        this.path = path;
    }

    /**
     * @param distance the length of the current shortest path
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
