/**
 * The <class>System.view.component.panel.tree.BaseSystemTreeGridPanel</class> defines the
 * base tree grid for displaying data loaded from a tree data store.
 *
 * This is a data response that contains the entity along with an array of children objects that can be
 * shown in a single grid as expansion arrow rows.
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.tree.BaseSystemTreeGridPanel', {
    extend: 'Ext.tree.Panel',

    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.util.*', 'Ext.state.*', 'Ext.form.*',
        'Ext.grid.RowEditor',
        'Ext.selection.TreeModel',
        'System.util.component.ToolbarUtils',
        'System.util.component.GridColumnUtils',
        'System.view.component.plugin.AutoSizeColumn',
        'System.util.system.search.SearchUtils'
    ],

    viewType: 'baseSystemTreeview',

    height: 370,
    width: 600,

    useArrows: true,
    rootVisible: false,
    multiSelect: true,
    reserveScrollbar: true,

    searchDepth: 1,
    extraSearchParams: [],

    /**
     * PULLED FROM System.view.component.panel.grid.BaseSystemGridPanel START
     */

    columnLines: true, lines: true, rowLines: true,
    forceFit: true,

    plugins: ['gridfilters', 'autosizecolumn'],

    detailFormWindow: undefined,
    frame: true

});