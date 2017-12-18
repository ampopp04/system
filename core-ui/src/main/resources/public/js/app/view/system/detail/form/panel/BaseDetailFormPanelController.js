/**
 * The <class>System.view.system.detail.form.panel.BaseDetailFormPanelController</class> defines
 * the controller methods for the System.view.system.detail.form.panel.BaseDetailFormPanel
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.form.panel.BaseDetailFormPanelController', {
    extend: 'Ext.app.ViewController',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    alias: 'controller.baseDetailFormPanelController',

    requires: [
        'System.util.data.RecordUtils',
        'System.util.application.Util'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                         //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Close the window
     */
    cancel: function () {
        this.view.up('window').close();
    },

    /**
     * If the form is valid then save it to the server and close the window
     */
    save: function () {
        var form = this.view.form;
        var record = form.getRecord(); // get the underlying model instance

        form.getFieldValues = function (dirtyOnly) {
            return this.getValues(false, true, false, true);
        };

        // make sure the form contains valid data before submitting
        if (form.isValid()) {

            if (form.isDirty()) {
                // update the record with the form data
                var fieldValuesWithId = form.getValues(false, false, false, true, false);

                if (record.id == undefined && fieldValuesWithId.id == null) {
                    delete fieldValuesWithId['id'];
                }

                record.set(fieldValuesWithId);

                //Iterate over each of the fields an update reference combo fields to have
                // the entire record available to the total form record.
                if (form.owner && form.owner.items && form.owner.items.items) {
                    Ext.Array.forEach(form.owner.items.items, function (field) {
                        var me = this;

                        var lastRecord = (field.lastSelectedRecords ? field.lastSelectedRecords[0] : undefined);

                        if (lastRecord && lastRecord.get(field.valueField) && lastRecord.get(field.displayField)) {
                            me.data[field.name] = lastRecord;
                        }

                    }, record);
                }

                if (Ext.isEmpty(record.id)) {
                    record.store.add(record);
                }

            }

            this.view.up('window').close();

        } else {
            // display error alert if the data is invalid
            System.util.application.Util.showInfoMessage('Invalid Data', 'Please correct form errors.')
        }
    }
});