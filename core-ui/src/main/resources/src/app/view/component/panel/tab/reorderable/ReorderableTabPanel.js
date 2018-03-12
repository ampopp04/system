/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.panel.tab.reorderable.ReorderableTabPanel</class> defines
 *  a tab panel that can have tabs that can be moved and reordered
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.tab.reorderable.ReorderableTabPanel', {
    extend: 'Ext.tab.Panel',

    requires: [
        'System.view.component.panel.tab.reorderable.ReorderableTabPanelController'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////
    xtype: 'reorderable-tab-panel', controller: 'reorderable-tab-panel',
    defaults: {bodyPadding: 10, scrollable: false, closable: false},
    plain: true,

    items: [{
        title: 'Workspace', iconCls: 'fa fa-home fa-lg tabIcon'
    }],

    tabBar: {
        items: [
            /*{
                closable: false,
                iconCls: 'x-fa fa-plus',
                listeners: {
                    click: 'onAddTabClick'
                }
            }*/]
    }
});



