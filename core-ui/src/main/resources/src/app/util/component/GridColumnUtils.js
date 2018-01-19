/**
 * The <class>System.util.component.GridColumnUtils</class> defines
 * utility methods for managing and dynamically creating grid columns
 *
 * @author Andrew
 */
Ext.define('System.util.component.GridColumnUtils', {

    requires: [
        'System.util.StringUtils',
        'System.util.data.StoreUtils',
        'System.util.component.GridUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * This will create a store via a query for all of its columns
         * and building the stores inner model. It will produce this
         * asynchronously but this means the store
         * will be available at a later time.  This is useful when you know
         * you will need to have that store available for use later in other code.
         */
        fullyInitializeStoreByModelName: function (modelName) {
            System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {
            });
        },

        /**
         * Return a store for a specific model name into the callback provided
         */
        getStoreByModelName: function (modelName, callback, scope) {
            var modelStore = System.util.data.StoreUtils.lookupStore(modelName);

            if (modelStore) {
                callback(modelStore, scope);
            } else {
                System.util.component.GridColumnUtils.createEntityGridColumnsFromModelName(modelName,
                    function (columns, scope) {
                        System.util.component.GridUtils.createSystemModelAndStore(modelName, columns, callback, undefined, scope);
                    }, scope);
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
            System.util.component.GridColumnUtils.createEntityGridColumnsFromSchemaTableName(System.util.data.ModelUtils.modelNameToSchemaTableName(modelName), columnCompleteCallback, scope);
        },

        /**
         * Create the basic hidden ID column
         *
         * @returns {{text: string, dataIndex: string, flex: number, renderer: renderer, hidden: boolean}}
         */
        createIdGridColumn: function (schemaTableColumn) {
            var column = schemaTableColumn ? schemaTableColumn.data : {};

            var uiColumnConfiguration = System.util.component.GridColumnUtils.safeJsonDecode(column.uiColumnConfiguration);
            var uiFieldConfiguration = System.util.component.GridColumnUtils.safeJsonDecode(column.uiFieldConfiguration);
            var uiModelFieldConfiguration = System.util.component.GridColumnUtils.safeJsonDecode(column.uiModelFieldConfiguration);

            return Ext.apply({
                text: 'ID',
                dataIndex: 'id',
                xtype: 'numbercolumn',
                flex: 1,
                schemaTableColumn: schemaTableColumn,
                uiFieldConfiguration: uiFieldConfiguration,
                uiModelFieldConfiguration: uiModelFieldConfiguration,
                hidden: true
            }, uiColumnConfiguration);
        },

        safeJsonDecode: function (json) {
            if (!Ext.isEmpty(json)) {
                try {
                    return Ext.decode(json);
                } catch (e) {
                    console.log(e);
                    return null;
                }
            }
            return null;
        },

        /**
         * Create the grid columns as textfields for each of the entities fields
         *
         * @param column
         * @returns {{text: *, dataIndex: *, flex: number, reference: undefined, editor: string, renderer: renderer}}
         */
        createGridColumn: function (schemaTableColumn) {
            var column = schemaTableColumn.data;

            var modelName = undefined;
            var displayFieldName = undefined;

            if (column.relationshipTableName) {
                modelName = System.util.data.ModelUtils.schemaTableNameToModelName(column.relationshipTableName)

                if (column.schemaTable == undefined || (column.relationshipTableName != (column.schemaTable.name ? column.schemaTable.name : column.schemaTable.data.name))) {
                    System.util.component.GridColumnUtils.getStoreByModelName(modelName,
                        function (store) {
                            //we are referring to the model as a reference but it may have never been created
                        });
                }

                var parentDisplayColumn = schemaTableColumn._parentDisplaySchemaTableColumn;
                if (parentDisplayColumn && parentDisplayColumn.data && parentDisplayColumn.data.name) {
                    displayFieldName = parentDisplayColumn.data.name;
                }
            }

            var columnText = column.displayName ? column.displayName : column.name;

            //uiColumnConfiguration defines extra configuration for this column
            var uiColumnConfiguration = System.util.component.GridColumnUtils.safeJsonDecode(column.uiColumnConfiguration);

            //Defines extra field level configuration
            // Fields are dynamically generated from columns so we
            //place a reference to this configuration on the column so
            //the fields can pull it off in the future
            var uiFieldConfiguration = System.util.component.GridColumnUtils.safeJsonDecode(column.uiFieldConfiguration);
            var uiModelFieldConfiguration = System.util.component.GridColumnUtils.safeJsonDecode(column.uiModelFieldConfiguration);

            return System.util.component.GridColumnUtils.processGridColumnRenderer(Ext.apply({
                text: columnText,
                dataIndex: column.name,
                autoSizeColumn: true,
                displayFieldName: displayFieldName,
                reference: modelName,
                hidden: column.displayHidden,
                schemaTableColumn: schemaTableColumn,
                uiFieldConfiguration: uiFieldConfiguration,
                uiModelFieldConfiguration: uiModelFieldConfiguration
            }, uiColumnConfiguration));
        },

        processGridColumnRenderer: function (column) {
            if (column.reference) {
                return Ext.apply(column, {
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {

                        if (metaData && metaData.column && metaData.column.reference) {

                            var displayFieldName = metaData.column.displayFieldName;

                            if (displayFieldName && value && (typeof value.get === 'function') && value.get(displayFieldName)) {
                                return value.get(displayFieldName);
                            }

                            if (value == undefined && metaData.record) {
                                var recordData = metaData.record['_' + metaData.column.dataIndex];

                                if (recordData) {
                                    if (recordData.data[displayFieldName]) {
                                        return recordData.data[displayFieldName];
                                    }
                                    else if (recordData.data.name) {
                                        return recordData.data.name;
                                    } else {
                                        value = recordData.data.id;
                                    }
                                }

                            }

                            if (value) {
                                var columnStore = System.util.data.StoreUtils.lookupStoreByName(metaData.column.reference + 'Store');
                                if (columnStore) {

                                    if (Ext.isString(value) && System.util.StringUtils.startsWith(value, 'http')) {
                                        value = value.replace(columnStore.proxy.url + '/', '');
                                    }

                                    var columnStoreRecord = columnStore.getById(value);

                                    if (columnStoreRecord == undefined || columnStoreRecord.data == undefined) {
                                        //We cannot find the record in the store so let's look at the previous record values.
                                        //f the ID is the same then we will use that data, this is because the server strips most of the data

                                        if (record && record.systemPreviousValues) {
                                            var previousEntityValues = record.systemPreviousValues[metaData.column.dataIndex];

                                            if (previousEntityValues && previousEntityValues.id == value) {
                                                return previousEntityValues.get(displayFieldName);
                                            }
                                        }
                                    }

                                    //Lets check to see if this record exists in the store, if it does then use it
                                    if (columnStoreRecord && columnStoreRecord.data) {
                                        record.data[metaData.column.dataIndex] = columnStoreRecord;
                                        record['_' + metaData.column.dataIndex] = columnStoreRecord;
                                        record[metaData.column.dataIndex] = columnStoreRecord;

                                        if (columnStoreRecord.data[displayFieldName]) {
                                            return columnStoreRecord.data[displayFieldName];
                                        } else if (columnStoreRecord.data.name) {
                                            return columnStoreRecord.data.name;
                                        }

                                    }

                                }
                            }
                        }
                        return value;
                    }
                });
            }
            return column;
        },

        /**
         * For a set of Schema Table Columns
         * convert this meta-data into the actual grid columns
         *
         * @param schemaTableColumnList
         * @returns {Array}
         */
        processTableSchemaColumnList: function (schemaTableColumnList) {
            schemaTableColumnList.sort(function (a, b) {
                return a.data.defaultColumnOrder - b.data.defaultColumnOrder;
            });

            var gridColumns = [];
            if (schemaTableColumnList) {
                schemaTableColumnList.forEach(function (column) {
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
        processSchemaTableColumn: function (schemaTableColumn) {
            var column = schemaTableColumn.data;
            return 'id'.localeCompare(column.name) == 0 ? System.util.component.GridColumnUtils.createIdGridColumn(schemaTableColumn) : System.util.component.GridColumnUtils.createGridColumn(schemaTableColumn);
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
                });
        }
    }
});