package com.system.util.string;

import java.beans.Introspector;
import java.io.File;

/**
 * The <class>StringUtils</class> defines
 * string related utility methods
 *
 * @author Andrew
 */
public class StringUtils {

    /**
     * <p>Checks if CharSequence contains a search CharSequence, handling {@code null}.
     * This method uses {@link String#indexOf(String)} if possible.</p>
     * <p>
     * <p>A {@code null} CharSequence will return {@code false}.</p>
     * <p>
     * <pre>
     * StringUtils.contains(null, *)     = false
     * StringUtils.contains(*, null)     = false
     * StringUtils.contains("", "")      = true
     * StringUtils.contains("abc", "")   = true
     * StringUtils.contains("abc", "a")  = true
     * StringUtils.contains("abc", "z")  = false
     * </pre>
     *
     * @param str       the CharSequence to check, may be null
     * @param searchStr the CharSequence to find, may be null
     * @return true if the CharSequence contains the search CharSequence,
     * false if not or {@code null} string input
     */
    public static boolean contains(String str, String searchStr) {
        return org.apache.commons.lang3.StringUtils.contains(str, searchStr);
    }

    /**
     * Returns back the string that is everything after the last DOT ( . )
     *
     * @param str
     * @return
     */
    public static String substringAfterLastDot(String str) {
        return org.apache.commons.lang3.StringUtils.substringAfterLast(str, ".");
    }

    /**
     * Lowercase the first characters of the given string
     *
     * @param str
     * @return
     */
    public static String decapitalize(String str) {
        return Introspector.decapitalize(str);
    }


    /**
     * Substring provided string such that the substring starts at the provided
     * searchString up until the end of the string
     * <p>
     * Ex. removeBefore("spring.jpa.hibernate.ejb.naming_strategy","hibernate")
     * return "hibernate.ejb.naming_strategy"
     *
     * @param str
     * @param searchString
     * @return
     */
    public static String removeBefore(String str, String searchString) {
        if (str == null || searchString == null) {
            return null;
        }
        return str.substring(str.indexOf(searchString));
    }

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
