Ext.define('PrmReport.view.PrmContractPandectView', {
    extend: 'Scdp.mvc.AbstractReportView',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/report',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'prmContractPandect-query-layout.xml',
	//extraLayoutFile: 'balanceofcontract-extra-layout.xml',
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
		me.getCmp("conditionPanel->office").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
	}
});