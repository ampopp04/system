/**
 * The <class>System.model.base.BaseModel</class> defines
 * base data model to be used by all data entities in the system.
 *
 * This class defines the Hal communication proxy to use to talk to the server.
 *
 * @author Andrew
 */
Ext.define('System.model.base.BaseModel', {
    extend: 'Ext.data.Model',

    requires: [
        'System.model.proxy.HalProxy'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    identifier: 'sequential',

    schema: {
        namespace: 'System.model',

        proxy: {
            type: 'hal'
        }
    }
});