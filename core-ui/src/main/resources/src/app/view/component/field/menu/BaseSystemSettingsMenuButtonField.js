Ext.define('System.view.component.field.menu.BaseSystemSettingsMenuButtonField', {
    extend: 'Ext.button.Button',

    requires: [],

    xtype: 'base-system-settings-menu-button-field',

    iconCls: 'fa fa-wrench',

    arrowVisible: false,
    border: false,
    showEmptyMenu: true,

    listeners: {
        click: function (button, e, eOpts) {
            if (button.menuInitializedNotComplete == undefined) {
                button.setMenu(Ext.create("Ext.menu.Menu", {items: button.getBaseToolMenuItems(button)}));
                button.getMenu().showBy(button);
                button.menuInitializedNotComplete = false;
            }
        }
    },

    getBaseToolMenuItems: function (me) {
        var gridPanel = me.getGridPanel(me);

        if (me.disableToolsMenu) {
            return [];
        }

        var menu = [];

        menu.push(me.getClearFilterMenuItem(gridPanel));
        menu.push('-');
        menu.push(me.getExportMenuItem(gridPanel));
        menu.push(me.getImportMenuItem(gridPanel, me));
        menu.push('-');
        menu.push(me.getReportsMenu(me.getDocumentsMenuItem(gridPanel), gridPanel, me));

        return menu;
    },

    getGridPanel: function (me) {
        return me.up('base-system-grid-panel');
    },

    getClearFilterMenuItem: function (me) {
        return {
            text: 'Clear All Filters',
            iconCls: 'fa fa-times',

            scope: me,
            handler: function () {
                me.store.clearFilter();
                me.down('system-tagfield').setValue([]);
            }
        };
    },

    getExportMenuItem: function (gridPanel) {
        return {
            text: 'Export',
            iconCls: 'fa fa-cloud-download',

            menu: {
                items: [
                    {
                        text: 'All Columns',
                        iconCls: 'fa fa-list',

                        handler: function () {
                            System.util.component.GridExportUtils.exportGrid(gridPanel, 'tsv', true)
                        }
                    },
                    {
                        text: 'Visible Columns',
                        iconCls: 'fa fa-eye',

                        handler: function () {
                            System.util.component.GridExportUtils.exportGrid(gridPanel, 'tsv', false);
                        }
                    }
                ]
            }
        };
    },

    getImportMenuItem: function (gridPanel, button) {
        return {
            text: 'Import',
            iconCls: 'fa fa-cloud-upload',

            menu: {
                items: [
                    button.createFileUploadField('Save', 'Save', 'fa fa-floppy-o', gridPanel, 'Updates existing matching entities or inserts a new entity if no match is found. Entities are matched by ID or name if present. Save combines the functionality of Update and Insert.'),
                    '-',
                    button.createFileUploadField('Update', 'Update', 'fa fa-refresh', gridPanel, 'Only updates existing entities that match on either the provided ID or name field.'),
                    button.createFileUploadField('Insert', 'Insert', 'fa fa-sign-in', gridPanel, 'Only inserts new entities and will not update existing entities that match on ID or name.'),
                    '-',
                    button.createFileUploadField('Delete then Insert', 'Insert or Delete', 'fa fa-exchange', gridPanel, 'Warning: Deletes all entities of this type and then inserts all entities in the provided file. If an entity of this type is referenced by another in the system it will not be deleted and an error will be thrown.'),
                    '-',
                    button.createFileUploadField('Advanced Import', undefined, 'fa fa-cloud-upload', gridPanel, 'There are other import types to select from. This allows you to select other import types.')
                ]
            }
        };
    },

    getDocumentsMenuItem: function (gridPanel) {
        var modelName = gridPanel.store.model.entityName;
        var existingExportMenu = Ext.ComponentQuery.query('#' + modelName + '-toolbar-export-menu');

        var exportMenu = (existingExportMenu && existingExportMenu.length == 1) ? existingExportMenu[0] : {
            text: 'Documents',
            id: modelName + '-toolbar-export-menu',
            iconCls: 'fa fa-file-text-o',
            parentGridPanel: gridPanel,
            listeners: {

                focus: function (menu, event, eOpts) {
                    //Only allow updating this menu on focus every 5 seconds
                    if (menu.focusStartTime == undefined || (Date.now() - menu.focusStartTime) > 5000) {
                        var button = menu.up('base-system-settings-menu-button-field');
                        button.populateDocumentReportMenuItems(menu.parentGridPanel, menu);
                        menu.focusStartTime = Date.now();
                    }

                }

            },
            menu: {
                items: []
            }
        };

        return exportMenu;
    },

    populateDocumentReportMenuItems: function (gridPanel, exportMenu) {
        System.util.component.ToolbarUtils.populateExportReportMenuItems(exportMenu, gridPanel.store.model.entityName);
    },

    getReportsMenu: function (exportMenu, gridPanel, button) {
        return {
            text: 'Reports',
            iconCls: 'fa fa-files-o',

            menu: {
                items: [
                    exportMenu,
                    '-',
                    button.createExportTemplateUploadMenuItem('Upload Template', 'fa fa-file-code-o', gridPanel, 'Upload a report template that allows generation of reports against this template definition.')
                ]
            }
        };
    }
    ,

    createExportTemplateUploadMenuItem: function (text, iconCls, scope, tooltip) {
        return {
            text: text,
            iconCls: iconCls,
            tooltip: tooltip,
            scope: scope,
            handler: function () {

                //Preload stores if they don't exist for this entity
                System.util.component.GridColumnUtils.getStoreByModelName('SystemExportTaskTypes',
                    function (store) {
                        System.util.component.GridColumnUtils.getStoreByModelName('SystemExportFileTypes',
                            function (store) {

                                var uploadWindow = Ext.create('System.view.component.window.upload.SimpleUploadFormWindow', {
                                    uploadPanelType: 'export-template-upload-panel',
                                    iconCls: iconCls,
                                    uploadType: 'Template',
                                    title: text,
                                    parentScope: scope
                                });

                                uploadWindow.show();

                            });
                    });

            }
        };
    }
    ,

    createFileUploadField: function (text, uploadTypeValue, iconCls, scope, tooltip) {
        return {
            text: text,
            iconCls: iconCls,
            tooltip: tooltip,
            scope: scope,
            handler: function () {

                var uploadWindow = Ext.create('System.view.component.window.upload.SimpleUploadFormWindow', {
                    iconCls: iconCls,
                    uploadType: text,
                    parentScope: scope
                });

                uploadWindow.down('simple-upload-panel').getForm().setValues({
                    uploadType: uploadTypeValue
                });

                uploadWindow.show();

            }
        };
    }

})
;