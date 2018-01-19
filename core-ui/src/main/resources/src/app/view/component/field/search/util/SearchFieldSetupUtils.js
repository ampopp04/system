/**
 * The <class>System.view.component.field.search.util.SearchFieldSetupUtils</class> defines
 *  search field specific utility methods. These are used in assisting the
 *  global search feature across various stores in the system.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.search.util.SearchFieldSetupUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * This method is the main entry point for configuring the search field.
         * It handles ensuring that the store, associated with this search field,
         * is fully setup, created, and connected to this search field. It ensures that
         * whenever the store loads that the search feature is invoked to be given the
         * opportunity to highlight any search results. It also allows searching since it
         * binds the store to this search field. The search field uses the store to filter any
         * new search that is entered into the search field.
         *
         * @param searchField - The search field object itself
         */
        configureSearchFieldSetup: function (searchField) {

            if (Ext.isString(searchField.parentEntity.store)) {

                var existingStore = System.util.data.StoreUtils.lookupStoreByName(searchField.parentEntity.store);

                if (existingStore) {
                    searchField.parentEntity.store = existingStore;
                    System.view.component.field.search.util.SearchFieldSetupUtils.storeLoadListenerSetup(searchField, searchField.parentEntity, searchField.parentEntity.store);
                } else {
                    Ext.data.StoreManager.addListener('add', System.view.component.field.search.util.SearchFieldSetupUtils.storeNameLoadListener, searchField);
                }

            }
            else if (searchField.parentEntity.store && searchField.parentEntity.store.storeId != 'ext-empty-store') {

                System.view.component.field.search.util.SearchFieldSetupUtils.storeLoadListenerSetup(searchField, searchField.parentEntity, searchField.parentEntity.store);

            } else {

                searchField.parentEntity.addListener('reconfigure', System.view.component.field.search.util.SearchFieldSetupUtils.storeLoadListenerSetup, searchField);

            }

        },

        /**
         * Helper method to add an on load listener to the underlying store
         * supporting this search field.  When the store loads new records
         * this load listener callback will get activated because of it's setup
         * in this function.  Specifically calling highlightSearchResults.
         */
        storeLoadListenerSetup: function (searchField, parentEntity, store) {
            var store = store;

            if (parentEntity && parentEntity.store && parentEntity.store.$className) {
                store = parentEntity.store;
            }

            if (this.xtype == 'system-search-field') {
                searchField = this;
            }

            if (store && store.$className) {
                store.addListener('load', System.view.component.field.search.util.SearchFieldRenderUtils.highlightSearchResults, searchField);
                searchField.initSearch(searchField);
            }
        },

        /**
         *This method is called when a store is created that matches
         * the string store id of the store that is connected to this search field.
         *
         * Once the underlying store is created this method is invoked and
         * it will continue on to configure that store to listen to load events and
         * highlight search results
         *
         * @param newStore - The store that has been created and added to the store manager (Any store)
         * @param records - Any records for this newStore
         * @param index - The index at which the records were inserted
         * @param eOpts - The options object passed to Ext.util.Observable.addListener
         */
        storeNameLoadListener: function (index, newStore, key, eOpts) {
            var me = this; //'this' is the search field
            //Get store as currently configured, most likely it's storeId
            var store = me.parentEntity.store;

            if (Ext.isString(store) && store == key) {
                //Store was created and added before our parent even knows
                me.parentEntity.store = newStore;
                System.view.component.field.search.util.SearchFieldSetupUtils.storeLoadListenerSetup(me, me.parentEntity, me.parentEntity.store);
            }

        }
    }

});