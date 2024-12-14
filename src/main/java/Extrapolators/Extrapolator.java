package Extrapolators;

import Extensions.Node;

public interface Extrapolator {
    Node[][] getExtrapolatedNodesX(Node[][] newNodesC, double tau);

    Node[][] getExtrapolatedNodesY(Node[][] newNodesC, double tau);
}
