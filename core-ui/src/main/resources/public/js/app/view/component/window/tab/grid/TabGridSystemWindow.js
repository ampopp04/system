/**
 * The <class>System.view.component.window.tab.grid.TabGridSystemWindow</class> defines
 *  a basic tabed window that inserts a base system grid panel within each tab.
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.tab.grid.TabGridSystemWindow', {
    extend: 'System.view.component.window.tab.TabSystemWindow',

    requires: [
        'System.view.component.panel.grid.BaseSystemGridPanel'
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
        me.tabs = [];

        me.tabs.forEach(function (tab) {
            Ext.apply(tab, {
                xtype: 'base-system-grid-panel',
                modelName: tab.modelName
            })
        });

        me.callParent();
    }
});



