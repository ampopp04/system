package com.system.util.string;

import java.beans.Introspector;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import static com.system.util.collection.CollectionUtils.iterable;

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
     * Lowercase the first character of the given string
     *
     * @param str
     * @return
     */
    public static String decapitalize(String str) {
        return Introspector.decapitalize(str);
    }

    /**
     * Uppercase the first character of the given string
     *
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        return org.apache.commons.lang3.StringUtils.capitalize(str);
    }

    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     * <p>
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     * <p>
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     * {@code null} if null String input
     * @see #replace(String text, String searchString, String replacement, int max)
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return org.apache.commons.lang3.StringUtils.replace(text, searchString, replacement);
    }

    /**
     * Lower case a given string
     *
     * @param str
     * @return
     */
    public static String lowercase(String str) {
        return org.apache.commons.lang3.StringUtils.lowerCase(str);
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
     * Return everything after search string.
     * In other words remove everything up to the end of the search string.
     * <p>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     *
     * @param str
     * @param searchString
     * @return
     */
    public static String removeBeforeInclusive(String str, String searchString) {
        return org.apache.commons.lang3.StringUtils.substringAfter(str, searchString);
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


    /**
     * Converts a comma delimited string list to a set collection
     *
     * @param str
     * @return
     */
    public static Set<String> commaDelimitedListToSet(String str) {
        Set<String> set = new LinkedHashSet<String>();
        String[] tokens = org.apache.commons.lang3.StringUtils.split(str, ",");
        for (String token : tokens) {
            set.add(token);
        }
        return set;
    }

    /**
     * Returns whether this string starts with the given prefix
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
        return org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(str, prefix);
    }

    /**
     * Converts an array to a string representation of that array. The function is apply to each element of
     * the array and it's result is concated into the string array.
     * <p>
     * Ex.
     * ['SchemaTable', 'SchemaTableColumn', 'SchemaTableColumnRelationship', 'SchemaDataType']
     *
     * @param array
     * @param function
     * @param <E>
     * @return
     */
    public static <E> String arrayToString(E[] array, Function<E, ?> function) {
        final StringBuffer result = new StringBuffer();
        result.append('[');
        Iterator<E> itr = iterable(array).iterator();
        while (itr.hasNext()) {
            result.append("'").append(function.apply(itr.next())).append("'");
            if (itr.hasNext()) {
                result.append(',');
            }
        }
        result.append(']');

        return result.toString();
    }

    /**
     * Adds spaces before every capitial letter
     * <p>
     * ex. "SystemBeanDefinitionType" -> "System Bean Definition Type"
     *
     * @param str
     * @return
     */
    public static String addSpaceOnCapitialLetters(String str) {
        return str.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
    }
}
