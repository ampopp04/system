/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.application.login.LoginController</class> defines
 * the login controller to handle login view events
 *
 * @author Andrew
 */
Ext.define('System.view.application.login.LoginController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'System.view.application.login.CapsLockTooltip',
        'System.util.application.Util',
        'System.util.application.SessionMonitor',
        'System.util.system.UiComponentUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'controller.login',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * If the enter key is hit then perform the login
     *
     * @param field
     * @param e
     * @param options
     */
    onTextFieldSpecialKey: function (field, e, options) {
        if (e.getKey() === e.ENTER) {
            this.doLogin();
        }
    },

    /**
     * Check if caps lock is on and notify the user
     *
     * @param field
     * @param e
     * @param options
     */
    onTextFieldKeyPress: function (field, e, options) {
        var charCode = e.getCharCode(),
            me = this;

        if ((e.shiftKey && charCode >= 97 && charCode <= 122) ||
            (!e.shiftKey && charCode >= 65 && charCode <= 90)) {

            if (me.capslockTooltip === undefined) {
                me.capslockTooltip = Ext.widget('capslocktooltip');
            }
            me.capslockTooltip.show();

        } else {
            if (me.capslockTooltip !== undefined) {
                me.capslockTooltip.hide();
            }
        }
    },

    /**
     * If the form is valid then perform the login
     *
     * @param button
     * @param e
     * @param options
     */
    onButtonClickSubmit: function (button, e, options) {
        var me = this;
        if (me.lookupReference('form').isValid()) {
            me.doLogin();
        }
    },

    /**
     * Method that handles the login request form submit to the server
     */
    doLogin: function () {
        var me = this, form = me.lookupReference('form');
        me.getView().mask('Authenticating... Please wait...');

        Ext.util.Cookies.clear('remember-me');
        Ext.util.Cookies.clear('SESSION');

        form.submit({
            clientValidation: true,
            url: 'login',
            scope: me,
            success: 'onLoginSuccess',
            failure: 'onLoginFailure'
        });
    },

    /**
     * Handle a login failure message
     *
     * @param form
     * @param action
     */
    onLoginFailure: function (form, action) {
        this.getView().unmask();
        System.util.application.ErrorUtils.showErrorMsg("Incorrect username and/or password. Try again or contact your administrator.")
    },

    /**
     * Load the main application on login success
     *
     * @param form
     * @param action
     */
    onLoginSuccess: function (form, action) {
        localStorage.setItem("user", true);
        localStorage.setItem("username", form.getFieldValues().username);

        var view = this.getView();
        view.unmask();
        view.close();
        System.util.system.UiComponentUtils.loadAppMain();
        System.util.application.SessionMonitor.start();
    }
});