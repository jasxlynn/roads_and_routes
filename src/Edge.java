/**
 * An Edge represents an adjacency between two cities in our country.
 * @author Jasmine Ngo
 * SB ID: 115632314
 * Homework #7 for CSE 214
 */
public class Edge implements Comparable<Edge> {
    private Node A;//first node
    private Node B; //second node
    private int cost; //cost between two nodes

    /**
     * Constructor to create an edge between two nodes
     * @param A //first node
     * @param B //second node
     * @param cost //cost between nodes
     */
    public Edge(Node A, Node B, int cost){
        this.A = A;
        this.B = B;
        this.cost = cost;
    }

    /**
     * @return first node
     */
    public Node getA() {
        return A;
    }

    /**
     * @return second node
     */
    public Node getB() {
        return B;
    }

    /**
     * @return cost between two nodes
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets first node
     * @param a
     */
    public void setA(Node a) {
        A = a;
    }

    /**
     * Sets second node
     * @param b
     */
    public void setB(Node b) {
        B = b;
    }

    /**
     * Sets cost
     * @param cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * @return string format of node
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @param edge
     * @return an int based on comparison of edges
     */
    @Override
    public int compareTo(Edge edge) {
        return 0;
    }

}
