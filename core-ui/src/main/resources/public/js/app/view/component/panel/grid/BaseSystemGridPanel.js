/**
 * The <class>System.view.component.panel.grid.BaseSystemGridPanel</class> defines
 * the base system grid panel that will dynamically create a grid with columns for a given
 * model.
 *
 * A model contains all of the fields and information related to a specific entity. We infer
 * the columns and types for the grid from this model data.
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.grid.BaseSystemGridPanel', {
    extend: 'Ext.grid.Panel',

    requires: [
        'Ext.grid.*', 'Ext.data.*', 'Ext.util.*', 'Ext.state.*', 'Ext.form.*', 'Ext.grid.RowEditor',
        'System.util.data.StoreUtils', 'System.util.data.ModelUtils',
        'System.util.component.GridUtils', 'System.util.component.ToolbarUtils',
        'System.util.component.GridColumnUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'base-system-grid-panel',

    modelName: 'UNDEFINED_MODEL_NAME',
    frame: true,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    listeners: {
        /**
         * When a row within the grid is double clicked we will open the DetailFormWindow
         * with the record corresponding to that grid row data.
         *
         * @param grid
         * @param record
         */
        itemdblclick: function (grid, record) {
            var win = Ext.create("System.view.system.detail.window.DetailFormWindow", {
                title: System.util.data.ModelUtils.modelNameToDisplayName(record.store.model.entityName),
                viewModel: {
                    data: {
                        rec: record
                    }
                }
            });

            win.show();
        }
    },

    /**
     * This method acts as the constructor to each grid that is created and it will
     * dynamically create the grid from the given model name.
     */
    initComponent: function () {
        var me = this;

        System.util.component.GridColumnUtils.createEntityGridColumnsFromModelName(me.modelName,
            function (gridColumns, scope) {
                var me = scope;
                me.configureStore(me.modelName, gridColumns);
            }, me);

        me.configure();
        me.callParent();
    },

    /**
     * The store and model might not be created yet so we must create it and then
     * repopulate the grid
     *
     * @param modelName
     * @param columns
     */
    configureStore: function (modelName, columns) {
        var me = this;
        System.util.component.GridUtils.createSystemModelAndStore(modelName, columns, function (store) {
            me.reconfigure(store, columns);
            me.bbar = System.util.component.ToolbarUtils.pagingtoolbar(System.util.data.StoreUtils.getStoreName(modelName));
        });
    },

    /**
     * Add our default docked items and selection type
     */
    configure: function () {
        var me = this;
        me.dockedItems = [System.util.component.ToolbarUtils.defaultAddAndDeleteToolbar(this)];
        me.selType = System.util.component.GridUtils.rowSelectionModel();
        //   me.plugins = System.util.component.GridUtils.getDefaultGridPlugins();
    }
});