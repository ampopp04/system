/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.window.tab.grid.TabGridSystemWindow</class> defines
 *  a basic tabbed window that inserts a base system grid panel within each tab.
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.tab.grid.TabGridSystemWindow', {
    extend: 'System.view.component.window.tab.TabSystemWindow',

    requires: [
        'System.view.component.panel.grid.BaseSystemGridPanel',
        'System.view.component.panel.grid.ParentSearchCallerGridPanel'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'tab-grid-system-window',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Initialize each of the tabs with a base system grid panel
     */
    constructor: function (config) {
        Ext.suspendLayouts();

        var me = config ? config : this;

        if (me.tabs) {
            var updatedTabs = [];

            me.tabs.forEach(function (tab) {

                updatedTabs.push(this.createTabItem(tab));

            }, this);

            me.tabs = updatedTabs;
        }

        this.callParent([me]);

        Ext.resumeLayouts(true);
    },

    createTabItem: function (tab, detailFormWindow) {
        var xtypeValue = tab.parentSearchCaller ? 'parent-search-caller-grid-panel' : 'base-system-grid-panel';

        return Ext.apply(tab, {
            parentSearchCaller: tab.parentSearchCaller,
            xtype: xtypeValue,
            modelName: tab.modelName,
            detailFormWindow: detailFormWindow
        });

    }

});



