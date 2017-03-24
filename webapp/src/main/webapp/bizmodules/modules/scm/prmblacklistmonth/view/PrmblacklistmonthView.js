Ext.define('Prmblacklistmonth.view.PrmblacklistmonthView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/prmblacklistmonth',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'prmblacklistmonth-query-layout.xml',
	editLayoutFile: 'prmblacklistmonth-edit-layout.xml',
	//extraLayoutFile: 'prmblacklistmonth-extra-layout.xml',
	bindings: ['prmBlacklistMonthDto'],
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