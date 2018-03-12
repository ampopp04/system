/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

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
        dataRequestErrorCheck: function (success, errorMsg, statusCode, operation) {
            if (!success) {
                var proxy = undefined;
                var params = {};

                if (operation) {
                    if (operation.getProxy && operation.getParams) {
                        proxy = operation.getProxy();
                        params = operation.getParams();
                    } else if (operation.request && operation.request.proxy && operation.request.params) {
                        proxy = operation.request.proxy;
                        params = operation.request.params;
                    }
                }

                if (statusCode == 401) {
                    System.util.application.Util.showToast("Session Expired. Logging out.");
                    System.util.application.UserUtils.onLogoutSuccess();
                } else if (statusCode == 404 && proxy) {
                    //Entity not found
                    var entityName = System.util.data.ModelUtils.modelNameToDisplayName(System.util.StringUtils.capitalize(proxy.entityName));
                    var paramObject = params;
                    delete paramObject["projection"];
                    var paramMessage = System.util.StringUtils.replace(System.util.StringUtils.replace(System.util.StringUtils.replace(System.util.StringUtils.replace(JSON.stringify(paramObject), '"', ''), ':', ' = '), '{', '['), '}', ']');
                    System.util.application.Util.showToast(entityName + " not found. Request Details: " + paramMessage);
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