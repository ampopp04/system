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
        'Ext.button.Button',
        'System.view.component.field.menu.BaseSystemSettingsMenuButtonField'
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
                    {
                        xtype: 'base-system-settings-menu-button-field'
                    }
                ]
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
                                                System.util.application.ErrorUtils.dataRequestErrorCheck(success, operation.error.response.responseText, operation.error.response.status, operation);
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
                        System.util.application.ErrorUtils.dataRequestErrorCheck(success, operation.error.response.responseText, operation.error.response.status, operation);
                    }
                }, exportMenu);

        },

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
                                    me.store.sync();
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