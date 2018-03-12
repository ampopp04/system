/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

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
         * Return model name from store name
         *
         * @param storeName
         * @returns {*|string}
         */
        getModelNameFromStoreName: function (storeName) {
            return System.util.StringUtils.replace(storeName, 'Store', '');
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
            var currentRecord = undefined;//store.getById(entityId);
            if (currentRecord) {
                var operation = {_scope: {store: store}, _params: {}};
                callbackImpl([currentRecord], operation, true, scope);
            } else {
                var params = {
                    filter: Ext.encode([{"operator": "=", "value": entityId, "property": "id"}]),
                    searchDepth: 1
                };
                System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, params, 'read', callbackImpl, projectionName, scope);
            }
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
         *
         *
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

            System.util.data.StoreUtils.doQueryStoreByPropertyNameValueWithCache(store, extendedUrl, params, callbackImpl, scope);
        },

        /**
         * @private
         */
        doQueryStoreByPropertyNameValueWithCache: function (store, extendedUrl, params, callbackImpl, scope) {

            //Attempt to pull request from cache
            if (System.util.data.StoreUtils.isStoreCacheEnabled(store, extendedUrl, params)) {

                var cachedRecords = System.util.data.StoreUtils.getRemoteQueryStoreResultFromCache(store, extendedUrl, params);

                //If we have cached records return them
                if (cachedRecords) {
                    if (callbackImpl) {
                        var operation = {_scope: {store: store}, _params: params};
                        callbackImpl(cachedRecords, operation, true, scope);
                    }
                    return cachedRecords;
                }

            }

            //No cache result therefore perform remote network query
            System.util.data.StoreUtils.doExecuteRemoteQueryByPropertyNameValue(store, extendedUrl, params, callbackImpl, scope);

        },

        /**
         * @private
         */
        isStoreCacheEnabled: function (store, extendedUrl, params) {

            var schemaTableStore = System.util.data.StoreUtils.lookupStoreByName('SchemaTablesStore');

            if (schemaTableStore) {
                var tableData = schemaTableStore.query('name', System.util.data.ModelUtils.modelNameToSchemaTableName(store.model.entityName), false, true, true).first();

                if (tableData && tableData.data) {
                    if (tableData.data.cacheEnabled) {
                        return true;
                    } else {
                        Ext.util.LocalStorage.get(store.storeId).clear();
                        Ext.util.LocalStorage.get(store.storeId).release();
                        return false;
                    }
                }
            }

            if (store.model.entityName = 'SchemaTableColumns') {
                return true;
            }

            return false;
        },

        /**
         * @private
         */
        getMaxElapsedTimeInSecondsForStoreCacheItem: function (store) {
            //Allow items to be cached for 9 hours
            //Allow this to be configurable in the future
            var cacheTimeToLiveInSeconds = 32400;

            var schemaTableStore = System.util.data.StoreUtils.lookupStoreByName('SchemaTablesStore');

            if (schemaTableStore) {
                var tableData = schemaTableStore.query('name', System.util.data.ModelUtils.modelNameToSchemaTableName(store.model.entityName), false, true, true).first();
                if (tableData && tableData.data) {
                    return parseInt(tableData.data.cacheTimeToLiveInSeconds);
                }
            }

            return cacheTimeToLiveInSeconds;
        },

        /**
         * @private
         */
        getRemoteQueryStoreResultFromCache: function (store, executionUrl, params) {
            var storage = Ext.util.LocalStorage.get(store.storeId);
            var cacheKey = System.util.data.StoreUtils.getCacheKey(executionUrl, params);
            var rawCacheItem = storage.getItem(cacheKey);

            if (rawCacheItem == undefined) {
                return false;
            }

            var cacheResult = Ext.JSON.decode(System.util.data.StoreUtils.decompressData(rawCacheItem));

            var cacheDate = cacheResult.createDate;
            var records = cacheResult.records;

            var maxElapsedTimeInSeconds = System.util.data.StoreUtils.getMaxElapsedTimeInSecondsForStoreCacheItem(store);

            if (cacheDate == undefined || records == undefined || System.util.data.StoreUtils.isCacheDateExpired(cacheDate, Date.now(), maxElapsedTimeInSeconds)) {
                return false;
            }

            var decodedRecords = [];

            Ext.Array.forEach(records, function (record) {
                //make this transform smarter to get rid of deserialization with the _ vals???
                decodedRecords.push(System.util.data.RecordUtils.transformJsonEncodedRecordLayout(Ext.JSON.decode(record)));
            });

            return decodedRecords;
        },

        isCacheDateExpired: function (cacheDate, comparisonDate, maxElapsedTimeInSeconds) {
            return Ext.Date.diff(cacheDate, comparisonDate, Ext.Date.SECOND) >= maxElapsedTimeInSeconds;
        },

        getCacheKey: function (executionUrl, params) {
            return executionUrl + '?' + Ext.JSON.encode(params);
        },

        /**
         * @private
         */
        doCacheRemoteQueryStoreRecords: function (records, store, extendedUrl, params) {
            if (!System.util.data.StoreUtils.isStoreCacheEnabled(store, extendedUrl, params)) {
                return;
            }

            var storage = Ext.util.LocalStorage.get(store.storeId);

            var cacheKey = System.util.data.StoreUtils.getCacheKey(extendedUrl, params);

            var encodedRecordsArray = [];
            Ext.Array.forEach(records, function (record) {
                encodedRecordsArray.push(Ext.JSON.encode(record));
            }, this);

            var encodedData = System.util.data.StoreUtils.compressData(Ext.JSON.encode({
                records: encodedRecordsArray,
                createDate: Date.now()
            }));

            try {
                storage.setItem(cacheKey, encodedData);
            } catch (e) {
                try {
                    Ext.util.LocalStorage.clear();
                    Ext.util.LocalStorage.release();
                    Ext.util.LocalStorage.get(store.storeId).setItem(cacheKey, encodedData);
                } catch (e) {
                }
            }
        },

        clearCache: function (storeId) {
            try {

                if (Ext.util.LocalStorage.cache[storeId]) {
                    Ext.util.LocalStorage.get(storeId).clear();
                }

            }
            catch (e) {
            }
        },

        compressData: function (data) {
            var Buffer = require('buffer').Buffer;
            var LZ4 = require('lz4');

            // LZ4 can only work on Buffers
            var input = new Buffer(data);

            // Initialize the output buffer to its maximum length based on the input data
            var output = new Buffer(LZ4.encodeBound(input.length));

            // block compression (no archive format)
            var compressedSize = LZ4.encodeBlock(input, output);

            if (compressedSize > 0) {
                // remove unnecessary bytes
                output = Ext.Array.slice(output, 0, compressedSize);
                output = output.toString();
                return output;
            }

            return data;
        }
        ,

        decompressData: function (data) {
            var Buffer = require('buffer').Buffer;
            var LZ4 = require('lz4');

            // LZ4 compressed block
            var compressedBlock = new Buffer(data.split(','));
            var uncompressedBlock = new Buffer(compressedBlock.length * 10);

            var n = LZ4.decodeBlock(compressedBlock, uncompressedBlock);

            if (n == -1) {
                return data;
            }

            uncompressedBlock = Ext.Array.slice(uncompressedBlock, 0, n);
            var decodedOutput = decodeURIComponent(escape(String.fromCharCode.apply(null, uncompressedBlock)));

            return decodedOutput;
        },


        /**
         * @private
         */
        doExecuteRemoteQueryByPropertyNameValue: function (store, extendedUrl, params, callbackImpl, scope) {

            store.fetch({
                url: store.proxy.url + (extendedUrl ? '/' + extendedUrl : ""),
                params: params,
                callback: System.util.data.StoreUtils.getStoreFetchCallback(store, extendedUrl, params),
                scope: {
                    scope: (scope ? scope : this),
                    store: store,
                    scopedCallBack: callbackImpl
                }
            });
        },

        /**
         * @private
         */
        getStoreFetchCallback: function (store, extendedUrl, params) {
            return function (records, operation, success) {

                if (success) {
                    System.util.data.StoreUtils.doCacheRemoteQueryStoreRecords(records, store, extendedUrl, params);
                }

                if (this.scopedCallBack) {
                    this.scopedCallBack(records, operation, success, this.scope);
                }

            };
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

        createNewStore: function (source) {
            source = Ext.isString(source) ? Ext.data.StoreManager.lookup(source) : source;
            var storeName = source.$className;

            var target = Ext.create(storeName, {
                model: source.model,
                autoSync: false
            });

            target.add(Ext.Array.map(source.getRange(), function (record) {
                return record.copy();
            }));

            return target;
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
})
;