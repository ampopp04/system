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

    // title: 'DEFINE-ME',//tableNames: [],

    /**
     * When we show this window add the tabs for
     * each table
     */
    onShow: function () {
        var me = this;

        if (me.tabs.length < 1) {
            me.addTabsByTableNames(me.tableNames);
        }

        me.callParent();
    },

    /**
     * Iterate over each table and create it's
     * associated tab
     *
     * @param tableNames
     */
    addTabsByTableNames: function (tableNames) {
        var me = this;

        tableNames.forEach(function (tableName) {
            me.processTableName(tableName);
        }, this);
    },

    /**
     * Process a specific table name. Query the
     * server for the data needed to dynamically
     * create the grid within each tab.
     *
     * @param tableName
     */
    processTableName: function (tableName) {
        System.util.system.SchemaUtils.retrieveSchemaTableByName(tableName, function (table, scope) {
            System.util.system.UiComponentUtils.retrieveComponentByAssignment('UiComponentAssignments', table.name, table.id, 'Panel', function (uiComponent, scope) {
                scope.processUiComponentTableTab(uiComponent, scope);
            }, scope);
        }, this);
    },

    /**
     * Process the UI Component that was assigned to this entities table.
     *
     * @param uiComponent
     * @param scope
     */
    processUiComponentTableTab: function (uiComponent, scope) {
        var me = this;

        var index = scope.tableNames.indexOf(System.util.data.ModelUtils.modelNameToSchemaTableName(uiComponent.modelName));
        scope.tabs.splice(index, 0, uiComponent)

        me.addTab(uiComponent);
    },

    /**
     * Add a given tab to the container
     *
     * @param tab
     */
    addTab: function (tab) {
        var me = this;
        me.items.items[0].add(Ext.apply(tab, {
            xtype: 'base-system-grid-panel'
        }));
    }
});



