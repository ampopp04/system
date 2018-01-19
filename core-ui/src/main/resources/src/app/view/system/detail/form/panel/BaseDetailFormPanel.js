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
        'System.view.component.field.SystemFieldText',
        'System.view.component.field.SystemFieldCurrency',
        'System.view.component.field.SystemFileField'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'base-detail-form-panel',
    controller: 'baseDetailFormPanelController',

    /**
     * Default filter values used to set or filter various
     * fields on this form panel.
     *
     * They are represented in filter form but will be applied directly
     * as values to any non-combo box. If the property is a combo box
     * then it will be used to set the filterBy values on the combo.
     *
     * Ex.
     *
     * [{
            "operator": "=",
            "value": 4,
            "property": "fkEntityId"
        }, {
            "operator": "=",
            "value": 12,
            "property": "systemEntityNoteType.schemaTableColumn.id"
        }]
     *
     */
    newEntityDetailWindowDefaultValueFilters: undefined,

    bodyPadding: 10,
    frame: true,
    scrollable: true,
    // consider layout: 'fit'
    layout: {type: 'vbox', align: 'stretch'},

    //trackResetOnLoad: true,

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

    constructor: function (config) {
        Ext.suspendLayouts();

        var me = this;
        var fields = config.fieldSet;
        var initialRecord = config.initialFormRecord;

        me.newEntityDetailWindowDefaultValueFilters = config.newEntityDetailWindowDefaultValueFilters;
        me.referenceEntity = config.referenceEntity;

        var items = me.items;

        if (fields && fields.length > 0) {
            me.fieldDefaults.labelWidth = me.calculateLabelWidth(fields);
        }

        if (items == undefined) {
            items = [];
        }

        fields.forEach(function (field) {
            var uiFieldConfiguration = field.uiFieldConfiguration == undefined ? {} : field.uiFieldConfiguration;

            var item = field.reference ?
                {
                    xtype: 'system-field-combo-box',
                    valueField: 'id',
                    displayField: field.displayFieldName,
                    name: field.name,
                    fieldLabel: field.fieldDisplayLabel,
                    store: field.store ? field.store : field.reference.type + 'Store'
                } :
                {
                    xtype: 'system-field-text',
                    name: field.name,
                    fieldLabel: field.fieldDisplayLabel,
                    hidden: field.name == 'id' ? true : false
                };

            item.schemaTableColumn = field.schemaTableColumn;
            items.push(me.setEntityValueFromRecord(me.setNewEntityDetailWindowDefaultValueFilters(me.adjustCheckboxLabel(Ext.apply(item, uiFieldConfiguration))), initialRecord));
        });

        //Erase the default value filters once they are set of their associated fields
        me.newEntityDetailWindowDefaultValueFilters = undefined;
        delete config['newEntityDetailWindowDefaultValueFilters'];

        config.items = items;
        me.callParent([config]);

        Ext.resumeLayouts(true);
    },

    setEntityValueFromRecord: function (field, record) {
        if (record && record.data && record.data[field.name]) {
            field.value = record.data[field.name];
            if (record.phantom == true) {
                field.dirty = true;
            }
        }

        return field;
    },

    setNewEntityDetailWindowDefaultValueFilters: function (formField) {
        var me = this;

        if (me.newEntityDetailWindowDefaultValueFilters) {
            me.newEntityDetailWindowDefaultValueFilters.forEach(function (defaultFilterValue) {
                var formFieldName = formField.name;
                var filterFormFieldPath = defaultFilterValue.property;

                //If our property starts with the name of this form field then this filter is for that field
                if (filterFormFieldPath && filterFormFieldPath.split('.')[0] == formFieldName) {
                    if (formField.xtype == 'system-field-combo-box') {
                        //This is a combo box filter so we need to apply this filter to it's defaultLoadFilters

                        //The defaultFilterValue filterFormFieldPath contains the name of this formField
                        //Remove the name and the . which will leave you with the actual path we want to filter on
                        var comboLoadFilter = Ext.apply(Ext.clone(defaultFilterValue), {property: System.util.StringUtils.replace(filterFormFieldPath, formFieldName + '.', '')});

                        //Add the filter to the combo box, account for multiple filters being possible
                        if (Ext.isArray(formField.defaultLoadFilters)) {
                            formField.defaultLoadFilters.push(comboLoadFilter);
                        } else {
                            formField.defaultLoadFilters = [comboLoadFilter];
                        }

                    } else {
                        //Not a combo box filter so it's just a raw value we should set
                        // since this is a simple entity like a text, text area, checkbox, numberfield, etc
                        formField.value = defaultFilterValue.value;
                    }
                }

            });

        }

        return formField;
    },

    adjustCheckboxLabel: function (formField) {
        if (formField && formField.xtype == 'checkbox') {
            var fieldLabel = formField.fieldLabel;
            if (fieldLabel && formField.boxLabel == undefined) {
                formField.boxLabel = fieldLabel;
                formField.hideLabel = true;
            }
        }
        return formField;
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

        return maxLabelWidth * 8;
    }
});