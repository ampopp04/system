/**
 * The <class>System.util.application.Util</class> defines
 *  utility methods for working with the user.
 *
 * @author Andrew
 */
Ext.define('System.util.application.Util', {

    requires: [
        'Ext.window.Toast'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        required: '<span style="color:red;font-weight:bold" data-qtip="Required"> *</span>',

        /**
         * Decode a json response
         *
         * @param text
         * @returns {*|{expression, map}}
         */
        decodeJSON: function (text) {
            var result = Ext.JSON.decode(text, true);

            if (!result) {
                result = {};
                result.success = false;
                result.msg = text;
            }

            return result;
        },

        /**
         * Show a toast
         *
         * @param text
         */
        showToast: function (text) {
            if (!Ext.ComponentQuery.query('#systemToast').length) {
                Ext.toast({
                    itemId: 'systemToast',
                    html: text,
                    closable: false,
                    align: 't'
                });
            }
        },

        doShowMessage: function (title, text, icon, buttons, fn, scope) {
            Ext.MessageBox.show({
                title: title,
                message: text,
                buttons: buttons || Ext.MessageBox.OK,

                //multiline: true,

                fn: function () {
                    if (fn) {
                        Ext.callback(fn, scope, arguments);
                    }
                },
                scope: scope,

                width: null,
                height: null,

                maxHeight: '500',
                maxWidth: '200',

                onRender: function (ct, pos) {
                    //Call superclass
                    this.callParent(arguments);
                    if (this.getHeight() > this.maxHeight) {
                        this.setHeight(this.maxHeight);
                    }
                    if (this.getWidth() > this.maxWidth) {
                        this.setWidth(this.maxWidth);
                    }
                    this.center();
                }

            });

        },

        showInfoMessage: function (text) {
            System.util.application.Util.doShowMessage("Info", text);
        },

        showErrorMessage: function (text) {
            System.util.application.Util.doShowMessage("Error", text, Ext.MessageBox.ERROR);
        },

        showConfirmationMessage: function (text, fn, scope) {
            System.util.application.Util.doShowMessage("Confirm", text, Ext.MessageBox.QUESTION, Ext.MessageBox.OKCANCEL, fn, scope);
        }
        
    }
});