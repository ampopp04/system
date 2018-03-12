/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

Ext.define('System.view.component.plugin.AutoSizeColumn', function () {

    return {
        extend: 'Ext.plugin.Abstract',

        alias: 'plugin.autosizecolumn',

        config: {
            /**
             * @cfg {Number} [maxAutoSizeWidth] The maximum auto-sized column width.
             */
            maxAutoSizeWidth: null
        },

        onViewRefresh: function (view) {
            this.autoSizeColumns();
        },

        /**
         * @inheritdoc
         */
        init: function (grid) {
            this.setCmp(grid);
            this.getCmp().getView().on('refresh', this.onViewRefresh, this);

            var tabPanel = this.getCmp().up('base-system-tab-panel');

            if (tabPanel) {
                this.getCmp().up('base-system-tab-panel').on('beforetabchange', this.onViewRefresh, this);
            }
        },

        /**
         * Sizes visible columns to fit the max content width.
         */
        autoSizeColumns: function () {
            var grid = this.getCmp(),
                view = grid.getView(),
                // HACK: not sure why Ext.state.Stateful#getStateId is private 'cause it seems to be valid to be public.
                id = grid.stateful && grid.getStateId(),
                maxAutoSizeWidth = this.getMaxAutoSizeWidth();

            if (view.width == undefined) {
                return;
            }

            grid.suspendLayouts();

            var leftPaddingDefault = 25;
            var defaultColumnPadding = 10;
            var useMaxVisibleHeaderWidth = true;

            var totalHeaderWidth = view.getWidth() - leftPaddingDefault;
            var visibleColumnCount = grid.getVisibleColumns().length;
            var maxVisibleHeaderWidth = totalHeaderWidth / visibleColumnCount;
            var calculatedColumnWidth = totalHeaderWidth;
            var calculatedColumnCount = visibleColumnCount;
            var subHeaderSkipEncountered = false;

            Ext.each(grid.getVisibleColumns(), function (column) {

                if (column.isSubHeader) {
                    subHeaderSkipEncountered = true;
                } else if (subHeaderSkipEncountered) {
                    //Done iterating over a grouped subheader collection of columns
                    //We want to treat them as one so we ignore the padding subtraction
                    //But now we subtract the padding once since we are done with all the subheading columns
                    calculatedColumnWidth = calculatedColumnWidth - defaultColumnPadding;
                    subHeaderSkipEncountered = false;
                }

                var maxContentWidth = view.getMaxContentWidth(column);

                if (column.maxWidth && column.maxWidth < maxContentWidth) {
                    maxContentWidth = column.getMaxWidth();
                }

                if (Ext.isBoolean(column.resizeable) && !column.resizeable) {
                    maxContentWidth = column.getWidth();
                }

                if (((calculatedColumnWidth / calculatedColumnCount) < maxContentWidth) || (Ext.isBoolean(column.resizeable) && !column.resizeable)) {
                    useMaxVisibleHeaderWidth = false;

                    //A column is computed to have a greater content width than would be applied
                    // using the evenly distributed width across the total size of the headers
                    //This means reduce how much space we have left by how much this column will auto size
                    //A decrease how many columns we are computing are standard column width by.
                    calculatedColumnWidth = calculatedColumnWidth - maxContentWidth - (subHeaderSkipEncountered ? 0 : defaultColumnPadding);
                    calculatedColumnCount = calculatedColumnCount - 1;
                }
            });

            //Handle edge case
            if (subHeaderSkipEncountered) {
                calculatedColumnWidth = calculatedColumnWidth - defaultColumnPadding;
                subHeaderSkipEncountered = false;
            }

            var calculatedPerColumnWidth = calculatedColumnWidth / calculatedColumnCount;

            Ext.each(grid.getVisibleColumns(), function (column) {
                var maxContentWidth = view.getMaxContentWidth(column);

                if (Ext.isBoolean(column.resizeable) && !column.resizeable) {
                    column.setWidth(column.getWidth());
                }
                else if (column.getMaxWidth() && column.getMaxWidth() < maxContentWidth) {
                    column.setWidth(column.getMaxWidth());
                }
                else if (maxAutoSizeWidth > 0 && maxContentWidth > maxAutoSizeWidth) {
                    column.setWidth(maxAutoSizeWidth);
                }
                else if (calculatedPerColumnWidth > maxContentWidth) {
                    column.setWidth(calculatedPerColumnWidth);
                }
                else {
                    column.autoSize();
                }
            });

            grid.resumeLayouts();
            grid.updateLayout();
        },

        /**
         * Returns true, if plugin is disabled, false otherwise.
         *
         * @return {Boolean}
         */
        isDisabled: function () {
            return this.disabled;
        }

    };
});