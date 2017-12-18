/**
 * The <class>System.model.proxy.reader.HalJson</class> defines
 * the hal proxy reader for reading json messages in the hal format
 *
 * @author Andrew
 */
Ext.define('System.model.proxy.reader.HalJson', {
    extend: 'Ext.data.reader.Json',

    requires: [
        'System.util.StringUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Properties/Methods                                            //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'reader.halJson',

    totalProperty: 'page.totalElements',

    config: {
        /**
         * When reading the incoming json, when we hit the LINKS we need to translate those into models
         *
         * @param raw
         * @returns {*}
         */
        rootProperty: function (raw) {
            return raw._embedded ? Ext.Object.getValues(raw._embedded)[0] : raw;
        }
    }
});