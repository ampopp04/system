/**
 * The <class>System.view.component.panel.tab.reorderable.ReorderableTabPanel</class> defines
 *  a tab panel that can have tabs that can be moved and reordered
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.tab.reorderable.ReorderableTabPanel', {
    extend: 'Ext.tab.Panel',

    requires: [
        'Ext.ux.TabReorderer',
        'System.view.component.panel.tab.reorderable.ReorderableTabPanelController'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'reorderable-tab-panel', controller: 'reorderable-tab-panel',
    defaults: {bodyPadding: 10, scrollable: true, closable: true},
    plugins: 'tabreorderer', plain: true,

    items: [{
        title: 'Home', iconCls: 'fa fa-home fa-lg tabIcon'
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



