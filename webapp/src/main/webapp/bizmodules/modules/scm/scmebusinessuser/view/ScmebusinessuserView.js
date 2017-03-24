Ext.define('Scmebusinessuser.view.ScmebusinessuserView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmebusinessuser',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 130,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmebusinessuser-query-layout.xml',
	editLayoutFile: 'scmebusinessuser-edit-layout.xml',
	//extraLayoutFile: 'scmebusinessuser-extra-layout.xml',
	bindings: ['scmEbusinessUserDto'],
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