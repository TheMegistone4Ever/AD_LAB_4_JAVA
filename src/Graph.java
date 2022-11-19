import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

class Graph {
    private final int[][] adjMatrix;
    private final int[] colors;

    public Graph(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
        this.colors = new int[adjMatrix.length];
        Arrays.fill(colors, -1);
        /*for (int vertex = 0; vertex < constants.VERTEX_COUNT; ++vertex) {
            int[] vertexConnections = this.adjMatrix[vertex];
            int currentVertexDegree = IntStream.of(vertexConnections).sum();
            int finalVertexDegree = Math.min(rand(constants.MIN_VERTEX_DEGREE, constants.MAX_VERTEX_DEGREE+1)-currentVertexDegree, constants.VERTEX_COUNT - vertex - 1);
            for (int newConnection = 0; newConnection < finalVertexDegree; ++newConnection) {
                boolean isConnectedAlready = true;
                int tryCount = 0;
                int newConnectionVertex;
                while (isConnectedAlready && tryCount < constants.VERTEX_COUNT) {
                    newConnectionVertex = rand(vertex + 1, constants.VERTEX_COUNT);
                    ++tryCount;
                    int newConnectionVertexDegree = IntStream.of(this.adjMatrix[newConnectionVertex]).sum();
                    if (vertexConnections[newConnectionVertex] == 0 && newConnectionVertexDegree < constants.MAX_VERTEX_DEGREE) {
                        isConnectedAlready = false;
                        this.adjMatrix[vertex][newConnectionVertex] = 1;
                        this.adjMatrix[newConnectionVertex][vertex] = 1;
                    }
                }
            }
        }*/
    }

    public final int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public void validateAdjMatrix() {
        for (int row = 0; row < adjMatrix.length; row++) {
            for (int col = 0; col < adjMatrix[0].length; col++) {
                if (adjMatrix[row][col] == 1) System.out.println("ERROR!");
                int degree = IntStream.of(adjMatrix[row]).sum();
                if (degree > constants.MAX_VERTEX_DEGREE) {
                    System.out.println("Degree is bigger than MAX_VERTEX_DEGREE");
                }
            }
        }
    }

    public void printDegrees() {
        for (int row = 0; row < adjMatrix.length; row++) {
            for (int col = 0; col < adjMatrix[0].length; col++) {
                int degree = IntStream.of(adjMatrix[row]).sum();
                System.out.println(degree);
            }
        }
    }
    public void printAdjMatrix() {
        for (int row = 0; row < adjMatrix.length; row++) {
            for (int col = 0; col < adjMatrix[0].length; col++)
                System.out.println('\t' + adjMatrix[row][col]);
            System.out.println();
        }
    }
    public void printColors() {
        System.out.println(String.join(" ", Arrays.toString(colors)));
    }

    public ArrayList<Integer> getVertexArray() {
        ArrayList<Integer> vertArr = new ArrayList<>();
        for (int i = 0; i < constants.VERTEX_COUNT; i++) vertArr.add(i);
        return vertArr;
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

    private int getVertexColor(int vertex) {
        return colors[vertex];
    }

    public boolean isAllVerticesValidColored() {
        int uncoloredVerticesCount = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == -1) {
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
            for (int col = 0; col < adjMatrix[0].length; col++) {
                if (adjMatrix[row][col] == 1 && getVertexColor(row) != -1
                && getVertexColor(col) != -1 && getVertexColor(row) == getVertexColor(col)) {
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