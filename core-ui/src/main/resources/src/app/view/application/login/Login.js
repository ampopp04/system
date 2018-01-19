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
            bodyPadding: '10px 10px 8px 10px',
            bodyStyle: {
                background: '#ffffff'
            },
            defaults: {
                xtype: 'textfield',
                anchor: '100%',
                labelWidth: 95,
                allowBlank: false,
                minLength: 1,
                msgTarget: 'side',
                enableKeyEvents: true,
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

                    style: 'border-width: 0px !important;',

                    padding: '10px 20px 0px 20px',
                    layout: 'column',

                    items: [
                        {
                            xtype: 'checkbox',

                            fieldLabel: 'Remember Me',
                            name: 'remember-me',

                            style: 'margin-top:3px;',
                            columnWidth: 0.7,

                            checked: true,
                            isFormField: true,
                            formBind: true
                        },
                        {
                            xtype: 'button',
                            itemId: 'submit',
                            formBind: true,

                            columnWidth: 0.3,
                            style: 'border-radius:3px 3px 3px 3px;box-shadow:0 2px 20px 1px rgba(0, 58, 93, 0.33), inset 0 0 1px 0 #FFF;',

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