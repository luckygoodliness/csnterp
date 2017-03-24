Ext.define('Monitorlaborcostandothershareexpense.view.MonitorlaborcostandothershareexpenseView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/monitorlaborcostandothershareexpense',
    aHeight: 400,  //指定内容面板高度
    aWidth: 850,  //指定内容面板宽度
    hideScroll: true,
    layoutFile: 'monitorlaborcostandothershareexpense-input-data-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }
});