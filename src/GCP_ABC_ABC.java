public class GCP_ABC_ABC {
    public static void main(String[] args) {
//        GraphGenerator gg = new GraphGenerator();
        Graph graph = GraphGenerator.generateGraph();
        ABCAlgorithm algorithm = new ABCAlgorithm(graph);
//        ABCAlgorithmTester algorithmTester = new ABCAlgorithmTester(algorithm);
        new ABCAlgorithmTester(algorithm).test();
    }
}
