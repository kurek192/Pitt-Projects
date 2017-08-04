
/**
 * WILLIAM KUREK
 * CS 1501
 * SUMMER 2017
 * ASSIGNMENT 5
 *
 * Graph utilizes modified versions of the authors code including EdgeWeightedDigraph, DijkstraSP, DirectedEdge,
 * IndexMinPQ, PrimMSTTrace, and DFS.
 * For additional documentation, see
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

import java.util.*;

public class Graph {

    private ArrayList<DirectedEdge>[] adj;
    private ArrayList<Node> vertex;
    private ArrayList<Integer> components;
    private ArrayList<Integer> path;     // the current path

    private int V, compNum, vertices, numberOfPaths, count;
    private int[] DFScomponents; 
    // components[v] = components of connected component containing v
    private double[] distTo, MSTdistTo;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo, MSTedgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private boolean[] marked, MSTmarked, onPath;     // marked[v] = true if v on tree, false otherwise
    
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    /**
     * Create an empty edge-weighted digraph with V vertices.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Graph(int V) {
        if (V < 0) {
            throw new RuntimeException("Number of vertices must be nonnegative");
        }
        vertices = V;
        this.V = V;

        adj = (ArrayList<DirectedEdge>[]) new ArrayList[V];
        vertex = new ArrayList<Node>();

        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<DirectedEdge>();
            vertex.add(new Node(v));
        }
    }

    /**
     * Return the number of vertices in this digraph.
     */
    public int V() {
        return V;
    }

    public Node getVertex(int index) {

        return vertex.get(index);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addVertex() {

        this.V = V + 1;

        ArrayList<DirectedEdge>[] temp = (ArrayList<DirectedEdge>[]) new ArrayList[V];
        System.arraycopy(adj, 0, temp, 0, adj.length);
        temp[V - 1] = new ArrayList<DirectedEdge>();

        vertex.add(new Node(V - 1));
        adj = temp;

    }

    /**
     * Add the edge e to this digraph.
     */
    public void addEdge(int v1, int v2, double w) {
        DirectedEdge e = new DirectedEdge(v1, v2, w);
        int v = e.from();
        adj[v].add(e);
    }

    public void addEdge(DirectedEdge e) {
        int v = e.from();
        adj[v].add(e);
    }

    // remove edge from digraph
    public void removeEdge(DirectedEdge e) {

        int v = e.from();
        adj[v].remove(e);

    }

    // changes the given Edges weight
    public void changeEdgeWeight(DirectedEdge oldEdge, double newWeight) {

        oldEdge.changeWeight(newWeight);

    }

    // checks for existance of an edge in the this
    public boolean hasEdge(DirectedEdge e) {

        for (int v = 0; v < V; v++) {
            for (DirectedEdge match : adj(v)) {

                if (match.to() == e.to() && match.from() == e.from()) {
                    return true;
                }

            }
        }

        return false;

    }

    // gets edge that matches to and from
    public DirectedEdge getEdge(DirectedEdge e) {

        for (int v = 0; v < V; v++) {
            for (DirectedEdge match : adj(v)) {
                if (match.to() == e.to() && match.from() == e.from()) {
                    return match;
                }
            }
        }

        return null;

    }

    public DirectedEdge getEdgeBi(DirectedEdge e) {
        for (int v = 0; v < V; v++) {
            for (DirectedEdge comp : adj(v)) {

                if (comp.to() == e.from() && comp.from() == e.to()) {
                    return comp;
                }
            }
        }
        return null;
    }

    // returns up vertices
    public Iterable<Node> getStatus() {
        return vertex;
    }

    /**
     * Return the edges leaving vertex v as an Iterable. To iterate over the
     * edges leaving vertex v, use foreach notation:
     * <tt>for (DirectedEdge e : this.adj(v))</tt>.
     */
    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }

    /**
     * Return all edges in this this as an Iterable. To iterate over the edges,
     * use foreach notation:
     * <tt>for (DirectedEdge e : this.edges())</tt>.
     */
    public Iterable<DirectedEdge> edges() {
        ArrayList<DirectedEdge> list = new ArrayList<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    //LIST() 
    //return the adjacency list of v
    public ArrayList<DirectedEdge> getList(int v) {
        return adj[v];
    }

    ///////////////////////////////////////////////////////////
    /////////GRAPH FUNCTINALITY METHODS BELOW//////////////////
    ///////////////////////////////////////////////////////////
    //TOSTRING()
    //Return a string representation of this graph and components.
    public String displayGraph(int compNum) {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();

        int prev = 0;
        boolean comps = false, isComp = true, isNew = true;

        System.out.print("Component 0: ");
        for (int i = 0; i < compNum + 1; i++) {
            for (int v = 0; v < V; v++) {
                if (this.getVertex(v).getComponent() == i) {
                    for (DirectedEdge e : adj[v]) {
                        if (e.getStatus()) {
                            if (comps) {
                                if (this.getVertex(v).getComponent() != this.getVertex(prev).getComponent()) {
                                    isComp = false;
                                }
                            }
                            if (!isComp) {
                                s.append("Component ").append(this.getVertex(e.from()).getComponent()).append(": \n");
                                isComp = true;
                            }
                            if (isNew) {
                                if (this.getVertex(v).getStatus()) {
                                    s.append("   " + v + ": ");
                                    isNew = false;
                                }
                            }
                            prev = v;
                            s.append(e + "  ");
                        }
                    }
                    isNew = true;
                    comps = true;
                    if (this.getVertex(v).getStatus()) {
                        s.append(NEWLINE);
                    }
                }
            }
        }
        return s.toString();
    }

    //GETREPORT()
    //display the current active network (all active nodes and edges, including edge weights);
    //show the status of the network (connected or not); show the connected components of the network
    public void getReport() {
        System.out.println();
        getComponents();
        // check this connection status
        if (compNum >= 1) {
            System.out.println("The network is currently disconnected!\n");
        } else {
            System.out.println("The network is currently connected!\n");
        }

        // check nodes up
        System.out.println("Following nodes are currently up:\n");

        for (Node v : this.getStatus()) {

            if (v.getStatus()) {
                System.out.print(v.toString() + " ");
            }
        }

        System.out.println("\n");

        // check nodes down
        System.out.println("Following nodes are currently down:\n");
        for (Node v : this.getStatus()) {
            if (!v.getStatus()) {
                System.out.print(v.toString() + " ");
            }
        }

        System.out.println("\n");
        System.out.println("The connected components are:");
        System.out.println("\n" + displayGraph(compNum) + "\n");

    }

    //GETCOMPONENTS()
    //this method is neccessary to seperate the graph into subgraphs   
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getComponents() {
        // display current network
        //System.out.println("Connected Components:\n");
        DFS(this);

        int m = count();

        // compute list of vertices in each connected component
        ArrayList<Integer>[] comps = (ArrayList<Integer>[]) new ArrayList[m];
        for (int i = 0; i < m; i++) {
            comps[i] = new ArrayList<Integer>();
        }
        for (int v = 0; v < V; v++) {
            comps[components(v)].add(v);
        }

        boolean isComp = true;
        compNum = -1;

        // print results
        for (int i = 0; i < m; i++) {
            for (int v : comps[i]) {
                if (this.getVertex(v).getStatus()) {
                    if (isComp) {
                        compNum++;
                        this.getVertex(v).setComponent(compNum);
                        isComp = false;
                    } else {
                        this.getVertex(v).setComponent(compNum);
                    }
                }
            }

            isComp = true;
        }

        components = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            components.add(this.getVertex(i).getComponent());
        }

    }

    //MST()
    //show the vertices and edges (with weights) in the current minimum
    //spanning tree of the network. If the graph is not connected this command should show the minimum
    //spanning tree of each connected subgraph. 
    public void MST() {

        // check for subgraph's
        // use PrimMSTTrace Eager
        PrimMSTTrace();

        getComponents();
        System.out.println("The edges in the MST follow: \n");
        DirectedEdge prev = null;
        boolean comps = false;

        System.out.println("Component 0:\n ");
        for (int i = 0; i < compNum + 1; i++) {
            for (DirectedEdge e : MSTedges()) {
                if (this.getVertex(e.from()).getComponent() == i) {
                    if (comps) {
                        if (this.getVertex(e.from()).getComponent() != this.getVertex(prev.from()).getComponent()) {
                            System.out.println("Component " + this.getVertex(e.from()).getComponent() + ": \n");

                        }
                    }

                    System.out.println("" + e + "\n");

                    comps = true;
                    prev = e;
                }
            }
        }
        // mst for this/subgraph's + display
    }

    public void PrimMSTTrace() {
        MSTedgeTo = new DirectedEdge[V];
        MSTdistTo = new double[V];
        MSTmarked = new boolean[V];
        pq = new IndexMinPQ<Double>(V);
        for (int v = 0; v < V; v++) {
            MSTdistTo[v] = Double.POSITIVE_INFINITY;
        }

        for (int v = 0; v < V; v++) // run from each vertex to find
        {
            if (!MSTmarked[v]) {
                prim(v);      // minimum spanning forest
            }
        }

    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(int s) {
        MSTdistTo[s] = 0.0;
        pq.insert(s, MSTdistTo[s]);
        //showPQ(pq);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            //System.out.println("	Next Vertex (Weight): " + v + " (" + MSTdistTo[v] + ")");
            scan(v);
            //showPQ(pq);
        }
    }

    // scan vertex v
    private void scan(int v) {
        MSTmarked[v] = true;
        //System.out.println("	Checking neighbors of " + v);
        for (DirectedEdge e : adj(v)) {
            int w = e.other(v);
            //System.out.print("		Neighbor " + w);
            if (MSTmarked[w] || !e.getStatus() || !getVertex(w).getStatus()) {
                //System.out.println(" is in the tree ");
                continue;         // v-w is obsolete edge
            }
            if (e.weight() < MSTdistTo[w]) {
                //System.out.print(" OLD distance: " + MSTdistTo[w]);
                MSTdistTo[w] = e.weight();
                MSTedgeTo[w] = e;
                //System.out.println(" NEW distance: " + MSTdistTo[w]);
                if (pq.contains(w)) {
                    pq.change(w, MSTdistTo[w]);
                    //System.out.println("			PQ changed");
                } else {
                    pq.insert(w, MSTdistTo[w]);
                    //System.out.println("			Inserted into PQ");
                }
            } else {
                //System.out.println(" distance " + MSTdistTo[w] + " NOT CHANGED");
            }
        }
    }

    // return iterator of edges in MST
    public Iterable<DirectedEdge> MSTedges() {
        ArrayList<DirectedEdge> mst = new ArrayList<DirectedEdge>();
        for (DirectedEdge e : MSTedgeTo) {
            if (e != null) { // --> [change here maybe needed]
                mst.add(e);
            }
        }
        return mst;
    }

    //GETSHORTESTPATH()
    //display the shortest path (by latency) from vertex i to vertex j in the graph. If vertices i and j are
    //not connected this fact should be stated
    public void getShortestPath(int v1, int v2) {

        // shortest path from v1 node to v2 node
        System.out.println("Shortest Path From " + v1 + " -> " + v2 + ":\n");

        this.DijkstraSP(v1);
        double weight = 0;

        if (this.hasPathTo(v2) && this.getVertex(v1).getStatus() && this.getVertex(v2).getStatus()) {
            for (DirectedEdge e : this.pathTo(v2)) {
                if (!e.getStatus()) {
                    System.out.println("There is no path from " + v1 + " to " + v2 + "!\n");
                    return;
                }
                weight += e.weight();
            }
            System.out.println("" + this.pathTo(v2).toString().replaceAll("[\\[\\]\\,]", "") + "\n");
            System.out.println("Total Weight: [" + weight + "]\n");
        } else {
            if (this.getVertex(v1).getStatus() && this.getVertex(v2).getStatus()) {
                //System.out.println("Invalid arguments!\n");
            }
            System.out.println("There is no path from " + v1 + " to " + v2 + "!");
        }

    }

    public void DijkstraSP(int s) {

        distTo = new double[V];
        edgeTo = new DirectedEdge[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(V);
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : this.adj(v)) {
                if (this.getVertex(e.to()).getStatus()) {
                    relax(e);
                }
            }
        }

    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {

        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) {
                pq.change(w, distTo[w]);
            } else {
                pq.insert(w, distTo[w]);
            }

        }
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        if (v > V) {
            return false;
        }
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // shortest path from s to v as an Iterable, null if no such path
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        ArrayList<DirectedEdge> temp = new ArrayList<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            temp.add(e);
        }

        Collections.reverse(temp);
        return temp;
    }

    //GETDISTINCTPATHS()
    //display each of the distinct paths (differing by at least one edge) from vertex i to vertex j with
    //total weight less than or equal to x. All of the vertices and edges in each path should be shown and the
    //total number of distinct paths should be shown at the end
    public int getDistinctPaths(int v1, int v2, int w) {
        System.out.println("Distinct Paths from " + v1 + " to " + v2 + " (differing by at least one edge):");
        DFS(this, v1, v2, w);
        return getNumPaths();
    }

    // show all simple paths from s to t - use DFS
    public void DFS(Graph G, int s, int t, int limit) {
        onPath = new boolean[V];
        distTo = new double[V];
        path = new ArrayList<Integer>();
        edgeTo = new DirectedEdge[V];
        numberOfPaths = 0;
        getAllPaths(s, t, limit);
    }

    public void DFS(Graph G) {
        marked = new boolean[V];
        DFScomponents = new int[V];
        edgeTo = new DirectedEdge[V];
        count = 0;
        for (int v = 0; v < V; v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // use DFS
    private void getAllPaths(int v, int t, int limit) {

        // add v to current path  
        path.add(v);
        onPath[v] = true;

        // found path from s to t
        if (v == t && distTo[v] < limit) {
            numberOfPaths++;
            System.out.print("Path " + numberOfPaths + ": Total weight: (" + distTo[v] + ") ");
            getAllPaths();

        } // consider all neighbors that would continue path with repeating a node
        else {
            for (DirectedEdge w : adj(v)) {
                if (!onPath[w.to()] && getVertex(v).getStatus()) {
                    int x = w.from(), y = w.to();
                    distTo[y] = distTo[x] + w.weight();
                    edgeTo[w.to()] = w;
                    getAllPaths(w.to(), t, limit);
                }
            }
        }

        // done exploring from v, so remove from path
        path.remove(path.size() - 1);
        onPath[v] = false;
    }

    // this implementation just prints the path to standard output
    private void getAllPaths() {
        ArrayList<Integer> reverse = new ArrayList<Integer>();
        for (int v : path) {
            reverse.add(v);
        }

        Collections.reverse(reverse);

        while (!reverse.isEmpty()) {
            int t = reverse.remove(reverse.size() - 1);

            if (edgeTo[t] == null) {
                continue;
            }
            System.out.print(edgeTo[t].toString());

        }
        System.out.println();
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        DFScomponents[v] = count;
        for (DirectedEdge e : G.adj(v)) {
            int w = e.other(v);
            if (!marked[w] && G.getVertex(v).getStatus()) {
                dfs(G, w);
            }
        }
    }

    public int getNumPaths() {
        return numberOfPaths;
    }

    public int components(int v) {
        return DFScomponents[v];
    }

    public int count() {
        return count;
    }

    //NODEUP()
    //connect node and incident nodes
    public void nodeUp(int vertex) {

        this.getVertex(vertex).up();
        boolean up = false;
        for (DirectedEdge e : this.getList(vertex)) {

            if (e.up() && this.getEdgeBi(e).up()) {
                up = true;
            }

        }

        if (up) {
            System.out.println("Vertex " + vertex + " is back online\n");
            vertices++;
        } else {
            System.out.println("Vertex is already online!\n");
        }
    }

    //NODEDOWN()
    //disconnect node and incident node
    public void nodeDown(int vertex) {

        this.getVertex(vertex).down();
        boolean down = false;
        for (DirectedEdge e : getList(vertex)) {

            if (e.down() && this.getEdgeBi(e).down()) {
                down = true;
            }

        }

        if (down) {
            System.out.println("Vertex " + vertex + " has gone down\n");
            vertices--;
        } else {
            System.out.println("Vertex is already offline!\n");
        }

    }

    //CHANGEWEIGHT()
    //change/create edge u, v with new weight//
    //for 0 or - input edge will be deleted permanently
    public void changeWeight(int u, int v, double weight) {

        DirectedEdge e1 = new DirectedEdge(u, v, weight);
        DirectedEdge e2 = new DirectedEdge(v, u, weight);

        // remove edge if weight is negative or 0
        if (weight <= 0) {
            System.out.println("Edge weight <= 0, edge removed from graph!\n");
            this.removeEdge(this.getEdge(e1));
            this.removeEdge(this.getEdge(e2));
            System.out.println("\n");
            return;
        }

        // if edge exists change weight if not create new edge
        if (this.hasEdge(e1) && this.hasEdge(e2)) {
            this.changeEdgeWeight(this.getEdge(e1), weight);
            this.changeEdgeWeight(this.getEdge(e2), weight);
            System.out.println("Weight of edge " + u + "->" + v + " changed to " + weight);

        } else {
            this.addEdge(e1);
            this.addEdge(e2);
            System.out.println("New edge added with weight " + e1.toString() + "\n");
        }

        System.out.println("\n");

    }

    public void Quit() {
        System.exit(0);
    }

    //NODE CLASS
    //represents a vertex
    public class Node {

        private final int V;
        private boolean status;
        private int component;

        public Node(int i) {
            V = i;
            status = true;
        }

        // status is changed to up/true
        public void up() {
            this.status = true;
        }

        // status is changed to down/false
        public void down() {
            this.status = false;
        }

        // return the status
        public boolean getStatus() {
            return status;
        }

        public void setComponent(int c) {
            component = c;
        }

        public int getComponent() {
            return this.component;
        }

        public String toString() {
            return Integer.toString(V);
        }

    }

    public class DirectedEdge {

        private final int v;
        private final int w;
        private double weight;
        private boolean status;

        /**
         * Create a directed edge from v to w with given weight.
         */
        public DirectedEdge(int v, int w, double weight) {

            this.v = v;
            this.w = w;
            this.weight = weight;
            this.status = true;

        }

        /**
         * Return the vertex where this edge begins.
         */
        public int from() {
            return v;
        }

        /**
         * Return the vertex where this edge ends.
         */
        public int to() {
            return w;
        }

        /**
         * Return either endpoint of this edge.
         */
        public int either() {
            return v;
        }

        /**
         * Return the endpoint of this edge that is different from the given
         * vertex (unless a self-loop).
         */
        public int other(int vertex) {
            if (vertex == v) {
                return w;
            } else if (vertex == w) {
                return v;
            } else {
                throw new RuntimeException("Illegal endpoint");
            }
        }

        /**
         * Return the weight of this edge.
         */
        public double weight() {
            return weight;
        }

        public void changeWeight(double newWeight) {
            this.weight = newWeight;
        }

        // status of the edge
        public boolean getStatus() {
            return status;
        }

        // change edge status to true, if already true output will be false
        public boolean up() {

            boolean out = true;

            if (this.status == true) {
                out = false;
            }

            this.status = true;

            return out;

        }

        // change edge status to false, if already false output will be false
        public boolean down() {

            boolean out = true;

            if (this.status == false) {
                out = false;
            }

            this.status = false;

            return out;

        }

        /**
         * Return a string representation of this edge.
         */
        public String toString() {
            return "  " + v + "->" + w + " " + String.format("%5.2f", weight);
        }

    }

    public class IndexMinPQ<Key extends Comparable<Key>> {

        private int N;           // number of elements on PQ
        private int[] pq;        // binary heap using 1-based indexing
        private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
        private Key[] keys;      // keys[i] = priority of i

        @SuppressWarnings({"unchecked", "rawtypes"})
        public IndexMinPQ(int NMAX) {
            keys = (Key[]) new Comparable[NMAX + 1];    // make this of length NMAX??
            pq = new int[NMAX + 1];
            qp = new int[NMAX + 1];                   // make this of length NMAX??
            for (int i = 0; i <= NMAX; i++) {
                qp[i] = -1;
            }
        }

        // is the priority queue empty?
        public boolean isEmpty() {
            return N == 0;
        }

        // is k an index on the priority queue?
        public boolean contains(int k) {
            return qp[k] != -1;
        }

        // number of keys in the priority queue
        public int size() {
            return N;
        }

        // associate key with index k
        public void insert(int k, Key key) {
            if (contains(k)) {
                throw new RuntimeException("item is already in pq");
            }
            N++;
            qp[k] = N;
            pq[N] = k;
            keys[k] = key;
            swim(N);
        }

        // return the index associated with a minimal key
        public int min() {
            if (N == 0) {
                throw new RuntimeException("Priority queue underflow");
            }
            return pq[1];
        }

        // return a minimal key
        public Key minKey() {
            if (N == 0) {
                throw new RuntimeException("Priority queue underflow");
            }
            return keys[pq[1]];
        }

        // delete a minimal key and returns its associated index
        public int delMin() {
            if (N == 0) {
                throw new RuntimeException("Priority queue underflow");
            }
            int min = pq[1];
            exch(1, N--);
            sink(1);
            qp[min] = -1;            // delete
            keys[pq[N + 1]] = null;    // to help with garbage collection
            pq[N + 1] = -1;            // not needed
            return min;
        }

        // change the key associated with index k
        public void change(int k, Key key) {
            if (!contains(k)) {
                throw new RuntimeException("item is not in pq");
            }
            keys[k] = key;
            swim(qp[k]);
            sink(qp[k]);
        }

        // decrease the key associated with index k
        public void decrease(int k, Key key) {
            if (!contains(k)) {
                throw new RuntimeException("item is not in pq");
            }
            if (keys[k].compareTo(key) <= 0) {
                throw new RuntimeException("illegal decrease");
            }
            keys[k] = key;
            swim(qp[k]);
        }

        // increase the key associated with index k
        public void increase(int k, Key key) {
            if (!contains(k)) {
                throw new RuntimeException("item is not in pq");
            }
            if (keys[k].compareTo(key) >= 0) {
                throw new RuntimeException("illegal decrease");
            }
            keys[k] = key;
            sink(qp[k]);
        }

        /**
         * ************************************************************
         * General helper functions
         * ************************************************************
         */
        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }

        /**
         * ************************************************************
         * Heap helper functions
         * ************************************************************
         */
        private void swim(int k) {
            while (k > 1 && greater(k / 2, k)) {
                exch(k, k / 2);
                k = k / 2;
            }
        }

        private void sink(int k) {
            while (2 * k <= N) {
                int j = 2 * k;
                if (j < N && greater(j, j + 1)) {
                    j++;
                }
                if (!greater(k, j)) {
                    break;
                }
                exch(k, j);
                k = j;
            }
        }
    }
}
