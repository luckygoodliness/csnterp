Ext.define('Contract.view.ContractView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/contract',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 125,
	//epHeight: 220,
	//epiHeight: 1500,
	//xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'contract-query-layout.xml',
	editLayoutFile: 'contract-edit-layout.xml',
	//extraLayoutFile: 'contract-extra-layout.xml',
	bindings: ['prmContractDto','prmContractDto.cdmFileRelationDto'],
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
        me.getCmp("prmContractDto->bidInfoId").setDisabled(true);
		me.initToolbar();
		me.initFileGrid();
	},
	UIStatusChanged: function (view, newStatus) {
		view.getCmp("editPanel->addNew2Btn").setVisible(false);
		view.getCmp("editPanel->modifyBtn").setVisible(false);

	},
	initToolbar: function () {
		var me = this;
		me.getCmp("editPanel->copyAddBtn").setVisible(false);
		me.getCmp("queryPanel->addNew1Btn").setVisible(false);
		me.getCmp("queryPanel->batchDelBtn").setVisible(false);
        me.getEditToolbar().add(
            {
                text: '确认',
                cid: 'btnConfirmedDate',
                iconCls: 'temp_icon_16'
            });
        me.getEditToolbar().add(
            {
                text: '考核时间修正',
                cid: 'btnExamDate',
                iconCls: 'erp-examDate'
            });
	},
	initFileGrid: function () {
		var me = this;
		var fileGrid = me.getCmp("cdmFileRelationDto");
		var fileToolbar = fileGrid.getEditToolbar();

		me.getCmp("cdmFileRelationDto->rowMoveTopBtn").setVisible(false);
		me.getCmp("cdmFileRelationDto->rowMoveBottomBtn").setVisible(false);
		me.getCmp("cdmFileRelationDto->rowMoveUpBtn").setVisible(false);
		me.getCmp("cdmFileRelationDto->rowMoveDownBtn").setVisible(false);

        me.getCmp("editPanel->deleteBtn").setVisible(false);
        me.getCmp("editPanel->cancelBtn").setVisible(false);
        me.getCmp("editPanel->saveBtn").setVisible(false);


		fileToolbar.add({
			xtype: 'button',
			cid: 'fileDownload',
			iconCls: 'file_download_icon'
		});

		fileGrid.doAddRow = function () {
			var me = this;
			Erp.FileUtil.erpFileUpload(me);
		};
	}
});