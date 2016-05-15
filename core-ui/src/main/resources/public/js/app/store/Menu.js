Ext.define('System.store.Menu', {
    extend: 'Ext.data.Store',

    requires: [
        'System.util.Util'
    ],

    model: 'System.model.menu.Accordion',

    proxy: {
        type: 'ajax',
        url: '../systemMenuList',

        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        listeners: {
            exception: function(proxy, response, operation){
                System.util.Util.showErrorMsg(response.responseText);
            }
        }
    }
});