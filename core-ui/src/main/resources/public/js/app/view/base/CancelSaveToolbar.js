Ext.define('System.view.base.CancelSaveToolbar', {
    extend: 'Ext.toolbar.Toolbar',
    xtype: 'cancel-save-toolbar',

    requires: [
        'System.util.Glyphs'
    ],

    dock: 'bottom',
    ui: 'footer',
    layout: {
        pack: 'end',
        type: 'hbox'
    },
    items: [
        {
            xtype: 'button',
            text: 'Save',
            glyph: System.util.Glyphs.getGlyph('save'),
            listeners: {
                click: 'onSave'
            }
        },
        {
            xtype: 'button',
            text: 'Cancel',
            glyph: System.util.Glyphs.getGlyph('cancel'),
            listeners: {
                click: 'onCancel'
            }
        }
    ]
});