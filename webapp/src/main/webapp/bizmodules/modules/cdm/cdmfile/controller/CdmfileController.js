Ext.define("Cdmfile.controller.CdmfileController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Cdmfile.view.CdmfileView',
    uniqueValidateFields: [],
    extraEvents: [{cid: 'btnTemplateImport', name: 'click', fn: 'importTemplate'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.cdm.cdmfile.dto.CdmFileRelationDto',
    queryAction: 'cdmfile-query',
    loadAction: 'cdmfile-load',
    addAction: 'cdmfile-add',
    modifyAction: 'cdmfile-modify',
    deleteAction: 'cdmfile-delete',
    exportXlsAction: "cdmfile-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
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
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
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
    testFile:function(){
        var me =  this;
    },
    importTemplate: function(){
        var form = Ext.widget("JForm", {
            height: 120,
            width: 600,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JCombo',
                    comboType: 'scdp_fmcode',
                    codeType: 'CDM_FILE_TYPE',
                    allowBlank: false,
                    fieldLabel: '模板类别',
                    cid: 'cdmFileType',
                    valueField:'code',
                    forceSelection: false,
                    displayDesc:true,
                    fullInfo: true,
                    width: 100,
                    height: 21
                },
                {
                    xtype: 'JCombo',
                    comboType: 'scdp_fmcode',
                    codeType: 'CDM_FILE_TYPE_DETAIL',
                    allowBlank: false,
                    fieldLabel: '模板分类',
                    cid: 'fileClassify',
                    valueField:'code',
                    forceSelection: false,
                    displayDesc:true,
                    fullInfo: true,
                    width: 100,
                    height: 21
                },
                {
                    xtype: 'JFile',
                    fieldLabel: Erp.I18N.UPLOAD_FILE_NAME,
                    cid: 'uploadFile'
                }
            ]
        });
        var callBack = function (subView) {
            var uploadFile = subView.getCmp("uploadFile").gotValue();
            if (Scdp.ObjUtil.isEmpty(uploadFile)) {
                Scdp.MsgUtil.info("Please select file!");
                return;
            }
            var cdmFileType = subView.getCmp("cdmFileType").gotValue();
            var fileClassify = subView.getCmp("fileClassify").gotValue();
            var postData = {cdmFileType: cdmFileType};
            postData.fileClassify = fileClassify;
            postData.fileType = Scdp.StrUtil.replaceNull(Scdp.StrUtil.getLastSplit(uploadFile, ".")).toUpperCase();
            Scdp.doAction("template-fileupload", postData, function (result) {
                if(result.success){
                    win.close();
                    Scdp.MsgUtil.info(Erp.I18N.UPLAOD_SUCCESS);
                }
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', Erp.I18N.FILE_UPLOAD, Erp.I18N.UPLOAD_FILE);
    }
});