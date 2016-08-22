/**
 * The <class>System.util.component.GridColumnUtils</class> defines
 * utility methods for managing and dynamically creating grid columns
 *
 * @author Andrew
 */
Ext.define('System.util.component.GridColumnUtils', {

    requires: [
        'System.util.system.SchemaUtils',
        'System.util.data.StoreUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Return a store for a specific model name into the callback provided
         *
         * @param modelName
         * @param storeCallback
         */
        getStoreByModelName: function (modelName, storeCallback) {
            if (modelName != null && !Ext.ClassManager.isCreated(modelName)) {
                System.util.component.GridColumnUtils.createEntityGridColumnsFromModelName(modelName,
                    function (gridColumns) {
                        System.util.data.StoreUtils.createSystemStoreFromColumns(modelName, gridColumns, storeCallback);
                    });
            }
        },

        /**
         * Create the grid columns for a specific entity
         *
         * @param modelName
         * @param columnCompleteCallback
         * @param scope
         */
        createEntityGridColumnsFromModelName: function (modelName, columnCompleteCallback, scope) {
            System.util.component.GridColumnUtils.createEntityGridColumnsFromSchemaTableName(modelName.slice(0, -1), columnCompleteCallback, scope);
        },

        /**
         * Create the basic hidden ID column
         *
         * @returns {{text: string, dataIndex: string, flex: number, renderer: renderer, hidden: boolean}}
         */
        createIdGridColumn: function () {
            return {
                text: 'ID',
                dataIndex: 'id',
                flex: 1,
                renderer: function (v, meta, rec) {
                    return rec.phantom ? '' : v;
                },
                hidden: true
            };
        },

        /**
         * Create the grid columns as textfields for each of the entities fields
         *
         * @param column
         * @returns {{text: *, dataIndex: *, flex: number, reference: undefined, editor: string, renderer: renderer}}
         */
        createGridColumn: function (column) {
            var modelName = undefined;
            if (column.schemaTableColumnRelationship) {
                modelName = System.util.data.ModelUtils.columnNameToModelName(column.name);
                System.util.component.GridColumnUtils.getStoreByModelName(modelName,
                    function (store) {
                        //we are referring to the model as a reference but it may have never been created
                        console.log(store);
                    });
            }
            return {
                text: column.name,
                dataIndex: column.name,
                flex: 1,
                reference: modelName,
                editor: 'textfield',
                renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                    if (metaData.column.reference) {
                        var recordData = record["_" + metaData.column.dataIndex].data;
                        return recordData.name ? recordData.name : recordData.id;
                    }
                    return value;
                }
            };
        },

        /**
         * For a set of Schema Table Columns
         * convert this meta-data into the actual grid columns
         *
         * @param schemaTableColumnList
         * @returns {Array}
         */
        processTableSchemaColumnList: function (schemaTableColumnList) {
            var gridColumns = [];
            if (schemaTableColumnList) {
                schemaTableColumnList.forEach(function (column) {
                        column = column.data;
                        gridColumns.push(System.util.component.GridColumnUtils.processSchemaTableColumn(column));
                    }
                );
            }
            return gridColumns;
        },

        /**
         * For a specific column data create the correct grid column type
         * @param column
         * @returns {*}
         */
        processSchemaTableColumn: function (column) {
            return 'id'.localeCompare(column.name) == 0 ? System.util.component.GridColumnUtils.createIdGridColumn() : System.util.component.GridColumnUtils.createGridColumn(column);
        },

        /**
         * Create all the grid columns for a specific entity table name
         *
         * @param tableName
         * @param columnCompleteCallback
         * @param scope
         */
        createEntityGridColumnsFromSchemaTableName: function (tableName, columnCompleteCallback, scope) {
            System.util.system.SchemaUtils.retrieveSchemaTableColumnListByTableName(tableName,
                function (schemaTableColumnList) {
                    columnCompleteCallback(System.util.component.GridColumnUtils.processTableSchemaColumnList(schemaTableColumnList), scope);
                })
            ;
        }
    }
});