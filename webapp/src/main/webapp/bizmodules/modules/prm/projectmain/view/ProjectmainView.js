Ext.define('Projectmain.view.ProjectmainView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/projectmain',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	//epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'projectmain-query-layout.xml',
	editLayoutFile: 'projectmain-edit-layout.xml',
	//extraLayoutFile: 'projectmain-extra-layout.xml',
	bindings: ['prmProjectMainDto', 'prmProjectMainDto.prmAssociatedUnitsDto', 'prmProjectMainDto.prmMemberDetailPDto',
		'prmProjectMainDto.prmQsPDto', 'prmProjectMainDto.prmReceiptsDetailPDto', 'prmProjectMainDto.prmPayDetailPDto',
		'prmProjectMainDto.prmProgressDetailPDto', 'prmProjectMainDto.prmSquareDetailPDto', 'prmProjectMainDto.prmReceiptsDetailPDto',
		'prmProjectMainDto.prmAssociatedUnitsHisDto', 'prmProjectMainDto.prmMemberDetailPHisDto',
		'prmProjectMainDto.prmQsPHisDto', 'prmProjectMainDto.prmReceiptsDetailPHisDto', 'prmProjectMainDto.prmPayDetailPHisDto',
		'prmProjectMainDto.prmProgressDetailPHisDto', 'prmProjectMainDto.prmSquareDetailPHisDto', 'prmProjectMainDto.prmReceiptsDetailPHisDto',
		'prmProjectMainDto.prmBudgetRunDto', 'prmProjectMainDto.prmBudgetOutsourceDto', 'prmProjectMainDto.prmBudgetAccessoryDto',
		'prmProjectMainDto.prmBudgetPrincipalDto', 'prmProjectMainDto.prmBudgetDetailDto',
		'prmProjectMainDto.prmBudgetRunHisDto', 'prmProjectMainDto.prmBudgetOutsourceHisDto', 'prmProjectMainDto.prmBudgetAccessoryHisDto',
		'prmProjectMainDto.prmBudgetPrincipalHisDto', 'prmProjectMainDto.prmBudgetDetailHisDto',
		'prmProjectMainDto.innerProjectInfos', 'prmProjectMainDto.prmContractDetailDto'
	],
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
		me.getCmp("queryPanel->addNew1Btn").setVisible(false);
		me.getCmp("queryPanel->batchDelBtn").setVisible(false);
		var editToolbar = me.getEditToolbar();
		var buttons = editToolbar.query('[xtype=button]');
		Ext.Array.each(buttons, function (button) {
			button.setVisible(false);
		});
		//editToolbar.add({
		//	xtype: 'button',
		//	text: Erp.I18N.UPDATE_SIJI_NO,
		//	cid: 'updateSijiNo',
		//	iconCls: 'temp_icon_16',
		//	disabled: true
		//});
		editToolbar.add({
			xtype: 'button',
			text: '项目情况明细表',
			cid: 'btnShowReport2',
			iconCls: 'temp_icon_16',
			disabled: true
		});
		//M3_C3_F1_界面功能增加
		editToolbar.add({
			xtype: 'button',
			text: 'EXCEL导出',
			cid: 'btnExport',
			iconCls: 'x-btn-icon-el export_btn',
			disabled: false
		});

		var queryToolbar = me.getQueryToolbar();
		queryToolbar.add({
			xtype: 'button',
			text: '项目情况明细表',
			cid: 'btnShowReport1',
			iconCls: 'temp_icon_16',
			disabled: false
		});

		// M3_C3_F2_合同信息跳转
		me.prmContractDetailGridInit(me);

		me.getCmp("prmBudgetDetailHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmBudgetPrincipalHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmBudgetAccessoryHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmBudgetOutsourceHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmBudgetRunHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmAssociatedUnitsHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmMemberDetailPHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmPayDetailPHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmProgressDetailPHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmSquareDetailPHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmReceiptsDetailPHisDto").rowColorConfigFn = me.rowColorConfigFn;
		me.getCmp("prmQsPHisDto").rowColorConfigFn = me.rowColorConfigFn;
	},
	UIStatusChanged: function (view, newStatus) {
		//view.getCmp("editPanel->addNew2Btn").setDisabled(true);
		//view.getCmp("editPanel->modifyBtn").setDisabled(true);
		//view.getCmp("editPanel->deleteBtn").setDisabled(true);
		if (newStatus == Scdp.Const.UI_INFO_STATUS_NULL) {
			view.getCmp("projectBudgetHisTab->showBudgetHistoryBtn").setDisabled(true);
			view.getCmp("projectPlanHisTab->showPlanHistoryBtn").setDisabled(true);
		} else {
			view.getCmp("projectBudgetHisTab->showBudgetHistoryBtn").setDisabled(false);
			view.getCmp("projectPlanHisTab->showPlanHistoryBtn").setDisabled(false);
		}

		var updateSijiBtn = view.getCmp("editPanel->updateSijiNo");
		var btnShowReport2 = view.getCmp("editPanel->btnShowReport2");
		if (updateSijiBtn) {
			if (Scdp.Const.UI_INFO_STATUS_VIEW == newStatus) {
				updateSijiBtn.setDisabled(false);
			} else {
				updateSijiBtn.setDisabled(true);
			}
		}
		if (btnShowReport2) {
			if (Scdp.Const.UI_INFO_STATUS_VIEW == newStatus) {
				btnShowReport2.setDisabled(false);
			} else {
				btnShowReport2.setDisabled(true);
			}
		}
	},
	rowColorConfigFn: function (record) {
		if (record.get("changeStatus") == "变化") {
			return 'erp-grid-font-color-red';
		} else if (record.get("changeStatus") == "当前") {
			return 'erp-grid-font-color-blue';
		} else if (record.get("changeStatus") == "删除") {
			return 'erp-grid-row-delete';
		} else {
			return null;
		}
	},

	// M3_C3_F2_合同信息跳转
	prmContractDetailGridInit : function (view) {
        openPrmContract = function (uuid) {
			var postData = {};
			postData.erp_msg_business_obj ={uuid:uuid};
			var menuCode = "CONTRACT";
			Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
		};
		var prmContractDetailGrid = view.getCmp("prmContractDetailDto");
		var contractNameColumns = prmContractDetailGrid.getColumnBydataIndex("contractName");

		contractNameColumns.renderer = function (value, p, record) {
			return Ext.String.format(
				'<a href="javascript:openPrmContract(\'' + record.data.prmContractId + '\');" style="color: blue">' + value + '</a>'
			);
		};
	}

});