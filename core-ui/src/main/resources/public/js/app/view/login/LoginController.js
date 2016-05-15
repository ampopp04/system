Ext.define('System.view.login.LoginController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.login',

    requires: [
        'System.view.login.CapsLockTooltip',
        'System.util.Util',
        'System.util.SessionMonitor'
    ],

    onTextFieldSpecialKey: function (field, e, options) {
        if (e.getKey() === e.ENTER) {
            this.doLogin();
        }
    },

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

    onButtonClickCancel: function (button, e, options) {
        this.lookupReference('form').reset();
    },

    onButtonClickSubmit: function (button, e, options) {
        var me = this;

        if (me.lookupReference('form').isValid()) {
            me.doLogin();
        }
    },

    doLogin: function () {

        var me = this,
            form = me.lookupReference('form');

        me.getView().mask('Authenticating... Please wait...');

        form.submit({
            clientValidation: true,
            url: '../login',
            scope: me,
            success: 'onLoginSuccess',
            failure: 'onLoginFailure'
        });
    },

    onLoginFailure: function (form, action) {

        this.getView().unmask();

        //alternative to code above - reuse code
        System.util.Util.handleFormFailure(action);
    },

    onLoginSuccess: function (form, action) {
        var view = this.getView();
        view.unmask();
        view.close();
        Ext.create('System.view.main.Main');
        System.util.SessionMonitor.start();
    }
});