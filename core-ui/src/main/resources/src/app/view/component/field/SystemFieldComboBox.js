/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.field.SystemFieldComboBox</class> defines
 *  a combo box that can be used within the UI. This is primarily used within an entity's detail page.
 *
 * A combo box supports double clicking to drill into it and open up the reference detail page for
 * further details on that entity. It also populates a list of choices from the server for selection.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.SystemFieldComboBox', {
    extend: 'Ext.form.field.ComboBox',

    requires: [
        'System.util.StringUtils',
        'System.util.data.StoreUtils',
        'System.view.system.detail.search.DetailSearchWindow'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'system-field-combo-box',
    typeAhead: true,
    typeAheadDelay: 500,
    selectOnFocus: true,
    forceSelection: true,
    anyMatch: true,
    enableKeyEvents: true,
    pageSize: 20,

    //Not needed, will be handled by remote filtering
    minChars: 2,

    //growToLongestValue: true,
    //grow: true,

    /**
     * Defines an array of filters applied to the store on load
     * If only a single entity remains in the store after the filters are applied
     * then that value will automatically be set
     *
     * These filters will only be removed on destroy
     */
    defaultLoadFilters: undefined,

    filterByRoot: undefined,
    filterByValue: undefined,
    filterByOperator: '=',

    //If this field has a value set upon load then mask this field as
    // it should not be allowed to change
    maskIfInitialValueExists: false,
    //If we are to mask the combo box then we only mask the text area
    maskElement: 'inputWrap',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    initComponent: function () {
        var me = this;
        me.wasDirty = false;
        var store = me.store;

        if (Ext.isString(store)) {

            var modelName = System.util.data.StoreUtils.getModelNameFromStoreName(store);

            //Don't allow binding to occur on a string without the store being created
            me.store = undefined;

            System.util.component.GridColumnUtils.getStoreByModelName(modelName,
                function (store, scope) {
                    scope.bindStore(System.util.data.StoreUtils.createNewStore(store), true, true);
                    if (scope.value == undefined && scope.config && scope.config.value) {
                        //Value was set before this store was initialized so we must set the value now that the store is created
                        scope.setValue(scope.config.value);
                    }
                }, me);

        } else {
            me.store = System.util.data.StoreUtils.createNewStore(store);
        }

        this.callParent(arguments);
    },

    triggers: {
        loadDetailPage: {
            cls: 'x-fa fa-external-link',
            // el: {'data-qtip': 'Show Detail Window'},
            handler: function (combo) {

                var selectedValue = combo.value;
                var store = combo.store;

                if (selectedValue) {
                    var scope = combo.up('window');

                    if (selectedValue.isModel) {
                        System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(selectedValue, scope);
                    }
                    else {
                        System.util.data.StoreUtils.queryStoreById(store, selectedValue, function (records, operation, success, scope) {
                            var record = records[0];
                            record.store = operation._scope.store;
                            System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(record, scope);
                        }, undefined, scope);
                    }

                } else {
                    System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindow(store.model, combo);
                }

            }
        },

        gridSearchSelection: {
            cls: 'x-fa fa-search',
            //el: {'data-qtip': 'Show Advanced Search Window'},
            handler: function (combo) {

                var detailSearchWindow = 'System.view.system.detail.search.DetailSearchWindow';

                var searchModelName = combo.store.model.$className;
                var parentSearchCaller = combo;

                var win = Ext.create(detailSearchWindow, {
                    searchModelName: searchModelName,
                    parentSearchCaller: parentSearchCaller
                });

                win.show();

            }
        }
    },

    checkValueOnChange: function () {
        var me = this;
    },

    resetOriginalValue: function () {

        this.originalValue = this.getValue();
        this.checkDirty();

        if (this.maskIfInitialValueExists && this.isExistingEntity()) {
            //Mask and hide all triggers except the detail page trigger
            this.mask();
            this.triggers['gridSearchSelection'].setVisible(false);
            this.triggers['picker'].setVisible(false);
        }

    },

    /**
     * Inspects the parents record to see if it is new or
     * already has an ID.
     *
     * This allows the combo to know if it is being
     * created from a new entity window
     * or from a pre-existing entity record
     */
    isExistingEntity: function () {

        if (this.ownerCt && this.ownerCt.ownerCt && !Ext.isEmpty(this.ownerCt.ownerCt.ownerCt)) {
            var parentRecordEntity = this.ownerCt.ownerCt.ownerCt;

            if (parentRecordEntity && parentRecordEntity.config && !Ext.isEmpty(parentRecordEntity.config.initialFormRecord)) {
                var config = parentRecordEntity.config.initialFormRecord;

                if (!Ext.isEmpty(config.id) || (config && config.data && !Ext.isEmpty(config.data.id))) {
                    return true;
                }

            }
        }

        return false;
    },

    applyDefaultLoadFilters: function (store, defaultLoadFilters) {
        if (defaultLoadFilters) {

            store.addListener('load', function () {

                if (this.defaultLoadFiltersApplied == undefined) {

                    if (this.store.getTotalCount() > 0) {
                        this.setValue(this.store.last());
                        this.resetOriginalValue();
                        this.dirty = true;
                    }

                    this.defaultLoadFiltersApplied = true;
                }

            }, this);

            defaultLoadFilters.forEach(function (defaultLoadFilter) {
                if (!Ext.isEmpty(defaultLoadFilter.value)) {
                    var filterIdPrefix = defaultLoadFilter.property ? defaultLoadFilter.property : defaultLoadFilter.value;
                    defaultLoadFilter.id = this.getFilterId(filterIdPrefix);
                    store.addFilter(defaultLoadFilter);
                }
            }, this);

        }
    },

    getFilterId: function (searchValue) {
        return System.util.StringUtils.lowercase(System.util.StringUtils.replaceSpacesWithUnderscores(searchValue + 'Filter'));
    },

    setValue: function (v) {
        var combo = this;
        var comboValue = v != undefined ? v : combo.value;


        if (comboValue == undefined) {
            return combo;
        }

        if (comboValue.isModel) {
            //Already given the record so set it
            return System.view.component.field.SystemFieldComboBox.superclass.setValue.call(this, comboValue);
        }

        if (Ext.isString(comboValue) && System.util.StringUtils.startsWith(comboValue, 'http')) {
            comboValue = comboValue.replace(combo.store.proxy.url + '/', '');
        }

        comboValue = (comboValue.id == undefined) ? comboValue : comboValue.id;

        if (Ext.isArray(comboValue)) {
            return combo;
        }

        var storeValue = combo.getStore().getById(comboValue);

        if (storeValue == undefined) {

            this.value = comboValue;

            System.util.data.StoreUtils.queryStoreById(combo.store, comboValue, function (records, operation, success, scope) {

                System.view.component.field.SystemFieldComboBox.superclass.setValue.call(scope, records[0]);

            }, undefined, combo);

        } else {
            return System.view.component.field.SystemFieldComboBox.superclass.setValue.call(this, storeValue);
        }

        return combo;
    },

    createFilter: function () {
        var me = this;
        return new System.view.component.field.filter.SystemEntityFilter({
            column: me,
            grid: me,
            isGridFilter: false,
            operator: me.filterByOperator,
            topLevelRoot: me.filterByRoot,
            value: me.filterByValue,
            getGridStore: function () {
                return this.column.store;
            },
            setColumnActive: function (active) {
                if (this.owner) {
                    this.column[active ? 'addCls' : 'removeCls'](this.owner.filterCls);
                }
            }
        });
    },

    listeners: {

        afterrender: function () {
            //Apply default load filters only to phantom (new) entity fields
            var owner = this.ownerCt;
            if (Ext.isEmpty(this.getValue()) && owner && owner.getRecord && (owner.getRecord() == undefined || owner.getRecord().id == undefined)) {
                this.applyDefaultLoadFilters(this.store, this.defaultLoadFilters);
            }

            this.resetOriginalValue();
        },

        beforequery: function (queryPlan, eOpts, c, d, e, f) {
            var me = this;
            var store = me.store;

            if (me.filterByValue) {
                store.addFilter(me.createFilter(), true);
            }

            var newValue = queryPlan.query;

            if (store.filters.containsKey('comboSearchFilter')) {
                store.removeFilter('comboSearchFilter');
            }

            if (newValue && newValue != '') {
                var filter = new Ext.util.Filter({
                    id: 'comboSearchFilter',
                    property: this.displayField,
                    anyMatch: true,
                    value: newValue,
                    disableOnEmpty: true
                });

                store.addFilter(filter, true);
            }

        },

        select: function (combo, record, eOpts) {
            combo.store.clearFilter();
            combo.validate();
        },

        focusleave: function () {
            this.store.clearFilter();
        },

        beforedestroy: function (eOpts) {
            this.store.clearFilter();
        }

    },

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Validation                                                       //////////
    //////////////////////////////////////////////////////////////////////

    validator: function (val) {
        return (val || this.allowBlank) ? true : "This field is required";
    }

});