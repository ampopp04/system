/**
 * The <class>System.view.component.window.tab.TabSystemWindow</class> defines
 *  the basic tab system window. This is the lowest base level tabed window.
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.tab.TabSystemWindow', {
    extend: 'System.view.component.window.BaseSystemWindow',

    requires: ['System.view.component.panel.tab.BaseSystemTabPanel'],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'tab-system-window',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Initialize this window with a base system tab panel
     */
    initComponent: function () {
        var me = this;

        if (me.tabs === undefined) {
            me.tabs = [];
        }

        me.items = Ext.apply({
            xtype: 'base-system-tab-panel'
        }, {items: me.tabs});

        me.callParent();
    },


    listeners: {

        /**
         * Enable drag and drop of the window by clicking on the main
         * tab panel header bar
         *
         * @param form
         * @param layout
         * @param eOpts
         */
        afterlayout: function (form, layout, eOpts) {
            if (layout.layoutCount == 1) {
                var me = this,
                    ddConfig, dd;

                if (me.draggable) {
                    ddConfig = Ext.applyIf({
                        el: me.el,
                        delegate: '#' + form.items.items[0].tabBar.id
                    }, me.draggable);

                    dd = me.dd = new Ext.util.ComponentDragger(me, ddConfig);
                    me.relayEvents(dd, ['dragstart', 'drag', 'dragend']);
                }
            }
        }
    }
});