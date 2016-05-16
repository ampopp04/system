Ext.define('System.view.main.header.HeaderController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.header',

    requires: [
        'System.util.Util'
    ],

    init: function () {
    },

    onLogout: function (button, e, options) {
        Ext.Ajax.request({
            url: 'logout',
            scope: this,
            success: 'onLogoutSuccess',
            failure: 'onLogoutFailure'
        });
    },

    onLogoutSuccess: function () {
        localStorage.removeItem('user');
        this.getView().destroy();
        window.location.reload();
    },

    onLogoutFailure: function (conn) {
        System.util.Util.showErrorMsg(conn.responseText);
    }
});