/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

Ext.define('System.view.component.panel.tree.BaseSystemTreeGridPanelView', {
    extend: 'Ext.tree.View',
    alias: 'widget.baseSystemTreeview',


    /**
     * Override parent implementation because it is not taking into account that a position can
     * have the row index defined on itself rather than having to rely on it's record
     *
     * Changed
     * position.record || position.row
     * to
     * position.rowIdx || position.record || position.row
     */
    getCellByPosition: function (position, returnDom) {
        if (position) {
            var view = position.view || this, row = view.getRow(position.rowIdx || position.record || position.row),
                header, cell;
            header = view.getColumnByPosition(position);
            if (header && row) {
                cell = row.querySelector(view.getCellSelector(header));
                return returnDom ? cell : Ext.get(cell);
            }
        }
        return false;
    }


});
