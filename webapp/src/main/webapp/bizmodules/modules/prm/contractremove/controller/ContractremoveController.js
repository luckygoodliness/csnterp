Ext.define("Contractremove.controller.ContractremoveController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Contractremove.view.ContractremoveView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'cdmFileRelationDto->fileDownload', name: 'click', fn: 'fnFileDownload'},
        {cid: 'cdmFileRelationDto->filePreview', name: 'click', fn: 'fnFilePreview'},
        {cid: 'bidInfoId', name: 'click', fn: 'pickBidInfoId'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.contractc.dto.PrmContractCDto',
    queryAction: 'contract-c-query',
    loadAction: 'contract-c-load',
    addAction: 'contract-c-add',
    modifyAction: 'contract-c-modify',
    deleteAction: 'contract-c-delete',
    exportXlsAction: "contract-c-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    doAdd: function () {
        var me = this;
        var view = me.view;
        var callBack = function (subView, isCancel) {
            var grid = subView.getQueryPanel().ownerCt.getCmp('resultPanel');
            var selectedRecords = grid.getSelectedRecords();
            if (selectedRecords.length > 0) {
                var rowData = selectedRecords[0].data;
                view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                view.getCmp("query\x26edit").setActiveTab(me.view.getEditPanel());
                me.afterAdd && me.afterAdd();
                view.getCmp("prmContractCDto->prmContractId").sotValue(rowData.uuid);
            }
            return true;
        };
        var queryController = Ext.create("Contract.controller.ContractQueryController");
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', null, null, null, 'SINGLE');
        queryController.view.getConditionPanel().queryExtraParams = {pickModule: 'pickForContractRemove'};

    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmContractCDto->countryCode").sotValue("CN");
        view.getCmp("prmContractCDto->state").sotValue("0");
        view.getCmp("prmContractCDto->contractStatus").sotValue("REMOVE");
        view.getCmp("cdmFileRelationDto").sotValue(null);
        var officeId = view.getCmp("prmContractCDto->contractorOffice").gotValue();
        if (officeId != null) {
            view.getCmp("prmContractCDto->affiliatedInstitutions").sotValue(officeId + "_DIRECTLY");
        }
    },
    doCopyAdd: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
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
    //文件预览
    fnFilePreview: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFilePreview(grid);
    },

    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        return true;
    },

    refreshUIStatusBasedOnWorkFlow: function (result) {
        var me = this;
        var view = me.view;
        this.callParent(arguments);
    },

    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmContractCDto->contractorOffice').gotValue();
        return processDeptCode;
    },

    pickBidInfoId: function () {
        var me = this;
        var view = me.view;
    }
});
