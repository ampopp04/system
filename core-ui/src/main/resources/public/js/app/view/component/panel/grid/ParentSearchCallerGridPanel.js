/**
 * The <class>System.view.component.panel.grid.BaseSystemGridPanel</class> defines
 * the base system grid panel that will dynamically create a grid with columns for a given
 * model.
 *
 * A model contains all of the fields and information related to a specific entity. We infer
 * the columns and types for the grid from this model data.
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.grid.ParentSearchCallerGridPanel', {
    extend: 'System.view.component.panel.grid.BaseSystemGridPanel',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'parent-search-caller-grid-panel',

    parentSearchCaller: undefined,

    additionalGridColumns: {
        text: 'Select',
        autoSizeColumn: true,
        menuDisabled: true,
        xtype: 'actioncolumn',
        align: 'center',
        iconCls: 'x-fa fa-sign-in',
        ignoreExport: true,
        handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
            var parentCaller = grid.initialConfig.grid.initialConfig.parentSearchCaller;
            parentCaller.setValue.call(parentCaller, record.id);

            Ext.Function.defer(function () {
                this.up('detail-search-window').close();
            }, 60, grid);

        }

    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

});