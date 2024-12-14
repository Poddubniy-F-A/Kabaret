package Extensions;

import Extensions.Fillers.Filler;

public class Nodes {
    private Node[][] nodesArray;
    private final double[] gridX, gridY;
    private final int sizeX, sizeY;

    public Nodes(double[] gridX, double[] gridY) {
        this.gridX = gridX;
        this.gridY = gridY;

        sizeX = gridX.length;
        sizeY = gridY.length;
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

    public void setNodesArray(Filler filler) {
        nodesArray = filler.getFilledArrayBy(gridX, gridY);
    }

    public void setNodesArray(Node[][] nodesArray) {
        this.nodesArray = nodesArray;
    }
}
