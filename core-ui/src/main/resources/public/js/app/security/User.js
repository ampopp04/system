Ext.define('System.view.security.User', {
    extend: 'Ext.panel.Panel',
    xtype: 'user',

    requires: [
        'System.view.security.UsersGrid',
        'System.view.security.UserModel',
        'System.view.security.UserController',
        'System.view.security.UserForm',
        'System.util.Glyphs'
    ],

    controller: 'user',
    viewModel: {
        type: 'user'
    },
    session: true,

    frame: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'users-grid',
            flex: 1
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'button',
                    text: 'Add',
                    glyph: System.util.Glyphs.getGlyph('add'),
                    listeners: {
                        click: 'onAdd'
                    }
                },
                {
                    xtype: 'button',
                    text: 'Edit',
                    glyph: System.util.Glyphs.getGlyph('edit'),
                    listeners: {
                        click: 'onEdit'
                    },
                    bind: {
                        disabled: '{!usersGrid.selection}'
                    }
                },
                {
                    xtype: 'button',
                    text: 'Delete',
                    glyph: System.util.Glyphs.getGlyph('destroy'),
                    listeners: {
                        click: 'onDelete'
                    },
                    bind: {
                        disabled: '{!usersGrid.selection}'
                    }
                }
            ]
        }
    ]
});