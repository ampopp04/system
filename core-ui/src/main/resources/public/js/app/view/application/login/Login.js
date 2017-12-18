/**
 * The <class>System.view.application.login.Login</class> defines
 * the login view for the application
 *
 * @author Andrew
 */
Ext.define('System.view.application.login.Login', {
    extend: 'Ext.window.Window',

    requires: [
        'System.view.application.login.LoginController'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'login-dialog', controller: 'login',
    title: 'Login', iconCls: 'fa fa-key fa-lg',
    autoShow: true, closable: false,
    height: 150, width: 320,
    layout: {type: 'fit'},

    ///////////////////////////////////////////////////////////////////////
    ////////                                                               Items                                                         //////////
    //////////////////////////////////////////////////////////////////////

    items: [
        {
            xtype: 'form',
            reference: 'form',
            listeners: {
                afterlayout: function (form, layout, eOpts) {
                    if (layout.layoutCount == 1) {
                        form.down('textfield[name="username"]').focus();
                    }
                }
            },
            bodyPadding: 15,
            bodyStyle: {
                background: '#ffffff'
            },
            defaults: {
                xtype: 'textfield',
                anchor: '100%',
                labelWidth: 70,
                allowBlank: false,
                minLength: 1,
                msgTarget: 'side',
                enableKeyEvents: true,
                fieldStyle: 'background-color: #ffffff; background-image: none;',
                listeners: {
                    specialKey: 'onTextFieldSpecialKey'
                }
            },
            items: [
                {
                    name: 'username',
                    maxLength: 25,
                    fieldStyle: 'font-family: FontAwesome',
                    emptyText: '\uF007 Username'
                },
                {
                    inputType: 'password',
                    name: 'password',
                    id: 'password',
                    maxLength: 15,
                    fieldStyle: 'font-family: FontAwesome',
                    emptyText: '\uF023 Password',
                    listeners: {
                        keypress: 'onTextFieldKeyPress',
                        specialKey: 'onTextFieldSpecialKey'
                    }
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    style: 'background-color: #ffffff; background-image: none; border-width: 0px !important; border-bottom-width: 0px !important; padding: 6px 0px 5px 8px;',
                    items: [
                        {
                            xtype: 'tbfill'
                        },
                        {
                            xtype: 'button',
                            itemId: 'submit',
                            formBind: true,
                            style: {
                                'background-color': '#ffffff',
                                'background-image': 'none'
                            },
                            iconCls: 'fa fa-sign-in fa-lg',
                            text: 'Login',
                            listeners: {
                                click: 'onButtonClickSubmit'
                            }
                        }
                    ]
                }
            ]
        }
    ]
});