package Extrapolators;

import Extensions.Node;

public interface Extrapolator {
    Node[][] getExtrapolatedNodesX(Node[][] newNodesCArray, double tau);

    Node[][] getExtrapolatedNodesY(Node[][] newNodesCArray, double tau);
}
