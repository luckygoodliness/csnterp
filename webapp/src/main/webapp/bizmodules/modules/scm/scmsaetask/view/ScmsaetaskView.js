Ext.define('Scmsaetask.view.ScmsaetaskView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsaetask',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmsaetask-query-layout.xml',
	editLayoutFile: 'scmsaetask-edit-layout.xml',
	//extraLayoutFile: 'scmsaetask-extra-layout.xml',
	bindings: ['scmSaeTaskDto','scmSaeTaskDto.scmSaeFormDto'],
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
		me.getCmp("addNew1Btn").hide();
		me.getCmp("editPanel->addNew2Btn").hide();
		me.getCmp("editPanel->modifyBtn").setText("考评");
		//状态栏隐藏
		var statusBar = me.getCmp("scmSaeFormDto").statusBar;
		statusBar.hide();
		var editToolbar = me.getEditToolbar();
		editToolbar.add({
			text:'提交',
			cid:'taskCommit',
			iconCls:'temp_icon_16',
			//disabled:'true'
		});
		editToolbar.add({
			text:'撤回',
			cid:'taskCancel',
			iconCls:'temp_icon_16',
			//disabled:'true'
		});
		me.getCmp("editPanel->taskCancel").setDisabled(true);
		me.getCmp("editPanel->taskCommit").setDisabled(true);
		me.resetDetailToolbar();
	},
	resetDetailToolbar: function () {
		var me = this;
		var scmSaeFormGrid = me.getCmp("scmSaeFormDto");

		scmSaeFormGrid.getCmp("toolbar->addRowBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->delRowBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->copyAddRowBtn").hide();
	}
});