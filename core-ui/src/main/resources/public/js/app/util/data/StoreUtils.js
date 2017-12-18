/**
 * The <class>System.util.data.StoreUtils</class> defines
 *  utility methods for working with stores.
 *
 * @author Andrew
 */
Ext.define('System.util.data.StoreUtils', {

    requires: [
        'System.util.application.ErrorUtils',
        'System.util.application.UserUtils',
        'System.util.data.ModelUtils',
        'System.store.base.BaseStore',
        'System.util.application.Util'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Return store name from a model name
         *
         * @param modelName
         * @returns {string}
         */
        getStoreName: function (modelName) {
            return modelName + 'Store';
        },

        /**
         * Retrieve a store by it's associated model name
         *
         * @param modelName
         * @returns {*}
         */
        lookupStore: function (modelName) {
            return System.util.data.StoreUtils.lookupStoreByName(System.util.data.StoreUtils.getStoreName(modelName));
        },

        /**
         * Retrieve store by name
         *
         * @param storeName
         * @returns {*}
         */
        lookupStoreByName: function (storeName) {
            return Ext.data.StoreManager.lookup(storeName);
        },

        /**
         * Create a store from a name and associated columns.
         * The columns provide meta-data required to create the stores fields.
         *
         * @param modelName
         * @param gridColumns
         * @param storeCreatedCallback
         */
        createSystemStoreFromColumns: function (modelName, gridColumns, storeCreatedCallback, scope) {
            System.util.data.ModelUtils.createSystemModelForColumns(modelName, gridColumns,
                function (modelName, scope) {
                    System.util.data.StoreUtils.getOrCreateStore(modelName, storeCreatedCallback, scope);
                }, scope);
        },

        /**
         * Execute a server query against a model for the given parameters
         * and url path. A projection name can also be provided but if it is not
         * a dynamic projection will be created for all fields of the model.
         *
         * @param modelName
         * @param parameterMap
         * @param urlPath
         * @param callbackImpl
         * @param projectionName
         * @param scope
         */
        searchTableByModelNameAndParameters: function (modelName, parameterMap, urlPath, callbackImpl, projectionName, scope) {
            System.util.component.GridColumnUtils.createEntityGridColumnsFromModelName(modelName,
                function (gridColumns, scope) {
                    System.util.data.StoreUtils.createSystemStoreFromColumns(modelName, gridColumns,
                        function (store) {
                            System.util.data.StoreUtils.searchStoreByParameterMapAndUrlPath(store, parameterMap, urlPath, callbackImpl, projectionName, scope);
                        });
                }, scope);
        },

        /**
         * Search a store for a given set of parameters on a given url path.
         * An optional projection name can be provided otherwise a dynamic
         * projection will be created for all fields in the store.
         *
         * @param store
         * @param parameterMap
         * @param urlPath
         * @param callbackImpl
         * @param projectionName
         * @param scope
         */
        searchStoreByParameterMapAndUrlPath: function (store, parameterMap, urlPath, callbackImpl, projectionName, scope) {
            System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, parameterMap, 'search/' + urlPath, callbackImpl, projectionName, scope);
        },

        /**
         * Query a store by a specific store entity name.
         *
         * @param store
         * @param entityName
         * @param callbackImpl
         * @param projectionName
         * @param scope
         */
        queryStoreByEntityName: function (store, entityName, callbackImpl, projectionName, scope) {
            var params = {};
            params['name'] = entityName;
            System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, params, 'search/findByName', callbackImpl, projectionName, scope);
        },

        /**
         * Query store by a specific entity id.
         *
         * @param store
         * @param entityId
         * @param callbackImpl
         * @param projectionName
         * @param scope
         */
        queryStoreById: function (store, entityId, callbackImpl, projectionName, scope) {
            var params = {};
            System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, params, entityId, callbackImpl, projectionName, scope);
        },

        addProjectionParams: function (params, store) {
            if (params === undefined) {
                params = {};
            }

            if (params.projection) {
                return;
            }

            var requestFields = undefined;
            store.model.fields.forEach(function (field) {
                var fieldName = field.name;

                if (fieldName) {
                    requestFields = (requestFields) ? requestFields + ',' + fieldName : fieldName;
                }
            });

            if (params.additionalProjectionFields) {
                params.additionalProjectionFields.forEach(function (additionalField) {
                    requestFields = (requestFields) ? requestFields + ',' + additionalField : additionalField;
                });
                delete params['additionalProjectionFields'];
            }

            params.projection = 'dynamicProjectionFields:' + requestFields + '';
        },

        /**
         * Query a store by a map of parameters for a given url extension and
         * optional projection name. A dynamic projection will be created for all fields in the store
         * if non is provided.
         *
         * @param store
         * @param parameterMap
         * @param extendedUrl
         * @param callbackImpl
         * @param projectionName
         * @param scope
         */
        queryStoreByPropertyNameValue: function (store, parameterMap, extendedUrl, callbackImpl, projectionName, scope, additionalProjectionFields) {
            var params = parameterMap;

            if (projectionName) {
                params['projection'] = projectionName;
            }

            if (additionalProjectionFields) {
                params['additionalProjectionFields'] = additionalProjectionFields;
            }

            System.util.data.StoreUtils.addProjectionParams(params, store);

            store.fetch({
                url: store.proxy.url + '/' + extendedUrl,
                params: params,
                callback: function (records, operation, success) {
                    if (success) {
                        callbackImpl(records, operation, success, this.scope);
                        //this.store.loadData(records, true);
                    } else {
                        System.util.application.ErrorUtils.dataRequestErrorCheck(success, operation.error.response.responseText, operation.error.response.status);
                    }
                },
                scope: {scope: (scope ? scope : this), store: store}
            });
        },

        getOrCreateStore: function (modelName, modelCreationCallback, scope) {
            if (modelName != null) {

                var storeName = System.util.data.StoreUtils.getStoreName(modelName.$className ? modelName.$className : modelName);
                var storeExists = System.util.data.StoreUtils.lookupStoreByName(storeName);

                if (storeExists) {
                    modelCreationCallback(storeExists, scope);
                } else {
                    System.util.data.StoreUtils.createSystemStore(modelName, function (storeDefinition) {
                        modelCreationCallback(Ext.create(storeName), scope);
                    });
                }
            }
        },

        /**
         * Create a store by model name which extends our base store.
         *
         * @param modelName
         */
        createSystemStore: function (modelName, modelCreationCallback) {
            var storeName = System.util.data.StoreUtils.getStoreName(modelName.$className ? modelName.$className : modelName);

            Ext.define(storeName, {
                extend: 'System.store.base.BaseStore',

                storeId: storeName,
                model: modelName
            }, modelCreationCallback);
        }
    }
});