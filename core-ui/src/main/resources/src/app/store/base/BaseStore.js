/**
 * The <class>System.store.base.BaseStore</class> defines
 * the base store to use to hold all the data of each of the models.
 *
 * @author Andrew
 */
Ext.define('System.store.base.BaseStore', {
    extend: 'Ext.data.Store',

    ///////////////////////////////////////////////////////////////////////
    ////////                                       Required Properties                                                  //////////
    /////////////////////////////////////////////////////////////////////

    config: {
        model: 'DEFINE-ME-MODEL-NAME',
        storeId: 'DEFINE-ME'
    },

    //Default page size rows to query from the server
    pageSize: 50,

    //For special cases we can increase our row load size
    // Ex. Export a grid to a file, increase to maxPageSize and reload
    // to have all rows to download to file.
    maxPageSize: 10000,

    autoLoad: false,
    autoSync: false,

    remoteFilter: true,
    remoteSort: true,

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Optional Properties                                                //////////
    /////////////////////////////////////////////////////////////////////

    listeners: {

        /**
         * Before we load any data from the server we need to append a request parameter
         * called dynamicProjectionFields for each field being requested.
         *
         * This is a feature of the server that allows us to dynamically created projections on these fields
         * to pull out the information to return to the UI without having to perform additional requests.
         *
         * @param store
         * @param operation
         * @param options
         */
        beforeload: function (store, operation, options) {
            var params = operation._params;
            if (params === undefined) {
                params = {};
                operation._params = params;
            }

            System.util.data.StoreUtils.addProjectionParams(params, store);
        },

        beforesync: function (options, eOpts, a, b, c) {
            if (options.update) {
                options.update.forEach(function (recordUpdate) {
                    recordUpdate.systemPreviousValues = recordUpdate.data;
                    recordUpdate.data = System.util.data.RecordUtils.transformRecordLayout(recordUpdate.data, recordUpdate.proxy.getModel());
                });
            }

            if (options.create) {
                options.create.forEach(function (recordUpdate) {
                    recordUpdate.systemPreviousValues = recordUpdate.data;
                    recordUpdate.data = System.util.data.RecordUtils.transformRecordLayout(recordUpdate.data, recordUpdate.proxy.getModel());
                });
            }

            if (options.destroy) {
                options.destroy.forEach(function (recordUpdate) {
                    recordUpdate.systemPreviousValues = recordUpdate.data;
                    recordUpdate.data = undefined;
                });
            }

            System.util.data.StoreUtils.clearCache(this.storeId);

        }
    }
});