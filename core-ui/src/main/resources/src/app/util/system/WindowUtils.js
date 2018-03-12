/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.util.system.WindowUtils</class> defines
 *  utility methods for working with windows.
 *
 * @author Andrew
 */
Ext.define('System.util.system.WindowUtils', {

    requires: ['System.view.system.window.SchemaTabGridSystemWindow'],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Create a schema tab grid system window
         *
         * @param title
         * @param tableNames
         */
        createSchemaTabGridSystemWindow: function (title, tableNames) {
            var win = Ext.create("System.view.system.window.SchemaTabGridSystemWindow", {
                title: title,
                tableNames: tableNames
            });

            win.show();
        }
    }
});