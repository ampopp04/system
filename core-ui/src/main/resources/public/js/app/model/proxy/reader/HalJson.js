/**
 * The <class>System.model.proxy.reader.HalJson</class> defines
 * the hal proxy reader for reading json messages in the hal format
 *
 * @author Andrew
 */
Ext.define('System.model.proxy.reader.HalJson', {
    extend: 'Ext.data.reader.Json',

    requires: [
        'System.util.application.ErrorUtils',
        'System.util.StringUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Properties/Methods                                            //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'reader.halJson',

    config: {
        /**
         * When reading the incoming json, when we hit the LINKS we need to translate those into models
         *
         * @param raw
         * @returns {*}
         */
        rootProperty: function (raw) {
            var entityName = System.util.StringUtils.uncapitalize(this.getModel().entityName);
            return raw._embedded ? raw._embedded[entityName] : raw;
        }
    },

    listeners: {
        /**
         * Exception handler for read errors
         *
         * @param reader
         * @param response
         * @param error
         * @param eOpts
         */
        exception: function (reader, response, error, eOpts) {
            System.util.application.ErrorUtils.dataRequestErrorCheck(false, error, eOpts);
        }
    }
});