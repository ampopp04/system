/**
 * The <class>System.util.system.detail.DetailFormUtils</class> defines
 *  utility methods for working with detail form windows.
 *
 * @author Andrew
 */
Ext.define('System.util.system.detail.DetailWindowFormUtils', {

    requires: [
        'System.util.data.ModelUtils',
        'System.util.StringUtils',
        'System.util.data.RecordUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {

        createNewDetailFormWindowFromRecord: function (record, referenceWindow, optionalDetailFormWindow, newEntityDetailWindowDefaultValueFilters) {
            //Verify that the passed in record is the correct one synced with the store
            record = System.util.data.RecordUtils.transformToStoreRecord(record, record.store);

            var windowToCreate = optionalDetailFormWindow ? optionalDetailFormWindow : "System.view.system.detail.window.DetailFormWindow";
            var completeRecord = System.util.system.detail.DetailWindowFormUtils.reformatRecordLayout(record);
            var detailForm = Ext.create(windowToCreate, {
                title: System.util.data.ModelUtils.modelNameToDisplayName(record.store.model.entityName),
                fieldSet: completeRecord.getFields(),
                initialFormRecord: completeRecord,
                newEntityDetailWindowDefaultValueFilters: newEntityDetailWindowDefaultValueFilters
            });

            detailForm.down('base-detail-form-panel').loadRecord(completeRecord);

            detailForm.animate({
                to: {x: referenceWindow.getXY()[0] + 20}
            }).animate({
                to: {y: referenceWindow.getXY()[1] + 20}
            });

            detailForm.show();

            return detailForm;
        }
        ,

        /**
         * Because of the way the record array names are constructed we need to adjust them
         * to follow the naming convention of our passed in records object so we can dynamically access the data
         */
        reformatRecordLayout: function (record) {
            var rec = record;

            for (var property in rec) {
                if (rec.hasOwnProperty(property) && System.util.StringUtils.startsWith(property, '_')) {
                    rec.data[System.util.StringUtils.removeFirstCharacter(property)] = rec[property].data.id;
                }
            }

            rec.store = record.store;

            return rec
        },

        /**
         * Creates a new entity detail form window of the type defined by the model name.
         * If the related record is populated it is assumed this related record is of a different model
         * from the one defined by the provided modelName.  The related record is intended to be used
         * to prepopulate the field on the modelName that is related to this record.
         *
         * For instance a Task instance may have a reference to a Job. Therefore we would have a related record
         * for the Job and then the new Task record would have it's Job information prepopulated via the related Job record.
         */
        createNewDetailFormWindow: function (modelName, referenceWindow, newRecordConfig, newEntityDetailWindowDefaultValueFilters) {
            newRecordConfig = newRecordConfig ? newRecordConfig : {};
            newRecordConfig.id = undefined;

            var newEntity = Ext.create(modelName, newRecordConfig);

            newEntity.id = undefined;
            newEntity.data.id = undefined;
            newEntity.store = System.util.data.StoreUtils.lookupStore(modelName);

            return System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(newEntity, referenceWindow, undefined, newEntityDetailWindowDefaultValueFilters);
        }

    }
});