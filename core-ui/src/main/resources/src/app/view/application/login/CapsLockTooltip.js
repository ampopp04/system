/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.application.login.CapsLockTooltip</class> defines
 * the tooltip used by the login dialog
 *
 * @author Andrew
 */
Ext.define('System.view.application.login.CapsLockTooltip', {
    extend: 'Ext.tip.QuickTip',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'capslocktooltip',

    target: 'password',
    anchor: 'top',
    anchorOffset: 0,
    width: 300,
    dismissDelay: 0,
    autoHide: false,
    title: '<div class="fa fa-exclamation-triangle">Caps Lock is On</div>',
    html: '<div>Having Caps Lock on may cause you to enter your password incorrectly.</div><br/>' +
    '<div>You should press Caps Lock to turn it off before entering your password.</div>'
});
