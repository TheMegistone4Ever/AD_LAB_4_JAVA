import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class ABCAlgorithm {
    final Graph initialGraph;
    Graph graph;
    LinkedList<Integer> availableVertices;
    final int[] palette;
    final LinkedList<Integer> usedColors;

    public int calculateChromaticNumber() {
        while (!this.isFinished()) {
            LinkedList<Integer> selectedVertices = sendEmployedBees();
            sendOnlookerBees(selectedVertices);
        }
        return usedColors.size();
    }

    public ABCAlgorithm(Graph initialGraph) {
        this.initialGraph = initialGraph;
        graph = new Graph(initialGraph);
        availableVertices = graph.getVertexArray();
        palette = IntStream.range(0, constants.MAX_VERTEX_DEGREE + 1).toArray();
        usedColors = new LinkedList<>();
    }

    public void resetAlgorithm() {
        usedColors.clear();
        availableVertices = graph.getVertexArray();
        graph = new Graph(initialGraph);
    }

    private boolean isFinished() {
        return graph.isAllVerticesValidColored();
    }

    private @NotNull LinkedList<Integer> sendEmployedBees() {
        LinkedList<Integer> selectedVertices = new LinkedList<>();
        selectedVertices.add(0);
        for (int employedBee = 0; employedBee < constants.EXPLORER_BEES_COUNT; employedBee++) {
            int randomSelectedVertexIndex = Graph.rand(0, availableVertices.size());
            int randomSelectedVertex = availableVertices.get(randomSelectedVertexIndex);
            availableVertices.remove((Object)randomSelectedVertex);
            selectedVertices.add(randomSelectedVertex);
        }
        return selectedVertices;
    }

    private void sendOnlookerBees(@NotNull LinkedList<Integer> selectedVertices) {
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

    private int @NotNull [] getOnlookerBeesSplit(int[] selectedVerticesDegrees) {
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

    private double @NotNull [] getNectarValues(int[] selectedVerticesDegrees) {
        int summarySelectedVerticesDegree = IntStream.of(selectedVerticesDegrees).sum();
        double[] res = new double[selectedVerticesDegrees.length];
        for (int i = 0; i < selectedVerticesDegrees.length; i++) {
            res[i] = (double)selectedVerticesDegrees[i] / summarySelectedVerticesDegree;
        }
        return res;
    }

    private void colorConnectedVertices(int @NotNull [] connectedVertices, int onlookerBeesCount) {
        for (int i = 0; i < connectedVertices.length; i++) {
            if (i < onlookerBeesCount - 1) colorVertex(connectedVertices[i]);
        }
    }

    private void colorVertex(int vertex) {
        LinkedList<Integer> availableColors = new LinkedList<>(usedColors);
        boolean isColoredSuccessfully = false;
        while (!isColoredSuccessfully) {
            if (availableColors.size() == 0) {
                int newColor = palette[usedColors.size()];
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

    public Graph train() {
        Graph resGraph = new Graph(graph);
        int bestCN = calculateChromaticNumber();
        System.out.println("Init colored graph:");
        System.out.printf("The new best solution of the graph is found on %d iteration - old: %d, new: %d:\n",
                0, constants.MAX_VERTEX_DEGREE + 1, bestCN);
        graph.printArrayByUnits(graph.getColors());
        resetAlgorithm();
        for (int iteration = 0; iteration < constants.ITERATIONS_COUNT;) {
            for (int k = 0; k < constants.ITERATIONS_PER_STEP; ++k, resetAlgorithm()) {
                int newCN = calculateChromaticNumber();
                if (newCN < bestCN) {
                    System.out.printf("New best solution of the graph is found on %d iteration, old: %d, new: %d...\n",
                            iteration + k, bestCN, newCN);
                    bestCN = newCN;
                    graph.printArrayByUnits(graph.getColors());
                    resGraph = new Graph(graph);
                }
            }
            iteration += constants.ITERATIONS_PER_STEP;
            System.out.printf("On iteration %4d best result is %d...\n", iteration, bestCN);
        }
        return resGraph;
    }
}
