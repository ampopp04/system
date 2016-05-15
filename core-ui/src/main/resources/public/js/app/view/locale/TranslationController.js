Ext.define('System.view.locale.TranslationController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.translation',

    init: function() {
        var lang = localStorage ? (localStorage.getItem('user-lang') || 'en') : 'en',
            button = this.getView();

        button.setIconCls(lang);

        if (lang == 'en'){
            button.setText('English');
        } else if (lang == 'es'){
            button.setText('Español');
        } else {
            button.setText('Português');
        }
    },

    onMenuItemClick: function(item, e, options){
        var button = this.getView();

        if (button.iconCls !== item.iconCls)
        {
            button.setIconCls(item.iconCls);
            button.setText(item.text);

            localStorage.setItem("user-lang", item.iconCls);

            window.location.reload();
        }
    }
});