/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * A GridPanel class with live search support.
 */
Ext.define('System.view.component.panel.grid.LiveSearchGridPanel', {
    extend: 'Ext.grid.Panel',

    requires: [
        'System.util.system.search.SearchUtils'
    ],

    forceFit: true, multiColumnSort: true,
    columnLines: true, lines: true, rowLines: true,

    liveSearchGridEnabled: true,
    searchDepth: 1,
    extraSearchParams: undefined,

    /**
     * Defines the default search filter to apply to this store and filter box
     */
    defaultSearchFilter: undefined,
    /**
     * Forced default search filter makes the default search filter required and
     * unable to be removed/deleted
     */
    defaultSearchFilterForced: false,


    initComponent: function () {
        var me = this;

        if (me.liveSearchGridEnabled) {
            System.util.system.search.SearchUtils.addSearchFieldToTopToolbarDock(me, me.defaultSearchFilter, me.defaultSearchFilterForced);
        }

        me.callParent(arguments);
    }

});
