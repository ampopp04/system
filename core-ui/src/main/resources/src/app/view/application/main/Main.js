/**
 * The <class>System.view.application.main.Main</class> defines
 * the basic main structure of the view area. This include the top application header
 * and the center reorderable tab panel.
 *
 * @author Andrew
 */
Ext.define('System.view.application.main.Main', {
    extend: 'Ext.container.Container',

    requires: [
        'Ext.container.Viewport',
        'System.view.application.main.header.*',
        'System.view.component.panel.tab.reorderable.ReorderableTabPanel',
        'System.view.application.main.MainController'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    style: 'background-image: none; background-color: #F5F5F5;',
    plugins: 'viewport',
    xtype: 'app-main',
    controller: 'main',
    layout: {type: 'border'},

    initComponent: function () {
        this.callParent(arguments);
        Ext.resumeLayouts(true);
    },

    ///////////////////////////////////////////////////////////////////////
    ////////                                                           Items                                                             //////////
    //////////////////////////////////////////////////////////////////////

    items: [
        {
            xtype: 'appheader',
            region: 'north'
        },
        {
            region: 'center',
            xtype: 'reorderable-tab-panel'
        }]
});