/**
 * A specialized upload window for creating new templates with auto-creation
 * of associated system entities
 */
Ext.define('System.view.component.panel.upload.ExportTemplateUploadPanel', {
    extend: 'System.view.system.detail.form.panel.BaseDetailFormPanel',

    xtype: 'export-template-upload-panel',

    requires: [
        'System.util.application.ErrorUtils',
        'System.util.data.StoreUtils',
        'System.view.component.field.SystemFieldComboBox',
        'System.view.component.field.SystemFieldText',
        'System.view.component.field.SystemFileField'
    ],

    parentScope: undefined,

    buttons: [
        {
            text: 'Upload',
            handler: function () {
                var win = this.up('window');
                var form = win.down('form');

                if (form.isValid()) {

                    win.setLoading('Please wait while processing your request.');

                    var urlEntityName = form.parentScope.store.proxy.entityName;
                    var schemaTableName = form.getForm().findField('schemaTable').getRawValue();

                    if (!Ext.isEmpty(schemaTableName)) {
                        var entityTableName = System.util.StringUtils.uncapitalize(System.util.data.ModelUtils.schemaTableNameToModelName(schemaTableName));
                        urlEntityName = entityTableName;
                    }

                    form.submit({
                        url: '/api/' + urlEntityName + '/uploadTemplate',
                        method: 'Post',
                        timeout: 120000,
                        scope: form,
                        success: function (form, result, data) {
                        },
                        failure: function (form, response) {
                            this.up('window').setLoading(false);

                            if (response.response.status == 200) {
                                this.up('window').close();

                                System.util.application.Util.showToast('Upload Template Successful');

                                var gridPanel = this.parentScope;
                                var exportMenu = gridPanel.down('menuitem#' + gridPanel.store.model.entityName + '-toolbar-export-menu');
                                System.util.component.ToolbarUtils.populateExportReportMenuItems(exportMenu, gridPanel.store.model.entityName);

                            } else {
                                System.util.application.ErrorUtils.dataRequestErrorCheck(false, response.response.responseText, response.response.status);
                            }

                        }
                    });
                }

            }
        },
        {
            text: 'Cancel',
            handler: function () {
                this.up('window').close();
            }
        }
    ],

    dockedItems: [],

    constructor: function (config) {

        System.util.component.GridColumnUtils.fullyInitializeStoreByModelName("SystemExportFileTypes");
        System.util.component.GridColumnUtils.fullyInitializeStoreByModelName("SystemExportTaskTypes");

        this.configureColumns(config);

        this.callParent([config]);
    },

    configureColumns: function (config) {

        var hideTableField = true;
        var parent = config.parentScope;
        if (
            (parent.hideTemplateUploadTableField != undefined && parent.hideTemplateUploadTableField == false) ||
            (parent.up().hideTemplateUploadTableField != undefined && parent.up().hideTemplateUploadTableField == false) ||
            (parent.up().up().hideTemplateUploadTableField != undefined && parent.up().up().hideTemplateUploadTableField == false)
        ) {
            hideTableField = false;
        }

        var schemaTableField = {
            xtype: 'system-field-combo-box',

            valueField: 'id',
            displayField: 'name',

            fieldLabel: 'Report Entity',
            name: 'schemaTable',
            store: 'SchemaTablesStore',
            allowBlank: false,
            hidden: hideTableField
        };

        if (hideTableField == true) {
            var schemaTableName = System.util.data.ModelUtils.modelNameToSchemaTableName(config.parentScope.store.model.$className);
            System.util.system.SchemaUtils.retrieveSchemaTableByName(schemaTableName, function (schemaTableRecord, scope) {
                scope.value = schemaTableRecord.id;
            }, schemaTableField);
        }

        this.items = [
            schemaTableField,
            {
                xtype: 'system-file-field',
                name: 'templateFile',
                fieldLabel: 'Template File',
                allowBlank: false
            },
            {
                xtype: 'system-field-combo-box',
                valueField: 'id',
                displayField: 'name',

                fieldLabel: 'Department',
                name: 'systemExportTaskType',
                store: 'SystemExportTaskTypesStore',
                allowBlank: false
            },
            {
                xtype: 'system-field-combo-box',
                valueField: 'id',
                displayField: 'name',

                fieldLabel: 'Export As',
                name: 'systemExportFileType',
                store: 'SystemExportFileTypesStore',
                value: 3,
                allowBlank: false
            },
            {
                xtype: 'system-field-text',
                name: 'name',
                fieldLabel: 'Name',
                allowBlank: false
            },
            {
                xtype: 'system-field-text',
                name: 'description',
                fieldLabel: 'Description'
            }
        ];

    }

});