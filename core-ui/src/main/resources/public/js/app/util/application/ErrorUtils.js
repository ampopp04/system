/**
 * The <class>System.util.application.ErrorUtils</class> defines
 * the error utils that can be used within the UI side code
 *
 * @author Andrew
 */
Ext.define('System.util.application.ErrorUtils', {

    requires: [
        'System.util.application.UserUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Check if data request is an error
         * @param success
         * @param errorMsg
         * @param statusCode
         */
        dataRequestErrorCheck: function (success, errorMsg, statusCode) {
            if (!success) {
                if (statusCode == 401) {
                    System.util.application.UserUtils.logout();
                } else {
                    System.util.application.ErrorUtils.showErrorMsg(errorMsg);
                }
            }
        },

        /**
         * Display an error message pop-up box
         * @param text
         */
        showErrorMsg: function (text) {
            Ext.Msg.show({
                title: 'Error!',
                msg: text,
                icon: Ext.Msg.ERROR,
                buttons: Ext.Msg.OK
            });
        }
    }
});