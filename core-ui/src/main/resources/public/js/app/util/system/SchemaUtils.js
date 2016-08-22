/**
 * The <class>System.util.system.SchemaUtils</class> defines
 *  utility methods for working with our entity schemas.
 *
 * @author Andrew
 */
Ext.define('System.util.system.SchemaUtils', {

    requires: [
        'System.util.data.StoreUtils'
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
            System.util.system.SchemaUtils.getSchemaTableStore(function (store) {
                System.util.data.StoreUtils.queryStoreByEntityName(store, tableName,
                    function (records, operation, success, scope) {
                        var table = records[0].data;
                        resultCallback(table, scope);
                    }, null, scope);
            });
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
                var parameterMap = {};
                parameterMap['schemaTableName'] = tableName;
                System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, parameterMap, 'search/findBySchemaTableName',
                    function (records, operation, success) {
                        resultCallback(records);
                    });
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
            var gridColumns = System.util.system.SchemaUtils.createNamedEntityDefaultGridColumns([
                {
                    text: 'Schema Table',
                    dataIndex: 'schemaTable',
                    reference: 'SchemaTables'
                }, {
                    text: 'Schema Data Type',
                    dataIndex: 'schemaDataType',
                    reference: 'SchemaDataTypes'
                }, {
                    text: 'Schema Table Column Relationship',
                    dataIndex: 'schemaTableColumnRelationship',
                    reference: 'SchemaTableColumnRelationships'
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
                flex: 1,
                renderer: function (v, meta, rec) {
                    return rec.phantom ? '' : v;
                },
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
                editor: 'textfield'
            }, {
                text: 'Description',
                dataIndex: 'description',
                flex: 1,
                editor: 'textfield'
            }]);

            if (additionalColumns) {
                gridColumns = gridColumns.concat(additionalColumns);
            }
            return gridColumns;
        }
    }
});