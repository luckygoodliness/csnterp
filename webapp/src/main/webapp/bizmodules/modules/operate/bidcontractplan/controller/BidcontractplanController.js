Ext.define("Bidcontractplan.controller.BidcontractplanController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Bidcontractplan.view.BidcontractplanView',
    uniqueValidateFields: ['contractName'],
    extraEvents: [
        {cid: 'cdmFileRelationDto->fileDownload', name: 'click', fn: 'fnFileDownload'}
    ],

    dtoClass: 'com.csnt.scdp.bizmodules.modules.operate.businessbidinfo.dto.OperateBusinessBidInfoDto',
    queryAction: 'bidcontractplan-query',
    loadAction: 'bidcontractplan-load',
    addAction: 'bidcontractplan-add',
    modifyAction: 'bidcontractplan-modify',
    deleteAction: 'bidcontractplan-delete',
    exportXlsAction: "bidcontractplan-exportxls",
    afterInit: function () {
        var me = this;
        this.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("operateBusinessBidInfoDto->projectType").sotValue("2");
        view.getCmp("operateBusinessBidInfoDto->projectSourceType").sotValue("1");
        view.getCmp("operateBusinessBidInfoDto->countryCode").sotValue("CN");
        view.getCmp("operateBusinessBidInfoDto->contractorOffice").putValue(Erp.Util.getCurrentDeptCode());
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
        var view = me.view;
        var canBeModified = view.getCmp("operateBusinessBidInfoDto->canBeModified").gotValue();
        if (canBeModified === "0") {
            Scdp.MsgUtil.warn("该合同协议草案已在合同新增中被引用，且新增合同已生效或在流程中，无法修改!")
            return false;
        }
        return true;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        return me.validateDate();
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
        me.callParent(arguments);
        var view = me.view;
        var canBeDeleteV = view.getCmp("operateBusinessBidInfoDto->canBeDeleted").gotValue();
        var canBeDeleted = Scdp.ObjUtil.isEmpty(canBeDeleteV) || canBeDeleteV == 1;
        view.getCmp("editPanel->deleteBtn").setDisabled(!canBeDeleted);

        var canBeModifiedV = view.getCmp("operateBusinessBidInfoDto->canBeModified").gotValue();
        var canBeModified = Scdp.ObjUtil.isEmpty(canBeModifiedV) || canBeModifiedV == 1;
        view.getCmp("editPanel->modifyBtn").setDisabled(!canBeModified);
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
        var view = me.view;
        var canBeDeleted = view.getCmp("operateBusinessBidInfoDto->canBeDeleted").gotValue();
        if (canBeDeleted === "0") {
            Scdp.MsgUtil.warn("该合同协议草案已在合同新增中被引用，无法删除!")
            return false;
        }
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
    //文件下载
    fnFileDownload: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    validateDate: function () {
        var me = this;
        var view = me.view;

        return true;
    }
});