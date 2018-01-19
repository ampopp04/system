/**
 * The <class>System.view.component.field.search.util.SearchFieldFilterUtils</class> defines
 *  search field specific utility methods for filtering the associated store
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.search.util.SearchFieldFilterUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Sets extra search parameters on a stores proxy to be sent
         * to the server
         *
         * @param store - the store that is bound to our search field
         * @param parentEntity - the entity whose view is connected and filtered by this search field and store
         */
        setExtraSearchParams: function (store, parentEntity) {
            var proxy = store.getProxy();
            proxy.setExtraParam('searchDepth', parentEntity.searchDepth);
            proxy.setExtraParam('extraSearchParams', parentEntity.extraSearchParams);
        },

        /**
         * A new search value has been entered. This function will take this new
         * search value and apply a filter to the store to ensure we are actually invoking
         * the search based on this new input search value
         *
         * @param value - the additional value to search on
         * @param parentEntity - the entity whose view will get filtered/updated based on the results of the search
         */
        filterStore: function (value, parentEntity) {
            var newFilter = Ext.apply({
                parentEntity: parentEntity
            }, value.data);

            parentEntity.store.addFilter(newFilter);
        },

        /**
         * A method that generates a standard filter ID.
         * This is used for adding new search filters to our store while
         * allowing for an easy way to remove them after the search value is removed
         *
         * @param searchValue - the search string
         * @returns {*|string} -  a filter ID that uniquely identifies this specific search string
         */
        getFilterId: function (searchValue) {
            return System.util.StringUtils.lowercase(System.util.StringUtils.replaceSpacesWithUnderscores(searchValue + 'Filter'));
        },

        /**
         * Adds or removes search filters from the associated search field store.
         * This method is used for when new search values are either added or remove
         * from the search field
         *
         * @param value - the search string
         * @param isAdd - whether the search string was added or removed to/from the search field
         * @param parentEntity - the associated entity whose view will be updated via the filter manipulation of the associated store
         */
        updateTagStoreFilters: function (value, isAdd, parentEntity) {
            if (parentEntity && parentEntity.store && value) {
                var parentStore = parentEntity.store;

                if (isAdd) {
                    System.view.component.field.search.util.SearchFieldFilterUtils.filterStore(value, parentEntity);
                } else {
                    parentStore.removeFilter(System.view.component.field.search.util.SearchFieldFilterUtils.getFilterId(value));
                }

                System.view.component.field.search.util.SearchFieldFilterUtils.setExtraSearchParams(parentStore, parentEntity);
            }
        }

    }

});