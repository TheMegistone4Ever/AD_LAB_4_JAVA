import java.util.ArrayList;
import java.util.stream.IntStream;

public class ABCAlgorithm {
    private final Graph initialGraph;
    public Graph graph;
    private ArrayList<Integer> availableVertices;
    private final ArrayList<Integer> palette;
    private final ArrayList<Integer> usedColors;

    public int calculateChromaticNumber() {
        while (!this.isFinished()) {
            ArrayList<Integer> selectedVertices = sendEmployedBees();
            sendOnlookerBees(selectedVertices);
        }
        return usedColors.size();
    }

    public ABCAlgorithm(Graph initialGraph) {
        this.initialGraph = initialGraph;
        graph = new Graph(initialGraph);
        availableVertices = graph.getVertexArray();
        palette = new ArrayList<>();
        for (int i = 0; i < constants.PALETTE_SIZE; i++) {
            palette.add(i);
        }
        usedColors = new ArrayList<>();
    }

    public void resetAlgorithm() {
        usedColors.clear();
        availableVertices = graph.getVertexArray();
        graph = new Graph(initialGraph);
    }

    private boolean isFinished() {
        return graph.isAllVerticesValidColored();
    }

    private ArrayList<Integer> sendEmployedBees() {
        ArrayList<Integer> selectedVertices = new ArrayList<>();
        selectedVertices.add(0);
        for (int employedBee = 0; employedBee < constants.EXPLORER_BEES_COUNT; employedBee++) {
            int randomSelectedVertexIndex = Graph.rand(0, availableVertices.size());
            int randomSelectedVertex = availableVertices.get(randomSelectedVertexIndex);
            availableVertices.remove((Object)randomSelectedVertex);
            selectedVertices.add(randomSelectedVertex);
        }
        return selectedVertices;
    }

    private void sendOnlookerBees(ArrayList<Integer> selectedVertices) {
        int[] selectedVerticesDegrees = new int[selectedVertices.size()];
        for (int i = 0; i < selectedVerticesDegrees.length; i++) {
            selectedVerticesDegrees[i] = graph.getVertexDegree(selectedVertices.get(i));
        }

        int[] onlookerBeesSplit = getOnlookerBeesSplit(selectedVerticesDegrees);

        for (int i = 0; i < selectedVertices.size(); i++) {
            int selectedVertex = selectedVertices.get(i);
            int onlookerBeesCountForVertex = onlookerBeesSplit[i];
            int[] connectedVertices = graph.getConnectedVertexes(selectedVertex);
            colorConnectedVertices(connectedVertices, onlookerBeesCountForVertex);
            colorVertex(selectedVertex);
        }
    }

    private int[] getOnlookerBeesSplit(int[] selectedVerticesDegrees) {
        double[] nectarValues = getNectarValues(selectedVerticesDegrees);
        int onlookerBeesCount = constants.TOTAL_BEES_COUNT - constants.EXPLORER_BEES_COUNT;
        int[] res = new int[nectarValues.length];
        for (int i = 0; i < nectarValues.length; i++){
            int onlookerBeesCountForCurrentVertex = (int)(onlookerBeesCount * nectarValues[i]);
            onlookerBeesCount -= onlookerBeesCountForCurrentVertex;
            res[i] = onlookerBeesCountForCurrentVertex;
        }
        return res;
    }

    private double[] getNectarValues(int[] selectedVerticesDegrees) {
        int summarySelectedVerticesDegree = IntStream.of(selectedVerticesDegrees).sum();
        double[] res = new double[selectedVerticesDegrees.length];
        for (int i = 0; i < selectedVerticesDegrees.length; i++) {
            res[i] = (double)selectedVerticesDegrees[i] / summarySelectedVerticesDegree;
        }
        return res;
    }

    private void colorConnectedVertices(int[] connectedVertices, int onlookerBeesCount) {
        for (int i = 0; i < connectedVertices.length; i++) {
            if (i < onlookerBeesCount - 1) colorVertex(connectedVertices[i]);
        }
    }

    private void colorVertex(int vertex) {
        ArrayList<Integer> availableColors = new ArrayList<>(usedColors);
        boolean isColoredSuccessfully = false;

        while (!isColoredSuccessfully) {
            if (availableColors.size() == 0) {
                int newColor = getNextColor();
                usedColors.add(newColor);
                graph.tryToColorAndCheckIsValid(vertex, newColor);
                break;
            }
            int randomAvailableColorIndex = Graph.rand(0, availableColors.size());
            int color = availableColors.get(randomAvailableColorIndex);
            availableColors.remove(randomAvailableColorIndex);
            isColoredSuccessfully = graph.tryToColorAndCheckIsValid(vertex, color);
        }

    }

    private int getNextColor() {
        return palette.get(usedColors.size());
    }

    public void test() {
        int bestResult = calculateChromaticNumber();
        resetAlgorithm();
        for (int iteration = 0; iteration <= constants.ITERATIONS_COUNT; ++iteration, resetAlgorithm()) {
            if (iteration % constants.ITERATIONS_PER_STEP == 0) {
                System.out.printf("on iteration %d best result is %d\n", iteration, bestResult);
            }
            int newChromaticNumber = calculateChromaticNumber();
            if (newChromaticNumber < bestResult) {
                System.out.printf("The best solution of the graph is found - old heuristic: %d, new heuristic: %d\n",
                        bestResult, newChromaticNumber);
                bestResult = newChromaticNumber;
                graph.printColors();
            }
        }
    }

}
