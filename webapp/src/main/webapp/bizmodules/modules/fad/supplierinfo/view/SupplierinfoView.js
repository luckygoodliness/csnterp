Ext.define('FadSupplierinfo.view.SupplierinfoView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/fad/supplierinfo',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'supplierinfo-query-layout.xml',
	editLayoutFile: 'supplierinfo-edit-layout.xml',
	//extraLayoutFile: 'supplierinfo-extra-layout.xml',
	bindings: ['fadSupplierDto','fadSupplierDto.scmSupplierBankDto'],
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
		//增加失效按钮
		var editToolbar = me.getEditToolbar();
		editToolbar.add({
			text: '设置NC编号',
			cid: 'updateNcCode_edit',
			iconCls: 'setting_btn'
			//disabled: "true"
		});
		var departMent = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
		if (departMent == "计划财务部") {
			me.getCmp("fadSupplierDto->taxRegistrationNo").allowBlank = true;
			me.getCmp("fadSupplierDto->taxRegistrationNo").labelEl.dom.innerHTML = me.getCmp("fadSupplierDto->taxRegistrationNo").labelEl.dom.innerHTML.replace('*', '');
			me.getCmp("fadSupplierDto->taxTypes").allowBlank = true;
			me.getCmp("fadSupplierDto->taxTypes").labelEl.dom.innerHTML = me.getCmp("fadSupplierDto->taxTypes").labelEl.dom.innerHTML.replace('*', '');
		}
	}
});