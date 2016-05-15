Ext.define('System.view.main.MainController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.main',

    requires: [
        'System.util.Util'
    ],

    init: function() {
    },

    onLogout: function(button, e, options){

        var me = this;
        Ext.Ajax.request({
            url: '../logout',
            scope: me,
            success: 'onLogoutSuccess',
            failure: 'onLogoutFailure'
        });
    },

    onLogoutSuccess: function(conn, response, options, eOpts){
        var result = System.util.Util.decodeJSON(conn.responseText);

        if (result.success) {

            this.getView().destroy();
            window.location.reload();
        } else {

            System.util.Util.showErrorMsg(result.msg);
        }
    },

    onLogoutFailure: function(conn, response, options, eOpts){
        System.util.Util.showErrorMsg(conn.responseText);
    }
});