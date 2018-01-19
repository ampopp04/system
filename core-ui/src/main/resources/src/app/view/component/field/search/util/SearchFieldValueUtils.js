/**
 * The <class>System.view.component.field.search.util.SearchFieldValueUtils</class> defines
 *  search field specific utility methods for handling the addition or removal of
 *  search strings to/from the search field.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.search.util.SearchFieldValueUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Interprets the searchString and creates a store filter specific to
         * the type of search string that was added
         *
         * @param searchString -  a search string or pre-created search string filter
         * @returns {*} - Returns a filter model instance from our in memory search filter store
         */
        createDefaultSearchValue: function (searchString, searchFieldStore) {
            var defaultModelValue = undefined;

            if (searchString.isModel) {
                return searchString;
            }
            else if (Ext.isString(searchString)) {
                defaultModelValue = {
                    id: System.view.component.field.search.util.SearchFieldFilterUtils.getFilterId(searchString),
                    value: searchString,
                    property: 'null-all-property',
                    operator: undefined
                };
            }
            else {
                defaultModelValue = searchString;
                defaultModelValue.id = System.view.component.field.search.util.SearchFieldFilterUtils.getFilterId(defaultModelValue.value);
            }

            return searchFieldStore.createModel(defaultModelValue);
        },

        /**
         * Iterates over all the search values currently set in the search field combo box
         * and generates our filter models that are used to filter the associated parent entitys
         * view/store
         *
         * @param searchValues - The values that are currently set in the search field combo box
         * @returns {Array} - An array of search field model filters
         */
        createSearchModels: function (searchValues, store) {
            var newValue = [];

            if (searchValues) {
                Ext.Array.forEach(searchValues, function (val) {
                    var store = this;
                    newValue.push(System.view.component.field.search.util.SearchFieldValueUtils.createDefaultSearchValue(val, store));
                }, store);
            }

            return newValue;
        }

    }

});