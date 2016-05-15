Ext.define('System.model.menu.Accordion', {
    extend: 'Ext.data.Model',

    requires: [
        'System.model.menu.TreeNode'
    ],

    fields: [
        { name: 'id', type: 'int'},
        { name: 'text' },
        { name: 'iconCls' }
    ],

    hasMany: {
        model: 'System.model.menu.TreeNode',
        foreignKey: 'parent_id',
        name: 'items'
    }
});