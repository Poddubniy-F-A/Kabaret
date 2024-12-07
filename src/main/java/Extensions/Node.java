package Extensions;

public record Node(double h, double u, double v) {
    public static Node getLimitedNode(Node original, Node[] limiters) {
        Node firstLimiter = limiters[0];
        double
                hMin = firstLimiter.h(), hMax = firstLimiter.h(),
                uMin = firstLimiter.u(), uMax = firstLimiter.u(),
                vMin = firstLimiter.v(), vMax = firstLimiter.v();
        for (int i = 1; i < limiters.length; i++) {
            Node curLimiter = limiters[i];
            double curH = curLimiter.h(), curU = curLimiter.u(), curV = curLimiter.v();
            hMin = Math.min(hMin, curH);
            hMax = Math.max(hMax, curH);
            uMin = Math.min(uMin, curU);
            uMax = Math.max(uMax, curU);
            vMin = Math.min(vMin, curV);
            vMax = Math.max(vMax, curV);
        }

        return new Node(
                limitedValue(original.h(), hMin, hMax),
                limitedValue(original.u(), uMin, uMax),
                limitedValue(original.v(), vMin, vMax)
        );
    }

    private static double limitedValue(double original, double min, double max) {
        return Math.min(max, Math.max(min, original));
    }
}
