Ext.define('Weeklyreceipt.view.WeeklyreceiptView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/weeklyreceipt',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'weeklyreceipt-query-layout.xml',
	editLayoutFile: 'weeklyreceipt-edit-layout.xml',
	//extraLayoutFile: 'weeklyreceipt-extra-layout.xml',
	bindings: ['prmWeeklyReceiptDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
	needSplitPage: true,
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
	}
});