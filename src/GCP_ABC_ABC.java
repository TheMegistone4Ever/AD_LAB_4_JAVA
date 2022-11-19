public class GCP_ABC_ABC {
    public static void main(String[] args) {
        Graph graph = new Graph(new int[constants.VERTEX_COUNT][constants.VERTEX_COUNT]);
        ABCAlgorithm algorithm = new ABCAlgorithm(graph);
        long start = System.currentTimeMillis();
        algorithm.test();
        System.out.printf("Estimated time in minutes: %d\n", (System.currentTimeMillis()-start)/60000);
    }
}
