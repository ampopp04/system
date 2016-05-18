package com.system.util.math;


public class MathUtils {

    public static boolean isGreaterThan(Number n1, Number n2) {
        return (1 == compare(n1, n2));
    }

    public static int compare(Number n1, Number n2) {
        if (n1 == null) {
            if (n2 == null) {
                return 0;
            }
            return -1;
        }
        if (n2 == null) {
            return 1;
        }
        double l1 = n1.doubleValue();
        double l2 = n2.doubleValue();

        if (l1 < l2) {
            return -1;
        }
        if (l1 > l2) {
            return 1;
        }
        return 0;
    }
}
