/**
 * The <class>System.util.component.ToolbarUtils</class> defines
 *  utilty methods for working with toolbars.
 *
 * @author Andrew
 */
Ext.define('System.util.component.ToolbarUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Returns a default paging toolbar implementation.
         *
         * @param storeName
         * @returns {{xtype: string, padding: number, pageSize: number, store: *, displayInfo: boolean}}
         */
        pagingtoolbar: function (storeName) {
            return {
                xtype: 'pagingtoolbar',
                padding: 2,
                pageSize: 10,
                store: storeName,
                displayInfo: true
            };
        },

        /**
         * Returns a default add/delete button toolbar
         *
         * @param scope
         * @returns {{dock: string, xtype: string, padding: number, items: *[]}}
         */
        defaultAddAndDeleteToolbar: function (scope) {
            return {
                dock: 'left',
                xtype: 'toolbar',
                padding: 0,
                items: [
                    System.util.component.ToolbarUtils.addButton(scope),
                    System.util.component.ToolbarUtils.deleteButton(scope)
                ]
            };
        },

        /**
         * Defines an add button to add new entities
         *
         * @param scope
         * @returns {{xtype: string, border: boolean, iconCls: string, scope: *, handler: handler}}
         */
        addButton: function (scope) {
            //TODO make open a detail page instead for a new entity
            return {
                xtype: 'button',
                border: false,
                iconCls: 'x-fa fa-plus',
                scope: scope,
                handler: function () {
                    var me = this;
                    me.store.insert(0, new SchemaTables());
                    me.plugins[0].startEdit(0, 0);
                }
            };
        },

        /**
         * Delete a row within a grid
         *
         * @param scope
         * @returns {{xtype: string, border: boolean, iconCls: string, scope: *, handler: handler}}
         */
        deleteButton: function (scope) {
            return {
                xtype: 'button',
                border: false,
                iconCls: 'x-fa fa-minus',
                scope: scope,
                handler: function () {
                    var me = this;
                    var selection = me.getView().getSelectionModel().getSelection()[0];
                    if (selection) {
                        me.store.remove(selection);
                    }
                }
            };
        }
    }
});