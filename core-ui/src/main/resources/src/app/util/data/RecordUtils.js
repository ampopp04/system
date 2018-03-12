/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

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
         * Return the record value defined by the modelFieldName. If there are nested
         * records within this record you can go a level deeper and grab out an associated record by
         * supplying the associatedRecordName
         *
         * @param record - the record to get the value from
         * @param modelFieldName - the field you want the value for
         * @param associatedRecordName - an optional associated record name that contains the value
         * @returns - the value of the field within the record
         */
        getRecordValue: function (record, modelFieldName, associatedRecordName) {
            if (record) {
                record = record['_' + associatedRecordName] ? record['_' + associatedRecordName] : record[associatedRecordName] ? record[associatedRecordName] : record.data ? record.data[associatedRecordName] : record.data && record.data['_' + associatedRecordName] ? record.data['_' + associatedRecordName] : record;
            }

            var fieldValue = record && record[modelFieldName] ? record[modelFieldName] : record && record.data ? record.data[modelFieldName] : record['_' + modelFieldName];
            if (record.get && fieldValue == undefined) {
                fieldValue = record.get(modelFieldName);
            }

            return fieldValue;
        },

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