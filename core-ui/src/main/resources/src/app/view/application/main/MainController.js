/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.application.main.MainController</class> defines
 * the event handlers for the main view.
 *
 * @author Andrew
 */
Ext.define('System.view.application.main.MainController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'System.util.data.ModelUtils',
        'System.util.component.GridColumnUtils',
        'System.util.data.StoreUtils',
        'System.util.system.UiComponentUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'controller.main'
});