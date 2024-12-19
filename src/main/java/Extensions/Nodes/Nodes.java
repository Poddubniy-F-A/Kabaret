package Extensions.Nodes;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

public class Nodes {
    private final double[] gridX, gridY;
    private final int sizeX, sizeY;

    private Node[][] nodesArray;

    public Nodes(double[] gridX, double[] gridY, Node[][] nodesArray) {
        this.gridX = gridX;
        this.gridY = gridY;

        sizeX = gridX.length;
        sizeY = gridY.length;

        this.nodesArray = nodesArray;
    }

    public Node[][] getNodesArray() {
        return nodesArray;
    }

    public Node getNode(int i, int j) {
        return nodesArray[i][j];
    }

    public double[] getGridX() {
        return gridX;
    }

    public double[] getGridY() {
        return gridY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public double[][] getValuesArrayBy(ToDoubleFunction<Node> func) {
        return Arrays.stream(nodesArray).map(
                slice -> Arrays.stream(slice).mapToDouble(func).toArray()
        ).toArray(double[][]::new);
    }

    public double getHMin() {
        return Arrays.stream(nodesArray).mapToDouble(
                slice -> Arrays.stream(slice).mapToDouble(Node::h).min().getAsDouble()
        ).min().getAsDouble();
    }

    public double getHMax() {
        return Arrays.stream(nodesArray).mapToDouble(
                slice -> Arrays.stream(slice).mapToDouble(Node::h).max().getAsDouble()
        ).max().getAsDouble();
    }

    public void setNodesArray(Node[][] nodesArray) {
        this.nodesArray = nodesArray;
    }
}
