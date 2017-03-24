Ext.define('Supplierinfochange.view.SupplierinfochangeView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/supplierinfochange',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'supplierinfochange-query-layout.xml',
	editLayoutFile: 'supplierinfochange-edit-layout.xml',
	//extraLayoutFile: 'supplierinfochange-extra-layout.xml',
	bindings: ['scmSupplierCDto','scmSupplierCDto.scmSupplierBankCDto','scmSupplierCDto.scmSupplierContactsCDto','scmSupplierCDto.cdmFileRelationDto','scmSupplierCDto.cdmFileRelationCDto','scmSupplierCDto.scmSukpplierKeyCDto'],
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
		var editToolbar = me.getEditToolbar();
		//editToolbar.add({
		//	text: '审批功能',
		//	cid: 'approval',
		//	iconCls: 'temp_icon_16'
		//});
		var departMent = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
		if (departMent == "计划财务部") {
			me.getCmp("scmSupplierCDto->taxRegistrationNo").allowBlank = true;
			//me.getCmp("scmSupplierCDto->taxRegistrationNo").labelEl.dom.innerHTML = me.getCmp("scmSupplierDto->taxRegistrationNo").labelEl.dom.innerHTML.replace('*', '');
			me.getCmp("scmSupplierCDto->taxTypes").allowBlank = true;
			//me.getCmp("scmSupplierCDto->taxTypes").labelEl.dom.innerHTML = me.getCmp("scmSupplierDto->taxTypes").labelEl.dom.innerHTML.replace('*', '');
		}
		me.getCmp("conditionPanel->changeType").sotValue("0");

	}
});