/**
 * The <class>System.util.data.ModelUtils</class> defines
 *  utility methods for creating and managing the UI entity models
 *
 * @author Andrew
 */
Ext.define('System.util.data.ModelUtils', {

    requires: [
        'Ext.data.schema.Schema',
        'System.util.StringUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Convert a column name to it's associated model name
         *
         * @param columnName
         * @returns {*}
         */
        columnNameToModelName: function (columnName) {
            return System.util.data.ModelUtils.schemaTableNameToModelName(System.util.StringUtils.capitalize(columnName));
        },

        /**
         * Convert an entity's schema table name to model name.
         *
         *This is done by 'pluralizing' the word. Which means
         * to make it plural. Api calls are simply
         * the schema entity table name pluralized
         *
         * (This means adding an 's' at the end in most causes but not all!)
         *
         * @param tableName
         * @returns {string}
         */
        schemaTableNameToModelName: function (tableName) {
            return Ext.util.Inflector.pluralize(tableName)
        },

        /**
         * Convert a model name to it's associated display name.
         * This is used in the UI to act as a title to detail screens usually.
         *
         * Ex. "SystemTables" to "System Table"
         *
         * @param modelName
         * @returns {*}
         */
        modelNameToDisplayName: function (modelName) {
            return System.util.StringUtils.insertSpaceBeforeCapitals(System.util.data.ModelUtils.modelNameToSchemaTableName(modelName));
        },

        schemaTableNameToDisplayName: function (schemaTableName) {
            return System.util.data.ModelUtils.modelNameToDisplayName(System.util.data.ModelUtils.schemaTableNameToModelName(schemaTableName));
        },

        /**
         *Convert the model name to schema table name.
         *
         * Ex. "SystemTables" to "SystemTable"
         *
         * @param modelName
         * @returns {*|string|ArrayBuffer|Blob|Array.<T>}
         */
        modelNameToSchemaTableName: function (modelName) {
            return Ext.util.Inflector.singularize(modelName);
        },

        /**
         * Create model fields from a list of SystemTableColumns and
         * create the associated model
         *
         * @param modelName
         * @param columns
         * @param modelCallback
         */
        createSystemModelForColumns: function (modelName, columns, modelCallback, scope, modelOverrides) {
            var fields = System.util.data.ModelUtils.createModelFieldsForColumns(columns);
            System.util.data.ModelUtils.createSystemModelForFields(modelName, fields, modelCallback, scope, modelOverrides);
        },

        /**
         * For a set of columns return a list of model fields
         *
         * @param columns
         * @returns {Array}
         */
        createModelFieldsForColumns: function (columns) {
            var fields = [];
            //These are required in terms of what get's submitted to the server
            // The proxy will send data that is mapped via the model fields
            //Therefore names are sufficient to get the data to be sent/updated
            // but field types will be required to format the json that get's sent
            // The field types by default are string which encodes the json as a string
            if (columns) {
                columns.forEach(function (column) {
                    fields.push(Ext.apply({
                        name: column.dataIndex, reference: column.reference,
                        uiFieldConfiguration: column.uiFieldConfiguration,
                        schemaTableColumn: column.schemaTableColumn,
                        fieldDisplayLabel: column.text,
                        displayFieldName: column.displayFieldName
                    }, column.uiModelFieldConfiguration));
                });
            }
            return fields;
        },

        /**
         * Create a model from a list of model fields
         *
         * @param modelName
         * @param fields
         * @param modelCallback
         */
        createSystemModelForFields: function (modelName, fields, modelCallback, scope, modelOverrides) {
            if (System.util.data.ModelUtils.isModelExist(modelName)) {
                modelCallback(System.util.data.ModelUtils.addFieldsToExistingModel(modelName, fields), scope);
            } else {
                System.util.data.ModelUtils.createSystemModel(modelName, fields, function () {
                    modelCallback(modelName, scope);
                }, scope, modelOverrides);
            }
        },

        /**
         * If the model already exists add the new fields to it
         *
         * @param modelName
         * @param fields
         * @returns {*}
         */
        addFieldsToExistingModel: function (modelName, fields) {
            var model = Ext.data.schema.Schema.get('default').getEntity(modelName);
            model.addFields(fields);
            return model;
        },

        /**
         * Checks the class manager to determine if the model
         * is already defined
         *
         * @param modelName
         * @returns {boolean|*}
         */
        isModelExist: function (modelName) {
            return (modelName != null && Ext.data.schema.Schema.get('default').getEntity(modelName) != null);
        },

        /**
         * Create a model which extends the base model
         *
         * Ext.create('System.store.base.BaseStore', {
                    storeId: storeName,
                    model: modelName
                });
         *
         *
         * @param modelName
         * @param fields
         * @param modelCreationCallback
         */
        createSystemModel: function (modelName, fields, modelCreationCallback, scope, modelOverrides) {

            try {
                Ext.define(modelName, System.util.data.ModelUtils.merge(modelOverrides ? modelOverrides : {}, {
                    extend: 'System.model.base.BaseModel',

                    fields: fields
                }))
            } catch (e) {
            }

            Ext.syncRequire(modelName, modelCreationCallback, scope);
        },

        merge: function (destination) {
            var i = 1,
                ln = arguments.length,
                mergeFn = Ext.Object.merge,
                cloneFn = Ext.clone,
                object, key, value, sourceKey;


            for (; i < ln; i++) {
                object = arguments[i];


                for (key in object) {
                    value = object[key];
                    if (value && value.constructor === Object) {
                        sourceKey = destination[key];
                        if (sourceKey && sourceKey.constructor === Object) {
                            mergeFn(sourceKey, value);
                        } else {
                            destination[key] = cloneFn(value);
                        }
                    } else if (value && value.constructor === Array) {
                        sourceKey = destination[key];
                        if (sourceKey && sourceKey.constructor === Array) {
// The destination exists and is an Array
// NOTE: items of the Array are NOT cloned for now
                            Ext.log({level: 'warn', stack: true}, 'Items of the Array are NOT cloned')
                            destination[key] = Ext.Array.merge(sourceKey, value);
                        } else {
// The destination does not exist or is not an Array
// Override the value
                            destination[key] = cloneFn(value);
                        }
                    } else {
                        destination[key] = value;
                    }
                }
            }

            return destination;
        }
    }
});