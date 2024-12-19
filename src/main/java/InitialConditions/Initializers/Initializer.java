package InitialConditions.Initializers;

import Extensions.Nodes.Node;

public interface Initializer {
    Node[][] getInitializedArrayBy(double[] gridX, double[] gridY);
}
