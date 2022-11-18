import java.util.Random;
import java.util.stream.IntStream;

public class GraphGenerator {
    public static int rand(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static Graph generateGraph() {
        int[][] adjMatrix = new int[constants.VERTEX_COUNT][constants.VERTEX_COUNT];
        for (int vertex = 0; vertex < constants.VERTEX_COUNT; ++vertex) {
            int[] vertexConnections = adjMatrix[vertex];
            int currentVertexDegree = IntStream.of(vertexConnections).sum();
            int finalVertexDegree = Math.min(rand(constants.MIN_VERTEX_DEGREE, constants.MAX_VERTEX_DEGREE+1)-currentVertexDegree, constants.VERTEX_COUNT - vertex - 1);
            for (int newConnection = 0; newConnection < finalVertexDegree; ++newConnection) {
                boolean isConnectedAlready = true;
                int tryCount = 0;
                int newConnectionVertex;
                while (isConnectedAlready && tryCount < constants.VERTEX_COUNT) {
                    newConnectionVertex = rand(vertex + 1, constants.VERTEX_COUNT);
                    ++tryCount;
                    int newConnectionVertexDegree = IntStream.of(adjMatrix[newConnectionVertex]).sum();
                    if (vertexConnections[newConnectionVertex] == 0 && newConnectionVertexDegree < constants.MAX_VERTEX_DEGREE) {
                        isConnectedAlready = false;
                        adjMatrix[vertex][newConnectionVertex] = 1;
                        adjMatrix[newConnectionVertex][vertex] = 1;
                    }
                }
            }
        }
        return new Graph(adjMatrix);
    }
}
