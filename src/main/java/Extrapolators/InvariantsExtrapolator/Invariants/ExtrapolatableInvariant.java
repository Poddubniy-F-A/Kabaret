package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

public interface ExtrapolatableInvariant {
    double getEigenvalue(Node node);

    double I(Node node, Node nodeC);

    default double extrapolateBy(Node node, Node nodeC) {
        return 2 * I(nodeC, nodeC) - I(node, nodeC);
    }

    default double limitedExtrapolateBy(
            Node node, Node nodeC,
            Node limiter1, Node limiterC, Node limiter2,
            double tau, double dh
    ) {
        double add = tau * ((I(nodeC, nodeC) - I(limiterC, nodeC)) / (tau / 2) +
                getEigenvalue(nodeC) * (I(limiter2, nodeC) - I(limiter1, nodeC)) / dh);
        return Math.min(
                Math.max(I(limiter1, nodeC), Math.max(I(limiterC, nodeC), I(limiter2, nodeC))) + add,
                Math.max(
                        Math.min(I(limiter1, nodeC), Math.min(I(limiterC, nodeC), I(limiter2, nodeC))) + add,
                        extrapolateBy(node, nodeC)
                )
        );
    }
}
