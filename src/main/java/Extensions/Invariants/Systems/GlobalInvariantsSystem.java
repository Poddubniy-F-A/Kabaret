package Extensions.Invariants.Systems;

import Extensions.Invariants.Extrapolator.InvariantsExtrapolator;
import Extensions.Node;

import java.util.Map;

import static Extensions.Constants.G;

public class GlobalInvariantsSystem implements ExtrapolatableInvariantsSystem {
    private static final InvariantsExtrapolator invariantsExtrapolator = new InvariantsExtrapolator(Map.of(
            "I1x", (node, _) -> node.u() + 2 * Math.sqrt(G * node.h()),
            "I2x", (node, _) -> node.u() - 2 * Math.sqrt(G * node.h()),
            "I3x", (node, _) -> node.v(),
            "I1y", (node, _) -> node.v() + 2 * Math.sqrt(G * node.h()),
            "I2y", (node, _) -> node.v() - 2 * Math.sqrt(G * node.h()),
            "I3y", (node, _) -> node.u()
    ));

    @Override
    public Node getExtrapolatedNodeX(
            Node nodeL, Node nodeLC, Node nodeMid, Node nodeRC, Node nodeR,
            Node newNodeLC, Node newNodeRC,
            double dxL, double dxR, double tau
    ) {
        double
                extrapolatedFromLeftI1 = invariantsExtrapolator.limitedExtrapolateBy(
                "I1x", nodeL, newNodeLC, nodeL, nodeLC, nodeMid, tau, dxL
                ),
                extrapolatedFromRightI2 = invariantsExtrapolator.limitedExtrapolateBy(
                        "I2x", nodeR, newNodeRC, nodeMid, nodeRC, nodeR, tau, dxR
                ),
                h = Math.pow((extrapolatedFromLeftI1 - extrapolatedFromRightI2) / 4, 2) / G,
                u = (extrapolatedFromLeftI1 + extrapolatedFromRightI2) / 2,
                v = u > 0 ? invariantsExtrapolator.limitedExtrapolateBy(
                        "I3x", nodeL, newNodeLC, nodeL, nodeLC, nodeMid, tau, dxL
                ) : invariantsExtrapolator.limitedExtrapolateBy(
                        "I3x", nodeR, newNodeRC, nodeMid, nodeRC, nodeR, tau, dxR
                );
        return new Node(h, u, v);
    }

    @Override
    public Node getExtrapolatedNodeY(
            Node nodeB, Node nodeBC, Node nodeMid, Node nodeTC, Node nodeT,
            Node newNodeBC, Node newNodeTC,
            double dyB, double dyT, double tau
    ) {
        double
                extrapolatedFromBottomI1 = invariantsExtrapolator.limitedExtrapolateBy(
                        "I1y", nodeB, newNodeBC, nodeB, nodeBC, nodeMid, tau, dyB
                ),
                extrapolatedFromTopI2 = invariantsExtrapolator.limitedExtrapolateBy(
                        "I2y", nodeT, newNodeTC, nodeMid, nodeTC, nodeT, tau, dyT
                ),
                h = Math.pow((extrapolatedFromBottomI1 - extrapolatedFromTopI2) / 4, 2) / G,
                v = (extrapolatedFromBottomI1 + extrapolatedFromTopI2) / 2,
                u = v > 0 ? invariantsExtrapolator.limitedExtrapolateBy(
                        "I3y", nodeB, newNodeBC, nodeB, nodeBC, nodeMid, tau, dyB
                ) : invariantsExtrapolator.limitedExtrapolateBy(
                        "I3y", nodeT, newNodeTC, nodeMid, nodeTC, nodeT, tau, dyT
                );
        return new Node(h, u, v);
    }
}
