/**
 * The <class>System.view.component.field.SystemFileField</class> defines
 *  a system field for file uploads.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.SystemFileField', {
    extend: 'Ext.form.field.File',

    xtype: 'system-file-field',

    requires: [
        'System.util.component.GridExportUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    fieldLabel: 'File',
    emptyText: 'Browse for a file to upload.',
    buttonText: 'Browse...',

    editable: false,
    msgTarget: 'side',
    readOnly: false,

    submitValue: true,
    fileBinaryData: undefined,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    triggers: {
        downloadFile: {
            cls: 'x-fa fa-download',
            handler: function (fileField) {
                var defaultFileParts = ["report", "docx"];

                var exportFileTypeFieldData = fileField.up().down("system-field-combo-box[name=systemExportFileType]").lastSelectedRecords[0].data;

                var contentType = exportFileTypeFieldData.contentType;
                var fileType = exportFileTypeFieldData.name;

                var fileNameParts = fileField.up().down("textfield[name=name]");

                if (fileNameParts == undefined) {
                    fileNameParts = defaultFileParts;
                } else {
                    fileNameParts = fileNameParts.getValue().split('.');
                    if (fileNameParts.length != 2) {
                        fileNameParts = defaultFileParts;
                    }
                }

                System.util.component.GridExportUtils.downloadEncodedRowsAsFile(Ext.util.Base64._utf8_decode(fileField.fileBinaryData), fileNameParts[0], fileType, contentType);
            }
        }
    },

    setValue: function (value) {
        this.fileBinaryData = value;
    },

    getModelData: function (includeEmptyText, /*private*/
                            isSubmitting) {
        var me = this,
            data = null;
        // Note that we need to check if this operation is being called from a Submit action because displayfields aren't
        // to be submitted,  but they can call this to get their model data.
        if (!me.disabled && (me.submitValue || !isSubmitting)) {
            data = {};
            data[me.getFieldIdentifier()] = Ext.util.Base64._utf8_encode(me.fileBinaryData);
        }
        return data;
    },

    onFileChange: function (button, e, value) {


        var fileReader = function getBase64(fileField) {

            var file = fileField.fileInputEl.dom.files[0];

            var reader = new FileReader();
            reader.fileField = fileField;

            reader.readAsText(file);

            reader.onload = function () {
                reader.fileField.fileBinaryData = reader.result;
            };

            reader.onerror = function (error) {
                alert('Error: ' + error);
            };

        };

        fileReader(this);

        this.duringFileSelect = true;
        Ext.form.field.File.superclass.setValue.call(this, value);
        delete this.duringFileSelect;
    }

});