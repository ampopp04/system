/**
 * The <class>System.view.system.detail.form.panel.BaseDetailFormPanel</class> defines
 * the base form panel used in detail windows.
 *
 * Provided a 'config.viewModel.data.rec' this class will iterate over each field
 * and dynamically construct a form populating it with the correct fields in order.
 *
 * For example these will be text fields or combo boxes.
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.form.panel.BaseDetailFormPanel', {
    extend: 'Ext.form.Panel',

    requires: [
        'System.view.system.detail.form.panel.BaseDetailFormPanelController',
        'System.util.StringUtils',
        'System.util.data.StoreUtils',
        'System.view.component.field.SystemFieldComboBox',
        'System.view.component.field.SystemFieldText'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'base-detail-form-panel',
    controller: 'baseDetailFormPanelController',

    bodyPadding: 10,
    frame: true,

    layout: {type: 'vbox', align: 'stretch'},

    fieldDefaults: {},

    buttons: [
        {text: 'Save', formBind: true, disabled: true, handler: 'save'},
        {text: 'Cancel', handler: 'cancel'}
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * This method acts as the constructor to each instance of this object.
     * Provided the record to construct from this method will build the fields for this detail form
     */
    initComponent: function () {
        var me = this;
        var rec = this.config.viewModel.data.rec;
        var fields = rec.getFields();
        me.fieldDefaults.labelWidth = me.calculateLabelWidth(fields);

        var items = [];

        fields.forEach(function (field) {
            var field = field.reference ?
            {xtype: 'system-field-combo-box', field: field} :
            {xtype: 'system-field-text', field: field};

            items.push(field);
        });

        me.items = items;
        me.callParent();
    },

    /**
     * This method will dynamically calculate the max label width
     * required from the longest name of all the fields to be displayed
     *
     * @param fields
     * @returns {number}
     */
    calculateLabelWidth: function (fields) {
        var maxLabelWidth = 0;

        fields.forEach(function (field) {
            var fieldLabelWidth = field.name.length;
            if (fieldLabelWidth > maxLabelWidth) {
                maxLabelWidth = fieldLabelWidth;
            }
        });

        return maxLabelWidth * 9;
    },

    /**
     * Because of the way the record array names are constructed we need to adjust them
     * to follow the naming convention of our passed in records object so we can dynamically access the data
     */
    initItems: function () {
        var me = this;
        me.callParent();

        var rec = me.config.viewModel.data.rec;

        for (var property in rec) {
            if (rec.hasOwnProperty(property) && System.util.StringUtils.startsWith(property, '_')) {
                rec.data[System.util.StringUtils.removeFirstCharacter(property)] = rec[property].data;
            }
        }

        this.loadRecord(rec);
    }
});