

/**
 * The main application class. An instance of this class is created by app.js when it calls
 * Ext.application(). This is the ideal place to handle application launch and initialization
 * details.
 */
function loadLocale(){

    var lang = localStorage ? (localStorage.getItem('user-lang') || 'en') : 'en',
        file = Ext.util.Format.format("js/lib/resources/locale/{0}.js", lang),
        extJsFile = Ext.util.Format.format("js/lib/ext/classic/locale/overrides/{0}/ext-locale-{0}.js", lang);
    Ext.Loader.loadScript({url: extJsFile});
}

loadLocale();

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
Ext.require('System.controller.Root');
Ext.require('System.view.menu.Tree');

Ext.require('Ext.data.validator.Exclusion');
Ext.require('Ext.data.validator.Format');
Ext.require('Ext.data.validator.Presence');
Ext.require('Ext.data.validator.Length');
Ext.require('Ext.data.validator.Email');

Ext.define('System.Application', {
    extend: 'Ext.app.Application',
    
    name: 'System',

    glyphFontFamily: 'FontAwesome',

    requires: [
        'System.overrides.tree.ColumnOverride',
        'System.overrides.grid.column.Action',

        'System.overrides.patch.data.ModelWithId' //ExtJS 5 bug fix - remove this once Sencha fixes it
        ,'System.view.base.Grid'
    ],

    enableQuickTips: true,

    views: [
    ],

    controllers: [
        'Menu',
        'StaticData'
    ],

    stores: [
    ],

    defaultToken : 'home',
    
    launch: function () {

        Ext.widget('login-dialog', {renderTo: Ext.getBody(),bodyCls : "background-image:url('js/lib/resources/images/background/background1.jpg');"});
    },

    init: function () {
    }
});