/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.field.search.util.SearchFieldRenderUtils</class> defines
 *  search field specific utility methods. These are used in assisting the
 *  rendering of new search results when the store loads.  They will
 *  high light the results in the associated parent entity's view.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.search.util.SearchFieldRenderUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {


        /**
         * The main method for iterating over the records of a store
         * and highlighting any search results found within it's parent entity's
         * view
         *
         * This function is a callback whose this scope is set to the search field itself
         */
        highlightSearchResults: function (treeStore, records, successful, operation, node, eOpts) {
            var me = this;
            var filteredStore = me.parentEntity.store;

            filteredStore.each(function (record, idx) {

                var node = me.parentEntity.view.getNode(record);
                System.view.component.field.search.util.SearchFieldRenderUtils.highlightNode(node, me);

                if (record.isExpandable && record.isExpandable() && record.childNodes) {

                    Ext.Array.forEach(record.childNodes, function (childNode) {
                        var childNodeView = me.parentEntity.view.getNode(childNode);
                        System.view.component.field.search.util.SearchFieldRenderUtils.highlightNode(childNodeView, me);
                    }, me);
                }

            }, me);
        },


        /**
         * Highlights any potential search results found in any cell of
         * a specific row node
         */
        highlightNode: function (node, searchField) {
            var me = searchField;

            if (node) {
                var columns = me.parentEntity.columnManager.getColumns();

                Ext.Array.forEach(columns, function (column) {
                    var cell = Ext.fly(node).down(column.getCellInnerSelector(), true);

                    if (cell) {

                        Ext.Array.forEach(me.getValue(), function (val) {
                            cell.innerHTML = System.view.component.field.search.util.SearchFieldRenderUtils.strMarkRedPlus(val, cell.innerHTML);
                        }, me);

                    }
                });
            }

        },

        strMarkRedPlus: function (searchString, currentValue) {
            return currentValue.replace(
                new RegExp('(' + Ext.String.escapeRegex(searchString) + ')', "gi"), "<span style='color: red;'><b>$1</b></span>");
        }

    }

});