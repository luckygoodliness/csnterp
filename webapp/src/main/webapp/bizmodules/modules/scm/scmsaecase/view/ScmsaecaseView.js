Ext.define('Scmsaecase.view.ScmsaecaseView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsaecase',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmsaecase-query-layout.xml',
	editLayoutFile: 'scmsaecase-edit-layout.xml',
	//extraLayoutFile: 'scmsaecase-extra-layout.xml',
	bindings: ['scmSaeCaseDto','scmSaeCaseDto.scmSaeCaseDDto'],
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
		//复制新增按钮隐藏
		me.getCmp("editPanel->copyAddBtn").hide();
		me.resetDetailToolbar();
	},
	resetDetailToolbar: function () {
		var me = this;
		var scmSaeCaseDetailGrid = me.getCmp("scmSaeCaseDDto");
		scmSaeCaseDetailGrid.beforeAddRow = function (model) {
			var num = scmSaeCaseDetailGrid.getStore().getCount();
			if (num >= 10) {
				Scdp.MsgUtil.info("新增失败！最多只能加10条", function(btn) {
					scmSaeCaseDetailGrid.getStore().removeAt(10);
				});
			}
		};

		scmSaeCaseDetailGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		scmSaeCaseDetailGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		scmSaeCaseDetailGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		scmSaeCaseDetailGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		scmSaeCaseDetailGrid.getCmp("toolbar->copyAddRowBtn").hide();
	}
});