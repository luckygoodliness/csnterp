Ext.define('Scmsaeappraisercase.view.ScmsaeappraisercaseView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsaeappraisercase',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmsaeappraisercase-query-layout.xml',
	editLayoutFile: 'scmsaeappraisercase-edit-layout.xml',
	//extraLayoutFile: 'scmsaeappraisercase-extra-layout.xml',
	bindings: ['scmSaeAppraiserCaseDto','scmSaeAppraiserCaseDto.scmSaeAppraiserCaseDDto'],
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
		var scmSaeAppraiserCaseDetailGrid = me.getCmp("scmSaeAppraiserCaseDDto");

		scmSaeAppraiserCaseDetailGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		scmSaeAppraiserCaseDetailGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		scmSaeAppraiserCaseDetailGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		scmSaeAppraiserCaseDetailGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		scmSaeAppraiserCaseDetailGrid.getCmp("toolbar->copyAddRowBtn").hide();
	}
});