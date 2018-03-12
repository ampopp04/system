/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.system.detail.window.DetailFormWindow</class> defines
 * the basic detail for window. This is the window that is used when drilling into entities.
 *
 * This window would be used when double clicking on a grid row or a combo box to drill into
 * the details of that entity
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.upload.SimpleUploadFormWindow', {
    extend: 'System.view.component.window.tab.TabSystemWindow',

    requires: [
        'System.view.component.panel.upload.SimpleUploadPanel',
        'System.view.component.panel.upload.ExportTemplateUploadPanel'
    ],

    width: null,
    height: null,

    maxHeight: '80%',
    maxWidth: '50%',

    uploadPanelType: 'simple-upload-panel',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'simple-upload-window',

    title: 'Import',

    /**
     * This detail form window primarily consists on the detail form panel
     */
    constructor: function (config) {
        var me = config;

        me.fieldSet = [];

        me.tabs = [{
            xtype: me.uploadPanelType ? me.uploadPanelType : this.uploadPanelType,
            title: me.uploadType,
            uploadType: me.uploadType,
            iconCls: me.iconCls,
            parentScope: me.parentScope,
            fieldSet: []
        }];

        //Clear the iconCls for the window header
        // since it is used by the tab
        me.iconCls = undefined;

        this.callParent([config]);
    }

});