/**
 * The <class>System.view.component.field.SystemFieldComboBox</class> defines
 *  a combo box that can be used within the UI. This is primarily used within an entity's detail page.
 *
 * A combo box supports double clicking to drill into it and open up the reference detail page for
 * further details on that entity. It also populates a list of choices from the server for selection.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.SystemFieldComboBox', {
    extend: 'Ext.form.field.ComboBox',

    requires: [
        'System.util.StringUtils',
        'System.util.data.StoreUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'system-field-combo-box',

    //field: DEFINE-THE-FIELD

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Initialize the component by configuring it's properties
     * from the provided field object
     */
    initComponent: function () {
        var me = this;
        var field = me.field;
        me.fieldLabel = System.util.StringUtils.insertSpaceBeforeCapitals(System.util.StringUtils.capitalize(field.name));
        me.store = System.util.data.StoreUtils.lookupStoreByName(field.reference.type + 'Store');
        me.displayField = 'name';
        me.valueField = 'id';
        me.name = field.name;
        me.setValue = function (v) {
            var me = this;
            var displayValue;

            if (v != null && typeof v === 'object') {
                displayValue = v[me.displayField] ? v[me.displayField] : v[me.valueField];
                me.initialValue = v;
            } else {
                displayValue = v;
            }

            Ext.form.ComboBox.superclass.setValue.call(this, displayValue);
        }
        me.callParent();
    },

    listeners: {
        /**
         * After the component is rendered we must add a double click event
         * on it so that when it is double clicked we will open the detail form window
         * associated with the entity currently selected within the combo box.
         *
         * @param event
         */
        afterrender: function (event) {
            event.getEl().on('dblclick', function (event) {
                var combo = Ext.getCmp(event.currentTarget.id);
                var selectedValue = combo.initialValue;
                var store = combo.store;

                System.util.data.StoreUtils.queryStoreById(store, selectedValue.id, function (records, operation, success, scope) {
                    var record = records[0];
                    var win = Ext.create("System.view.system.detail.window.DetailFormWindow", {
                        title: System.util.data.ModelUtils.modelNameToDisplayName(record.store.model.entityName),
                        viewModel: {
                            data: {
                                rec: record
                            }
                        }
                    });

                    win.animate({
                        to: {x: scope.getXY()[0] + 20}
                    }).animate({
                        to: {y: scope.getXY()[1] + 20}
                    });

                    win.show();

                }, undefined, combo.up('window'));
            });
        }
    }
});