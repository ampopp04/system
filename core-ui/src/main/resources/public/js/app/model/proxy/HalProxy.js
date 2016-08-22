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
        'System.util.StringUtils',
        'System.model.proxy.reader.HalJson'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    alternateClassName: 'System.model.proxy.HalProxy',
    alias: 'proxy.hal',

    type: 'rest',
    baseUrl: 'http://localhost:8080/api/',

    pageParam: false,
    startParam: false,
    limitParam: false,
    noCache: false,

    headers: {'Accept': 'application/json'},
    reader: {type: 'halJson'},
    writer: {type: 'json', writeAllFields: true},

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

        //Cleanup entity name to be uncapitalized without the initial slash
        var urlPostFix = System.util.StringUtils.uncapitalize(
            System.util.StringUtils.removeFirstCharacter(config.url))

        me.entityName = urlPostFix;
        me.url = me.baseUrl + urlPostFix;

        me.callParent(config);
    }
});