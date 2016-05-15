Ext.define('System.view.base.TopToolBar', {
    extend: 'Ext.toolbar.Toolbar',
    xtype: 'top-tool-bar',

    requires: [
        'System.util.Glyphs',
        'System.view.base.CustomButton'
    ],

    dock: 'top',
    items: [
        {
            xtype: 'button',
            text: 'Add',
            glyph: System.util.Glyphs.getGlyph('add'),
            listeners: {
                click: 'onAdd'
            }
        },
        {
            xtype: 'tbseparator'
        },
        {
            xtype: 'custom-btn',
            text: 'Print',
            glyph: System.util.Glyphs.getGlyph('print'),
            listeners: {
                click: 'onPrint'
            }
        },
        {
            xtype: 'custom-btn',
            text: 'Export to PDF',
            glyph: System.util.Glyphs.getGlyph('pdf'),
            listeners: {
                click: 'onExportPDF'
            }
        },
        {
            xtype: 'custom-btn',
            text: 'Export to Excel',
            glyph: System.util.Glyphs.getGlyph('excel'),
            listeners: {
                click: 'onExportExcel'
            }
        }
    ]
});