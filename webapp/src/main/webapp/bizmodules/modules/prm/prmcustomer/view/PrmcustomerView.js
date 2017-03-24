Ext.define('Prmcustomer.view.PrmcustomerView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmcustomer',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 70,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'prmcustomer-query-layout.xml',
	editLayoutFile: 'prmcustomer-edit-layout.xml',
	//extraLayoutFile: 'prmcustomer-extra-layout.xml',
	bindings: ['prmCustomerDto','prmCustomerDto.prmCustomerLinkmanDto','prmCustomerDto.prmCustomerBankDto'],
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