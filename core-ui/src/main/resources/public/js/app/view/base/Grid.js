Ext.define('System.view.base.Grid', {
    extend: 'Ext.grid.Panel',

    requires: [
        'System.util.Glyphs'
    ],

    columnLines: true,
    viewConfig: {
        stripeRows: true
    },

    initComponent: function() {
        var me = this;

        me.columns = Ext.Array.merge(
            me.columns,
            [{
                xtype    : 'datecolumn',
                text     : 'Last Update',
                width    : 150,
                dataIndex: 'last_update',
                format: 'Y-m-j H:i:s',
                filter: true
            },{
                xtype: 'widgetcolumn',
                width: 50,
                sortable: false,
                menuDisabled: true,
                widget: {
                    xtype: 'button',
                    glyph: System.util.Glyphs.getGlyph('edit'),
                    tooltip: 'Edit',
                    handler: 'onEdit'
                }
            },{
                xtype: 'widgetcolumn',
                width: 50,
                sortable: false,
                menuDisabled: true,
                widget: {
                    xtype: 'button',
                    glyph: System.util.Glyphs.getGlyph('destroy'),
                    tooltip: 'Delete',
                    handler: 'onDelete'
                }
            }]
        );

        me.callParent(arguments);
    }
});