/**
 * The <class>System.util.export.report.ExportUtils</class> defines
 *  utility methods for creating and managing UI elements
 *  related to showing or creating exports and reports.
 *
 * @author Andrew
 */
Ext.define('System.util.export.report.ExportUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    statics: {
        /**
         *Create and show a pop-up export menu item at a given event location for a specific model
         */
        populateAndShowExportMenu: function (gridEvent, gridRecord, modelName, menuTitle, menuIconCls) {
            var modelExportMenu;
            System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {
                modelExportMenu = new Ext.menu.Menu({items: []});
                System.util.component.ToolbarUtils.populateExportReportMenuItems(modelExportMenu, modelName);

                //Store is created or exists, menu is created and populated, so lets show it
                System.util.export.report.ExportUtils.showExportMenu(gridEvent, gridRecord, store, modelName, {
                    text: menuTitle,
                    iconCls: menuIconCls,
                    menu: modelExportMenu
                });

            });
        },

        /**
         * Generates an Export Menu to be shown at a specific event location
         */
        showExportMenu: function (gridEvent, gridRecord, store, modelName, exportMenu) {
            var reportMenu = new Ext.menu.Menu({
                reportRecord: {
                    store: store,
                    id: gridRecord.data.entityId
                },
                items: [],

                listeners: {
                    hide: function () {
                        setTimeout(function () {
                            reportMenu.destroy();
                        }, 2000);
                    }
                }
            });

            reportMenu.add(exportMenu);
            reportMenu.showAt(gridEvent.getXY());
        }

    }

});