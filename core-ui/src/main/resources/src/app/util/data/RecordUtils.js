/**
 * The <class>System.util.data.RecordUtils</class> defines
 *  utility methods for managing and saving records to the server
 *
 * @author Andrew
 */
Ext.define('System.util.data.RecordUtils', {

    requires: [
        'System.util.data.StoreUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Save a record to the server. This record can be
         * new or updated.
         *
         * @param record
         * @returns {*}
         */
        saveRecord: function (record) {
            // save the record to the server
            record.save({
                callback: function (record, operation, success) {
                    System.util.data.StoreUtils.lookupStore(record.entityName).reload();
                }
            }, record);
        },
        /*
            /**
             * Ensures that the provided record is
             * correctly synced to the store and that
             * there is no record within the store that is
             * equal to this record.
             *
             * If the store contains a record that is equal to this record
             * we will copy the provided records properties to the stores
             * record and return the stores record as if it were the record
             * we had been using all along.
             *
             * The server maintains an ID
             * within the database tables but the UI stores manage entities
             * based on an internally unique identifier.
             *
             * This fixes the cases where these can get out of sync.
             *
             * @param record - The record to essentially reassociate to our store if needed.
             * @param store - The store to associate our record with.
             * @returns - The record that is properly associated with our store.
             */    /*
     transformToStoreRecord: function (record, store) {

               is this needed ??? just replace old record with new???
                   syncin ant working properly with the copyfrom
               if (!Ext.isEmpty(record.id) && store.contains(record)) {
                   var storeRecord = store.getById(record.id);
                   //Update existing record with same system ID
                   // but different internally managed component IDs
                   if (storeRecord.internalId != record.internalId) {
                       //Copy over any changes or differences from the raw record
                       storeRecord.copyFrom(record);
                       //Return the record that is associated and linked to our store
                       return storeRecord;
                   }
               }
               //Either this is a new record, meaning no ID and newly created
               // or it's a record that already exists in the servers database but
               // has never been loaded into the UI store
               return record;
           },*/

        transformRecordLayout: function (data, model) {
            var returnData = {};

            for (var property in data) {
                if (data.hasOwnProperty(property) && !System.util.StringUtils.startsWith(property, '_')) {
                    var keyValue = data[property];

                    if (keyValue != undefined) {
                        var field = model.getField(property);

                        if (field && field.reference) {
                            if (keyValue != undefined && keyValue != "") {
                                returnData[property] = field.reference.association.right.cls.proxy.url + "/" + (keyValue.id ? keyValue.id : keyValue);
                            }
                        } else {
                            returnData[property] = keyValue;
                        }
                    }
                }
            }

            return returnData;
        },

        /**
         * Duplicate all properties in a record such that
         * that any property starting with _ will also have that
         * same property accessible without the initial _
         */
        transformJsonEncodedRecordLayout: function (data) {
            var returnData = data;

            for (var property in data) {
                if (data.hasOwnProperty(property) && System.util.StringUtils.startsWith(property, '_')) {
                    var propertyName = System.util.StringUtils.removeFirstCharacter(property);
                    var propertyData = data[property];

                    if (propertyData && propertyData.data) {
                        propertyData = System.util.data.RecordUtils.transformJsonEncodedRecordLayout(propertyData);
                    }

                    returnData[propertyName] = propertyData;
                    returnData.data[propertyName] = propertyData;
                }
            }

            for (var property in data.data) {
                if (data.data.hasOwnProperty(property) && System.util.StringUtils.startsWith(property, '_')) {
                    var propertyData = data.data[property];

                    if (propertyData.data) {
                        propertyData = System.util.data.RecordUtils.transformJsonEncodedRecordLayout(propertyData);
                    }

                    returnData.data[System.util.StringUtils.removeFirstCharacter(property)] = propertyData;
                }
            }

            return returnData;
        }

    }
});