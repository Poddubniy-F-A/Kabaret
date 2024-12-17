package Extensions.Invariants;

import Extensions.Node;

@FunctionalInterface
public interface Invariant {
    double I(Node node, Node nodeC);
}
