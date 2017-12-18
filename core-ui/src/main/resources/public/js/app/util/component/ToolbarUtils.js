/**
 * The <class>System.util.component.ToolbarUtils</class> defines
 *  utilty methods for working with toolbars.
 *
 * @author Andrew
 */
Ext.define('System.util.component.ToolbarUtils', {

    requires: [
        'System.util.data.RecordUtils',
        'System.util.system.detail.DetailWindowFormUtils',
        'System.util.component.GridExportUtils',
        'System.util.component.GridColumnUtils',
        'Ext.button.Button'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Returns a default paging toolbar implementation.
         *
         * @param storeName
         * @returns {{xtype: string, padding: number, pageSize: number, store: *, displayInfo: boolean}}
         */
        pagingtoolbar: function (storeName) {
            return {
                store: storeName,
                xtype: 'pagingtoolbar',
                dock: 'bottom',
                padding: 2,
                displayInfo: true
            };
        },

        /**
         * Returns a default add/delete button toolbar
         *
         * @param scope
         * @returns {{dock: string, xtype: string, padding: number, items: *[]}}
         */
        defaultAddAndDeleteToolbar: function (scope) {
            return {
                dock: 'left',
                xtype: 'toolbar',
                padding: 0,
                items: [
                    System.util.component.ToolbarUtils.addButton(scope),
                    System.util.component.ToolbarUtils.deleteButton(scope),
                    '-',
                    System.util.component.ToolbarUtils.addToolsMenu(scope)
                ]
            };
        },

        addToolsMenu: function (me) {
            var gridPanel = me;

            if (me.disableToolsMenu) {
                return;
            }

            var menu = [];

            menu.push({
                text: 'Clear All Filters',
                iconCls: 'fa fa-times',

                scope: me,
                handler: function () {
                    me.store.clearFilter();
                    me.down('system-tagfield').setValue([]);
                }
            });
            menu.push('-');

            menu.push({
                text: 'Export to TSV',
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
            });

            menu.push({
                text: 'Import from File',
                iconCls: 'fa fa-cloud-upload',

                menu: {
                    items: [
                        System.util.component.ToolbarUtils.createFileUploadField('Save', 'Save', 'fa fa-floppy-o', gridPanel, 'Updates existing matching entities or inserts a new entity if no match is found. Entities are matched by ID or name if present. Save combines the functionality of Update and Insert.'),
                        '-',
                        System.util.component.ToolbarUtils.createFileUploadField('Update', 'Update', 'fa fa-refresh', gridPanel, 'Only updates existing entities that match on either the provided ID or name field.'),
                        System.util.component.ToolbarUtils.createFileUploadField('Insert', 'Insert', 'fa fa-sign-in', gridPanel, 'Only inserts new entities and will not update existing entities that match on ID or name.'),
                        '-',
                        System.util.component.ToolbarUtils.createFileUploadField('Delete then Insert', 'Insert or Delete', 'fa fa-exchange', gridPanel, 'Warning: Deletes all entities of this type and then inserts all entities in the provided file. If an entity of this type is referenced by another in the system it will not be deleted and an error will be thrown.'),
                        '-',
                        System.util.component.ToolbarUtils.createFileUploadField('Advanced Import', undefined, 'fa fa-cloud-upload', gridPanel, 'There are other import types to select from. This allows you to select other import types.')
                    ]
                }
            });

            menu.push('-');

            var modelName = gridPanel.store.model.entityName;

            var existingExportMenu = Ext.ComponentQuery.query('#' + modelName + '-toolbar-export-menu');

            var exportMenu = (existingExportMenu && existingExportMenu.length == 1) ? existingExportMenu[0] : {
                text: 'Export',
                id: modelName + '-toolbar-export-menu',
                iconCls: 'fa fa-file-text-o',
                menu: {
                    items: []
                }
            };

            System.util.component.ToolbarUtils.populateExportReportMenuItems(gridPanel.down('menuitem#' + modelName + '-toolbar-export-menu'), gridPanel.store.model.entityName);

            menu.push({
                text: 'Reports',
                iconCls: 'fa fa-files-o',

                menu: {
                    items: [
                        exportMenu,
                        '-',
                        System.util.component.ToolbarUtils.createExportTemplateUploadMenuItem('Upload Template', 'fa fa-file-code-o', gridPanel, 'Upload a report template that allows generation of reports against this template definition.')
                    ]
                }
            });

            return {
                xtype: 'button',
                iconCls: 'fa fa-wrench',
                arrowVisible: false,
                border: false,
                menu: menu
            };

        },

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
        },

        getSystemExportTaskAssignmentsByTableName: function (tableName, callbackFunction, scope) {

            var storeFunction = function (store) {

                var parameterMap = {};
                parameterMap['schemaTableName'] = tableName;

                System.util.data.StoreUtils.searchStoreByParameterMapAndUrlPath(store, parameterMap, "findBySchemaTableName", callbackFunction, 'export-system-export-task-assignment-all', scope);

            };

            System.util.component.GridColumnUtils.getStoreByModelName('SystemExportTaskAssignments', storeFunction);

        },

        executeSystemExportTask: function (store, fkFieldId, systemExportTaskAssignment, callbackFunction, scope) {
            var parameterMap = {};
            parameterMap['fkFieldId'] = fkFieldId;
            parameterMap['systemExportTaskAssignment'] = systemExportTaskAssignment.id;

            System.util.data.StoreUtils.queryStoreByPropertyNameValue(store, parameterMap, 'taskExecutor', callbackFunction, "system-export-task-history-all", scope);
        },

        getObjectPropertyByPath: function (object, propertyName) {
            if (Ext.isEmpty(propertyName)) {
                return object;
            }

            var parts = propertyName.split("."),
                length = parts.length,
                i,
                property = object || this;

            for (i = 0; i < length; i++) {
                property = property[parts[i]];
            }

            return property ? property : object;
        },

        populateExportReportMenuItems: function (exportMenu, modelName) {

            if (exportMenu) {
                if (exportMenu.getMenu) {
                    exportMenu = exportMenu.getMenu();
                }
                exportMenu.removeAll()
            } else {
                exportMenu = modelName;
            }

            var tableName = System.util.data.ModelUtils.modelNameToSchemaTableName(modelName);

            System.util.component.ToolbarUtils.getSystemExportTaskAssignmentsByTableName(tableName,
                function (records, operation, success, scope) {
                    if (success) {
                        //We have a list of SystemExportTaskAssignments

                        //1) Group by systemExportTask.systemExportTaskType.name
                        var taskAssignmentsByType = records.reduce(function (r, a) {
                            var taskTypeName = System.util.component.ToolbarUtils.getObjectPropertyByPath(a, '_systemExportTask._systemExportTaskType.data.name');
                            r[taskTypeName] = r[taskTypeName] || [];
                            r[taskTypeName].push(a);
                            return r;
                        }, Object.create([]));

                        //2) Iterate group names, each is a menu within the exportMenu
                        Ext.Object.each(taskAssignmentsByType, function (taskType, value) {
                            var taskAssignments = value;

                            var taskTypeMenuItems = [];

                            //1) Add items (Reports) to this new Task Type menu
                            taskAssignments.forEach(function (taskAssignment) {
                                this.push({
                                    text: taskAssignment.data.name,
                                    taskAssignment: taskAssignment,
                                    iconCls: 'fa fa-file-text-o',
                                    handler: function () {

                                        var record = this.up('menu[reportRecord]');

                                        if (record == undefined) {
                                            var panel = this.up('base-system-grid-panel');

                                            if (panel == undefined) {
                                                //On a detail window
                                                panel = this.up('base-detail-form-panel');
                                            } else {
                                                //On a gridPanel
                                                var selectedRecords = panel.getSelection();
                                                if (selectedRecords == undefined || selectedRecords.length != 1) {
                                                    System.util.application.ErrorUtils.showErrorMsg('Please select a single grid row before generating export');
                                                    return;
                                                }

                                                record = selectedRecords[0];
                                            }
                                        } else {
                                            record = record.reportRecord;
                                        }

                                        var systemExportResultCallback = function (records, operation, success, scope) {
                                            if (success) {
                                                var record = records[0];

                                                var binaryFileContent = record.data.systemExportGeneratorContent.exportContent;
                                                var fileContentType = record.data.systemExportGeneratorContent.systemExportFileType.contentType;
                                                var exportName = record.data.systemExportGeneratorContent.name.split('.');

                                                System.util.component.GridExportUtils.downloadEncodedRowsAsFile(Ext.util.Base64._utf8_decode(binaryFileContent), exportName[0], exportName[1], fileContentType);
                                            } else {
                                                System.util.application.ErrorUtils.dataRequestErrorCheck(success, operation.error.response.responseText, operation.error.response.status);
                                            }
                                        };

                                        System.util.component.ToolbarUtils.executeSystemExportTask(record.store, record.id, this.taskAssignment, systemExportResultCallback, this);

                                    }
                                });
                            }, taskTypeMenuItems);

                            //2) Add Menu for this Task Type
                            var taskTypeMenu = {
                                text: taskType,
                                iconCls: 'fa fa-files-o',

                                menu: {
                                    items: taskTypeMenuItems
                                }
                            };

                            if (this && this.add) {
                                this.add(taskTypeMenu);
                            } else {
                                this.down('menuitem#' + this + '-toolbar-export-menu').getMenu().add(taskTypeMenu);
                            }

                        }, scope);

                    } else {
                        System.util.application.ErrorUtils.dataRequestErrorCheck(success, operation.error.response.responseText, operation.error.response.status);
                    }
                }, exportMenu);

        },

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

        /**
         * Defines an add button to add new entities
         *
         * @param scope
         * @returns {{xtype: string, border: boolean, iconCls: string, scope: *, handler: handler}}
         */
        addButton: function (scope) {
            //Create a viewModel containing a data and rec where the rec has all the fields defined properly...
            // Get the fields from this model from the scope some how!!! then throw that in a rec
            return {
                xtype: 'button',
                border: false,
                iconCls: 'x-fa fa-plus',
                scope: scope,
                handler: function () {
                    var me = this;
                    System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindow(me.config.modelName, scope, {}, scope.newEntityDetailWindowDefaultValueFilters);
                }
            };
        },

        /**
         * Delete a row within a grid
         *
         * @param scope
         * @returns {{xtype: string, border: boolean, iconCls: string, scope: *, handler: handler}}
         */
        deleteButton: function (scope) {
            return {
                xtype: 'button',
                border: false,
                iconCls: 'x-fa fa-minus',
                scope: scope,
                handler: function () {
                    var me = this;
                    var selection = me.getView().getSelectionModel().getSelection()[0];

                    if (selection) {

                        System.util.application.Util.showConfirmationMessage("Do you want to delete this item?",
                            function (btnText) {
                                var me = this;
                                if (btnText === "ok") {
                                    var selection = me.getView().getSelectionModel().getSelection()[0];
                                    me.store.remove(selection);
                                    System.util.application.Util.showToast("Item Deleted");
                                }
                            }, this);

                    } else {
                        //Notify user, must select row to delete
                        System.util.application.Util.showInfoMessage("Select a row before you delete an item.");
                    }
                }
            };
        }
    }
})
;