Ext.define("Maintain.controller.MaintainController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Maintain.view.MaintainView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'formPrint', name: 'click', fn: 'formPrintFn'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.asset.maintain.dto.AssetMaintainDto',
    queryAction: 'maintain-query',
    loadAction: 'maintain-load',
    addAction: 'maintain-add',
    modifyAction: 'maintain-modify',
    deleteAction: 'maintain-delete',
    exportXlsAction: "maintain-exportxls",
    afterInit: function () {
        var me = this;
        me.controlFileButton();
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("assetMaintainDto->operater").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        me.view.getCmp("assetMaintainDto->operateTime").sotValue(new Date());
        me.view.getCmp('assetMaintainDto->state').sotValue('0');
        me.controlFileButton();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp("assetMaintainDto->maintainApplycode").sotValue(null);
        me.view.getCmp("assetMaintainDto->operater").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        me.view.getCmp("assetMaintainDto->operateTime").sotValue(new Date());
        me.view.getCmp('assetMaintainDto->state').sotValue('0');
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
//        var state=me.view.getCmp('state').gotValue();
//        if(state!="" && state=='N') {
//            //me.view.getCmp('state').sotValue('Y');
//        }else if(state==null){
//            //me.view.getCmp('state').sotValue('N');
//        }
//
//        me.view.getCmp('state').sotValue('0');
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(retData.maintainApplycode)) {
            me.view.getCmp("assetMaintainDto->maintainApplycode").sotValue(retData.maintainApplycode);
        }
        me.controlFileButton();
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        /*var selectedSubject = me.view.getResultPanel().getSelectionModel().getSelection();
         me.view.getCmp("assetName").sotValue(selectedSubject[0].data.assetName);*/
        var state = this.view.getCmp("assetMaintainDto->state").gotValue();
        if (state != '0') {
            me.view.getCmp('editToolbar->modifyBtn').setDisabled(true);
            me.view.getCmp('editToolbar->deleteBtn').setDisabled(true);
        }
        me.controlFileButton();
        me.callParent(arguments);
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.controlFileButton();
        me.callParent(arguments);
    },
    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var appState = view.getCmp("assetMaintainDto->state").gotValue();
        if (appState != 0) {
            Scdp.MsgUtil.info("您不能删除非新增状态的维修申请");
            return false;
        }
        return true;
    },
    afterDelete: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeBatchDel: function () {
        var me = this;
        var view = me.view;
        var resultPanel = view.getCmp('resultPanel');
        var selection = resultPanel.getSelectionModel().getSelection();
        var isExist = false;
        ;
        Ext.Array.each(selection, function (item) {
            var state = item.get("state");
            if ("新增" != state) {
                Scdp.MsgUtil.warn("您不能删除非新增状态的维修申请！");
                isExist = true;
                return;
            }
        });
        return !isExist;
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
    formPrintFn: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("assetMaintainDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        var param = {uuid: uuid};
        Scdp.printReport("erp/asset/AssetSBWXSQB.cpt", [param], false, "pdf");
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('assetMaintainDto->officeId').gotValue();
        return processDeptCode;
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
    }
});