Ext.define('Fadsupplieradjust.view.FadsupplieradjustView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/fadsupplieradjust',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 150,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'fadsupplieradjust-query-layout.xml',
	editLayoutFile: 'fadsupplieradjust-edit-layout.xml',
	//extraLayoutFile: 'fadsupplieradjust-extra-layout.xml',
	bindings: ['fadSupplierAdjustDto'],
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