Ext.require('Ext.layout.container.Fit');
Ext.require('Ext.form.Panel');
Ext.require('Ext.layout.container.Border');
Ext.require('Ext.layout.container.Center');
Ext.require('Ext.form.FieldSet');
Ext.require('Ext.form.field.Hidden');
Ext.require('Ext.form.field.File');
Ext.require('Ext.Img');
Ext.require('Ext.form.field.ComboBox');
Ext.require('Ext.layout.container.Accordion');
Ext.require('Ext.grid.column.Date');
Ext.require('Ext.grid.column.Widget');
Ext.require('Ext.form.field.Tag');

Ext.require('System.view.login.Login');
Ext.require('System.view.main.Main');

Ext.require('Ext.data.validator.Exclusion');
Ext.require('Ext.data.validator.Format');
Ext.require('Ext.data.validator.Presence');
Ext.require('Ext.data.validator.Length');
Ext.require('Ext.data.validator.Email');

Ext.define('System.Application', {
    extend: 'Ext.app.Application',

    name: 'System',

    glyphFontFamily: 'FontAwesome',

    requires: [],

    enableQuickTips: true,

    views: [],
    controllers: [],
    stores: [],

    defaultToken: 'home',

    launch: function () {
        var loggedIn = localStorage.getItem("user");

        Ext.widget(loggedIn ? 'app-main' : 'login-dialog', {
            renderTo: Ext.getBody(),
            bodyCls: "background-image:url('js/lib/resources/images/background/background1.jpg');"
        });
    },

    init: function () {
    }
});