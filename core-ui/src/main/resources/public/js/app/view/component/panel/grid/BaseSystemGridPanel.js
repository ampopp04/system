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
    extend: 'System.view.component.panel.grid.LiveSearchGridPanel',

    requires: [
        'Ext.grid.*', 'Ext.data.*', 'Ext.util.*', 'Ext.state.*', 'Ext.form.*', 'Ext.grid.RowEditor',
        'System.util.data.StoreUtils', 'System.util.data.ModelUtils',
        'System.util.component.GridUtils', 'System.util.component.ToolbarUtils',
        'System.util.component.GridColumnUtils',
        'System.view.component.plugin.AutoSizeColumn'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'base-system-grid-panel',

    modelName: undefined,
    modelOverrides: undefined,

    /**
     * Determines whether or not to add the tools menu to this grid.
     * See System.util.component.ToolbarUtils.addToolsMenu
     */
    disableToolsMenu: false,

    /**
     *  gridColumns is an optional attribute that allows you to specify your own columns
     *  rather than query them from the server and dynamically building them
     */
    gridColumns: undefined,
    additionalGridColumns: undefined,

    detailFormWindow: undefined,
    newEntityDetailWindowDefaultValueFilters: undefined,

    /**
     * If defined this will include the grouping feature
     * and group the store by this field such that it will show
     * a grid with all entities in the grid grouped by that field
     * and expandable under it as a list
     *
     * This can either be a simple string referencing a simple property or an object
     *
     * Ex.
     * groupByField: {
                        property: 'task',
                        entityPropertyPath: 'taskRevision',
                        displayField: 'revisionNumber'
                    }
     *
     * If specified as an object entityPropertyPath and displayField are optional.
     * These parameters allow you to traverse nested objects easily for grouping.
     *
     * You can also specify your own object with the defined property and groupFn function
     * which will completely override any default grouping logic by this implementation.
     *
     * Usually you would use this config as such
     *
     * groupByField: 'name'
     *
     * In the simpliest case, grouping by the field directly on the entity called name
     */
    groupByField: undefined,
    gridSummaryEnabled: false,

    /**
     * By default automatically resize all grid columns to ideally fit the data in the table
     */
    autoSizeColumn: true,

    frame: true,
    plugins: ['gridfilters'],

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
            System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(record, grid.up('window'), this.detailFormWindow);
        }
    },

    /**
     * This method acts as the constructor to each grid that is created and it will
     * dynamically create the grid from the given model name.
     */
    initComponent: function () {
        var me = this;

        /**
         * DISABLE GROUP BY FIELD SINCE KEY LOOKS UP CANNOT
         * WORK ON NESTED COMPLEX OBJECTS FOR NEW ENTITIES...
         *
         * COULD PROBABLY FORCE RELOAD THE STORE BUT WILL DISABLE ALL GROUPING FOR NOW
         */
        me.groupByField = false;

        me.configureFeatures(me);

        if (me.gridColumns) {

            me.configureStore(me.modelName, me.gridColumns);

        } else {

            System.util.component.GridColumnUtils.createEntityGridColumnsFromModelName(me.modelName,
                function (gridColumns, scope) {
                    var me = scope;
                    me.configureStore(me.modelName, gridColumns);
                }, me);

        }

        me.callParent();
    },

    configureFeatures: function (me) {
        if (me.groupByField) {

            if (me.features == undefined) {
                me.features = [];
            }

            me.features.push({
                ftype: me.gridSummaryEnabled ? 'groupingsummary' : 'grouping',
                groupHeaderTpl: [
                    '{children:this.childName} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
                    {
                        grouper: me.createGrouper(me.groupByField, me),
                        childName: function (child) {
                            var childData = child[0];
                            /*      var property = this.owner.metaGroupCache.groupField;
                                  var mainElement = childData.systemPreviousValues ? childData.systemPreviousValues[property] : childData.data ? childData.data : childData;
                                  return mainElement.data.name;*/
                            return this.grouper.groupFn(childData);
                        }
                    }

                ],
                hideGroupedHeader: true,
                id: me.id + '-' + 'grouping'
            });

        }
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

        if (me.additionalGridColumns) {
            columns.push(me.additionalGridColumns);
        }

        me.reconfigure(columns);

        System.util.component.GridUtils.createSystemModelAndStore(modelName, columns, function (store) {

            me.configureAdditionalStoreProperties(store, me);
            me.reconfigure(store);

            me.addDocked([
                System.util.component.ToolbarUtils.defaultAddAndDeleteToolbar(me),
                System.util.component.ToolbarUtils.pagingtoolbar(store)
            ]);

            if (me.autoSizeColumn) {
                me.addPlugin(me.constructPlugin('autosizecolumn'));
            }

        }, me.modelOverrides);

    },

    configureAdditionalStoreProperties: function (store, grid) {
        var groupField = grid.groupByField;

        if (groupField) {
            var storeGrouper = grid.createGrouper(groupField, grid);

            //Add group field if grouping feature is enabled
            store.setGrouper(storeGrouper);
            store.group(storeGrouper);
        }

        //Then eventually remove it in destroy or something???
    },

    createGrouper: function (groupField, grid) {

        //If our groupField is a basic string then let's add a default 'renderer' groupFn
        // to compute what the groupField display text should be
        if (Ext.isString(groupField) || (groupField.groupFn == null && groupField.entityPropertyPath)) {
            return {
                property: groupField.property ? groupField.property : groupField,
                entityPropertyPath: groupField.entityPropertyPath,
                displayField: groupField.displayField ? groupField.displayField : 'name',
                getObjectPropertyByPath: grid.getObjectPropertyByPath,
                groupFn: function (val) {
                    var displayText = val.systemPreviousValues ? val.systemPreviousValues : val;

                    if (Ext.isObject(val)) {
                        var fieldName = this._property ? this._property : this.property;
                        var fieldData = displayText['_' + fieldName] ? displayText['_' + fieldName] : displayText[fieldName] ? displayText[fieldName] : displayText.data[fieldName];

                        displayText = fieldData.data ? this.getObjectPropertyByPath(fieldData.data, this._entityPropertyPath ? this._entityPropertyPath : this.entityPropertyPath)[this.displayField] : fieldData;

                    }

                    return displayText;
                }

            };
        }

        //GroupField is not a string so we can assume it has been overridden
        return groupField;
    },

    getObjectPropertyByPath: function (object, propertyName) {
        if (Ext.isEmpty(propertyName)) {
            return object;
        }

        var parts = propertyName.split("."),
            length = parts.length,
            i,
            property = object || this;

        for (i = 0; i < length; i++) {
            property = property[parts[i]];
        }

        return property ? property : object;
    }

});