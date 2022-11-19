public class GCP_ABC_ABC {
    public static void main(String[] args) {
        GraphGenerator gg = new GraphGenerator();
        Graph graph = gg.generateGraph();
        ABCAlgorithm algorithm = new ABCAlgorithm(graph);
        ABCAlgorithmTester algorithmTester = new ABCAlgorithmTester(algorithm);
        long start = System.currentTimeMillis();
        algorithmTester.test();
        System.out.printf("Estimated time in minutes: %d\n", (System.currentTimeMillis()-start)/3600);
    }
}
