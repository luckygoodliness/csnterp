Ext.define('Nonprmpurchasereq.view.NonprmpurchasereqView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/nonprmpurchasereq',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'nonprmpurchasereq-query-layout.xml',
	editLayoutFile: 'nonprmpurchasereq-edit-layout.xml',
	//extraLayoutFile: 'nonprmpurchasereq-extra-layout.xml',
	bindings: ['prmPurchaseReqDto','prmPurchaseReqDto.prmPurchaseReqDetailDto','prmPurchaseReqDto.cdmFileRelationDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
	needSplitPage: true,
    workFlowDefinitionKey: 'Non_Prm_Purchase_Request',
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
        me.hideReqToolBar(me.getCmp("prmPurchaseReqDetailDto"));
        me.getCmp("editPanel->editToolbar->copyAddBtn").hide();
        me.getCmp("prmPurchaseReqDetailDto").getCmp("toolbar").add({
            cid: 'subUpload',
            tooltip: '上传附件',
            iconCls: 'file_upload_icon',
            disabled: "true"
        });
        me.getCmp("prmPurchaseReqDetailDto").getCmp("toolbar").add({
            cid: 'subDownload',
            tooltip: '下载附件',
            iconCls: 'file_download_icon'
            //disabled: "true"
        });
        me.getCmp("prmPurchaseReqDetailDto").getCmp("toolbar").add({
            cid: 'subDelfile',
            tooltip: '删除附件',
            iconCls: 'delete_btn',
            disabled: "true"
        });
	},
    hideReqToolBar: function (reqGrid) {
        reqGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        reqGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        reqGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        reqGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        reqGrid.getCmp("toolbar->copyAddRowBtn").hide();
//        reqGrid.getCmp("toolbar->addRowBtn").hide();
    }
});