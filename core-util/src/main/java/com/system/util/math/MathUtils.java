package com.system.util.math;

/**
 * The <class>MathUtils</class> defines
 * math related utility methods
 *
 * @author Andrew
 */
public class MathUtils {

    /**
     * Returns n1 > n2
     *
     * @param n1
     * @param n2
     * @return
     */
    public static boolean isGreaterThan(Number n1, Number n2) {
        return (1 == compare(n1, n2));
    }

    /**
     * Determines if n1 is equal to n2
     *
     * @param n1
     * @param n2
     * @return
     */
    public static boolean isEqual(Number n1, Number n2) {
        return (0 == compare(n1, n2));
    }

    /**
     * Compares n1 to n2
     *
     * @param n1
     * @param n2
     * @return
     */
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
