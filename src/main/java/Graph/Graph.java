package Graph;

import extras.GraphViz;
import java.io.File;
import java.util.*;

public class Graph<T> implements GraphInterface<T>  {

    private final ArrayList<T> vertexArray;
    private Integer[][] adjacencyMatrix;
    private int index;
    private int edgesindex;

    private final ArrayList<T> isolatedVertex;
    private final ArrayList<T> conexoVertex;

    public Graph(){
        this.vertexArray = new ArrayList<>(10);
        this.adjacencyMatrix = new Integer[10][10];
        this.prepareMatrix();
        this.index = 0;
        this.edgesindex = 0;
        this.isolatedVertex = new ArrayList<>();
        this.conexoVertex = new ArrayList<>();
    }

    public Graph(int initialCapacity){
        this.vertexArray = new ArrayList<>(initialCapacity);
        this.adjacencyMatrix = new Integer[initialCapacity][initialCapacity];
        this.prepareMatrix();
        this.index = 0;
        this.edgesindex = 0;
        this.isolatedVertex = new ArrayList<>();
        this.conexoVertex = new ArrayList<>();
    }

    /**
     * Generates new random graph between 5 and 15 elements, the weight of the edges represents the meters to reach that
     * nth vertex
     * @return graph with all random values, like vertex number and all the edges
     *
     * Operation order = O(n^2)
     */
    public static Graph generateRandomGraph(){
        Random r = new Random();
        final int initialCapacity = (r.nextInt(10-5) + 5);
        final Graph newGraph = new Graph(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            newGraph.addVertex(String.valueOf(i));
        }
        for (int i = 0; i < initialCapacity; i++) {
            final int randomNumOfConnections = r.nextInt(initialCapacity+1);
            for (int j = 0; j < randomNumOfConnections; j++) {
                final int vertexIndex = r.nextInt(randomNumOfConnections);
                if(!newGraph.hasEdge(i,vertexIndex)){
                    final int randomWeight = (r.nextInt(26-1)+1);
                    newGraph.addEdge(i,vertexIndex,randomWeight);
                }
            }
        }
        return newGraph;
    }


    /**
     * adds a vertex to the graph
     * @param vertex element to add to the graph
     *
     * Operation Order (best case)= O(1)
     * Operation Order (worst case)= O(N^2)
     */
    @Override
    public void addVertex(T vertex) {
        if(this.adjacencyMatrix.length <= this.index) this.resizeMatrix();
        this.vertexArray.add(index,vertex);
        this.adjacencyMatrix[index][index] = 0;
        this.index++;
    }

    /**
     * Adds an edge to the graph giving the vertex's index which starts and finish the edge. Also asks for the weight
     * @param from starting vertex's index
     * @param to ending vertex's index
     * @param weight weight of the edge
     *
     * Operation Order = O(1)
     */
    @Override
    public void addEdge(int from, int to, int weight) {
        this.adjacencyMatrix[from][to] = this.adjacencyMatrix[to][from] = weight;
        this.edgesindex++;
    }

    /**
     * adds an element to the graph and adds an edge to it
     * @param vertex element to add to the graph
     * @param from starting vertex's index
     * @param to ending vertex's index
     * @param weight weight of the edge
     *
     * Operation Order (best case) = O(1)
     * Operation Order (worst case) = O(n^2)
     */
    public void add(T vertex, int from, int to, int weight){
        this.addVertex(vertex);
        this.addEdge(from,to,weight);
    }

    /**
     * Deletes an edge to the graph giving vertex's index which starts and finishes the edge
     * @param from starting vertex's index
     * @param to ending vertex's index
     */
    @Override
    public void deleteEdge(int from, int to) {
        this.adjacencyMatrix[from][to] = this.adjacencyMatrix[to][from] = Integer.MAX_VALUE;
        this.edgesindex--;
    }

    /**
     * Delete a vertex to the graph
     * @param vertexPosition vertex position index
     *
     * Operation Order = O(1)
     */
    @Override
    public void deleteVertex(int vertexPosition) {
        if(vertexPosition < 0) throw new IllegalArgumentException("Index cannot be negative");
        if(vertexPosition > this.index) throw new IllegalArgumentException("Index too big");
        this.vertexArray.remove(vertexPosition);

    }

    /**
     * Checks if exists an edge giving the start and the end of it
     * @param from starting vertex's index
     * @param to ending vertex's index
     * @return boolean if exists edge
     *
     * Operation Order = O(1)
     */
    @Override
    public boolean hasEdge(int from, int to) {
        return this.adjacencyMatrix[from][to] != Integer.MAX_VALUE;
    }

    /**
     * Getter to access to the order of the graph
     * @return int representing the order of the graph
     *
     * Operation Order = O(1)
     */
    @Override
    public int order() {
        return this.index;
    }

    /**
     * Getter to access the number of edges on the graph
     * @return int representing the number of edges on the graph
     *
     * Operation Order = O(1)
     */
    @Override
    public int edgeLength() {
        return this.edgesindex;
    }

    /**
     * Gets the vertex giving the position or the insertion order number
     * @param vertexPosition position of the verte
     * @return element stored in the graph
     *
     * Operation Order = (1)
     */
    @Override
    public T getVertex(int vertexPosition) {
        if(vertexPosition < 0) throw new IllegalArgumentException("Index cannot be negative");
        if(vertexPosition > this.index) throw new IllegalArgumentException("Index too big");
        return this.vertexArray.get(vertexPosition);
    }

