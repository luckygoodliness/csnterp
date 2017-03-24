Ext.define('PrmReport.view.PrmPurchaseRequisitionDetailView', {
    extend: 'Scdp.mvc.AbstractReportView',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/report',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'prmPurchaseRequisitionDetail-query-layout.xml',
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
		me.getCmp("conditionPanel->office").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
	}
});