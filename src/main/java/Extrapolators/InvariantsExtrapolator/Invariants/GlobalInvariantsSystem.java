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
    public Node getExtrapolatedNodeX(
            Node nodeL, Node newNodeLC, Node[] limitersL,
            Node nodeR, Node newNodeRC, Node[] limitersR
    ) {
        double
                extrapolatedFromLeftI1 = I1x.limitedExtrapolateBy(nodeL, newNodeLC, limitersL),
                extrapolatedFromRightI2 = I2x.limitedExtrapolateBy(nodeR, newNodeRC, limitersR),
                h = Math.pow((extrapolatedFromLeftI1 - extrapolatedFromRightI2) / 4, 2) / G,
                u = (extrapolatedFromLeftI1 + extrapolatedFromRightI2) / 2,
                v = u > 0 ?
                        I3x.limitedExtrapolateBy(nodeL, newNodeLC, limitersL) :
                        I3x.limitedExtrapolateBy(nodeR, newNodeRC, limitersR);
        return new Node(h, u, v);
    }

    @Override
    public Node getExtrapolatedNodeY(
            Node nodeB, Node newNodeBC, Node[] limitersB,
            Node nodeT, Node newNodeTC, Node[] limitersT
    ) {
        double
                extrapolatedFromBottomI1 = I1y.limitedExtrapolateBy(nodeB, newNodeBC, limitersB),
                extrapolatedFromTopI2 = I2y.limitedExtrapolateBy(nodeT, newNodeTC, limitersT),
                h = Math.pow((extrapolatedFromBottomI1 - extrapolatedFromTopI2) / 4, 2) / G,
                v = (extrapolatedFromBottomI1 + extrapolatedFromTopI2) / 2,
                u = v > 0 ?
                        I3y.limitedExtrapolateBy(nodeB, newNodeBC, limitersB) :
                        I3y.limitedExtrapolateBy(nodeT, newNodeTC, limitersT);
        return new Node(h, u, v);
    }
}
