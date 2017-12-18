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
    initComponent: function () {
        var me = this;

        this.config.fieldSet = [];

        me.tabs = [{
            xtype: me.config.uploadPanelType ? me.config.uploadPanelType : this.uploadPanelType,
            title: me.config.uploadType,
            uploadType: me.config.uploadType,
            iconCls: me.config.iconCls,
            parentScope: me.config.parentScope,
            fieldSet: []
        }];

        //Clear the iconCls for the window header
        // since it is used by the tab
        me.config.iconCls = undefined;
        me.initialConfig.iconCls = undefined;
        me.header.iconCls = null;

        me.callParent();
    }
});