public class ABCAlgorithmTester {
    ABCAlgorithm algorithm;
    public ABCAlgorithmTester(ABCAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void test() {
        int bestResult = algorithm.calculateChromaticNumber();
        algorithm.resetAlgorithm();

        for (int iteration = 0; iteration <= constants.ITERATIONS_COUNT; iteration++) {
            if (iteration % constants.ITERATIONS_PER_STEP == 0) {
                System.out.printf("on iteration %d best result is %d%n", iteration, bestResult);
            }
            int newChromaticNumber = algorithm.calculateChromaticNumber();
            if (newChromaticNumber < bestResult) {
                bestResult = newChromaticNumber;
                algorithm.graph.printColors();
            }
            algorithm.resetAlgorithm();
        }
    }
}
