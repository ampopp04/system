Ext.define('System.view.tab.panel.ReorderableTabPanelController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.reorderable-tab-panel',

    counter: 0,

    onAddTabClick: function () {
        var tabPanel = this.getView(),
            counter = ++this.counter,
            tab = tabPanel.add({
                title: 'Tab ' + counter
            });
        tabPanel.setActiveTab(tab);
    }
});