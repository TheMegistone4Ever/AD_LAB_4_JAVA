import com.sun.source.tree.BreakTree;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ABCAlgorithm {
    final Graph initialGraph;
    Graph graph;
    ArrayList<Integer> availableVertices;
    final int[] palette;
    final ArrayList<Integer> usedColors;

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
        palette = IntStream.range(0, constants.PALETTE_SIZE).toArray();
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

    private @NotNull ArrayList<Integer> sendEmployedBees() {
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

    private void sendOnlookerBees(@NotNull ArrayList<Integer> selectedVertices) {
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
        ArrayList<Integer> availableColors = new ArrayList<>(usedColors);
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
        System.out.printf("The new best solution of the graph is found - old: %d, new: %d:\n", constants.PALETTE_SIZE, bestCN);
        graph.printArrayByUnits(graph.getColors(), constants.MAX_VERTEX_DEGREE);
        resetAlgorithm();
        for (int iteration = 0; iteration <= constants.ITERATIONS_COUNT; ++iteration, resetAlgorithm()) {
            if (iteration % constants.ITERATIONS_PER_STEP == 0)
                System.out.printf("On iteration %4d best result is %d...\n", iteration, bestCN);
            int newCN = calculateChromaticNumber();
            if (newCN < bestCN) {
                System.out.printf("The new best solution of the graph is found - old: %d, new: %d...\n", bestCN, newCN);
                bestCN = newCN;
                graph.printArrayByUnits(graph.getColors(), constants.MAX_VERTEX_DEGREE);
                resGraph = new Graph(graph);
            }
        }
        return resGraph;
    }
}
