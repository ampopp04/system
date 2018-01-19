/**
 * The <class>System.view.system.detail.search.DetailSearchWindow</class> defines
 * the basic detail for window. This is the window that is used for advanced
 * searching on selecting detail entities within a combo box.
 *
 * In rare circumstances the combo box search is not enough to find the correct entity.
 * This class will aid the combo box by allow it to open a new window that is a grid of the entities
 * with all the additional columns. They can then search and filter on all of these columns to finally
 * select the entity they want to fill that initial combo box.
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.search.DetailSearchWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'detail-search-window',

    /**
     * Defines the model name of the entity we will show a grid for.
     * This includes all of that models columns and information.
     */
    searchModelName: undefined,

    /**
     * The parentSearchCaller is the entity in which we will call
     * setValue on for a specific row that is to be selected in the grid that is created.
     *
     *We will have an action column that gets added to the grid that is an icon to show that it will set the
     * value on the parent search caller entity. This action column will take whatever row is clicked and
     * use it's record to set the value on the parentSearchCaller and then close the grid search window.
     */
    parentSearchCaller: undefined,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Configuration                                                  //////////
    /////////////////////////////////////////////////////////////////////

    title: 'Advanced Search',

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: me.parentSearchCaller.fieldLabel,
                parentSearchCaller: me.parentSearchCaller,
                modelName: me.searchModelName
            }
        ];

        this.callParent([config]);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                              Layout                                                     //////////
    /////////////////////////////////////////////////////////////////////

});



