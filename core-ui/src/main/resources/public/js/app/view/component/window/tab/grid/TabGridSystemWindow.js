/**
 * The <class>System.view.component.window.tab.grid.TabGridSystemWindow</class> defines
 *  a basic tabed window that inserts a base system grid panel within each tab.
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.tab.grid.TabGridSystemWindow', {
    extend: 'System.view.component.window.tab.TabSystemWindow',

    requires: [
        'System.view.component.panel.grid.BaseSystemGridPanel',
        'System.view.component.panel.grid.ParentSearchCallerGridPanel'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'tab-grid-system-window',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Initialize each of the tabs with a base system grid panel
     */
    initComponent: function () {
        var me = this;

        if (me.tabs) {
            me.tabs.forEach(function (tab) {
                var xtypeValue = tab.parentSearchCaller ? 'parent-search-caller-grid-panel' : 'base-system-grid-panel';

                Ext.apply(tab, {
                    parentSearchCaller: tab.parentSearchCaller,
                    xtype: xtypeValue,
                    modelName: tab.modelName
                })
            });
        }

        me.callParent();
    }
});



