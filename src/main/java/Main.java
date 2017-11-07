import Graph.Graph;

public class Main {
    public static void main(String[] args) {

        /*final Graph graph = Graph.generateRandomGraph();
        graph.printGraph();
        System.out.println(graph);
        graph.runPlain();
        System.out.println("isolated vertex " + graph.getNumberOfIsolatedVertex());
        System.out.println("Init depth first search");
        graph.DFS(true);
        System.out.println("is Conexo " + graph.isConexo());
        System.out.println("cantidad conexas " + graph.getNumberOfConexoVertex());
        System.out.println("Init breath first search");
        graph.BFS();*/



        final Graph graph2 = new Graph(5);
        graph2.addVertex(1);
        graph2.addVertex(2);
        graph2.addVertex(3);
        graph2.addVertex(4);
        graph2.addVertex(5);

        graph2.addEdge(0,4,100);
        graph2.addEdge(0,3,30);
        graph2.addEdge(0,1,10);
        graph2.addEdge(1,2,50);
        graph2.addEdge(2,4,10);
        graph2.addEdge(2,3,20);
        graph2.addEdge(3,4,60);

        graph2.printGraph();
        graph2.runPlain();
        System.out.println(graph2.getNumberOfIsolatedVertex());
        graph2.DFS(true);
        System.out.println("es conexo " + graph2.isConexo());
        graph2.dijkstra(0,4);

    }
}
