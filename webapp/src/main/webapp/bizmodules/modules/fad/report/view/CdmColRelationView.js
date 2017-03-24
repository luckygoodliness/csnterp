Ext.define('FadReport.view.CdmColRelationView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/report',
    cpHeight: 80,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'cdmcolrelation-query-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }
});