Ext.define('System.view.login.Login', {
    extend: 'Ext.window.Window',

    xtype: 'login-dialog',

    requires: [
        'System.view.login.LoginController',
        'System.view.locale.Translation'
    ],

    controller: 'login',

    autoShow: true,
    height: 200,
    width: 360,
    layout: {
        type: 'fit'
    },
    iconCls: 'fa fa-key fa-lg',
    title: translations.login,
    closeAction: 'hide',
    closable: false,

    items: [
        {
            xtype: 'form',
            reference: 'form',
            bodyPadding: 15,
            defaults: {
                xtype: 'textfield',
                anchor: '100%',
                labelWidth: 70,
                allowBlank: false,
                vtype: 'alphanum',
                minLength: 3,
                msgTarget: 'side',
                enableKeyEvents: true,
                listeners: {
                    specialKey: 'onTextFieldSpecialKey'
                }
            },
            items: [
                {
                    name: 'username',
                    fieldLabel: translations.user,
                    maxLength: 25,
                    value: 'apopp'
                },
                {
                    inputType: 'password',
                    name: 'password',
                    fieldLabel: translations.password,
                    id: 'password',
                    maxLength: 15,
                    value: 'password',
                    vtype: 'alphanum',
                    listeners: {
                        keypress: 'onTextFieldKeyPress'
                    }
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    items: [
                        {
                            xtype: 'translation'
                        },
                        {
                            xtype: 'tbfill'
                        },
                        {
                            xtype: 'button',
                            iconCls: 'fa fa-times fa-lg',
                            text: translations.cancel,
                            listeners: {
                                click: 'onButtonClickCancel'
                            }
                        },
                        {
                            xtype: 'button',
                            itemId: 'submit',
                            formBind: true,
                            iconCls: 'fa fa-sign-in fa-lg',
                            text: translations.submit,
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