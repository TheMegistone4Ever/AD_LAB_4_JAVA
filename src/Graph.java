import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
        Arrays.fill(colors, constants.NO_COLOR);
        for (int vertex = 0; vertex < constants.VERTEX_COUNT; ++vertex) {
            int[] vertexConnections = this.adjMatrix[vertex];
            int currentVertexDegree = IntStream.of(vertexConnections).sum();
            int finalVertexDegree = Math.min(rand(constants.MIN_VERTEX_DEGREE,
                    constants.MAX_VERTEX_DEGREE+1)-currentVertexDegree, constants.VERTEX_COUNT - vertex - 1);
            for (int newConnection = 0; newConnection < finalVertexDegree; ++newConnection) {
                boolean isConnectedAlready = true;
                int tryCount = 0;
                int newConnectionVertex;
                while (isConnectedAlready && tryCount < constants.VERTEX_COUNT) {
                    newConnectionVertex = rand(vertex + 1, constants.VERTEX_COUNT);
                    ++tryCount;
                    int newConnectionVertexDegree = IntStream.of(this.adjMatrix[newConnectionVertex]).sum();
                    if (vertexConnections[newConnectionVertex] == 0
                            && newConnectionVertexDegree < constants.MAX_VERTEX_DEGREE) {
                        isConnectedAlready = false;
                        this.adjMatrix[vertex][newConnectionVertex] = this.adjMatrix[newConnectionVertex][vertex] = 1;
                    }
                }
            }
        }
    }

    public boolean validateAdjMatrix() {
        for (int[] row : adjMatrix) {
            int degree = IntStream.of(row).sum();
            if (degree > constants.MAX_VERTEX_DEGREE) {
                System.out.printf("Degree %d is bigger than %d!\n", degree, constants.MAX_VERTEX_DEGREE);
                return false;
            }
        }
        return true;
    }

    public void printAdjMatrix() {
        for (int[] row : adjMatrix) {
            for (int el : row) System.out.printf("%3d", el);
            System.out.println();
        }
    }

    public void printArrayByUnits(int @NotNull [] arr) {
        Arrays.stream(IntStream.range(0, arr.length).toArray()).forEach(i->System.out.printf("%-3d" +
                ((i+1) % constants.MAX_VERTEX_DEGREE > 0 ? "" : "\n"), arr[i]));
        System.out.println();
    }

    public int[] getColors() {
        return colors;
    }

    public int[] getDegrees() {
        int[] degrees = new int[adjMatrix.length];
        for (int i = 0; i < degrees.length; i++) degrees[i] = getVertexDegree(i);
        return degrees;
    }

    public ArrayList<Integer> getVertexArray() {
        return IntStream.range(0, constants.VERTEX_COUNT).collect(ArrayList::new, List::add, List::addAll);
    }

    public int getVertexDegree(int vertex) {
        return IntStream.of(adjMatrix[vertex]).sum();
    }

    public int[] getConnectedVertexes(int vertex) {
        int[] connectedVertexes = new int[IntStream.of(adjMatrix[vertex]).sum()];
        for (int i = 0, k = 0; i < adjMatrix[vertex].length; i++) {
            if (adjMatrix[vertex][i] == 1) connectedVertexes[k++] = i;
        }
        return connectedVertexes;
    }

    public boolean isAllVerticesValidColored() {
        int uncoloredVerticesCount = 0;
        for (int color : colors) {
            if (color == -1) {
                uncoloredVerticesCount++;
            }
        }
        return uncoloredVerticesCount == 0 && isColoringValid();
    }

    public boolean tryToColorAndCheckIsValid(int vertex, int newColor) {
        int oldColor = colors[vertex];
        colors[vertex] = newColor;
        boolean isValid = isColoringValid();
        if (!isValid) {
            this.colors[vertex] = oldColor;
        }
        return isValid;
    }

    private boolean isColoringValid() {
        for (int row = 0; row < adjMatrix.length; row++) {
            for (int col = 0; col < adjMatrix[row].length; col++) {
                if (adjMatrix[row][col] == 1
                        && colors[row] != constants.NO_COLOR
                        && colors[row] == colors[col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int rand(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
