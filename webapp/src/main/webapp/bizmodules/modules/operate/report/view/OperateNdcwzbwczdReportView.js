Ext.define('OperateReport.view.OperateNdcwzbwczdReportView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/operate/report',
    cpHeight: 0,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'operateNdcwzbwczdReport-query-layout.xml',
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("conditionToolbar").hide();
    }
});