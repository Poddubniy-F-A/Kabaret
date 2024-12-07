package Extrapolators.InvariantsExtrapolator.Invariants;

import Extensions.Node;

import static Extensions.Constants.G;

public class GlobalInvariantsSystem implements ExtrapolatableInvariantsSystem {
    private static final ExtrapolatableInvariant
            I1x = (node, _) -> node.u() + 2 * Math.sqrt(G * node.h()),
            I2x = (node, _) -> node.u() - 2 * Math.sqrt(G * node.h()),
            I3x = (node, _) -> node.v(),
            I1y = (node, _) -> node.v() + 2 * Math.sqrt(G * node.h()),
            I2y = (node, _) -> node.v() - 2 * Math.sqrt(G * node.h()),
            I3y = (node, _) -> node.u();

    @Override
    public Node getExtrapolatedNodeX(Node nodeL, Node newNodeLC, Node nodeR, Node newNodeRC) {
        double
                extrapolatedFromLeftI1 = I1x.extrapolateBy(nodeL, newNodeLC),
                extrapolatedFromRightI2 = I2x.extrapolateBy(nodeR, newNodeRC),
                h = Math.pow((extrapolatedFromLeftI1 - extrapolatedFromRightI2) / 4, 2) / G,
                u = (extrapolatedFromLeftI1 + extrapolatedFromRightI2) / 2,
                v = u > 0 ? I3x.extrapolateBy(nodeL, newNodeLC) : I3x.extrapolateBy(nodeR, newNodeRC);
        return new Node(h, u, v);
    }

    @Override
    public Node getExtrapolatedNodeY(Node nodeB, Node newNodeBC, Node nodeT, Node newNodeTC) {
        double
                extrapolatedFromBottomI1 = I1y.extrapolateBy(nodeB, newNodeBC),
                extrapolatedFromTopI2 = I2y.extrapolateBy(nodeT, newNodeTC),
                h = Math.pow((extrapolatedFromBottomI1 - extrapolatedFromTopI2) / 4, 2) / G,
                v = (extrapolatedFromBottomI1 + extrapolatedFromTopI2) / 2,
                u = v > 0 ? I3y.extrapolateBy(nodeB, newNodeBC) : I3y.extrapolateBy(nodeT, newNodeTC);
        return new Node(h, u, v);
    }
}
