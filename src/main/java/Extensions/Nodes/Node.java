package Extensions.Nodes;

public record Node(double h, double u, double v) {
    public static Node getAverage(Node node1, Node node2) {
        return new Node(
                (node1.h() + node2.h()) / 2,
                (node1.u() + node2.u()) / 2,
                (node1.v() + node2.v()) / 2
        );
    }
}
