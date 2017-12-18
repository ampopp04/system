/**
 * The <class>System.util.application.ErrorUtils</class> defines
 * the error utils that can be used within the UI side code
 *
 * @author Andrew
 */
Ext.define('System.util.application.ErrorUtils', {

    requires: [
        'System.util.application.UserUtils',
        'System.util.application.Util'
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
                    System.util.application.Util.showToast("Session Expired. Logging out.");
                    System.util.application.UserUtils.onLogoutSuccess();
                } else {
                    var decodedErrorMsg = errorMsg;

                    try {
                        decodedErrorMsg = Ext.JSON.decode(errorMsg);
                    } catch (e) {
                    }

                    if (decodedErrorMsg.apierror) {
                        decodedErrorMsg = decodedErrorMsg.apierror;
                    }

                    var message = "System error, retry again shortly.";

                    if (decodedErrorMsg) {

                        if (decodedErrorMsg.subErrors && decodedErrorMsg.subErrors.length > 0) {
                            message = decodedErrorMsg.subErrors[(decodedErrorMsg.subErrors.length - 1)].message;

                        } else {
                            message = decodedErrorMsg.message;

                            if (decodedErrorMsg.debugMessage) {
                                message = message + ' Technical Error [' + decodedErrorMsg.debugMessage + '].';
                            }

                        }

                    }

                    System.util.application.ErrorUtils.showErrorMsg(message);
                }
            }
        },

        /**
         * Display an error message pop-up box
         * @param text
         */
        showErrorMsg: function (text) {
            System.util.application.Util.showErrorMessage(text);
        }
    }
});