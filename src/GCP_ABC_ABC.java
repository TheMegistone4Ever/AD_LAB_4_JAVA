public class GCP_ABC_ABC {
    public static void main(String[] args) {
        Graph graph = new Graph(new int[constants.VERTEX_COUNT][constants.VERTEX_COUNT]);
        System.out.printf("Adjacency matrix is validate - %b\n", graph.validateAdjMatrix());
        System.out.println("Degrees of graph: ");
        graph.printArrayByUnits(graph.getDegrees(), constants.MAX_VERTEX_DEGREE);
        System.out.println("Start solving by ABC(ABC) algorithm...");
        ABCAlgorithm algorithm = new ABCAlgorithm(graph);
        long start = System.currentTimeMillis();
        System.out.printf("Estimated time in minutes: %d\n", (System.currentTimeMillis() - start) / constants.MIN);
        graph = algorithm.train();
        System.out.println("Final colored graph:");
        graph.printArrayByUnits(graph.getColors(), constants.MAX_VERTEX_DEGREE);
    }
}
