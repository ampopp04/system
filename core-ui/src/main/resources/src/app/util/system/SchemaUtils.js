/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.util.system.SchemaUtils</class> defines
 *  utility methods for working with our entity schemas.
 *
 * @author Andrew
 */
Ext.define('System.util.system.SchemaUtils', {

    requires: [
        'System.util.data.StoreUtils',
        'System.view.component.field.filter.SystemEntityFilter',
        'System.util.component.GridColumnUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Retrieve from the server the Schema Table object associated with
         * the given table name.
         *
         * @param tableName
         * @param resultCallback
         * @param scope
         */
        retrieveSchemaTableByName: function (tableName, resultCallback, scope) {
            var schemaTableStore = System.util.data.StoreUtils.lookupStoreByName('SchemaTablesStore');

            if (schemaTableStore) {
                var tableRecord = schemaTableStore.query('name', tableName, false, true, true).first();
                if (tableRecord && tableRecord.data) {
                    resultCallback(tableRecord.data, scope);
                    return;
                }
            }

            System.util.component.GridColumnUtils.getStoreByModelName('SchemaTables', function (store) {
                store.pageSize = 1000;
                store.load();
            }, scope);

        },

        /**
         * Retrieve from the server the Schema Table Column objects
         * associated with the given table name.
         *
         * @param tableName
         * @param resultCallback
         */
        retrieveSchemaTableColumnListByTableName: function (tableName, resultCallback) {
            System.util.system.SchemaUtils.getSchemaTableColumnStore(function (store) {

                /*
                var columnList = [];
                store.queryBy(function (record, id) {
                    if (record.data && record.data.schemaTable && record.data.schemaTable.name == tableName) {
                        columnList.push(record);
                    }
                });

                //TODO Clear cache mechanism or TTL
                if (!Ext.Object.isEmpty(columnList)) {
                    // Return cached values
                    resultCallback(columnList);
                    return;
                }*/

                var parameterMap = {};
                parameterMap['schemaTableName'] = tableName;
                System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, parameterMap, 'search/findBySchemaTableName',
                    function (records, operation, success) {
                        resultCallback(records);
                    }, undefined, undefined, ['relationshipTableName']);
            });
        },

        /**
         * Get the Schema Table store.
         *
         * @param storeCallback
         */
        getSchemaTableStore: function (storeCallback) {
            var modelName = 'SchemaTables';
            var gridColumns = System.util.system.SchemaUtils.createNamedEntityDefaultGridColumns([{
                text: 'Entity Class',
                dataIndex: 'entityClass'
            }, {
                text: 'Cache Enabled',
                dataIndex: 'cacheEnabled',
                xtype: 'checkcolumn', filter: 'boolean',
                uiFieldConfiguration: {xtype: 'checkbox', inputValue: true, uncheckedValue: false},
                uiModelFieldConfiguration: {type: 'boolean'}
            }, {
                text: 'Cache TTL',
                dataIndex: 'cacheTimeToLiveInSeconds',
                xtype: 'numbercolumn',
                format: '0',
                filter: {type: 'number', hideTrigger: true, keyNavEnabled: false, mouseWheelEnabled: false},
                uiFieldConfiguration: {
                    xtype: 'numberfield',
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false,
                    decimalPrecision: 0
                },
                uiModelFieldConfiguration: {type: 'int'}
            }]);
            System.util.data.StoreUtils.createSystemStoreFromColumns(modelName, gridColumns, storeCallback);
        },

        /**
         * Get the Schema Table Column store
         *
         * @param storeCallback
         */
        getSchemaTableColumnStore: function (storeCallback) {
            var modelName = 'SchemaTableColumns';

            var schemaTableColumnStore = System.util.data.StoreUtils.lookupStore(modelName);

            if (schemaTableColumnStore) {
                storeCallback(schemaTableColumnStore);
                return;
            }

            var gridColumns = System.util.system.SchemaUtils.createNamedEntityDefaultGridColumns([
                System.util.component.GridColumnUtils.processGridColumnRenderer({
                    text: 'Schema Table',
                    dataIndex: 'schemaTable',
                    reference: 'SchemaTables',
                    flex: 1,
                    filter: {type: 'entity'}
                }), System.util.component.GridColumnUtils.processGridColumnRenderer({
                    text: 'Schema Data Type',
                    dataIndex: 'schemaDataType',
                    reference: 'SchemaDataTypes',
                    flex: 1,
                    filter: {type: 'list', store: 'SchemaDataTypesStore', idField: 'id', labelField: 'name'}
                }), System.util.component.GridColumnUtils.processGridColumnRenderer({
                    text: 'Schema Table Column Relationship',
                    dataIndex: 'schemaTableColumnRelationship',
                    reference: 'SchemaTableColumnRelationships',
                    flex: 1
                }), System.util.component.GridColumnUtils.processGridColumnRenderer({
                    text: 'Parent Display Schema Table Column',
                    dataIndex: 'parentDisplaySchemaTableColumn',
                    reference: 'SchemaTableColumns',
                    flex: 1
                }), {
                    text: 'Default Column Order',
                    dataIndex: 'defaultColumnOrder',
                    xtype: 'numbercolumn',
                    uiFieldConfiguration: {xtype: 'numberfield'},
                    uiModelFieldConfiguration: {type: 'int'},
                }, {
                    text: 'Display Name',
                    dataIndex: 'displayName',
                    flex: 1,
                    uiModelFieldConfiguration: {type: 'string'}
                }, {
                    text: 'Display Hidden',
                    dataIndex: 'displayHidden',
                    flex: 1,
                    xtype: 'checkcolumn',
                    uiFieldConfiguration: {xtype: 'checkboxfield'},
                    uiModelFieldConfiguration: {type: 'boolean'}
                }, {
                    text: 'Ui Column Configuration',
                    dataIndex: 'uiColumnConfiguration',
                    flex: 1,
                    uiModelFieldConfiguration: {type: 'string'}
                }, {
                    text: 'Ui Field Configuration',
                    dataIndex: 'uiFieldConfiguration',
                    flex: 1,
                    uiModelFieldConfiguration: {type: 'string'}
                }, {
                    text: 'Ui Model Field Configuration',
                    dataIndex: 'uiModelFieldConfiguration',
                    flex: 1,
                    uiModelFieldConfiguration: {type: 'string'}
                }]);
            System.util.data.StoreUtils.createSystemStoreFromColumns(modelName, gridColumns, storeCallback);
        },

        /**
         * Create the base entity default grid ID column
         *
         * @param additionalColumns
         * @returns {*[]}
         */
        createBaseEntityDefaultGridColumns: function (additionalColumns) {
            var gridColumns = [{
                text: 'ID',
                dataIndex: 'id',
                xtype: 'numbercolumn',
                flex: 1,
                format: '0',
                filter: {type: 'number', hideTrigger: true, keyNavEnabled: false, mouseWheelEnabled: false},
                uiFieldConfiguration: {
                    xtype: 'numberfield',
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false,
                    decimalPrecision: 0
                },
                uiModelFieldConfiguration: {type: 'int'},
                hidden: true
            }];
            if (additionalColumns) {
                gridColumns = gridColumns.concat(additionalColumns);
            }
            return gridColumns;
        },

        /**
         * Create the named entity default grid columns.
         *
         * @param additionalColumns
         * @returns {*|*[]}
         */
        createNamedEntityDefaultGridColumns: function (additionalColumns) {
            var gridColumns = System.util.system.SchemaUtils.createBaseEntityDefaultGridColumns([{
                text: 'Name',
                dataIndex: 'name',
                flex: 1,
                uiModelFieldConfiguration: {type: 'string'}
            }, {
                text: 'Description',
                dataIndex: 'description',
                flex: 1,
                uiModelFieldConfiguration: {type: 'string'}
            }]);

            if (additionalColumns) {
                gridColumns = gridColumns.concat(additionalColumns);
            }
            return gridColumns;
        }
    }
});