public class GCP_ABC_ABC {
    public static void main(String[] args) {
        Graph graph = new Graph(new int[constants.VERTEX_COUNT][constants.VERTEX_COUNT]);
        System.out.printf("Adjacency matrix is validate - %b\n", graph.validateAdjMatrix());
        graph.printAdjMatrix();

        System.out.println("Degrees of graph: ");
        graph.printArrayByUnits(graph.getDegrees());

        System.out.println("Start solving by ABC(ABC) algorithm...");
        long start = System.currentTimeMillis();
        graph = new ABCAlgorithm(graph).train();
        System.out.printf("Estimated time in seconds: %d\n", (System.currentTimeMillis() - start) / 1000);

        System.out.println("Final colored graph:");
        graph.printArrayByUnits(graph.getColors());
        System.out.printf("Was graph colored valid? - %b", graph.isAllVerticesValidColored());
    }
}
