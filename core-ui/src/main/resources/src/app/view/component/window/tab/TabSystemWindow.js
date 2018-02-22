/**
 * The <class>System.view.component.window.tab.TabSystemWindow</class> defines
 *  the basic tab system window. This is the lowest base level tabed window.
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.tab.TabSystemWindow', {
    extend: 'System.view.component.window.BaseSystemWindow',

    requires: [
        'System.view.component.panel.tab.BaseSystemTabPanel',
        'Ext.plugin.LazyItems'
    ],

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
    constructor: function (config) {
        Ext.suspendLayouts();

        if (config.tabs === undefined) {
            config.tabs = [];
        }

        config.tabs = this.initializeLazyTabItems(config.tabs);

        var tabConfiguration = Ext.apply({
            xtype: 'base-system-tab-panel'
        }, {items: config.tabs});

        var tabConfig = config.tabConfig;

        if (tabConfig) {
            tabConfiguration = Ext.apply(tabConfiguration, tabConfig);
        }

        config.items = tabConfiguration;

        this.callParent([config]);

        Ext.resumeLayouts(true);
    },

    initializeLazyTabItems: function (tabs) {
        if (tabs) {
            var updatedTabs = [];

            tabs.forEach(function (tab) {
                updatedTabs.push(this.createLazyTabItem(tab));
            }, this);

            return updatedTabs;
        }

        return tabs;
    },

    createLazyTabItem: function (tab) {
        var title = tab.title;
        delete tab.title;

        return {

            title: title,

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [
                        Ext.apply({}, tab)
                    ]
                }
            }

        };

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