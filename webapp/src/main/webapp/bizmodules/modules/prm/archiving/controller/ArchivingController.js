Ext.define("Archiving.controller.ArchivingController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Archiving.view.ArchivingView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'excelSubmit', name: 'click', fn: 'clickedExecelSubmit'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.archiving.dto.PrmArchivingDto',
    queryAction: 'archiving-query',
    loadAction: 'archiving-load',
    addAction: 'archiving-add',
    modifyAction: 'archiving-modify',
    deleteAction: 'archiving-delete',
    exportXlsAction: "archiving-exportxls",
    //M3_C23_F1_归档明细导入
    clickedExecelSubmit : function(){
        var me = this;
        var formView = Ext.widget("JForm", {
            height: 150,
            width: 500,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            cid: 'uploadForm',
            items: [
                {
                    xtype: 'JFile',
                    fieldLabel: '上传文件名',
                    cid: 'uploadFile'
                }
            ]
        });
        var callBack = function (subView) {
            var uploadFile = subView.getCmp("uploadFile");
            if (Scdp.ObjUtil.isEmpty(uploadFile.gotValue())) {
                Scdp.MsgUtil.info("Please select file!");
                return;
            }
            Scdp.doAction("archiving-importexcel", null, function (result) {
                if (result.succ) {
                    me.view.getCmp("prmArchivingDetailDto").addRowItems(result.resData);
                } else {
                    Erp.Util.showLogView(Erp.I18N.EXCEL_UPLOAD_FAILURE + Erp.Const.BREAK_LINE + result.errorMsgLog);
                }
                win.close();
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', 'Excel导入', "上传文件");
    },
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmArchivingDto->state").sotValue("0");
        view.getCmp("prmArchivingDto->archivingReqDate").sotValue(new Date());
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmArchivingDto->state").sotValue("0");
        view.getCmp("prmArchivingDto->archivingReqDate").sotValue(new Date());
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
    },
    beforeBatchDel: function () {
        var me = this;
        var view = me.view;
        var resultPanel = view.getCmp('resultPanel');
        var selection = resultPanel.getSelectionModel().getSelection();
        var isExist = false;
        Ext.Array.each(selection, function (item) {
            var state = item.get("state");
            if("新增"!=state){
                Scdp.MsgUtil.warn("您不能删除非新增状态的项目归档！");
                isExist = true;
                return;
            }
        });
        return !isExist;
    },
    afterBatchDel: function () {
        var me = this;
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
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData);
    },
    //文件下载
    fileDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    //文件删除
    fileDeleteBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid);
    },
    loadWorkFlowProcessDeptCode:function(){
        var me = this;
        var processDeptCode =  me.view.getCmp('prmArchivingDto->prmContractorOffice').gotValue();
        return processDeptCode;
    }
});