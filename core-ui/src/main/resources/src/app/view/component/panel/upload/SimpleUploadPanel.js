/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * A GridPanel class with live search support.
 */
Ext.define('System.view.component.panel.upload.SimpleUploadPanel', {
    extend: 'System.view.system.detail.form.panel.BaseDetailFormPanel',

    xtype: 'simple-upload-panel',

    requires: [
        'Ext.form.field.File',
        'System.util.application.ErrorUtils'
    ],

    uploadType: undefined,
    parentScope: undefined,

    buttons: [
        {
            text: 'Upload',
            handler: function () {
                var win = this.up('window');
                var form = win.down('form');
                form.fileUpload(form.getValues().uploadType, form.fileField, form.filePath);
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
            xtype: 'combo',

            name: 'uploadType',

            fieldLabel: 'Upload Type',
            valueField: 'type',
            displayField: 'name',
            value: 'Save',

            queryMode: 'local',
            store: {
                fields: ['name', 'description', 'type'],
                data: [
                    {
                        'name': 'Update',
                        'description': 'Only updates existing entities that match on either the provided ID or name field.',
                        'type': 'Update'
                    },
                    {
                        'name': 'Insert',
                        'description': 'Only inserts new entities and will not update existing entities that match on ID or name.',
                        'type': 'Insert'
                    },
                    {
                        'name': 'Save',
                        'description': 'Updates existing matching entities or inserts a new entity if no match is found. Entities are matched by ID or name if present. Save combines the functionality of Update and Insert.',
                        'type': 'Save'
                    },
                    {
                        'name': 'Delete then Insert',
                        'description': 'Warning: Deletes all entities of this type and then inserts all entities in the provided file. If an entity of this type is referenced by another in the system it will not be deleted and an error will be thrown.',
                        'type': 'Insert or Delete'
                    },
                    {
                        'name': 'Delete',
                        'description': 'Warning: Deletes all entities matched from the provided file. This mean the system will only look at the ID and/or name, if provided, and find the associated entity and delete it.',
                        'type': 'Delete'
                    }
                ]
            },
            listConfig: {
                itemTpl: '<div data-qtip="{description}">{name}</div>'
            },

            autoSelect: true,
            forceSelection: true,
            allowBlank: false,
            editable: false
        },
        {
            xtype: 'fileuploadfield',

            fieldLabel: 'File',
            emptyText: 'Browse for a CSV or TSV file to upload.',
            buttonText: 'Browse...',

            editable: false,
            msgTarget: 'side',

            listeners: {
                change: function (field, path) {
                    var uploadPanel = this.up('simple-upload-panel');

                    uploadPanel.fileField = field;
                    uploadPanel.filePath = path;
                }
            }
        }
    ],

    fileUpload: function (uploadType, field, path) {
        if (field == undefined) {
            System.util.application.Util.showErrorMessage("Please select a file to upload.");
            return;
        }

        var file = field.fileInputEl.dom.files[0];
        var data = new FormData();

        data.append('file', file);
        data.append('uploadType', uploadType);


        this.up('window').setLoading('Please wait while processing your request.');

        Ext.Ajax.request({
            url: '/api/' + this.parentScope.store.proxy.entityName + '/upload',
            timeout: 120000,
            rawData: data,
            headers: {'Content-Type': null}, //to use content type of FormData
            scope: this,
            callback: function (options, success, response) {
                this.up('window').setLoading(false);

                if (success) {
                    this.parentScope.store.reload();
                    this.up('window').close();
                    System.util.application.Util.showToast('Upload Import Successful');
                } else {
                    System.util.application.ErrorUtils.dataRequestErrorCheck(success, response.responseText, response.status);
                }

            }
        });

    }
});
