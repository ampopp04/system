/**
 * The <class>System.view.component.panel.tab.BaseSystemTabPanel</class> defines
 * the base configuration for the tab panel.
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.tab.BaseSystemTabPanel', {
    extend: 'Ext.tab.Panel',

    requires: [
        'Ext.ux.TabScrollerMenu'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'base-system-tab-panel',
    region: 'center'

    /**
     * Broken : EXTJS-13538 Ext.ux.TabScrollerMenu does not work with Ext JS 5.0
     * , plugins: [{
       * ptype: 'tabscrollermenu'
    *}]
     * */
});