    /**
     * Gets the adjacent list of vertex, given the vertex's position
     * @param vertexPosition vertex's position you want to get its other adjacent vertex
     * @return iterator with the list of the adjacent vertex
     */
    @Override
    public Iterator<Integer> getAdjacentList(int vertexPosition) {
        ArrayList<Integer> auxList = new ArrayList<>();
        for (int i = 0; i < this.index; i++) {
            if(this.hasEdge(vertexPosition,i)) auxList.add(i);
        }
        return auxList.iterator();
    }

    public int getNumberOfIsolatedVertex(){
        return this.isolatedVertex.size();
    }

    public boolean isConexo(){
        return !this.conexoVertex.isEmpty();
    }

    public int getNumberOfConexoVertex(){
        return this.conexoVertex.size();
    }

    public void printGraph(){
        File out = new File("graph.png");
        GraphViz graphViz = new GraphViz();
        graphViz.addln(graphViz.start_graph()); // start the graphviz file

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < this.adjacencyMatrix[i].length; j++) {
                if(i != j && i>j && !(this.adjacencyMatrix[i][j].equals(Integer.MAX_VALUE))) graphViz.addln(
                        "\t\t\t"+this.vertexArray.get(i).toString() + " -> " + this.vertexArray.get(j).toString()+"[label="+ '\"' + this.adjacencyMatrix[i][j] + '\"' +"][dir=none];"
                );
            }
        }
        graphViz.addln(graphViz.end_graph());
//        System.out.println(graphViz.getDotSource());
        graphViz.writeGraphToFile(graphViz.getGraph(graphViz.getDotSource(),"png", "dot"), out);
    }

    public void runPlain(){
        for (int i = 0; i < this.order(); i++) {
            System.out.println("Vertex: " + this.vertexArray.get(i).toString() + " ->");
            for (int j = 0; j < this.order(); j++) {
                int count = 0;
                if(i != j && !(this.adjacencyMatrix[i][j].equals(Integer.MAX_VALUE))) {
                    System.out.println(" { " + this.vertexArray.get(j).toString() + ", weight: " +this.adjacencyMatrix[i][j].toString() + " }");
                }else count++;
                if(count == (this.index)) this.isolatedVertex.add(this.vertexArray.get(i));
            }
            System.out.println("--------------------");
        }
    }

    public void DFS( boolean calculateConexo){
//        final Random r = new Random();
        final boolean[] visited = new boolean[this.order()];
        for (int i = 0; i < this.order(); i++) {
            if(!visited[i]){
                this.depthFirstSearch(i, visited);
            }
            if(calculateConexo) this.calculateIsConexo(visited);
        }
    }

    private void depthFirstSearch(int i, boolean[] visited){
        int t;
        Stack<Integer> p = new Stack<>();
        Iterator<Integer> itr;
        p.add(i);
        while (!p.isEmpty()) {
            t = p.pop();
            if(!visited[t]){
                System.out.println(t + " ->");
                visited[t]= true;
            }
            itr = this.getAdjacentList(t);
            while (itr.hasNext()){
                int v = itr.next();
                if(!visited[v]) p.push(v);
            }
        }
    }

    public void BFS(){
        final Random r = new Random();
        boolean[] visited = new boolean[this.order()];
        LinkedList<Integer> queue = new LinkedList<>();
//        int value = r.nextInt(this.order());
        int value = 0;
        visited[value] = true;
        queue.add(value);
        while (!queue.isEmpty()){
            value = queue.poll();
            System.out.println(value + "->");
            Iterator<Integer> integerIterator = this.getAdjacentList(value);
            while (integerIterator.hasNext()){
                int next = integerIterator.next();
                if(!visited[next]){
                    visited[next] = true;
                    queue.add(next);
                }
            }
        }
    }

    public void dijkstra(int from, int to){
        final Integer[] d = this.adjacencyMatrix[from];
        final Integer[] path = new Integer[this.order()];
        for (int i = 0; i < path.length; i++) {
            path[i] = from;
        }
        final ArrayList<Integer> S = new ArrayList<>();
        S.add(from);
        int w;
        while (S.size() < this.order()-1) {
            for (int i = 0; i < d.length; i++) {
                if (!S.contains(i) && this.isMinimum(i, d, S)) {
                    w = i;
                    S.add(w);
                    for (int j = 0; j < d.length; j++) {
                        if (!S.contains(j)) {
                            if (d[j] > Math.abs(d[w] + this.adjacencyMatrix[w][j])) {
                                d[j] = Integer.min(d[j], d[w] + this.adjacencyMatrix[w][j]);
                                path[j] = w;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("costo" + Arrays.asList(d));
        System.out.println("Path " + Arrays.asList(path));

    }

    private boolean isMinimum(int j, Integer[] d, ArrayList<Integer> s) {
        int min = d[j];
        for (int i = 0; i < d.length; i++) {
            if(j!= i && d[i]!=0 && !s.contains(i) && min > d[i]) return false;
        }
        return true;
    }

    private void calculateIsConexo(boolean[] visited) {
        for (int i = 0; i < visited.length; i++) {
            if(visited[i]){
                this.conexoVertex.add(this.vertexArray.get(i));
            }
        }
    }

    private void prepareMatrix() {
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < this.adjacencyMatrix[i].length; j++) {
                this.adjacencyMatrix[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    private void resizeMatrix() {
        Integer[][] auxMatrix = new Integer[this.index+10][this.index+10];
        for (int i = 0; i < auxMatrix.length; i++) {
            for (int j = 0; j < auxMatrix[i].length; j++) {
                if(i >= this.index || j>= this.index) auxMatrix[i][j] = Integer.MAX_VALUE;
                else auxMatrix[i][j] = this.adjacencyMatrix[i][j];
            }
        }
    }

}
