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
    selectOnFocus: true,
    forceSelection: true,
    anyMatch: true,
    enableKeyEvents: true,
    pageSize: 20,

    //Not needed, will be handled by remote filtering
    query: false,
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

    /**
     * Fix an ID bug in extjs 6.0.1
     */
    initComponent: function () {
        this.callParent(arguments);
        this.pickerId = (this.getId() + "_" + this.fieldLabel + "_" + this.name + "_Picker").replace(/ /g, '');
    },

    triggers: {
        loadDetailPage: {
            cls: 'x-fa fa-external-link',
            // el: {'data-qtip': 'Show Detail Window'},
            handler: function (combo) {

                var selectedValue = combo.value;
                var store = combo.store;

                if (selectedValue) {
                    System.util.data.StoreUtils.queryStoreById(store, selectedValue, function (records, operation, success, scope) {
                        var record = records[0];
                        record.store = operation._scope.store;
                        System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(record, scope);
                    }, undefined, combo.up('window'));
                } else {
                    System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindow(combo.config.store.config.model, combo.up('window'));
                }

            }
        },

        gridSearchSelection: {
            cls: 'x-fa fa-search',
            //el: {'data-qtip': 'Show Advanced Search Window'},
            handler: function (combo) {

                var detailSearchWindow = 'System.view.system.detail.search.DetailSearchWindow';

                var searchModelName = combo.config.store.config.model;
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

        if (this.maskIfInitialValueExists && this.originalValue != null) {
            //Mask and hide all triggers except the detail page trigger
            this.mask();
            this.updateHideTrigger(true);
            this.triggers['loadDetailPage'].setVisible(true);
        }

    },

    applyDefaultLoadFilters: function (store, defaultLoadFilters) {
        if (defaultLoadFilters) {

            store.addListener('load', function () {
                if (this.store.getTotalCount() == 1) {
                    this.setValue(this.store.getAt(0));
                    this.resetOriginalValue();
                }
            }, this);

            defaultLoadFilters.forEach(function (defaultLoadFilter) {
                defaultLoadFilter.id = this.getFilterId(defaultLoadFilter.value);
                store.addFilter(defaultLoadFilter);
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

        if (Ext.isString(comboValue) && System.util.StringUtils.startsWith(comboValue, 'http')) {
            comboValue = comboValue.replace(combo.store.proxy.url + '/', '');
        }

        comboValue = (comboValue.id == undefined) ? comboValue : comboValue.id;

        if (Ext.isArray(comboValue)) {
            return combo;
        }

        var storeValue = combo.getStore().getById(comboValue)

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

        render: function () {
            if (Ext.isEmpty(this.originalValue)) {
                this.applyDefaultLoadFilters(this.store, this.defaultLoadFilters);
            }
        },

        keyup: function () {
            var newValue = this.getRawValue();

            this.store.removeFilter('comboSearchFilter');

            if (newValue && newValue != '') {
                var filter = new Ext.util.Filter({
                    id: 'comboSearchFilter',
                    property: this.displayField,
                    anyMatch: true,
                    value: newValue,
                    disableOnEmpty: true
                });

                this.store.addFilter(filter, true);
            }
        },

        beforequery: function (queryPlan, eOpts, c, d, e, f) {
            var me = this;
            if (me.filterByValue) {
                me.store.addFilter(me.createFilter(), true);
            }
        },

        select: function (combo, record, eOpts) {
            combo.store.clearFilter();
        },

        focusleave: function () {
            this.store.clearFilter();
        },

        beforedestroy: function (eOpts) {
            this.store.clearFilter();
        }

    }

});