/**
 * The <class>System.view.component.field.search.listener.SearchFieldListeners</class> defines
 *  listeners that a search field will use and listen on.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.search.listener.SearchFieldListeners', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Returns the default set of listeners that a search field will listen on
         */
        getListeners: function () {
            return {

                change: function (tagfield, currentValues, previousValues) {
                    var isAdd = undefined;
                    var arrayValues = undefined;

                    if (currentValues.length > previousValues.length) {
                        //Add Change
                        isAdd = true;
                        arrayValues = Ext.Array.difference(currentValues, previousValues);
                    } else if (previousValues.length > currentValues.length) {
                        //Remove Change
                        isAdd = false;
                        arrayValues = Ext.Array.difference(previousValues, currentValues)
                    }

                    if (arrayValues) {
                        Ext.Array.forEach(arrayValues, function (value) {
                            System.view.component.field.search.util.SearchFieldFilterUtils.updateTagStoreFilters(isAdd ? this.valueCollection.get(System.view.component.field.search.util.SearchFieldFilterUtils.getFilterId(value)) : value, isAdd, this.parentEntity);
                        }, tagfield);
                    }

                },

                render: function () {
                    var me = this;

                    //If this search is desginated to be manually initialized
                    //  at a later time we will skip this configuration attempt on render
                    if (me.manualInitialize) {
                        return;
                    }

                    me.configureSearchField(me);
                },

                beforedestroy: function () {
                    var me = this;
                    me.setValue([]);
                    me.parentEntity.store.clearFilter();
                }

            };
        }

    }

});