import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

class Graph {
    private final int[][] adjMatrix;
    private final int[] colors;

    public Graph(@NotNull Graph g) {
        this.adjMatrix = g.adjMatrix.clone();
        this.colors = g.colors.clone();
    }

    public Graph(int @NotNull [][] adjMatrix) {
        this.adjMatrix = adjMatrix;
        this.colors = new int[adjMatrix.length];
        Arrays.fill(this.colors, constants.NO_COLOR);
        for (int currV = 0; currV < constants.VERTEX_COUNT; ++currV) {
            int finalVertexDegree = Math.min(rand(constants.MIN_VERTEX_DEGREE,
                    constants.MAX_VERTEX_DEGREE + 1) - getVertexDegree(currV), constants.VERTEX_COUNT - currV - 1);
            for (int newConnection = 0; newConnection < finalVertexDegree; ++newConnection) {
                boolean isConnectedAlready = true;
                for (int tryCount = 0, newVertex = rand(currV + 1, constants.VERTEX_COUNT);
                     isConnectedAlready && tryCount < constants.VERTEX_COUNT;
                     ++tryCount, newVertex = rand(currV + 1, constants.VERTEX_COUNT)) {
                    if (this.adjMatrix[currV][newVertex] == 0
                            && getVertexDegree(newVertex) < constants.MAX_VERTEX_DEGREE) {
                        isConnectedAlready = false;
                        this.adjMatrix[currV][newVertex] = 1;
                        this.adjMatrix[newVertex][currV] = 1;
                    }
                }
            }
        }
    }

    public boolean validateAdjMatrix() {
        for (int vertex = 0; vertex < adjMatrix.length; ++vertex)
            if (getVertexDegree(vertex) > constants.MAX_VERTEX_DEGREE) return false;
        return true;
    }

    public void printAdjMatrix() {
        System.out.println(Arrays.deepToString(adjMatrix));
    }

    public void printArrayByUnits(int @NotNull [] arr) {
        Arrays.stream(IntStream.range(0, arr.length).toArray()).forEach(i -> System.out.printf("%4d" +
                ((i+1) % constants.UNIT_SIZE > 0 ? "" : "\n"), arr[i]));
        System.out.println();
    }

    public int[] getColors() {
        return colors;
    }

    public int[] getDegrees() {
        int[] degrees = new int[adjMatrix.length];
        for (int i = 0; i < degrees.length; ++i) degrees[i] = getVertexDegree(i);
        return degrees;
    }

    public static ArrayList<Integer> getVertexArray() {
        return IntStream.range(0, constants.VERTEX_COUNT).collect(ArrayList::new, List::add, List::addAll);
    }

    public int getVertexDegree(int vertex) {
        return IntStream.of(adjMatrix[vertex]).sum();
    }

    public int[] getConnectedVertexes(int vertex) {
        int[] connectedVertexes = new int[getVertexDegree(vertex)];
        for (int i = 0, k = -1; i < adjMatrix[vertex].length; ++i)
            if (adjMatrix[vertex][i] == 1) connectedVertexes[++k] = i;
        return connectedVertexes;
    }

    public boolean isAllVerticesValidColored() {
        return IntStream.of(colors).noneMatch(c -> c == constants.NO_COLOR) && isColoringValid();
    }

    public boolean tryToColorAndCheckIsValid(int vertex, int newColor) {
        int oldColor = colors[vertex];
        colors[vertex] = newColor;
        boolean isValid = isColoringValid();
        if (!isValid) colors[vertex] = oldColor;
        return isValid;
    }

    private boolean isColoringValid() {
        for (int row = 0; row < adjMatrix.length; ++row)
            for (int col = 0; col < adjMatrix[row].length; ++col)
                if (adjMatrix[row][col] == 1
                        && colors[row] != constants.NO_COLOR
                        && colors[row] == colors[col]) {
                    return false;
                }
        return true;
    }

    private static int rand(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
