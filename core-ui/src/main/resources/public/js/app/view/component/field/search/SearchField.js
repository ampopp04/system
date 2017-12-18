Ext.define('System.view.component.field.search.SearchField', {
    extend: 'System.view.component.field.search.Tagfield',

    requires: [
        'System.util.StringUtils'
    ],

    xtype: 'system-search-field',

    //User defined
    parentEntity: undefined,

    /**
     * Defines the default search filter to apply to this store and filter box
     */
    defaultSearchValue: undefined,
    /**
     * Forced default search filter makes the default search filter required and
     * unable to be removed/deleted, it also hides it from the display and
     * applys it directly as a filter to the store
     */
    defaultSearchFilterForced: false,

    valueField: 'value',
    displayField: 'value',

    //Defaults
    emptyText: 'Search',
    store: {
        type: 'array',
        fields: [
            {name: 'value', type: 'string'},
            {name: 'property', type: 'string', defaultValue: 'null-all-property'},
            {name: 'operator', type: 'string', defaultValue: undefined}
        ],
        isLoaded: function () {
            return true;
        },
    },

    queryMode: 'local',

    forceSelection: false,
    createNewOnEnter: true,
    createNewOnBlur: true,

    triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
    triggerAction: undefined,
    onTriggerClick: function () {
        var me = this;
        me.setValue([]);
        me.initSearch(me);
    },

    multiSelect: true,
    triggerOnClick: false,

    filterPickList: true,

    listeners: {

        change: function (tagfield, currentValues, previousValues) {
            var isAdd = undefined;
            var arrayValues = undefined;

            if (currentValues.length > previousValues.length) {
                //Add Change
                isAdd = true;
                arrayValues = Ext.Array.difference(currentValues, previousValues);
            } else if (previousValues.length > currentValues.length) {
                //Remove Change
                isAdd = false;
                arrayValues = Ext.Array.difference(previousValues, currentValues)
            }

            if (arrayValues) {
                Ext.Array.forEach(arrayValues, function (value) {
                    this.updateTagStoreFilters(isAdd ? this.valueCollection.get(this.getFilterId(value)) : value, isAdd);
                }, tagfield);
            }

        },

        render: function (field) {
            var me = this;
            this.configureSearchField(this);
        },

        beforedestroy: function (eOpts) {
            var me = this;
            me.setValue([]);
            me.parentEntity.store.clearFilter();
        }

    },

    configureSearchField: function (field) {
        var me = this;

        if (me.parentEntity) {

            me.parentEntity.searchField = field;

            var highlightSearchResults = function (a, b, c, d, e, f, g) {
                var me = this;
                var filteredStore = me.parentEntity.store;

                filteredStore.each(function (record, idx) {

                    var node = me.parentEntity.view.getNode(record);

                    if (node) {
                        var columns = me.parentEntity.columnManager.getColumns();

                        Ext.Array.forEach(columns, function (column) {
                            var cell = Ext.fly(node).down(column.getCellInnerSelector(), true);

                            if (cell) {

                                Ext.Array.forEach(me.getValue(), function (val) {
                                    cell.innerHTML = me.strMarkRedPlus(val, cell.innerHTML);
                                }, me);

                            }
                        });
                    }
                }, me);
            };

            var storeLoadListenerSetup = function (searchField, parentEntity, store) {
                var store = store;

                if (this.parentEntity && this.parentEntity.store && this.parentEntity.store.$className) {
                    store = this.parentEntity.store;
                }

                if (this.xtype == 'system-search-field') {
                    searchField = this;
                }

                if (store && store.$className) {
                    store.addListener('load', highlightSearchResults, searchField);
                    searchField.initSearch(searchField);

                    if (!store.isLoading) {
                        store.load();
                    }
                }
            };

            var storeNameLoadListener = function (index, o, key, eOpts) {
                var store = me.parentEntity.store;

                if (Ext.isString(store) && store == key) {
                    //Store was created and added before our parent even knows
                    me.parentEntity.store = o;
                }

                storeLoadListenerSetup(this, me.parentEntity, me.parentEntity.store);
            };

            if (Ext.isString(me.parentEntity.store)) {
                Ext.data.StoreManager.addListener('add', storeNameLoadListener, me);
            }
            else if (me.parentEntity.store && me.parentEntity.store.storeId != 'ext-empty-store') {
                storeLoadListenerSetup(me, me.parentEntity, me.parentEntity.store);
            } else {
                me.parentEntity.addListener('reconfigure', storeLoadListenerSetup, me);
            }

        }
    },

    initSearch: function (searchField) {
        if (searchField.defaultSearchValue) {

            if (searchField.defaultSearchFilterForced) {
                var newValues = this.createSearchModels(searchField.defaultSearchValue);

                Ext.Array.forEach(newValues, function (value) {
                    this.filterStore(value, this.parentEntity);
                }, this);

            } else {
                this.setValue(searchField.defaultSearchValue);
            }

        }

        if (this.parentEntity.store.getAutoLoad() == false) {
            this.parentEntity.store.setAutoLoad(true);
            this.parentEntity.store.reload();
        }

    },

    createSearchModels: function (searchValues) {
        var newValue = [];

        if (searchValues) {
            Ext.Array.forEach(searchValues, function (val) {
                newValue.push(this.createDefaultSearchValue(val));
            }, this);
        }

        return newValue;
    },

    setValue: function (v) {
        var newValue = this.createSearchModels(v);
        return System.view.component.field.search.SearchField.superclass.doSetValue.call(this, newValue, !Ext.isEmpty(newValue), true);
    },

    createDefaultSearchValue: function (searchString) {
        var defaultModelValue = undefined;

        if (searchString.isModel) {
            return searchString;
        }
        else if (Ext.isString(searchString)) {
            defaultModelValue = {
                id: this.getFilterId(searchString),
                value: searchString,
                property: 'null-all-property',
                operator: undefined
            };
        }
        else {
            defaultModelValue = searchString;
            defaultModelValue.id = this.getFilterId(defaultModelValue.value);
        }

        return this.store.createModel(defaultModelValue);
    },

    // Private implementation.
    // The display value is always the raw value.
    // Picked values are displayed by the tag template.
    getDisplayValue: function () {
        return this.getRawValue();
    },

    /**
     * @inheritdoc
     * Intercept calls to getRawValue to pretend there is no inputEl for rawValue handling,
     * so that we can use inputEl for user input of just the current value.
     */
    getRawValue: function () {
        var me = this,
            records = me.getValueRecords(),
            values = [],
            i, len;

        for (i = 0, len = records.length; i < len; i++) {
            values.push(records[i].data['value']);
        }

        return values.join(',');
    },

    setRawValue: function (value) {
        // setRawValue is not supported for tagfield.
        return;
    },


    updateTagStoreFilters: function (value, isAdd) {
        if (this.parentEntity && this.parentEntity.store && value) {
            var parentStore = this.parentEntity.store;
            if (isAdd) {
                this.filterStore(value, this.parentEntity);
            } else {
                parentStore.removeFilter(this.getFilterId(value));
            }

            this.setExtraSearchParams(parentStore, this.parentEntity);
        }
    },

    setExtraSearchParams: function (store, parentEntity) {
        var proxy = store.getProxy();
        proxy.setExtraParam('searchDepth', parentEntity.searchDepth);
        proxy.setExtraParam('extraSearchParams', parentEntity.extraSearchParams);
    },

    filterStore: function (value, parentEntity) {
        var newFilter = Ext.apply({
            parentEntity: parentEntity
        }, value.data);

        parentEntity.store.addFilter(newFilter);
    },

    getFilterId: function (searchValue) {
        return System.util.StringUtils.lowercase(System.util.StringUtils.replaceSpacesWithUnderscores(searchValue + 'Filter'));
    },

    strMarkRedPlus: function (searchString, currentValue) {
        return currentValue.replace(
            new RegExp('(' + Ext.String.escapeRegex(searchString) + ')', "gi"), "<span style='color: red;'><b>$1</b></span>");
    }

});