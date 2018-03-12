/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.field.search.SearchField</class> defines
 *  a search field combo box that can easily be added to any grid or entity within the system.
 *
 *  It will auto filter the associated entity's store and update its display view
 *  while highlighting the search results.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.search.SearchField', {
    extend: 'System.view.component.field.search.Tagfield',

    xtype: 'system-search-field',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Configurable Properties                                         //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * The parent entity that is associated with this search field.
     * This parent entity is updated and it's view will
     * change based on the filters applied to it's bound store.
     *
     * Ex. A grid would be the parent entity and it would have
     * a search field (this) that is linked to it to filter it's store
     * and adjust it's grid view to display the search results
     */
    parentEntity: undefined,

    /**
     * Ability to control whether this search field is automatically
     * initialized at render time or not. Manual initialize is useful
     * when creating a search field, setting some search filters later, and then
     * activating search initialization when needed.  This allows controlling
     * the automatic loading of the store.
     */
    manualInitialize: undefined,

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


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Search Field Store                                               //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * This is the in memory store that holds the values that are entered into this
     * search fields combo box.  Where the value field is one of the strings that the user
     * enters to search on.
     */
    store: {
        type: 'array',
        fields: [
            {name: 'value', type: 'string'},
            {name: 'property', type: 'string', defaultValue: 'null-all-property'},
            {name: 'operator', type: 'string', defaultValue: undefined}
        ],
        isLoaded: function () {
            return true;
        }
    },

    queryMode: 'local',

    ///////////////////////////////////////////////////////////////////////
    ////////                                      Default Display Properties                                      //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The field, within our search fields store, that we consider the value of this search field.
     */
    valueField: 'value',
    /**
     * The field, from the search fields store, that we will show to the UI.
     */
    displayField: 'value',

    /**
     * When no searches are entered then display an empty combo box with
     * the following string showing by default.
     */
    emptyText: 'Search',
    /**
     * Remove the prefix text label that this combo box would normally have.
     */
    fieldLabel: undefined,

    ///////////////////////////////////////////////////////////////////////
    ////////                                       Default Action Properties                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Do not require the user to search on specific predetermined values
     */
    forceSelection: false,
    /**
     * Allow the user to hit the enter key to enter new search values after typing them
     */
    createNewOnEnter: true,
    /**
     * If they enter a search value and move away focus from the search field then enter it as a value
     */
    createNewOnBlur: true,

    /**
     * Change the X icon button on the search field to clear
     * the search field and initialize it with the default forced search values
     */
    triggerCls: Ext.baseCSSPrefix + 'form-clear-trigger',
    triggerAction: undefined,
    onTriggerClick: function () {
        var me = this;
        me.setValue([]);
        me.initSearch(me);
    },

    /**
     * Allow selecting multiple search values in this combo box search field
     */
    multiSelect: true,
    triggerOnClick: false,
    filterPickList: true,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Listeners                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * The listeners for this search field. These listeners will be called when these events
     * are heard by this search field
     */
    listeners: System.view.component.field.search.listener.SearchFieldListeners.getListeners(),

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Init Methods                                                    //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Configure the search field and it's binding to the associated parent entity's store
     *
     * @param searchField - the search field to configure
     */
    configureSearchField: function (searchField) {
        var me = searchField;

        if (me.parentEntity) {
            me.parentEntity.searchField = me;
            System.view.component.field.search.util.SearchFieldSetupUtils.configureSearchFieldSetup(me);
        }
    },

    /**
     * Initialize the default search field search values.
     *
     * @param searchField - the search field to set the default values on
     */
    initSearch: function (searchField) {

        if (searchField.parentEntity.store.getAutoLoad() == false) {
            searchField.parentEntity.store.setAutoLoad(true);
        }

        if (searchField.defaultSearchValue) {

            if (searchField.defaultSearchFilterForced) {
                var newValues = System.view.component.field.search.util.SearchFieldValueUtils.createSearchModels(searchField.defaultSearchValue, searchField.store);

                Ext.Array.forEach(newValues, function (value) {
                    var me = this;
                    System.view.component.field.search.util.SearchFieldFilterUtils.filterStore(value, me.parentEntity);
                }, searchField);

            } else {
                searchField.setValue(searchField.defaultSearchValue);
            }

        }
    },

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Value Methods                                                 //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Set a search string model value to this fields store
     *
     * @param v - the search string
     * @returns {*} - the results of adding the search value
     */
    setValue: function (v) {
        var me = this;
        var newValue = System.view.component.field.search.util.SearchFieldValueUtils.createSearchModels(v, me.store);
        return System.view.component.field.search.SearchField.superclass.doSetValue.call(me, newValue, !Ext.isEmpty(newValue), true);
    },

    /**
     * The display value is always the raw value.
     * Picked values are displayed by the tag template.
     *
     * @returns {*} - the raw search string
     */
    getDisplayValue: function () {
        return this.getRawValue();
    },

    /**
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

    /**
     * setRawValue is not supported for tagfield.
     */
    setRawValue: function (value) {
        return;
    }

});