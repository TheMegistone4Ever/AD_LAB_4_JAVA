import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ABCAlgorithm {
    private final Graph initialGraph;
    private Graph graph;
    private ArrayList<Integer> availableVertices;
    private final int[] palette;
    private final ArrayList<Integer> usedColors;

    public int calculateChromaticNumber() {
        while (!graph.isAllVerticesValidColored()) {
            ArrayList<Integer> selectedVertices = sendEmployedBees();
            sendOnlookerBees(selectedVertices);
        }
        return usedColors.size();
    }

    public ABCAlgorithm(Graph initialGraph) {
        this.initialGraph = initialGraph;
        graph = new Graph(initialGraph);
        availableVertices = Graph.getVertexArray();
        palette = IntStream.rangeClosed(0, constants.MAX_VERTEX_DEGREE).toArray();
        usedColors = new ArrayList<>();
    }

    public void resetAlgorithm() {
        usedColors.clear();
        availableVertices = Graph.getVertexArray();
        graph = new Graph(initialGraph);
    }

    private @NotNull ArrayList<Integer> sendEmployedBees() {
        ArrayList<Integer> selectedVertices = new ArrayList<>(List.of(0));
        for (int employedBee = 0; employedBee < constants.EXPLORER_BEES_COUNT; ++employedBee) {
            int randomSelectedVertex = availableVertices.get(new Random().nextInt(availableVertices.size()));
            availableVertices.remove((Object)randomSelectedVertex);
            selectedVertices.add(randomSelectedVertex);
        }
        return selectedVertices;
    }

    private void sendOnlookerBees(@NotNull ArrayList<Integer> selectedVertices) {
        int[] selectedVerticesDegrees = new int[selectedVertices.size()];
        for (int i = 0; i < selectedVerticesDegrees.length; ++i)
            selectedVerticesDegrees[i] = graph.getVertexDegree(selectedVertices.get(i));
        int[] onlookerBeesSplit = getOnlookerBeesSplit(selectedVerticesDegrees);
        for (int i = 0; i < selectedVertices.size(); ++i) {
            int onlookerBeesCountForVertex = onlookerBeesSplit[i];
            int[] connectedVertices = graph.getConnectedVertexes(selectedVertices.get(i));
            colorConnectedVertices(connectedVertices, onlookerBeesCountForVertex);
            colorVertex(selectedVertices.get(i));
        }
    }

    private int @NotNull [] getOnlookerBeesSplit(int[] selectedVerticesDegrees) {
        double[] nectarValues = getNectarValues(selectedVerticesDegrees);
        int onlookerBeesCount = constants.TOTAL_BEES_COUNT - constants.EXPLORER_BEES_COUNT;
        int[] res = new int[nectarValues.length];
        for (int i = 0; i < nectarValues.length; ++i){
//            int onlookerBeesCountForCurrentVertex = (int)(onlookerBeesCount * nectarValues[i]);
            onlookerBeesCount -= res[i] = (int)(onlookerBeesCount * nectarValues[i]);
//            res[i] = onlookerBeesCountForCurrentVertex;
        }
        return res;
    }

    private double @NotNull [] getNectarValues(int[] selectedVerticesDegrees) {
        double[] res = new double[selectedVerticesDegrees.length];
        for (int i = 0, totalDegrees = IntStream.of(selectedVerticesDegrees).sum(); i < selectedVerticesDegrees.length; ++i)
            res[i] = (double)selectedVerticesDegrees[i] / totalDegrees;
        return res;
    }

    private void colorConnectedVertices(int @NotNull [] connectedVertices, int onlookerBeesCount) {
        for (int i = 0; i < connectedVertices.length; ++i)
            if (i < onlookerBeesCount - 1) colorVertex(connectedVertices[i]);
    }

    private void colorVertex(int vertex) {
        ArrayList<Integer> availableColors = new ArrayList<>(usedColors);
        boolean isColoredSuccessfully = false;
        while (!isColoredSuccessfully) {
            if (availableColors.isEmpty()) {
                int newColor = palette[usedColors.size()];
                usedColors.add(newColor);
                graph.tryToColorAndCheckIsValid(vertex, newColor);
                break;
            }
            int color = availableColors.get(new Random().nextInt(availableColors.size()));
            availableColors.remove((Object)color);
            isColoredSuccessfully = graph.tryToColorAndCheckIsValid(vertex, color);
        }
    }

    public Graph train() {
        Graph resGraph = new Graph(graph);
        int bestCN = calculateChromaticNumber();
        System.out.println("Init colored graph:");
        System.out.printf("The new best solution of the graph found on %4d iteration - old: %3d, new: %3d:, estimated time - %2d seconds\n",
                0, constants.MAX_VERTEX_DEGREE + 1, bestCN, 0);
        graph.printArrayByUnits(graph.getColors());
        resetAlgorithm();
        for (int iteration = 0; iteration < constants.ITERATIONS_COUNT;) {
            long start = System.currentTimeMillis();
            for (int k = 0; k < constants.ITERATIONS_PER_STEP; ++k, resetAlgorithm()) {
                int newCN = calculateChromaticNumber();
                if (newCN < bestCN) {
                    System.out.printf("New best solution of the graph found on %4d iteration, old: %3d, new: %3d, estimated time - %2d seconds\n",
                            iteration + k, bestCN, bestCN = newCN, (System.currentTimeMillis() - start) / 1000);
                    graph.printArrayByUnits(graph.getColors());
                    resGraph = new Graph(graph);
                }
            }
            System.out.printf("On iteration %4d best result is %3d, estimated time - %2d seconds\n",
                    iteration += constants.ITERATIONS_PER_STEP, bestCN, (System.currentTimeMillis() - start) / 1000);
        }
        return resGraph;
    }
}
