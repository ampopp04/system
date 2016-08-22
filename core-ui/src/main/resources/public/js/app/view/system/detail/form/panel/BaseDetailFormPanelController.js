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

        if (form.isValid()) { // make sure the form contains valid data before submitting
            form.updateRecord(record); // update the record with the form data
            record.save({ // save the record to the server
                success: function () {
                },
                failure: function () {
                    Ext.Msg.alert('Failure', 'Failed to save data.')
                }
            });

            this.view.up('window').close();
        } else { // display error alert if the data is invalid
            Ext.Msg.alert('Invalid Data', 'Please correct form errors.')
        }
    }
});