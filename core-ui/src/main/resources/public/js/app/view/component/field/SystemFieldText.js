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
    //field: DEFINE-THE-FIELD

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Initialize the component by configuring it's properties from the provided field
     */
    initComponent: function () {
        var me = this;
        var field = me.field;

        me.xtype = 'textfield';
        me.fieldLabel = System.util.StringUtils.insertSpaceBeforeCapitals(System.util.StringUtils.capitalize(field.name));
        me.name = field.name;

        me.callParent();
    }
});