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

                win.setLoading('Please wait while processing your request.');

                form.submit({
                    url: '/api/' + form.parentScope.store.proxy.entityName + '/uploadTemplate',
                    method: 'Post',
                    timeout: 120000,
                    scope: this,
                    success: function (form, result, data) {
                    },
                    failure: function (form, response) {
                        this.up('window').setLoading(false);

                        if (response.response.status == 200) {
                            this.up('window').close();

                            System.util.application.Util.showToast('Upload Template Successful');

                            var gridPanel = form.owner.parentScope;
                            var exportMenu = gridPanel.down('menuitem#' + gridPanel.store.model.entityName + '-toolbar-export-menu');
                            System.util.component.ToolbarUtils.populateExportReportMenuItems(exportMenu, gridPanel.store.model.entityName);

                        } else {
                            System.util.application.ErrorUtils.dataRequestErrorCheck(false, response.response.responseText, response.response.status);
                        }

                    }
                });

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

    items: [
        {
            xtype: 'system-file-field',
            name: 'templateFile',
            fieldLabel: 'Template File'
        },
        {
            xtype: 'system-field-combo-box',
            valueField: 'id',
            displayField: 'name',

            fieldLabel: 'Department',
            name: 'systemExportTaskType',
            store: System.util.data.StoreUtils.lookupStoreByName('SystemExportTaskTypesStore')
        },
        {
            xtype: 'system-field-combo-box',
            valueField: 'id',
            displayField: 'name',

            fieldLabel: 'Export As',
            name: 'systemExportFileType',
            store: System.util.data.StoreUtils.lookupStoreByName('SystemExportFileTypesStore')
        },
        {
            xtype: 'system-field-text',
            name: 'name',
            fieldLabel: 'Name'
        },
        {
            xtype: 'system-field-text',
            name: 'description',
            fieldLabel: 'Description'
        }
    ]

});