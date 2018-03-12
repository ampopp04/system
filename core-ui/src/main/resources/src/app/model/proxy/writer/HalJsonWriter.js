/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.model.proxy.writer.HalJsonWriter</class> defines
 * the hal proxy writer for encoding json messages in the hal format
 * to the server
 *
 * @author Andrew
 */
Ext.define('System.model.proxy.writer.HalJsonWriter', {
    extend: 'Ext.data.writer.Json',

    requires: [
        'System.util.StringUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Properties/Methods                                            //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'writer.halJsonWriter',

    type: 'json',
    writeAllFields: true,

    transform: function (data, request) {
        return System.util.data.RecordUtils.transformRecordLayout(data, request.getProxy().getModel());
    }

});