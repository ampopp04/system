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
         *Ex. "SystemTable" to "SystemTables"
         *
         * @param tableName
         * @returns {string}
         */
        schemaTableNameToModelName: function (tableName) {
            return tableName + 's';
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

        /**
         *Convert the model name to schema table name.
         *
         * Ex. "SystemTables" to "SystemTable"
         *
         * @param modelName
         * @returns {*|string|ArrayBuffer|Blob|Array.<T>}
         */
        modelNameToSchemaTableName: function (modelName) {
            return modelName.slice(0, -1);
        },

        /**
         * Create model fields from a list of SystemTableColumns and
         * create the associated model
         *
         * @param modelName
         * @param columns
         * @param modelCallback
         */
        createSystemModelForColumns: function (modelName, columns, modelCallback) {
            var fields = System.util.data.ModelUtils.createModelFieldsForColumns(columns);
            System.util.data.ModelUtils.createSystemModelForFields(modelName, fields, modelCallback);
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
                    fields.push({
                        name: column.dataIndex, reference: column.reference,
                        mapping: function (data) {
                            return data[this.name];
                        }
                    });
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
        createSystemModelForFields: function (modelName, fields, modelCallback) {
            if (System.util.data.ModelUtils.isModelExist(modelName)) {
                modelCallback(System.util.data.ModelUtils.addFieldsToExistingModel(modelName, fields));
            } else {
                System.util.data.ModelUtils.createSystemModel(modelName, fields, modelCallback);
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
            return modelName != null && Ext.ClassManager.isCreated(modelName);
        },

        /**
         * Create a model which extends the base model
         *
         * @param modelName
         * @param fields
         * @param modelCreationCallback
         */
        createSystemModel: function (modelName, fields, modelCreationCallback) {
            Ext.define(modelName, {
                extend: 'System.model.base.BaseModel',

                fields: fields
            }, modelCreationCallback);
        }
    }
});