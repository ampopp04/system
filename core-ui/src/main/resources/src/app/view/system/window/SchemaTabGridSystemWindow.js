/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.system.window.SchemaTabGridSystemWindow</class> defines
 *  a tabed grid system window. This window accepts a title and an array of table names.
 *
 *  These table names are iterated over and the tabs and grids are queried/constructed with
 *  data queried from the server.
 *
 * @author Andrew
 */
Ext.define('System.view.system.window.SchemaTabGridSystemWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'schema-tab-grid-system-window',

    //Required or Optional Parameters
    // title: 'DEFINE-ME',//tableNames: [],
    detailFormWindow: undefined,

    listeners: {

        /**
         * When we show this window add the tabs for
         * each table
         */
        beforerender: function (window, eOpts) {
            var me = window;

            if (me.tabs == undefined || me.tabs.length < 1) {
                me.addTabsByTableNames(me.tableNames);
            }
        }

    },

    /**
     * Iterate over each table and create it's
     * associated tab
     *
     * @param tableNames
     */
    addTabsByTableNames: function (tableNames) {
        Ext.suspendLayouts();

        var me = this;

        var tabPanel = me.items.items[0];

        tabPanel.on('tabchange', function (tabPanel, newCard, oldCard, eOpts) {
            var me = this;
            if (!newCard.systemConfigured) {
                me.processTableName(newCard, me);
            }
        }, me);

        tableNames.forEach(function (tableName) {
            me.addTabByTableName(tableName);
        }, this);

        Ext.resumeLayouts(true);
    },

    /**
     * Process a specific table name. Query the
     * server for the data needed to dynamically
     * create the grid within each tab.
     *
     * @param tableName
     */
    processTableName: function (newTabCard, scope) {
        System.util.system.SchemaUtils.retrieveSchemaTableByName(newTabCard.tableName, function (table, scope) {
            System.util.system.UiComponentUtils.retrieveComponentByAssignment('UiComponentAssignments', table.name, table.id, 'Panel', function (uiComponent, scope) {
                scope.processUiComponentTableTab(newTabCard, uiComponent, scope);
            }, scope);
        }, scope);
    },

    /**
     * Process the UI Component that was assigned to this entities table.
     *
     * @param uiComponent
     * @param scope
     */
    processUiComponentTableTab: function (newTabCard, uiComponent, scope) {
        var me = this;

        var index = scope.tableNames.indexOf(System.util.data.ModelUtils.modelNameToSchemaTableName(uiComponent.modelName));
        scope.tabs.splice(index, 0, uiComponent);

        var newTabItem = me.createTabItem(uiComponent, me.detailFormWindow);

        if (newTabItem.iconCls) {
            newTabCard.setIconCls(newTabItem.iconCls);
            delete newTabItem.iconCls;
        }

        delete newTabItem.title;

        newTabCard.add(newTabItem);
        newTabCard.systemConfigured = true;
    },

    addTabByTableName: function (tableName) {
        var me = this;

        var tabPanel = me.items.items[0];

        tabPanel.add({
            title: System.util.data.ModelUtils.schemaTableNameToDisplayName(tableName),
            tableName: tableName,
            systemConfigured: false,
            layout: {type: 'fit'}
        });

        if (tabPanel.getActiveTab() == undefined) {
            tabPanel.setActiveTab(0);
        }

    }

});