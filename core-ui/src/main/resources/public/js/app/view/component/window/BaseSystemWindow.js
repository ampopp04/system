/**
 * The <class>System.view.component.window.BaseSystemWindow</class> defines
 * the base system window configuration.
 *
 * @author Andrew
 */
Ext.define('System.view.component.window.BaseSystemWindow', {
    extend: 'Ext.window.Window',

    requires: ['System.view.component.panel.grid.BaseSystemGridPanel'],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    //title: 'UNDEFINED-PLEASE-SPECIFY',
    xtype: 'base-system-window',

    autoShow: true,
    layout: {type: 'fit'},
    headerPosition: 'right',
    width: 900, minWidth: 550, height: 600,
    header: {titlePosition: 2, titleAlign: 'center'}
});



