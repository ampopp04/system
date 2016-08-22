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

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Optional Properties                                                //////////
    /////////////////////////////////////////////////////////////////////

    autoLoad: true,
    autoSync: true,

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

            params.projection = 'dynamicProjectionFields:' + requestFields + '';
        }
    }
});