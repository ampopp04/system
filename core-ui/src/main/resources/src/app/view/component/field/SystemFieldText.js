/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.field.SystemFieldText</class> defines
 *  a text field that can be used within the UI. This is primarily used within an entity's detail page.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.SystemFieldText', {
    extend: 'Ext.form.field.Text',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'system-field-text',

    validateBlank: true,

    //If this field has a value set upon load then mask this field as
    // it should not be allowed to change
    maskIfInitialValueExists: false,
    //If we are to mask the field then we only mask the text area
    maskElement: 'inputWrap',

    /**
     * Hide this field while it's value is empty/null/undefined
     */
    hideIfNull: false,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Initialize the component by configuring it's properties from the provided field
     */
    initComponent: function () {
        var me = this;

        me.wasDirty = false;

        if (me.hideIfNull && Ext.isEmpty(me.config.value)) {
            me.hidden = true;
        }

        me.callParent();
    },

    resetOriginalValue: function () {

        this.originalValue = this.getValue();
        this.checkDirty();

        if (this.maskIfInitialValueExists && !Ext.isEmpty(this.originalValue)) {
            //Mask and hide all triggers except the detail page trigger
            this.mask();
        }

    },

    listeners: {

        afterrender: function () {
            this.resetOriginalValue();
        }

    }

});