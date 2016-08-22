/**
 * The <class>System.util.application.Util</class> defines
 *  utility methods for working with the user.
 *
 * @author Andrew
 */
Ext.define('System.util.application.Util', {

    requires: [
        'Ext.window.Toast',
        'System.util.application.ErrorUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        required: '<span style="color:red;font-weight:bold" data-qtip="Required"> *</span>',

        /**
         * Decode a json response
         *
         * @param text
         * @returns {*|{expression, map}}
         */
        decodeJSON: function (text) {
            var result = Ext.JSON.decode(text, true);

            if (!result) {
                result = {};
                result.success = false;
                result.msg = text;
            }

            return result;
        },

        /**
         * Handle a form submit server failure
         *
         * @param action
         */
        handleFormFailure: function (action) {
            var me = this, result = System.util.application.Util.decodeJSON(action.response.responseText);

            switch (action.failureType) {
                case Ext.form.action.Action.CLIENT_INVALID:
                    System.util.application.ErrorUtils.showErrorMsg('Form fields may not be submitted with invalid values');
                    break;
                case Ext.form.action.Action.CONNECT_FAILURE:
                    System.util.application.ErrorUtils.showErrorMsg(action.response.responseText);
                    break;
                case Ext.form.action.Action.SERVER_INVALID:
                    System.util.application.ErrorUtils.showErrorMsg(result.msg);
            }
        },

        /**
         * Show a toast
         *
         * @param text
         */
        showToast: function (text) {
            Ext.toast({
                html: text,
                closable: false,
                align: 't',
                slideInDuration: 400,
                minWidth: 400
            });
        }
    }
});