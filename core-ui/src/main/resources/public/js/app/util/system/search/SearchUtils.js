/**
 * The <class>System.util.system.search.SearchUtils</class> defines
 * utility methods for working with search related
 * UI components. This typically revolves around
 * enabling advanced searching within various components
 * in the UI.
 *
 * @author Andrew
 */
Ext.define('System.util.system.search.SearchUtils', {

    requires: [
        'System.view.component.field.search.SearchField'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        addSearchFieldToTopToolbarDock: function (parentEntity, defaultSearchFilter, defaultSearchFilterForced) {
            if (parentEntity.dockedItems == undefined) {
                parentEntity.dockedItems = [];
            }

            parentEntity.dockedItems.push({
                dock: 'top',
                xtype: 'toolbar',
                items: [
                    '->',
                    System.util.system.search.SearchUtils.getSearchFilterItem(parentEntity, defaultSearchFilter, defaultSearchFilterForced)
                ]
            });
        },

        getSearchFilterItem: function (parentEntity, defaultValue, defaultSearchFilterForced) {
            return {
                xtype: 'system-search-field',

                parentEntity: parentEntity,
                defaultSearchValue: defaultValue,
                defaultSearchFilterForced: defaultSearchFilterForced
            };
        }

    }
});