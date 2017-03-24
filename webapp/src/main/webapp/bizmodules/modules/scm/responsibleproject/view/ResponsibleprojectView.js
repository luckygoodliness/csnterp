Ext.define('Responsibleproject.view.ResponsibleprojectView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/responsibleproject',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 150,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'responsibleproject-query-layout.xml',
	editLayoutFile: 'responsibleproject-edit-layout.xml',
	//extraLayoutFile: 'responsibleproject-extra-layout.xml',
	bindings: ['scmResponsibleProjectDto'],
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