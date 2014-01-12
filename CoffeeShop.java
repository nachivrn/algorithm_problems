import java.util.*;

/** Find the closest coffee shop given a location in x,y co-ordinates **/

class AStar {
    private TreeSet<Vertex> openSet = new TreeSet<Vertex>(new VertexComparator());
    private Set<Vertex> closedSet = new HashSet<Vertex>();

    List<Integer> aStar(Graph map, int start, int goal) {
        Vertex startVertex = map.getVertex(start);
        Vertex goalVertex = map.getVertex(goal);
        HashMap<Vertex, Vertex> cameFrom = new HashMap<Vertex, Vertex>();

        startVertex.setgScore(0);
        startVertex.setfScore(startVertex.getgScore() + heuristics(startVertex, goalVertex));
        openSet.add(startVertex);

        while (!openSet.isEmpty()) {
            Vertex current = openSet.first();
            if (current.equals(goalVertex)) {
                List<Integer> pathList = new ArrayList<Integer>();
                reConstructPath(cameFrom, goalVertex, pathList);
                return pathList;
            }
            openSet.remove(current);
            closedSet.add(current);
            List<Vertex> neighbors = current.getNeighbors();
            for (Vertex neighbor : neighbors) {
                if (closedSet.contains(neighbor))
                    continue;
                double tentativegScore = current.getgScore() + map.getDistance(current.getName(), neighbor.getName());
                if (!openSet.contains(neighbor) || (tentativegScore < neighbor.getgScore())) {
                    cameFrom.put(neighbor, current);
                    neighbor.setgScore(tentativegScore);
                    neighbor.setfScore(tentativegScore + heuristics(current, neighbor));
                    if (!openSet.contains(neighbor))
                        openSet.add(neighbor);
                }
            }
        }
        throw new RuntimeException("Path cannot be found.");
    }

    private void reConstructPath(HashMap<Vertex, Vertex> cameFrom, Vertex goal, List<Integer> pathList) {
        pathList.add(goal.getName());
        if (cameFrom.containsKey(goal)) {
            reConstructPath(cameFrom, cameFrom.get(goal), pathList);
        }
    }

    private int heuristics(Vertex startVertex, Vertex goalVertex) {
        return 0;
    }


}

class Graph {
    private Vertex[] vertexes;
    private Map<Location, Vertex> locationMap = new HashMap<Location, Vertex>();
    private Map<Edge, Double> edgeWeight = new HashMap<Edge, Double>();

    /**
     * Initializes an empty graph with V vertex.
     *
     * @throws java.lang.IllegalArgumentException
     */
    public Graph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Vertices cannot be negative");
        }
        vertexes = new Vertex[V];
        for (int v = 0; v < V; v++) {
            // Note: The location objects in map are created
            // with same x and y co-ordinate.
            Location loc = new Location(v, v);
            if (v % 5 == 0) {           // Coffee Shop
                vertexes[v] = new Vertex(v, loc, true);
            } else {
                vertexes[v] = new Vertex(v, loc, false);
            }
            locationMap.put(loc, vertexes[v]);
        }
    }

    /**
     * Initializes a random graph with V vertex and E edges.
     *
     * @param V number of vertex
     * @param E number of edges
     * @throws java.lang.IllegalArgumentException
     */
    public Graph(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("Edges cannot be negative");
        }
        for (int e = 0; e < E; e++) {
            int u = (int) (Math.random() * V);
            int v = (int) (Math.random() * V);
            addEdge(u, v);
        }
    }

    /**
     * Adds the undirected edge u-v to this graph.
     *
     * @param u source vertex in the edge
     * @param v destination vertex in the edge
     * @throws java.lang.IndexOutOfBoundsException
     */
    public void addEdge(int u, int v) {
        if (u < 0 || u >= vertexes.length || v < 0 || v >= vertexes.length) {
            throw new IndexOutOfBoundsException();
        }
        vertexes[u].addNeighbors(vertexes[v]);
        double weight = Math.round(100 * Math.random()) / 100.0;
        Edge edge = new Edge(u, v);
        edgeWeight.put(edge, weight);
    }

    /**
     * Returns the number of vertex in this graph.
     *
     * @return the number of vertex in this graph
     */
    public int getNumberOfVertexes() {
        return vertexes.length;
    }

    /**
     * Returns the map of location and vertex
     *
     * @return the map of location and vertex
     */
    public Map<Location, Vertex> getLocationMap() {
        return locationMap;
    }

    public Vertex getVertex(int vertex) {
        return vertexes[vertex];
    }

    public double getDistance(int u, int v) {
        return edgeWeight.get(new Edge(u, v));
    }
}

