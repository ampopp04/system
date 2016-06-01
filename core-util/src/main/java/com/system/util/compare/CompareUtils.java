package com.system.util.compare;

import java.util.Objects;

/**
 * The <class>ClassUtils</class> defines
 * comparison related utility methods
 *
 * @author Andrew
 */
public class CompareUtils {

    /**
     * Return if two objects are equal
     *
     * @return
     */
    public static boolean equals(Object o1, Object o2) {
        return Objects.deepEquals(o1, o2);
    }
}
