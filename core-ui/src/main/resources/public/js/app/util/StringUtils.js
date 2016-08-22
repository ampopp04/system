/**
 * The <class>System.util.StringUtils</class> defines
 *  string utility methods.
 *
 * @author Andrew
 */
Ext.define('System.util.StringUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Convert string to lowercase
         *
         * @param str
         * @returns {string}
         */
        lowercase: function (str) {
            return str.toLowerCase();
        },

        /**
         * Replace fromChar to toChar within the string
         *
         * @param str
         * @param fromChar
         * @param toChar
         * @returns {string}
         */
        replace: function (str, fromChar, toChar) {
            return str.split(fromChar).join(toChar);
        },

        /**
         * Replace all spaces within a string with underscores
         *
         * @param str
         * @returns {*|string}
         */
        replaceSpacesWithUnderscores: function (str) {
            return System.util.StringUtils.replace(System.util.StringUtils.lowercase(str), ' ', '_');
        },

        /**
         * Insert a space before each capital letter in a string
         *
         * @param str
         * @returns {string}
         */
        insertSpaceBeforeCapitals: function (str) {
            return str.replace(/([A-Z])/g, ' $1').trim();
        },

        /**
         * Uncapitalize a string
         *
         * @param str
         * @returns {string}
         */
        uncapitalize: function (str) {
            return str.charAt(0).toLowerCase() + str.substr(1);
        },

        /**
         * Capitalize a string
         *
         * @param str
         * @returns {string}
         */
        capitalize: function (str) {
            return str.charAt(0).toUpperCase() + str.substr(1);
        },

        /**
         * Remove the first character of a string
         *
         * @param str
         * @returns {string}
         */
        removeFirstCharacter: function (str) {
            return str.substr(1);
        },

        /**
         * Boolean expression returning whether
         * the string starts with another string.
         *
         * @param str
         * @param startsWithStr
         * @returns {*}
         */
        startsWith: function (str, startsWithStr) {
            return str.startsWith(startsWithStr);
        }
    }
});