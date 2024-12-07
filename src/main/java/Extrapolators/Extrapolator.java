package Extrapolators;

import Extensions.Node;

public interface Extrapolator {
    Node[][] getNodesX(Node[][] nodesX, Node[][] nodesC, Node[][] newNodesC);

    Node[][] getNodesY(Node[][] nodesY, Node[][] nodesC, Node[][] newNodesC);
}
