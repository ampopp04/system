package com.system.util.math;

import org.junit.Test;

import static com.system.util.validation.ValidationUtils.*;

/**
 * The <class>MathUtilsTests</class> defines
 * math related tests
 *
 * @author Andrew
 */
public class MathUtilsTests {

    @Test
    public void testEquals() {
        assertEqualTo(1, 1);
        assertTrue(MathUtils.isEqual(1, 1));
    }

    @Test
    public void testGreaterThan() {
        assertGreaterThan(2, 1);
        assertTrue(MathUtils.isGreaterThan(2, 1));
    }

    @Test
    public void testCompare() {
        assertEqualTo(MathUtils.compare(1, 1), 0);
        assertEqualTo(MathUtils.compare(2, 1), 1);
        assertEqualTo(MathUtils.compare(1, 2), -1);
    }
}