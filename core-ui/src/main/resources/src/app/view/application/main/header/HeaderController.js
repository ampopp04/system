/**
 * The <class>System.view.application.main.header.HeaderController</class> defines
 * the header controller methods for the header view
 *
 * @author Andrew
 */
Ext.define('System.view.application.main.header.HeaderController', {
    extend: 'Ext.app.ViewController',

    requires: [
        'System.util.application.UserUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'controller.header',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Handle the logout event
     *
     * @param button
     * @param e
     * @param options
     */
    onLogout: function (button, e, options) {
        System.util.application.UserUtils.onLogout();
    }
});