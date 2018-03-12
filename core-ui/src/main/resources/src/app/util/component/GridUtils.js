/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.util.component.GridUtils</class> defines
 * utility methods for working with grids.
 *
 * @author Andrew
 */
Ext.define('System.util.component.GridUtils', {

    requires: [
        'System.util.data.ModelUtils',
        'System.util.data.StoreUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Create a system model and store given a model name and columns
         *
         * @param modelName
         * @param columns
         * @param storeCallback
         */
        createSystemModelAndStore: function (modelName, columns, storeCallback, modelOverrides, scope) {
            var storeName = System.util.data.StoreUtils.getStoreName(modelName.$className ? modelName.$className : modelName);
            var storeExists = System.util.data.StoreUtils.lookupStoreByName(storeName);

            if (storeExists) {
                return storeCallback(storeExists, scope);
            } else {
                System.util.data.ModelUtils.createSystemModelForColumns(modelName, columns, function (model, scope) {
                    System.util.data.StoreUtils.getOrCreateStore(model, storeCallback, scope);
                }, scope, modelOverrides);
            }


        },

        /**
         * Returns the default rowmodel section
         * @returns {string}
         */
        rowSelectionModel: function () {
            return 'rowmodel';
        },

        /**
         * Returns the default plugins to be used by the base grids
         *
         * @returns {*[]}
         */
        getDefaultGridPlugins: function () {
            //System.util.component.GridUtils.rowEditingPlugin(),
            return System.util.component.GridUtils.gridFiltersPlugin();
        },

        /**
         * Defines a plugin that can be used to allow editing the rows of a grid
         *
         * @returns {{ptype: string, clicksToEdit: number, autoCancel: boolean}}
         */
        rowEditingPlugin: function () {
            return {
                ptype: 'rowediting',
                clicksToEdit: 2,
                autoCancel: false
            };
        },

        /**
         * Defines gridfilters as the default filters plugin for grids
         * @returns {string}
         */
        gridFiltersPlugin: function () {
            return 'gridfilters';
        }
    }
});