package Extrapolators;

import Extensions.Node;

public interface Extrapolator {
    Node[][] getExtrapolatedNodesX(Node[][] nodesX, Node[][] nodesC, Node[][] newNodesC, double tau);

    Node[][] getExtrapolatedNodesY(Node[][] nodesY, Node[][] nodesC, Node[][] newNodesC, double tau);
}
