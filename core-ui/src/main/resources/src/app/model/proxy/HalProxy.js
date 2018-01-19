/**
 * The <class>System.model.proxy.HalProxy</class> defines
 * proxy used for reading and writing json messages to and from the server
 * in the hal format
 *
 * @author Andrew
 */
Ext.define('System.model.proxy.HalProxy', {
    extend: 'Ext.data.proxy.Rest',

    requires: [
        'System.util.application.ErrorUtils',
        'System.util.StringUtils',
        'System.model.proxy.reader.HalJson',
        'System.model.proxy.writer.HalJsonWriter'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    alternateClassName: 'System.model.proxy.HalProxy',
    alias: 'proxy.hal',


    type: 'rest',
    baseUrl: 'api/',

    pageParam: 'page',
    limitParam: 'size',
    groupParam: 'sort',

    enablePaging: true,
    startParam: false,
    noCache: false,


    headers: {'Accept': 'application/json'},
    reader: {type: 'halJson'},
    writer: {type: 'halJsonWriter'},

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Constructs the initial proxy with defaults from the provided config
     *
     * @param config
     */
    constructor: function (config) {
        var me = this;

        if (config.completeUrl) {
            me.url = config.completeUrl;
        } else {
            //Cleanup entity name to be uncapitalized without the initial slash
            var urlPostFix = System.util.StringUtils.uncapitalize(
                System.util.StringUtils.removeFirstCharacter(config.url));

            me.entityName = urlPostFix;
            me.url = window.location.origin + '/' + me.baseUrl + urlPostFix;
        }

        me.callParent(config);

        me.api = {read: me.url + '/read'};
    },

    encodeSorters: function (sorters, preventArray) {
        var out = '',
            length = sorters.length,
            i;

        for (i = 0; i < length; i++) {
            if (i > 0) {
                out = out + '&sort=';
            }
            out = out + sorters[i]._property + ',' + sorters[i]._direction;
        }

        return out;
    },

    encodeFilters: function (filters) {
        var out = [],
            length = filters.length,
            i, op;

        for (i = 0; i < length; i++) {
            var property = filters[i]._property;

            if (filters[i].defaultSubRoot) {
                property = filters[i]._root + '.' + filters[i].defaultSubRoot
            }

            var operator = filters[i]._operator;
            var value = filters[i]._value;

            if (!Ext.isEmpty(value)) {
                out[i] = {operator: operator, value: value, property: property};
            }

        }

        return this.applyEncoding(out);
    },

    listeners: {
        /**
         * Monitor for any errors in requests going to and from the server
         * and report them to the user. Usually during a save/delete/update operation
         *
         * @param response
         * @param operation
         * @param eOpts
         */
        exception: function (response, operation, eOpts) {
            System.util.application.ErrorUtils.dataRequestErrorCheck(false, operation.responseText, operation.status, operation);
        }
    }
});