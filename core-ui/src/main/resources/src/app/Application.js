/**
 * The <class>System.Application</class> defines the UI
 * application entry point. This is where the UI application is first loaded
 *
 * @author Andrew
 */
Ext.define('System.Application', {
    extend: 'Ext.app.Application',

    requires: [
        'System.util.system.UiComponentUtils',
        'System.view.application.login.Login'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    name: 'System',

    glyphFontFamily: 'FontAwesome',
    enableQuickTips: true,
    defaultToken: 'home',

    quickTips: false,
    platformConfig: {
        desktop: {
            quickTips: true
        }
    },

    onAppUpdate: function () {
        Ext.Msg.alert('Application Update', 'This application has an update, reload?',
            function () {
                System.util.application.UserUtils.onLogout();
            }
        );
    },

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    launch: function () {
        var loggedIn = localStorage.getItem("user");

        if (loggedIn) {
            System.util.system.UiComponentUtils.loadAppMain();
        } else {
            Ext.widget('login-dialog', {
                renderTo: Ext.getBody()
            });
        }
    }
});