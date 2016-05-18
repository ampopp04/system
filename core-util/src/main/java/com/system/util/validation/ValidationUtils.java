package com.system.util.validation;


import com.system.util.math.MathUtils;
import org.apache.commons.lang3.Validate;


public class ValidationUtils {
    public static void assertGreaterThan(Number n1, Number n2) {
        Validate.isTrue(MathUtils.isGreaterThan(n1, n2));
    }
}
