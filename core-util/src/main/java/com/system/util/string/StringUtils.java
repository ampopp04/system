package com.system.util.string;

import java.io.File;

/**
 * The <class>StringUtils</class> defines
 * string related utility methods
 *
 * @author Andrew
 */
public class StringUtils {

    /**
     * Converts file line separators to a period
     *
     * @param str
     * @return
     */
    public static String lineSeparatorToPeriod(String str) {
        return str.replace(File.separator, ".");
    }
}
