package com.system.util.validation;


import com.system.util.math.MathUtils;
import org.apache.commons.lang3.Validate;

/**
 * The <class>ValidationUtils</class> defines
 * validation related utility methods
 *
 * @author Andrew
 */
public class ValidationUtils {

    /**
     * Asserts n1 greater than n2
     *
     * @param n1
     * @param n2
     */
    public static void assertGreaterThan(Number n1, Number n2) {
        Validate.isTrue(MathUtils.isGreaterThan(n1, n2));
    }

    /**
     * Asserts n1 is equal to n2
     *
     * @param n1
     * @param n2
     */
    public static void assertEqualTo(Number n1, Number n2) {
        Validate.isTrue(MathUtils.isEqual(n1, n2));
    }

    /**
     * Asserts the expression is true
     *
     * @param expression
     */
    public static void assertTrue(final boolean expression) {
        Validate.isTrue(expression);
    }

    /**
     * Asserts that object is not null
     *
     * @param object    the object to check
     * @param message   the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param msgValues the optional values for the formatted exception message
     */
    public static void assertNotNull(Object object, String message, Object... msgValues) {
        Validate.notNull(object, message, msgValues);
    }
}
