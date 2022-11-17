import java.util.Random;
import java.util.stream.IntStream;

public class GraphGenerator {
    final static int VERTEX_COUNT = 300;
    final static int MIN_VERTEX_DEGREE = 1;
    final static int MAX_VERTEX_DEGREE = 30;

    public static int rand(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static Graph generateGraph() {
        int[][] adjMatrix = new int[VERTEX_COUNT][VERTEX_COUNT];
        for (int vertex = 0; vertex < VERTEX_COUNT; ++vertex) {
            int[] vertexConnections = adjMatrix[vertex];
            int currentVertexDegree = IntStream.of(vertexConnections).sum();
            int finalVertexDegree = Math.min(rand(MIN_VERTEX_DEGREE, MAX_VERTEX_DEGREE+1)-currentVertexDegree, VERTEX_COUNT - vertex - 1);
            for (int newConnection = 0; newConnection < finalVertexDegree; ++newConnection) {
                boolean isConnectedAlready = true
                int tryCount = 0;
                int newConnectionVertex;
                while (isConnectedAlready && tryCount < VERTEX_COUNT) {
                    newConnectionVertex = rand(vertex + 1, VERTEX_COUNT);
                    ++tryCount;
                    int newConnectionVertexDegree = IntStream.of(adjMatrix[newConnectionVertex]).sum();
                    if (vertexConnections[newConnectionVertex] == 0 && newConnectionVertexDegree < MAX_VERTEX_DEGREE) {
                        isConnectedAlready = false;
                        adjMatrix[vertex][newConnectionVertex] = 1;
                        adjMatrix[newConnectionVertex][vertex] = 1;
                    }
                }
            }
        }
        return Graph(adjMatrix);
    }
}
