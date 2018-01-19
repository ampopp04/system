/**
 * The <class>System.view.SystemExportMainToolbarDefinition</class> defines
 *  the main toolbar menu for system exports.
 *
 * @author Andrew
 */
Ext.define('System.view.SystemExportMainToolbarDefinition', {
    extend: 'Ext.Container',

    requires: [
        'System.util.system.JobUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'system-export-main-toolbar-definition',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////
    listeners: {

        added: function (container, index, eOpts) {
            container.up().insert(0, [
                {xtype: 'tbspacer'},
                {
                    text: 'Admin',
                    iconCls: 'x-fa fa-user-plus',
                    menu: new Ext.menu.Menu({
                        items: [{
                            text: 'Setup',
                            iconCls: 'x-fa fa-gears',
                            menu: new Ext.menu.Menu({
                                items: [{
                                    text: 'Expression Management',
                                    iconCls: 'x-fa fa-folder-open-o',
                                    menu: new Ext.menu.Menu({
                                        items: [{
                                            text: 'Entity Expressions',
                                            iconCls: 'x-fa fa-files-o',
                                            handler: function () {
                                                System.util.system.JobUtils.createWindow('System.view.folder.path.FolderPathSetupWindow');
                                            }
                                        }, {
                                            text: 'Expression Operations',
                                            iconCls: 'x-fa fa-cogs',
                                            handler: function () {
                                                System.util.system.JobUtils.createWindow('System.view.folder.path.FolderOperationSetupWindow');
                                            }
                                        }]
                                    })
                                }]
                            })
                        }]
                    })
                }]);

            System.util.system.JobUtils.createWindow('System.view.job.tracker.dashboard.DashboardWindow');

        }
    }
});