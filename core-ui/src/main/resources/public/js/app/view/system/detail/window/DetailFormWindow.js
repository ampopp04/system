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
        'System.view.system.detail.form.panel.BaseDetailFormPanel',
        'System.util.data.ModelUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'detail-form-window',

    width: null,
    height: null,

    maxHeight: '99%',
    maxWidth: '99%',
    
//    initialFormRecord: undefined,
    /**
     * When creating this entity it is possible that a set of filters
     * have been provided for various fields on this entity
     * These will be passed to the form panel to filter or set their default values
     */
    newEntityDetailWindowDefaultValueFilters: undefined,

    /**
     * This detail form window primarily consists on the detail form panel
     */
    initComponent: function () {
        var me = this;

        if (me.tabs === undefined) {
            me.tabs = [];
        }

        me.tabs.unshift(
            Ext.apply({
                title: 'Details',
                newEntityDetailWindowDefaultValueFilters: this.newEntityDetailWindowDefaultValueFilters,
                xtype: 'base-detail-form-panel'
            }, {
                fieldSet: this.config.fieldSet
            })
        );

        me.callParent();
    },

    getInitialFormRecordEntityTableName: function () {
        return this.initialFormRecord ? System.util.data.ModelUtils.modelNameToSchemaTableName(this.initialFormRecord.store.model.entityName) : undefined;
    },

    getInitialFormRecordEntityId: function () {
        return this.initialFormRecord && this.initialFormRecord.data && this.initialFormRecord.data.id ? this.initialFormRecord.data.id : undefined;
    }
});



