Ext.define('System.view.main.Main', {
    extend: 'Ext.container.Container',

    plugins: 'viewport',

    xtype: 'app-main',

    requires: [
        'System.view.main.header.Header',
        'System.view.tab.panel.ReorderableTabPanel',
        'System.view.main.MainController'
    ],

    controller: 'main',

    layout: {
        type: 'border'
    },

    style: 'background-image: none; background-color: #F5F5F5;',

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