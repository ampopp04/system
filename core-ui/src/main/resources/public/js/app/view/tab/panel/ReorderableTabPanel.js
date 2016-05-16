Ext.define('System.view.tab.panel.ReorderableTabPanel', {
    extend: 'Ext.tab.Panel',

    xtype: 'reorderable-tab-panel',

    requires: [
        'Ext.ux.TabReorderer',
        'System.view.tab.panel.ReorderableTabPanelController'
    ],

    controller: 'reorderable-tab-panel',

    plugins: 'tabreorderer',
    plain: true,

    defaults: {
        bodyPadding: 10,
        scrollable: true,
        closable: true
    },

    items: [{
        title: 'Home',
        iconCls: 'fa fa-home fa-lg tabIcon'
    }],

    tabBar: {
        items: [
            {
                closable: false,
                iconCls: 'x-fa fa-plus',
                listeners: {
                    click: 'onAddTabClick'
                }
            }]
    }
});



