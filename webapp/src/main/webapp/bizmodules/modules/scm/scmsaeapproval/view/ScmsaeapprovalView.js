Ext.define('Scmsaeapproval.view.ScmsaeapprovalView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsaeapproval',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
	//xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmsaeapproval-query-layout.xml',
	editLayoutFile: 'scmsaeapproval-edit-layout.xml',
	//extraLayoutFile: 'scmsaeapproval-extra-layout.xml',
	bindings: ['scmSaeApprovalDto','scmSaeApprovalDto.scmSaeApprovalDDto','scmSaeApprovalDto.scmSaeObjectDto'],
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
		me.getCmp("editPanel->copyAddBtn").hide();
		var grid = me.getCmp("scmSaeApprovalDDto");
		grid.getCmp("toolbar->rowMoveTopBtn").hide();
		grid.getCmp("toolbar->rowMoveUpBtn").hide();
		grid.getCmp("toolbar->rowMoveDownBtn").hide();
		grid.getCmp("toolbar->rowMoveBottomBtn").hide();
		grid.getCmp("toolbar->copyAddRowBtn").hide();
		grid.doAddRow = function (model) {
			var controller = Scdp.getActiveModule().controller;
			controller.addFn();
		};
		grid.getCmp("toolbar").add({
			cid: 'addSupplierOfLevelC',
			tooltip: '添加C级供方',
			iconCls: 'green_add_btn',
			disabled: "true"
		});
	},

});