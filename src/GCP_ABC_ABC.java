public class GCP_ABC_ABC {
    public static void main(String[] args) {
        GraphGenerator gg = new GraphGenerator();
        Graph g = gg.generateGraph();
        ABCAlgorithm algorithm = ABCAlgorithm(graph);
        ABCAlgorithmTester algorithmTester = ABCAlgorithmTester(algorithm);
        algorithmTester.test();
    }
}
