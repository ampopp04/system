/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.field.SystemFieldText</class> defines
 *  a text field that can be used within the UI. This is primarily used within an entity's detail page.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.SystemFieldCurrency', {
    extend: 'Ext.form.field.Number',

    requires: [
        'Ext.form.field.Number',
        'Ext.util.Format'
    ],

    xtype: 'system-field-currency',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    hideTrigger: true,
    keyNavEnabled: false,
    mouseWheelEnabled: true,
    step: 100,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Initialize the component by configuring it's properties from the provided field
     */
    initComponent: function () {
        var me = this;
        me.wasDirty = false;
        me.callParent();
    },

    getSubmitValue: function () {
        var submitValue = this.callParent();
        return this.trimCurrencyDetails(submitValue);
    },

    getErrors: function (value) {
        return value ? this.callParent([this.trimCurrencyDetails(value)]) : [];
    },
    
    rawToValue: function (rawValue) {
        return this.callParent([this.trimCurrencyDetails(rawValue)]);
    },

    trimCurrencyDetails: function (value) {
        if (value) {
            return Ext.String.trim(value.replace('$', '').replace(new RegExp(',', 'g'), ''));
        } else {
            return value;
        }
    },

    valueToRaw: function (value) {
        var processedValue = this.callParent([value]);

        if (processedValue) {
            return this.formatFieldValue(processedValue);
        } else {
            return processedValue;
        }
    },

    formatFieldValue: function (value) {
        return Ext.util.Format.currency(value);
    },

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Validation                                                       //////////
    //////////////////////////////////////////////////////////////////////

    validator: function (val) {
        return (val || this.allowBlank) ? true : "This field is required";
    }

});