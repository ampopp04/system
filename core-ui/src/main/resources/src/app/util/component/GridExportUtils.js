/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.util.component.GridExportUtils</class> defines
 *  utility methods for exporting data from grids.
 *
 * @author Andrew
 */
Ext.define('System.util.component.GridExportUtils', {

    requires: [
        'System.util.application.ErrorUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {

        exportGrid: function (grid, fileType, isAllColumns) {

            //Set for reference after store reload callback
            grid.fileType = fileType;
            grid.isAllColumns = isAllColumns;

            //Reload our grid to have all possible rows up to a maximum page size (Normally we limit the page size to reduce server load)
            // after reload call doExportGrid callback
            System.util.component.GridExportUtils.reloadStoreWithLargePageLimit(grid, false, System.util.component.GridExportUtils.doExportGrid);

        },

        doExportGrid: function (gridpanel) {
            var grid = gridpanel ? gridpanel : this;

            var columns = System.util.component.GridExportUtils.getColumnsForGrid(grid, grid.isAllColumns);
            var rowDataForColumns = System.util.component.GridExportUtils.getRowDataByColumnsFromGrid(grid, columns);
            var columnHeaderNames = System.util.component.GridExportUtils.getColumnHeaderNames(columns);
            System.util.component.GridExportUtils.encodeRowValues(columnHeaderNames, rowDataForColumns);

            var fileName = System.util.component.GridExportUtils.getDownloadFileName(grid);

            var joinedRowData = System.util.component.GridExportUtils.getJoinedRowData(grid, rowDataForColumns);
            System.util.component.GridExportUtils.downloadEncodedRowsAsFile(joinedRowData, fileName, grid.fileType);
        },

        getJoinedRowData: function (grid, rowDataForColumns) {
            var fileType = grid.fileType;
            var separator = ',';

            if (fileType == 'tsv') {
                separator = '\t'
            }

            var joinedRowData = rowDataForColumns.map(function (row) {
                return row.join(separator);
            }).join('\r\n');

            return joinedRowData;
        },

        reloadStoreWithLargePageLimit: function (grid, isUseDefaultPageSize, exportCallback) {
            var store = grid.store;
            var maxPageSize = isUseDefaultPageSize ? store.pageSize : store.maxPageSize;
            var lastOptions = store.lastOptions;

            if (store.getCount() == store.getTotalCount() && store.getCount() <= store.pageSize) {

                //Means we aren't paging right now and all possible results are loaded in the store
                if (exportCallback == undefined) {
                    return;
                } else {
                    exportCallback.call(grid);
                }

            } else {

                if (isUseDefaultPageSize == false && store.getTotalCount() > maxPageSize) {
                    //User is attempting to download a file that has more rows than we allow as our max page count
                    //Notify them that this is an error and that they should further filter their results so the total
                    // row count is below the maxPageSize
                    System.util.application.ErrorUtils.showErrorMsg("Cannot download [" + store.getTotalCount() + "] rows, max total rows allowed is [" + maxPageSize + "], filter grid further and perform multiple exports.")
                }

                // make a copy of the last params so we don't affect future reload() calls
                var lastParams = lastOptions.params ? Ext.clone(lastOptions.params) : {};
                //Increase to max page size to get all the rows to download to the file
                lastParams.size = maxPageSize;

                store.reload({
                    params: lastParams,
                    scope: grid,
                    callback: function (records, operation, success) {
                        if (exportCallback) {
                            exportCallback.call(this);
                        }
                    }
                });
            }
        },

        /**
         * Get the file name based on the name of the store backing the grid
         */
        getDownloadFileName: function (grid) {
            return grid.store.model.entityName;
        },

        /**
         * Get either all the columns from the grid
         * or only the currently visible columns.
         */
        getColumnsForGrid: function (grid, isAllColumns) {
            return isAllColumns ? grid.getColumns() : grid.getVisibleColumns();
        },

        /**
         * Returns an array of the current grid rows filtered by columns
         */
        getRowDataByColumnsFromGrid: function (grid, columns) {
            var rowData = [];

            columns.forEach(function (column) {

                var dataRoot = 'data';
                var fieldName = column.dataIndex;
                var referenceDisplayFieldName = undefined; //Used in cases that our column references another nested entity

                if (column.reference) {
                    //This column has a store reference which means it is a foreign key relationship to another entity
                    dataRoot = '_' + column.dataIndex;
                    fieldName = 'data';
                    referenceDisplayFieldName = grid.getStore().getModel().getField(column.dataIndex).displayFieldName;
                }

                var columnGridData = grid.getStore().getData().getValues(fieldName, dataRoot);

                rowData = columnGridData.map(function (gridCellValue, i) {
                    if (rowData[i] == undefined) {
                        rowData[i] = [];
                    }

                    //Default to undefined as ""
                    var decodedGridCellValue = "";

                    if (gridCellValue) {
                        //We have a value at this grid cell, it is not empty/undefined

                        //If a referenceDisplayFieldName is defined that means that this column is not a simple text column
                        // but rather that it references another entity within the system (Like a database foreign key)

                        // If referenceDisplayFieldName exists then we can assume gridCellValue is an array containing referenceDisplayFieldName
                        // rather than being a simple text value
                        decodedGridCellValue = referenceDisplayFieldName ? gridCellValue[referenceDisplayFieldName] : gridCellValue;
                    }

                    return rowData[i].concat([decodedGridCellValue]);
                });

            });

            //Reset store/grid to show data using the default page size
            // Now that we have all of the data that we need for the file export
            System.util.component.GridExportUtils.reloadStoreWithLargePageLimit(grid, true);

            return rowData;
        },

        /**
         * Converts an array of columns to an array of column names
         */
        getColumnHeaderNames: function (columns) {
            var columnHeaderNames = [];
            columns.forEach(function (column) {
                columnHeaderNames.push(column.dataIndex);
            });
            return columnHeaderNames;
        },

        /**
         *Returns an array where each
         * element in the array is a comma
         * separated string of all the row field values
         * concatenated together.  The first row
         * of the array is the header (names)
         * concatenated together.
         */
        encodeRowValues: function (headers, rows) {
            return rows.unshift(headers);
        },

        /**
         * Provided encoded row values, in the form of an array of
         * comma separated values, this function will
         * download a file with this array converted into the
         * specified file type and file name.
         */
        downloadEncodedRowsAsFile: function (fileData, fileName, fileType, contentType) {
            var filename = fileName + "." + fileType;

            if (contentType == undefined) {
                contentType = 'text/' + fileType;
            }

            var s2ab = function (s) {
                var buf = new ArrayBuffer(s.length);
                var view = new Uint8Array(buf);
                for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
                return buf;
            };

            var blob;

            try {
                blob = new Blob([s2ab(atob(fileData))], {type: contentType});
            } catch (e) {
                // String is not base64 encoded, assume it is encoded already
                blob = new Blob([fileData], {type: contentType});
            }

            if (window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveBlob(blob, filename);
            }
            else {
                var elem = window.document.createElement('a');
                elem.href = window.URL.createObjectURL(blob);
                elem.download = filename;
                document.body.appendChild(elem);
                elem.click();
                document.body.removeChild(elem);
            }

        }

    }
});