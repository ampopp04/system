Ext.define('System.view.main.Main', {
    extend: 'Ext.container.Container',

    plugins: 'viewport',

    xtype: 'app-main',

    requires: [
        'System.view.main.Header',
        'System.view.tab.panel.ReorderableTabPanel',
        'System.view.main.MainController',
        'System.view.main.MainModel',
    ],

    controller: 'main',
    viewModel: {
        type: 'main'
    },

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