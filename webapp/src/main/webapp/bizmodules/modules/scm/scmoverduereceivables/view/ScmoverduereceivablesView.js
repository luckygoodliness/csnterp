Ext.define('Scmoverduereceivables.view.ScmoverduereceivablesView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmoverduereceivables',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 160,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmoverduereceivables-query-layout.xml',
	editLayoutFile: 'scmoverduereceivables-edit-layout.xml',
	//extraLayoutFile: 'scmoverduereceivables-extra-layout.xml',
	bindings: ['scmOverdueReceivablesDto'],
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