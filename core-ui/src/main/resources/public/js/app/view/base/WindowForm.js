Ext.define('System.view.base.WindowForm', {
    extend: 'Ext.window.Window',
    alias: 'widget.windowform',

    requires: [
        'System.util.Util',
        'System.util.Glyphs',
        'System.view.base.CancelSaveToolbar'
    ],

    height: 400,
    width: 550,
    autoScroll: true,
    layout: {
        type: 'fit'
    },
    modal: true,
    closable: false,

    bind: {
        title: '{title}',
        glyph: '{glyph}'
    },

    //items must be overrriden in subclass

    dockedItems: [{
        xtype: 'cancel-save-toolbar'
    }]
});