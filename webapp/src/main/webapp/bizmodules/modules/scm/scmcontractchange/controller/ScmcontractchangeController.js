Ext.define("Scmcontractchange.controller.ScmcontractchangeController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Scmcontractchange.view.ScmcontractchangeView',
    uniqueValidateFields: [],
    extraEvents: [
        //{cid: "submit", name: "click", fn: "submit"},
        //{cid: "approved", name: "click", fn: "approved"},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.dto.ScmContractChangeDto',
    queryAction: 'scmcontractchange-query',
    loadAction: 'scmcontractchange-load',
    addAction: 'scmcontractchange-add',
    modifyAction: 'scmcontractchange-modify',
    deleteAction: 'scmcontractchange-delete',
    exportXlsAction: "scmcontractchange-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        //me.doQuery();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);
        me.view.getCmp("scmContractChangeDto->closeChange").sotValue("0");
        me.view.getCmp("scmContractChangeDto->state").sotValue("0");
        me.view.getCmp("scmContractChangeDto->createBy").putValue(Scdp.getCurrentUserId());
        //if(me.view.getCmp("contractNature")!=null){
        //    //me.view.getCmp("contractNature").sotValue = "0";
        //}
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
    },
    beforeModify: function () {
        var me = this;
        //var curRecord = me.view.getResultPanel().getCurRecord();
        //var state = curRecord.data.state;
        //if (Scdp.ObjUtil.isNotEmpty(state)) {
        //    if ("审核通过" == state) {
        //        Scdp.MsgUtil.info("审核通过的记录不能修改！");
        //        return false;
        //    }
        //}
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);
    },
    beforeSave: function () {
        var me = this;
        //1.如果合同性质为采购，则变更后金额不应大于变更前金额
        //if(me.view.getCmp("contractNature").value=="0"){
        //    var moneyBeforeChange = me.view.getCmp("originalValue").value;
        //    var moneyAfterChange = me.view.getCmp("newValue").value;
        //    if(parseFloat(moneyAfterChange)>parseFloat(moneyBeforeChange)){
        //        Scdp.MsgUtil.info("采购合同变更后金额应小于变更前金额！");
        //        return false;
        //    }
        //}
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(retData.runningNo)) {
            me.view.getCmp("runningNo").sotValue(retData.runningNo);
        }
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        me.afterloadItemInit();
        //me.doQuery();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.afterloadItemInit();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
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
        return true;
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
        var fileClassify = "SCM_CONTRACT_CHANGE";
        var fileObjConfig = {};
        fileObjConfig.regex = /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)|(\.bmp)|(\.jpg)|(\.jpeg)|(\.gif)|(\.png)(\w)?)$/i;
        fileObjConfig.regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt,bmp,jpg,jpeg,gif,png';
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
    afterloadItemInit: function () {
        var me = this;
        var view = me.view;
        //var reportJPanel = view.getCmp("scmContractReport");
        var scmContractId = view.getCmp("scmContractChangeDto->scmContractId").gotValue();
        var url=Scdp.getSysConfig("base_path") + Scdp.getSysConfig("report_servlet") + '?reportlet=erp/scm/ScmContractSingleQuery.cpt&scmContractId=' + scmContractId;
        var fd = Ext.get('scmContractChangeReportForm');
        if (!fd) {
            fd = Ext.DomHelper.append(
                Ext.getBody(), {
                    tag: 'form',
                    method: 'post',
                    id: 'scmContractChangeReportForm',
                    action: url,
                    target: 'scmContractChangeReportIframe',
                    cls: 'x-hidden',
                    cn: [

                    ]
                }, true);

        } else {
            $(fd.dom).attr("action", url);
        }
        fd.dom.submit();
    }

});
