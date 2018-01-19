/**
 * The <class>System.util.application.UserUtils</class> defines
 *  utility methods for working with the user.
 *
 * @author Andrew
 */
Ext.define('System.util.application.UserUtils', {

    requires: [
        'System.util.application.SessionMonitor'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Log out the user
         */
        logout: function () {
            Ext.TaskManager.stop(System.util.application.SessionMonitor.countDownTask);

            System.util.application.SessionMonitor.window.hide();
            System.util.application.UserUtils.onLogout();
        },

        /**
         * Perform the logout request to the server
         */
        onLogout: function () {
            try {
                Ext.Ajax.request({
                    url: 'logout',
                    success: function () {
                    },
                    failure: System.util.application.UserUtils.onLogoutFailure
                });
            } finally {
                System.util.application.UserUtils.onLogoutSuccess();
            }
        },

        /**
         * Handle a successful logout
         */
        onLogoutSuccess: function () {
            try {
                localStorage.clear();
                sessionStorage.clear();
                Ext.util.Cookies.clear('SESSION');
                Ext.util.Cookies.clear('remember-me');
            } catch (e) {
                console.log(e);
            } finally {
                window.location.reload();
            }
        },

        /**
         * Handle a failed logout attempt
         *
         * @param conn
         */
        onLogoutFailure: function (conn) {
            System.util.application.ErrorUtils.showErrorMsg(conn.responseText);
        },

        /**
         * Send a heart beat to the server to maintain the users server session
         */
        heartBeat: function () {
            Ext.TaskManager.stop(System.util.application.SessionMonitor.countDownTask);
            System.util.application.SessionMonitor.window.hide();
            System.util.application.SessionMonitor.start();
            // 'poke' the server-side to update your session.
            Ext.Ajax.request({
                url: '../login'
            });
        }
    }
});