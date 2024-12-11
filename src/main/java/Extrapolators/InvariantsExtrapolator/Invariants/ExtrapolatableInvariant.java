package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

public interface ExtrapolatableInvariant {
    double I(Node node, Node nodeC);

    default double extrapolateBy(Node node, Node nodeC) {
        return 2 * I(nodeC, nodeC) - I(node, nodeC);
    }

    default double limitedExtrapolateBy(Node node, Node nodeC, Node[] limiters) {
        double min = I(limiters[0], nodeC), max = min;
        for (int i = 1; i < limiters.length; i++) {
            double val = I(limiters[i], nodeC);
            min = Math.min(min, val);
            max = Math.max(max, val);
        }

        return Math.min(max, Math.max(min, extrapolateBy(node, nodeC)));
    }
}
