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
            // //Verify that the passed in record is the correct one synced with the store
            //record = System.util.data.RecordUtils.transformToStoreRecord(record, record.store);

            var windowToCreate = optionalDetailFormWindow ? optionalDetailFormWindow : "System.view.system.detail.window.DetailFormWindow";
            var completeRecord = System.util.data.RecordUtils.transformJsonEncodedRecordLayout(record);

            var detailForm = Ext.create(windowToCreate, {
                title: System.util.data.ModelUtils.modelNameToDisplayName(record.store.model.entityName),
                fieldSet: completeRecord.store.model.getFields(),
                initialFormRecord: completeRecord,
                newEntityDetailWindowDefaultValueFilters: newEntityDetailWindowDefaultValueFilters,
                referenceEntity: referenceWindow
            });

            //Show all required fields that are missing or invalid
            detailForm.down('base-detail-form-panel').isValid();

            var animationReferenceEntity = referenceWindow;

            if (animationReferenceEntity.getXY == undefined) {
                animationReferenceEntity = animationReferenceEntity.up('window');
            }

            detailForm.animate({
                to: {x: animationReferenceEntity.getXY()[0] + 20}
            }).animate({
                to: {y: animationReferenceEntity.getXY()[1] + 20}
            });

            //detailForm.show();

            return detailForm;
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
            modelName = modelName.$className ? modelName.$className : modelName;

            System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {

                newRecordConfig = newRecordConfig ? newRecordConfig : {};
                newRecordConfig.id = undefined;

                var newEntity = Ext.create(modelName, newRecordConfig);

                newEntity.id = undefined;
                newEntity.data.id = undefined;
                newEntity.store = store;
                newEntity.dirty = true;

                System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(newEntity, referenceWindow, undefined, newEntityDetailWindowDefaultValueFilters);

            });

        }

    }
});