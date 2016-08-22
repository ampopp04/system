/**
 * The <class>System.view.component.panel.tab.reorderable.ReorderableTabPanelController</class> defines
 *  methods for handling events from the ReorderableTabPanel
 *
 * @author Andrew
 */
Ext.define('System.view.component.panel.tab.reorderable.ReorderableTabPanelController', {
    extend: 'Ext.app.ViewController',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'controller.reorderable-tab-panel', counter: 0,

    /**
     * Add a new tab on add click
     */
    onAddTabClick: function () {
        var tabPanel = this.getView(),
            counter = ++this.counter,
            tab = tabPanel.add({
                title: 'Tab ' + counter
            });
        tabPanel.setActiveTab(tab);
    }
});