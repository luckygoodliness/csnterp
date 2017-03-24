Ext.define("ContractE.controller.ContractEController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'ContractE.view.ContractEView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'cdmFileRelationDto->fileDownload', name: 'click', fn: 'fnFileDownload'},
        {cid: 'cdmFileRelationDto->filePreview', name: 'click', fn: 'fnFilePreview'},
        {cid: 'prmContractCDto->customerId', name: 'blur', fn: 'customerIdBlur'},
        {cid: 'prmContractCDto->projectSourceType', name: 'blur', fn: 'changeProjectSourceType'},
        {cid: 'prmContractCDto->contractSignMoney', name: 'change', fn: 'changeIsMajor'},
        {cid: "prmContractCDto->taxType", name: "change", fn: "changeTaxType"},
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
        queryController.view.getConditionPanel().queryExtraParams = {pickModule: 'pickForContractC'};

    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmContractCDto->countryCode").sotValue("CN");
        view.getCmp("prmContractCDto->contractorOffice").putValue(Erp.Util.getCurrentDeptCode());
        view.getCmp("prmContractCDto->state").sotValue("0");
        view.getCmp("prmContractCDto->contractStatus").sotValue("REVISE");
        view.getCmp("cdmFileRelationDto").sotValue(null);
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
        var operateBusinessId = view.getCmp("prmContractCDto->operateBusinessBidInfoId");
        if (Scdp.ObjUtil.isNotEmpty(operateBusinessId.gotValue())) {
            operateBusinessId.sotEditable(false);
        }
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
//        me.changeIsMajor();
        return me.validateHeader();
    },
    validateHeader: function () {
        var me = this;
        var view = me.view;

        var countryCode = view.getCmp("prmContractCDto->countryCode").gotValue();
        var buildRegion = view.getCmp("prmContractCDto->buildRegion").gotValue();
        var contractSignDate = view.getCmp("prmContractCDto->contractSignDate").gotValue();
        var contractStartDate = view.getCmp("prmContractCDto->contractStartDate").gotValue();
        var contractEndDate = view.getCmp("prmContractCDto->contractEndDate").gotValue();
        var successBidDate = view.getCmp("prmContractCDto->successBidDate").gotValue();
        var projectManager = view.getCmp("prmContractCDto->projectManager").gotValue();
        var generalEngineer = view.getCmp("prmContractCDto->generalEngineer").gotValue();

        if ("CN" == countryCode && Scdp.ObjUtil.isEmpty(buildRegion)) {
            Scdp.MsgUtil.info('建设地点国家为中国时，建设省市不能为空！');
            return false;
        }
        if (Scdp.ObjUtil.isNotEmpty(successBidDate) && Scdp.ObjUtil.isNotEmpty(contractSignDate)) {
            if (successBidDate > contractSignDate) {
                Scdp.MsgUtil.info('合同中标时间不能晚于合同签订日期！');
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(successBidDate) && Scdp.ObjUtil.isNotEmpty(contractEndDate)) {
            if (successBidDate > contractEndDate) {
                Scdp.MsgUtil.info('合同中标时间不能晚于合同截止日期！');
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(contractStartDate) && Scdp.ObjUtil.isNotEmpty(contractEndDate)) {
            if (contractStartDate > contractEndDate) {
                Scdp.MsgUtil.info('合同生效日期不能晚于合同截止日期！');
                return false;
            }
        }
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
        me.view.afterChangeUIStatus();
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
    changeIsMajor: function () {
        var me = this;
        var view = me.view;
        var contractSignMoney = view.getCmp("prmContractCDto->contractSignMoney").gotValue();
        var deptCode = view.getCmp("prmContractCDto->contractorOffice").gotValue();
        var deptMajorMoney = Erp.Util.getDeptMajorProjectMoney(deptCode);
        var isMajorProject = view.getCmp("prmContractCDto->isMajorProject");
        if (Scdp.ObjUtil.isNotEmpty(deptMajorMoney) && contractSignMoney >= deptMajorMoney) {
            isMajorProject.sotValue(1);
        } else {
            isMajorProject.sotValue(0);
        }
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
    validateHeader: function () {
        var me = this;
        var view = me.view;

        var countryCode = view.getCmp("prmContractCDto->countryCode").gotValue();
        var buildRegion = view.getCmp("prmContractCDto->buildRegion").gotValue();
        var contractSignDate = view.getCmp("prmContractCDto->contractSignDate").gotValue();
        var contractStartDate = view.getCmp("prmContractCDto->contractStartDate").gotValue();
        var contractEndDate = view.getCmp("prmContractCDto->contractEndDate").gotValue();
        var successBidDate = view.getCmp("prmContractCDto->successBidDate").gotValue();

        if ("CN" == countryCode && Scdp.ObjUtil.isEmpty(buildRegion)) {
            Scdp.MsgUtil.info('建设地点国家为中国时，建设省市不能为空！');
            return false;
        }
        if (Scdp.ObjUtil.isNotEmpty(successBidDate) && Scdp.ObjUtil.isNotEmpty(contractSignDate)) {
            if (successBidDate > contractSignDate) {
                Scdp.MsgUtil.info('合同中标时间不能晚于合同签订日期！');
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(successBidDate) && Scdp.ObjUtil.isNotEmpty(contractEndDate)) {
            if (successBidDate > contractEndDate) {
                Scdp.MsgUtil.info('合同中标时间不能晚于合同截止日期！');
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(contractStartDate) && Scdp.ObjUtil.isNotEmpty(contractEndDate)) {
            if (contractStartDate > contractEndDate) {
                Scdp.MsgUtil.info('合同生效日期不能晚于合同截止日期！');
                return false;
            }
        }
        return true;
    },
    customerIdBlur: function () {
        var me = this;
        var customerId = me.view.getCmp("prmContractCDto->customerId").gotValue();
        var postData = {
            customerId: customerId
        };
        Scdp.doAction("contract-c-customerIdQuery", postData, function (result) {
            if (result.isInScdpOrg == true) {
                me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(true);
                me.view.getCmp("prmContractCDto->innerPurchaseReqId").allowBlank = false;
            }
            else {
                me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotValue(null);
                me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(false);
                me.view.getCmp("prmContractCDto->innerPurchaseReqId").allowBlank = true;
            }
        });
    },
    changeProjectSourceType: function () {
        var me = this;
        var view = me.view;
        var projectSourceTypeCmp = view.getCmp("prmContractCDto->projectSourceType");
        if (projectSourceTypeCmp.isChanged()) {
            var value = projectSourceTypeCmp.gotValue();
            if ("4" == value) {
                view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(true);
                view.getCmp("prmContractCDto->innerPurchaseReqId").allowBlank = false;
            } else {
                view.getCmp("prmContractCDto->innerPurchaseReqId").sotValue(null);
                view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(false);
                view.getCmp("prmContractCDto->innerPurchaseReqId").allowBlank = true;
            }
        }
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        if (Erp.ArrayUtil.anyContains(me.workFlowFormData, "wf_erp_enable_taxType=1")) {
            var taxType = view.getCmp("prmContractCDto->taxType").gotValue();
            if (Scdp.ObjUtil.isEmpty(taxType)) {
                Scdp.MsgUtil.warn("提交前，请补充税款类别！");
                return false;
            }
        }
        return true;
    },
    refreshUIStatusBasedOnWorkFlow: function (result) {
        var me = this;
        var view = me.view;
        this.callParent(arguments);
        var wfData = result.wf_formdata;
        if (Scdp.Const.UI_INFO_STATUS_VIEW == view.getUIStatus()) {
            if (Erp.ArrayUtil.anyContains(wfData, "wf_erp_enable_modifyBtn=1")) {
                view.getCmp("editPanel->modifyBtn").setDisabled(false);
            }
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmContractCDto->contractorOffice').gotValue();
        return processDeptCode;
    },
    changeTaxType: function () {
        var me = this;
        var taxTypeCmp = me.view.getCmp("prmContractCDto->taxType");
        var taxType = taxTypeCmp.gotValue();
        if ("PP" == taxType) {
            Scdp.MsgUtil.warn("从2016年5月1日起，全面启用营改增，取消普票！");
            taxTypeCmp.suspendEvent("change");
            taxTypeCmp.sotValue(null);
            taxTypeCmp.resumeEvent("change");
        }
    },
    pickBidInfoId: function () {
        var me = this;
        var view = me.view;
    }
});
