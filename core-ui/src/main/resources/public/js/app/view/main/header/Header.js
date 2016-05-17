Ext.define('System.view.main.header.Header', {
    extend: 'Ext.toolbar.Toolbar',
    xtype: 'appheader',

    requires: [
        'System.view.main.header.HeaderController'
    ],

    controller: 'header',

    ui: 'footer',

    items: [
        {
            xtype: 'label',
            cls: 'app-header-logo',
            html: '<img src="../../../../js/lib/resources/images/app/icon_2.png">'
        },
        {
            xtype: 'component',
            cls: 'app-header-title',
            html: 'SYS'
        }, '->',
        {
            text: "Tools",
            iconCls: 'x-fa fa-cog',
            border: 1,
            menu: {
                items: [{
                    text: 'Administration',
                    iconCls: 'x-fa fa-wrench',
                    menu: {
                        items: [
                            {
                                text: 'Tasks',
                                iconCls: 'x-fa fa-tasks'
                            },
                            {
                                text: 'Beans',
                                iconCls: 'x-fa fa-leaf'
                            },
                            '-',
                            {
                                text: 'Schema',
                                iconCls: 'x-fa fa-database'
                            },
                            {
                                text: 'Security',
                                iconCls: 'x-fa fa-lock'
                            },
                            '-',
                            {
                                text: 'Logging',
                                iconCls: 'x-fa fa-file'
                            },
                            {
                                text: 'Statistics',
                                iconCls: 'x-fa fa-line-chart '
                            }
                        ]
                    }
                }
                ]
            }
        }, '-', {
            itemId: 'logout',
            border: 1,
            iconCls: 'x-fa fa-sign-out',
            text: 'Logout',
            reference: 'logout',
            listeners: {
                click: 'onLogout'
            }
        }
    ]
});