class Edge {
    private int source;
    private int destination;

    public Edge(int u, int v) {
        this.source = u;
        this.destination = v;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this == null)
            return false;
        if (!(obj instanceof Edge))
            return false;
        final Edge edge = (Edge) obj;
        if ((this.source == edge.source) &&
                this.destination == edge.destination)
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 17 * source;
        hash = hash + 19 * destination;
        return hash;
    }
}

class Vertex {
    /**
     * Represents the locations on map.
     */
    private int name;
    private boolean coffeeShop;
    private Location location;
    private LinkedList<Vertex> neighbors = new LinkedList<Vertex>();
    private double gScore;
    private double fScore;

    public Vertex(int vertex, Location loc, boolean coffeeShop) {
        this.name = vertex;
        this.location = loc;
        this.coffeeShop = coffeeShop;
    }

    public void addNeighbors(Vertex v) {
        if (!neighbors.contains(v))
            neighbors.add(v);
    }

    public boolean isCoffeeShop() {
        return coffeeShop;
    }

    public Location getLocation() {
        return location;
    }

    public List<Vertex> getNeighbors() {
        return neighbors;
    }

    public int getName() {
        return name;
    }

    public double getgScore() {
        return gScore;
    }

    public void setgScore(double score) {
        this.gScore = score;
    }

    public void setfScore(double score) {
        this.fScore = score;
    }

    public double getfScore() {
        return fScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this == null)
            return false;
        if (!(obj instanceof Edge))
            return false;
        final Vertex v = (Vertex) obj;
        if ((this.name == v.name))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 17 * name;
        return hash;
    }
}

class VertexComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        Vertex v1 = (Vertex) o1;
        Vertex v2 = (Vertex) o2;
        return ((Double) v1.getfScore()).compareTo((Double) v2.getfScore());
    }
}

class Location {
    /**
     * x,y co-ordinates of the location object
     */
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this == null)
            return false;
        if (!(obj instanceof Location))
            return false;
        final Location location2 = (Location) obj;
        if ((this.x == location2.x) &&
                this.y == location2.y)
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 17 * x;
        hash = hash + 19 * y;
        return hash;
    }
}

public class CoffeeShop {
    static AStar astar = new AStar();

    public static Location findClosestCoffeeShop(Graph localMap, Location location) {
        Map<Location, Vertex> locationMap = localMap.getLocationMap();
        if (!locationMap.containsKey(location)) {
            throw new RuntimeException("Location not available in map. " +
                    "Check your co-ordinates");
        }
        Vertex v = locationMap.get(location);
        // BFS Algorithm to search for the nearest coffee shop location
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
        boolean[] visited = new boolean[localMap.getNumberOfVertexes()];
        queue.add(v);
        while (!queue.isEmpty()) {
            Vertex temp = queue.poll();
            if (!visited[temp.getName()]) {
                if (temp.isCoffeeShop())
                    return temp.getLocation();
                visited[temp.getName()] = true;
                for (Vertex neighbors : temp.getNeighbors()) {
                    queue.add(neighbors);
                }
            }
        }
        System.out.println((String.format("Coffee shop is not reachable from location (%d,%d)",
                location.getX(), location.getY())));
        return null;
    }

    public static List<Integer> findClosestCoffeeShopPath(Graph localMap,
                                                          Vertex start, Vertex goal) {
        return astar.aStar(localMap, start.getName(),
                goal.getName());
    }

    public static void main(String[] args) {
        Graph localMap = new Graph(50, 110);
        Location givenLoc = new Location(26, 26);
        Location coffeeShopLoc = findClosestCoffeeShop(localMap, givenLoc);
        if (coffeeShopLoc != null) {
            System.out.println("Closest coffee shop for location " +
                    "(" + givenLoc.getX() + "," + givenLoc.getY() + ") is at (" +
                    coffeeShopLoc.getX() + "," + coffeeShopLoc.getY() + ")");
            Vertex goal = localMap.getLocationMap().get(coffeeShopLoc);
            Vertex start = localMap.getLocationMap().get(givenLoc);
            List<Integer> coffeeShopPath = findClosestCoffeeShopPath(localMap,
                    start, goal);
            System.out.println();
            System.out.println("Shortest path in location co-ordinates");
            for (int i = coffeeShopPath.size() -1 ; i >=0; i --) {
                Location loc = localMap.getVertex(coffeeShopPath.get(i)).getLocation();
                System.out.print("(" + loc.getX() + "," + loc.getY() + ") ");
            }
        }
    }

}




