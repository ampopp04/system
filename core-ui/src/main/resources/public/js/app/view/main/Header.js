Ext.define('System.view.main.Header', {
    extend: 'Ext.toolbar.Toolbar',
    xtype: 'appheader',

    requires: [
    ],

    ui: 'footer',

    items: [ '->',
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
                                                       text: 'System Schema',
                                                       iconCls: 'x-fa fa-database'
                                                   },
                                                   {
                                                       text: 'System Bean',
                                                       iconCls: 'x-fa fa-leaf'
                                                   },
                                                   {
                                                       text: 'System Security',
                                                       iconCls: 'x-fa fa-lock'
                                                   },
                                                    {
                                                        text: 'System Logging',
                                                        iconCls: 'x-fa fa-file'
                                                    }
                                               ]
                                           }
                                      }
                                   ]
                               }
                 },'-',{
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