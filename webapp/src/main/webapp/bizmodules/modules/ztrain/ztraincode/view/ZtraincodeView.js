Ext.define('Ztraincode.view.ZtraincodeView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/ztrain/ztraincode',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'ztraincode-query-layout.xml',
	editLayoutFile: 'ztraincode-edit-layout.xml',
	//extraLayoutFile: 'ztraincode-extra-layout.xml',
	//bindings: ['testTableDto'],
	bindings: ['testTableDto','testTableDto.testTableContentDto'],
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