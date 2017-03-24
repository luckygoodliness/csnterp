Ext.define("Supplierinfochange.controller.FadCustomToSupplierController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Supplierinfochange.view.FadCustomToSupplierView',
    uniqueValidateFields: [],
	extraEvents: [
		{cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
		{cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
		{cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
		{cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
		{cid: 'fileUploadC', name: 'click', fn: 'fileUploadCBtn'},
		{cid: 'fileDownloadC', name: 'click', fn: 'fileDownloadCBtn'},
		{cid: 'filePreviewC', name: 'click', fn: 'filePreviewCBtn'},
		{cid: 'fileDeleteC', name: 'click', fn: 'fileDeleteCBtn'},
		{cid: 'scmSupplierCDto->supplierGenre', name: 'change', fn: 'supplierGenreChange'},
		{cid: 'scmSupplierCDto->completeName', name: 'blur', fn: 'supplierNameValidate'},
		{cid: 'scmSupplierCDto->simpleName', name: 'blur', fn: 'supplierNameValidate'}
		//{cid: 'approval', name: 'click', fn: 'approval'}
	],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.supplierinfochange.dto.ScmSupplierCDto',
	queryAction: 'supplierinfochange-query',
	loadAction: 'supplierinfochange-load',
	addAction: 'supplierinfochange-add',
	modifyAction: 'supplierinfochange-modify',
	deleteAction: 'supplierinfochange-delete',
    exportXlsAction:"supplierinfochange-exportxls",
	afterInit: function () {
		var me = this;
		me.callParent(arguments);
		//me.doQuery();
		me.controlFileButton();
	},
	beforeAdd: function () {
		var me = this;
		return true;
	},
	doAdd: function () {
		var me = this;
		var view = me.view;
		var callBack = function (subView) {
			var grid = subView.getQueryPanel().getCmp("resultPanel");
			var selectedRecords = grid.getSelectionModel().getSelection();
			if (selectedRecords.length >= 1) {
				var uuid = selectedRecords[0].get("uuid");
				var postData = {};
				postData.uuid = uuid;
				Scdp.doAction("supplierinfochange-supplierinfochange", postData, function (result) {
					view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
					view.getCmp("query\x26edit").setActiveTab(me.view.getEditPanel());
					view.sotValue(result, true);
					view.getCmp("scmSupplierCDto->changeType").sotValue("1");
					view.getCmp("scmSupplierCDto->state").sotValue("0");
					me.controlFileButton();
				}, null, true);
				return true;
			} else if (selectedRecords.length == 0) {
				Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
				return false;
			} else {
				return true;
			}
		};
		var queryController = Ext.create("Supplierinfor.controller.PickSupplierQueryController");
		var param = {
			changeType: "1",

		};
		queryController.actionParams = param;
		Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
	},
	afterAdd: function () {
		var me = this;
		me.view.getCmp("scmSupplierCDto->taxTypes").sotValue("0");
		me.view.getCmp("scmSupplierCDto->supplierGenre").sotValue("4");
		me.view.getCmp("scmSupplierCDto->state").sotValue("0");
		me.controlFileButton();

	},
	beforeCopyAdd: function () {
		var me = this;
		return true;
	},
	afterCopyAdd: function () {
		var me = this;
		me.controlFileButton();
	},
	beforeModify: function () {
		var me = this;
		return true;
	},
	afterModify: function () {
		var me = this;
		//var supplierGenre=me.view.getCmp("scmSupplierDto->supplierGenre");
		//if (Scdp.ObjUtil.isEmpty(supplierGenre.gotValue())) {
		//    supplierGenre.sotValue("1");
		//}
		me.controlFileButton();
	},
	beforeSave: function () {
		var me = this;
		return true;
	},
	afterSave: function (retData) {
		var me = this;
		me.callParent(arguments);
		if (Scdp.ObjUtil.isNotEmpty(retData.supplierCode)) {
			me.view.getCmp("scmSupplierCDto->supplierCode").sotValue(retData.supplierCode);
		}
		//me.view.getCmp("query\x26edit").setActiveTab(me.view.getQueryPanel());
		me.controlFileButton();
	},
	beforeLoadItem: function () {
		var me = this;
		return true;
	},
	afterLoadItem: function () {
		var me = this;
		me.callParent(arguments);
		me.controlFileButton();

	},
	beforeCancel: function () {
		var me = this;
		return true;
	},
	afterCancel: function () {
		var me = this;
		me.callParent(arguments);
		me.controlFileButton();

	},
	beforeDelete: function () {
		var me = this;
		return true;
	},
	afterDelete: function () {
		var me = this;
		//me.view.getCmp("query\x26edit").setActiveTab(me.view.getQueryPanel());
		me.controlFileButton();
	},
	beforeBatchDel: function () {
		var me = this;
		return true;
	},
	afterBatchDel: function () {
		var me = this;
		me.controlFileButton();
	},
	beforeExport: function () {
		var me = this;
		return true;
	},
	afterExport: function () {
		var me = this;

	},

	beforeWorkFlowCommit: function () {
		var me = this;
		var view = me.view;
		var fileGrid = view.getCmp("cdmFileRelationCDto");
		if (fileGrid.getStore().getCount() == 0) {
			Scdp.MsgUtil.warn("请上传附件作为变更说明文件，谢谢！");
			return false;
		}
		return true;
	},
		//文件上传
	fileUploadBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationDto");
		var fileClassify = "CLASSIFY_FOR_SCM_SUPPLIER";
		Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData);
	},
	//文件下载
	fileDownloadBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationDto");
		Erp.FileUtil.erpFileDownLoad(grid);
	},
	//文件预览
	filePreviewBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationDto");
		Erp.FileUtil.erpFilePreview(grid);
	},
	//文件删除
	fileDeleteBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationDto");
		Erp.FileUtil.erpFileDelete(grid);
	},
	//文件上传
	fileUploadCBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationCDto");
		var fileClassify = "SCM_SUPPLIER_CHANGE";
		var fileObjConfig = {};
		fileObjConfig.regex = /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)(\w)?)$/i;
		fileObjConfig.regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt';
		Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData, null, fileObjConfig);
	},
	//文件下载
	fileDownloadCBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationCDto");
		Erp.FileUtil.erpFileDownLoad(grid);
	},
	//文件预览
	filePreviewCBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationCDto");
		Erp.FileUtil.erpFilePreview(grid);
	},
	//文件删除
	fileDeleteCBtn: function () {
		var me = this;
		var grid = me.view.getCmp("cdmFileRelationCDto");
		Erp.FileUtil.erpFileDelete(grid);
	},
	//文件按钮的禁用启用
	controlFileButton: function () {
		var me = this;
		var status = Ext.getCmp("editStatus").getValue();
		if (Scdp.ObjUtil.isEmpty(status)) {
			me.view.getCmp("fileUpload").setDisabled(true);
			me.view.getCmp("fileDownload").setDisabled(true);
			me.view.getCmp("fileDelete").setDisabled(true);
			me.view.getCmp("fileUploadC").setDisabled(true);
			me.view.getCmp("fileDownloadC").setDisabled(true);
			me.view.getCmp("fileDeleteC").setDisabled(true);
			return false;
		}
		if (status == Scdp.I18N.VIEW_STATUS) {
			me.view.getCmp("fileUpload").setDisabled(true);
			me.view.getCmp("fileDelete").setDisabled(true);
			me.view.getCmp("fileDownload").setDisabled(false);
			me.view.getCmp("fileUploadC").setDisabled(true);
			me.view.getCmp("fileDeleteC").setDisabled(true);
			me.view.getCmp("fileDownloadC").setDisabled(false);
		} else if (status == Scdp.I18N.MODIFY_STATUS || status == Scdp.I18N.ADD_STATUS) {
			me.view.getCmp("fileUpload").setDisabled(false);
			me.view.getCmp("fileDownload").setDisabled(false);
			me.view.getCmp("fileDelete").setDisabled(false);
			me.view.getCmp("fileUploadC").setDisabled(false);
			me.view.getCmp("fileDownloadC").setDisabled(false);
			me.view.getCmp("fileDeleteC").setDisabled(false);
		}
	},
	refreshUIStatusBasedOnWorkFlow: function (returnData) {
		var me = this;
	},

	supplierNameValidate: function () {
		var me = this;
		var completeName = me.view.getCmp('scmSupplierCDto->completeName').gotValue();
		var simpleName = me.view.getCmp('scmSupplierCDto->simpleName').gotValue();
		var supplierCode = me.view.getCmp('scmSupplierCDto->supplierCode').gotValue();
		var postData = {completeName: completeName,simpleName:simpleName,supplierCode:supplierCode};
		Scdp.doAction("supplierinfochange-supplierNameValidate", postData, function () {
		});
	},
	//approval: function () {
	//	var me = this;
	//	var uuid = me.view.getCmp('scmSupplierCDto->uuid').gotValue();
	//	if (uuid == "") {
	//		Scdp.MsgUtil.info("未选择数据");
	//		return false;
	//	}
	//	var postData = {uuid: uuid};
	//	Scdp.doAction("supplierchange-supplierchange", postData, function () {
	//	});
	//},
	supplierGenreChange:function(){
		var me = this;
		var view = me.view;
		var supplierGenreCmp = view.getCmp("scmSupplierCDto->supplierGenre");
		var supplierGenre = supplierGenreCmp.gotValue();
		if (supplierGenre == "0") {
			Scdp.MsgUtil.info("合格供方只能通过合格供方审批产生！");
			supplierGenreCmp.sotValue("");
			return;
		}
		if (supplierGenre == "0" || supplierGenre == "1") {//如果供方类型为合格供方、普通供方，税务登记号与纳税人类型为必填
			view.getCmp("scmSupplierCDto->taxRegistrationNo").allowBlank = false;
			view.getCmp("scmSupplierCDto->taxTypes").allowBlank = false;
		} else {
			view.getCmp("scmSupplierCDto->taxRegistrationNo").allowBlank = true;
			view.getCmp("scmSupplierCDto->taxTypes").allowBlank = true;
		}
	}
})
;