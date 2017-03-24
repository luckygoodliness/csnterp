Ext.define("Apply.controller.ApplyController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Apply.view.ApplyView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'singlePrint', name: 'click', fn: 'doSinglePrint'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.asset.apply.dto.AssetDiscardApplyDto',
    queryAction: 'apply-query',
    loadAction: 'apply-load',
    addAction: 'apply-add',
    modifyAction: 'apply-modify',
    deleteAction: 'apply-delete',
    exportXlsAction: "apply-exportxls",
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.getCmp('assetDiscardApplyDto->assetName').afterWinShowFn = function (controller, win) {
            win.down('IView').getCmp('cardNotUse').sotValue("1");
        };
        me.controlFileButton();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp('assetDiscardApplyDto->state').sotValue('0');
        me.view.getCmp('assetDiscardApplyDto->applyOfficeId').putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
        me.view.getCmp('assetDiscardApplyDto->applyUserCode').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        me.view.getCmp('assetDiscardApplyDto->applyUserName').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        me.view.getCmp('assetDiscardApplyDto->applyDate').sotValue(new Date());
        me.controlFileButton();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp('assetDiscardApplyDto->applyCode').sotValue(null);
        me.view.getCmp('assetDiscardApplyDto->state').sotValue('0');
        me.view.getCmp('assetDiscardApplyDto->applyOfficeId').putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
        me.view.getCmp('assetDiscardApplyDto->applyUserCode').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        me.view.getCmp('assetDiscardApplyDto->applyUserName').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        me.view.getCmp('assetDiscardApplyDto->applyDate').sotValue(new Date());
        me.controlFileButton();
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp('assetDiscardApplyDto->applyCode').sotValue(retData.applyCode);
        me.controlFileButton();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        var view = me.view;
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
        me.controlFileButton();
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "";
        var fileObjConfig = {};
        fileObjConfig.regex = /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)(\w)?)$/i;
        fileObjConfig.regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt';
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData, null, fileObjConfig);
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

    //文件按钮的禁用启用
    controlFileButton: function () {
        var me = this;
        var status = Ext.getCmp("editStatus").getValue();
        if (Scdp.ObjUtil.isEmpty(status)) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(true);
            me.view.getCmp("filePreview").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            return;
        }
        if (status == Scdp.I18N.VIEW_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("filePreview").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(true);
        } else if (status == Scdp.I18N.MODIFY_STATUS || status == Scdp.I18N.ADD_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(false);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("filePreview").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(false);
        }
    },
    collectMoreWorkFlowParamOnComplete: function () {
        var me = this;
        var localValue = me.view.getCmp('localValue').gotValue();
        var param = {localValue: localValue};
        return param;
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('assetDiscardApplyDto->applyOfficeId').gotValue();
        return processDeptCode;
    },
    doSinglePrint: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("assetDiscardApplyDto->uuid").gotValue();
        var param = {uuid: uuid};

        var deviceType = view.getCmp("assetDiscardApplyDto->deviceType").gotValue();
        if (deviceType == "RJ") {
            Scdp.printReport("erp/asset/无形资产报废申请表.cpt", [param], false, "pdf");
        }
        else {
            Scdp.printReport("erp/asset/固定资产报废申请表.cpt", [param], false, "pdf");
        }
    }
});