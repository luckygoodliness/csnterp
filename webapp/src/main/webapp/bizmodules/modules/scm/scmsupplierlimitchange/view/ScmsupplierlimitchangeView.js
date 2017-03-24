Ext.define('Scmsupplierlimitchange.view.ScmsupplierlimitchangeView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsupplierlimitchange',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmsupplierlimitchange-query-layout.xml',
	editLayoutFile: 'scmsupplierlimitchange-edit-layout.xml',
	//extraLayoutFile: 'scmsupplierlimitchange-extra-layout.xml',
	bindings: ['scmSupplierLimitChangeDto','scmSupplierLimitChangeDto.scmSupplierLimitChangeDDto'],
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
		me.resetDetailToolbar();
		var editToolbar = me.getEditToolbar();
		me.scmContractGridInit(me);
		//editToolbar.add({
		//	text: '审批功能',
		//	cid: 'approval',
		//	iconCls: 'temp_icon_16'
		//});
	},
	scmContractGridInit: function (view) {
		openScmContract = function (uuid) {
			var postData = {};
			postData.erp_msg_business_obj ={uuid:uuid};
			var menuCode = "CONTRACTESTABLISHMENT";
			Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
		};

		var scmContractGrid = view.getCmp('scmContractDto');
		var  scmContractGridColumns = scmContractGrid.getColumnBydataIndex("scmContractCode");
		scmContractGridColumns.renderer = function (value, p, record) {
			return Ext.String.format(
				'<a href="javascript:openScmContract(\'' + record.data.uuid + '\');" target="_blank" style="color: blue">' + value + '</a>'
			);
		};
	},
	resetDetailToolbar: function () {
		var me = this;
		var supplierLimitDetailGrid = me.getCmp("scmSupplierLimitChangeDDto");
			supplierLimitDetailGrid.doAddRow = function (model) {
			var supplierLimitDetailController = Scdp.getActiveModule().controller;
			supplierLimitDetailController.pickContract();
		};
		//    contractInvoiceGrid.afterDeleteRow = function (model) {
		//        var contractInvoiceController = Scdp.getActiveModule().controller;
		//        contractInvoiceController.afterDeleteRow();
		//};
		supplierLimitDetailGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		supplierLimitDetailGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		supplierLimitDetailGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		supplierLimitDetailGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		supplierLimitDetailGrid.getCmp("toolbar->copyAddRowBtn").hide();


	}
});