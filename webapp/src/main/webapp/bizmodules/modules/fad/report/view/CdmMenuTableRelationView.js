Ext.define('FadReport.view.CdmMenuTableRelationView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/report',
    cpHeight: 80,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'cdmmenutablerelation-query-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }
});