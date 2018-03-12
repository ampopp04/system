/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.util.system.UiComponentUtils</class> defines
 *  utility methods for working with ui components.
 *
 * @author Andrew
 */
Ext.define('System.util.system.UiComponentUtils', {

    requires: [
        'System.util.component.GridColumnUtils',
        'System.util.data.StoreUtils',
        'System.util.system.SchemaUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        /**
         *Retrieve a component by assignment for a specific model name, table name,
         * fkFieldId, and component type name.
         *
         * @param modelName
         * @param tableName
         * @param fkFieldId
         * @param componentTypeName
         * @param callbackImpl
         * @param scope
         */
        retrieveComponentByAssignment: function (modelName, tableName, fkFieldId, componentTypeName, callbackImpl, scope) {
            var projection = 'ui-component-assignment-all';
            var urlPath = 'findBySchemaTableNameAndFkFieldIdAndUiComponentUiComponentTypeName';

            var parameterMap = {};
            parameterMap['schemaTableName'] = tableName;
            parameterMap['fkFieldId'] = fkFieldId;
            parameterMap['uiComponentUiComponentTypeName'] = componentTypeName;

            System.util.data.StoreUtils.searchTableByModelNameAndParameters(modelName,
                parameterMap, urlPath,
                function (records, operation, success, scope) {
                    callbackImpl(System.util.system.UiComponentUtils.convertUiComponentDataToJsConfig(records[0].uiComponent ? records[0].uiComponent : records[0].getAssociatedData().uiComponent), scope);
                }, projection, scope);
        },

        /**
         * Load the main application by retrieving the starting components from the server
         */
        loadAppMain: function () {
            Ext.suspendLayouts();

            System.util.system.SchemaUtils.retrieveSchemaTableByName('SchemaTable', function (table, scope) {
            }, this);

            var modelName = 'UiComponentDefinitions';
            var entityName = 'System.view.application.main.header.Header';
            var projection = 'ui-component-definition-all';

            //Execute call to UiComponentDefinition for name = 'System.view.main.header.Header'

            System.util.component.GridColumnUtils.getStoreByModelName(modelName,
                function (store) {

                    System.util.data.StoreUtils.queryStoreByEntityName(store, entityName,
                        function (records, operation, success) {

                            System.util.system.UiComponentUtils.convertUiComponentDefinitionDataToJs(records[0],
                                function () {

                                    Ext.create('Ext.container.Container', {
                                        items: [{
                                            requires: ['System.view.application.main.Main'],
                                            xtype: 'app-main'
                                        }]
                                    });

                                });

                        }, projection);
                });
        },

        /**
         * Convert the Ui Component Definition Data To Javascript objects
         *
         * @param uiComponentDefinitionData
         * @returns {*}
         */
        convertUiComponentDefinitionDataToJs: function (uiComponentDefinitionData, callback) {
            var uiComponentDefinition = uiComponentDefinitionData.data;

            var name = uiComponentDefinition.name;
            var xtype = uiComponentDefinition.xtype;
            var extend = uiComponentDefinition.extend;
            var requires = uiComponentDefinition.requires;
            var controller = uiComponentDefinition.controller;

            var uiComponent = uiComponentDefinitionData.uiComponent ? uiComponentDefinitionData.uiComponent : uiComponentDefinitionData.getAssociatedData().uiComponent;

            var config = Ext.apply({
                extend: extend,
                xtype: xtype,

                requires: requires,
                controller: controller

            }, System.util.system.UiComponentUtils.convertUiComponentDataToJsConfig(uiComponent));

            Ext.define(name, config, callback);
        },

        /**
         * Convert a Ui Component json data to Javascript Config
         *
         * @param uiComponent
         * @returns {{}}
         */
        convertUiComponentDataToJsConfig: function (uiComponent) {
            var jsonObject = {};
            jsonObject = System.util.system.UiComponentUtils.populateUiComponentDataToJsConfig(uiComponent, jsonObject);
            return jsonObject;
        },

        /**
         * Create UI Component Data to a Javascript Config
         *
         * @param uiComponent
         * @param jsonObject
         * @returns {*}
         */
        populateUiComponentDataToJsConfig: function (uiComponent, jsonObject) {
            if (uiComponent && jsonObject) {
                return System.util.system.UiComponentUtils.convertUiComponentConfigDataToJsConfig(uiComponent.uiComponentConfig, jsonObject);
            }
            return jsonObject;
        },

        /**
         * Convert UI Component config data to javascript config
         *
         * @param uiComponentConfig
         * @param jsonObject
         * @returns {*}
         */
        convertUiComponentConfigDataToJsConfig: function (uiComponentConfig, jsonObject) {
            return System.util.system.UiComponentUtils.convertUiComponentConfigAttributeList(uiComponentConfig.uiComponentConfigAttributeList ? uiComponentConfig.uiComponentConfigAttributeList : (uiComponentConfig.data ? uiComponentConfig.data.uiComponentConfigAttributeList : undefined), jsonObject);
        },

        /**
         * Convert Ui Component Config Attribute list to Javascript
         *
         * @param uiComponentConfigAttributeList
         * @param jsonObject
         * @returns {*}
         */
        convertUiComponentConfigAttributeList: function (uiComponentConfigAttributeList, jsonObject) {
            var result = null;
            if (uiComponentConfigAttributeList) {
                uiComponentConfigAttributeList.forEach(function (uiComponentConfigAttribute) {
                    result = System.util.system.UiComponentUtils.convertUiComponentConfigAttribute(uiComponentConfigAttribute, jsonObject);
                });
            }
            return result;
        },

        /**
         * Convert a Ui Component Config Attribute to javascript
         *
         * @param uiComponentConfigAttribute
         * @param jsonObject
         * @returns {*}
         */
        convertUiComponentConfigAttribute: function (uiComponentConfigAttribute, jsonObject) {
            var key = uiComponentConfigAttribute.attributeKey;
            var value = uiComponentConfigAttribute.attributeValue;

            if (Ext.isString(value)) {
                try {
                    value = Ext.decode(value);
                } catch (e) {
                    value == null;
                    console.log(e);
                }
            }

            if (value == null) {
                if ("items".localeCompare(key) == 0) {
                    value = [];
                } else {
                    value = {};
                }

                value = System.util.system.UiComponentUtils.populateUiComponentDataToJsConfig(uiComponentConfigAttribute.uiComponent, value);
            }

            if (key) {
                jsonObject[key] = value;
            } else if (jsonObject.constructor === Array) {
                jsonObject.push(value);
            } else {
                return value;
            }

            return jsonObject;
        }
    }
});