/**
 * The <class>System.view.system.detail.window.DetailFormWindow</class> defines
 * the basic detail for window. This is the window that is used when drilling into entities.
 *
 * This window would be used when double clicking on a grid row or a combo box to drill into
 * the details of that entity
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.window.DetailFormWindow', {
    extend: 'System.view.component.window.tab.TabSystemWindow',

    requires: [
        'System.view.system.detail.form.panel.BaseDetailFormPanel'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'detail-form-window',

    width: 600,
    height: 400,

    /**
     * This detail form window primarily consists on the detail form panel
     */
    initComponent: function () {
        var me = this;

        me.tabs = [
            Ext.apply({
                title: 'Details',
                xtype: 'base-detail-form-panel'
            }, {viewModel: this.config.viewModel})
        ];

        me.callParent();
    }
});



