Ext.define('Prmprojectmainc.view.ReceiptsmainpcView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmprojectmainc',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 255,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'prm-projectmainc2-query-layout.xml',
	editLayoutFile: 'prm-receiptsmainpc-edit-layout.xml',
	//extraLayoutFile: 'receiptsmainpc-extra-layout.xml',
	bindings: ['prmProjectMainCDto', 'prmProjectMainCDto.prmReceiptsDetailPCDto'],
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
		me.getCmp("editPanel->copyAddBtn").setVisible(false);
		me.getCmp("conditionPanel->detailType").sotValue("PRM_RECEIPTS_DETAIL");
	},
	UIStatusChanged: function (view, newStatus) {
		view.getCmp("prmProjectMainCDto").sotEditable(false);
	}
});