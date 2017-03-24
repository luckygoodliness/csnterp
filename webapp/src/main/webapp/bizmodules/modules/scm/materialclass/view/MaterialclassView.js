Ext.define('Materialclass.view.MaterialclassView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/materialclass',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 220,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'materialclass-query-layout.xml',
	editLayoutFile: 'materialclass-edit-layout.xml',
	//extraLayoutFile: 'materialclass-extra-layout.xml',
	bindings: ['scmMaterialClassDto','scmMaterialClassDto.scmMaterialKeyDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
	needSplitPage: true,
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
		var batchDelBtn =me.getCmp("batchDelBtn");
		batchDelBtn.hidden=false;
		batchDelBtn.text="批量删除";
	},
	afterInit: function () {
		var me = this;
	}
});