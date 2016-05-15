Ext.define('System.model.Base', {
    extend: 'Ext.data.Model',

    requires: [
        'System.util.Util',
        'System.ux.data.writer.AssociatedWriter'
    ],

    schema: {
        namespace: 'System.model',
        urlPrefix: 'php',
        proxy: {
            type: 'ajax',
            api :{
                read : '{prefix}/{entityName:lowercase}/list.php',
                create: '{prefix}/{entityName:lowercase}/create.php',
                update: '{prefix}/{entityName:lowercase}/update.php',
                destroy: '{prefix}/{entityName:lowercase}/destroy.php'
            },
            reader: {
                type: 'json',
                rootProperty: 'data'
            },
            writer: {
                type: 'associatedjson',
                writeAllFields: true,
                encode: true,
                rootProperty: 'data',
                allowSingle: false
            },
            listeners: {
                exception: function(proxy, response, operation){
                    System.util.Util.showErrorMsg(response.responseText);
                }
            }
        }
    }
});