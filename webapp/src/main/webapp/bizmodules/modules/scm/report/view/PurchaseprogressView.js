Ext.define('ScmReport.view.PurchaseprogressView', {
    extend: 'Scdp.mvc.AbstractReportView',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/report',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    allowNullConditions: false,
    queryLayoutFile: 'purchaseprogress-query-layout.xml',
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
	}
});