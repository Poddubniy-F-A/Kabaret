package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

public interface ExtrapolatableInvariant {
    double I(Node node, Node nodeC);

    default double extrapolateBy(Node node, Node nodeC) {
        return I(nodeC, nodeC) + (I(nodeC, nodeC) - I(node, nodeC));
    }
}
