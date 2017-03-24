Ext.define("ContractC.controller.ContractCController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'ContractC.view.ContractCView',
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
        var callBack = function (subView) {
            var projectSourceType = subView.getCmp("projectSourceTypeSelect").gotValue();
            if (Scdp.ObjUtil.isEmpty(projectSourceType)) {
                Scdp.MsgUtil.warn("请选择项目来源！");
                return false;
            } else {
                view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                view.getCmp("query\x26edit").setActiveTab(me.view.getEditPanel());
                view.getCmp("prmContractCDto->projectSourceType").sotValue(projectSourceType);
                me.afterAdd && me.afterAdd();
                if (projectSourceType == '1' || projectSourceType == '2') {
                    me.pickBidInfoId();
                    view.getCmp("prmContractCDto->bidInfoId").setDisabled(false);
                } else if (projectSourceType == '4') {
                    view.getCmp("prmContractCDto->prmCodeType").sotValue("NEI_WEI");
                }
                return true;
            }
        };
        var form = Ext.widget("JForm", {
            height: 120,
            width: 350,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JCombo',
                    fieldLabel: '项目来源',
                    cid: 'projectSourceTypeSelect',
                    comboType: "scdp_fmcode",
                    codeType: "CDM_PROJECT_SOURCE_TYPE",
                    displayDesc: true,
                    fullInfo: false
                }
            ]
        });
        Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '项目来源选择', "确认");

    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmContractCDto->countryCode").sotValue("CN");
        view.getCmp("prmContractCDto->contractorOffice").putValue(Erp.Util.getCurrentDeptCode());
        view.getCmp("prmContractCDto->state").sotValue("0");
        view.getCmp("prmContractCDto->contractStatus").sotValue("NEW");
        view.getCmp("cdmFileRelationDto").sotValue(null);
        var officeId = view.getCmp("prmContractCDto->contractorOffice").gotValue();
        if (officeId != null) {
            view.getCmp("prmContractCDto->affiliatedInstitutions").sotValue(officeId + "_DIRECTLY");
        }
        me.changeProjectSourceType();
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
        me.changeProjectSourceType();
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        me.changeIsMajor();
        return me.validateHeader();
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
        var view = me.view;
        view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(false);
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
    customerIdBlur: function (a, b) {
        var me = this;
        var customerId = me.view.getCmp("prmContractCDto->customerId").rawValue;
        if (Scdp.ObjUtil.isNotEmpty(customerId)) {
            var postData = {
                customerId: customerId
            };
            Scdp.doAction("contract-c-customerIdQuery", postData, function (result) {
                if (result.isInScdpOrg == true) {
                    me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(true);
                }
                else {
                    me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotValue(null);
                    me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(false);
                    if (me.view.getCmp("prmContractCDto->projectSourceType").gotValue() == "4") {
                        Scdp.MsgUtil.warn('内委项目业主单位必须为公司内部部门！');
                    }
                }
            });
        }
//        else {
//            me.view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(true);
//        }
    },
    changeProjectSourceType: function () {
        var me = this;
        var view = me.view;
        var projectSourceTypeCmp = view.getCmp("prmContractCDto->projectSourceType");
        var value = projectSourceTypeCmp.gotValue();
        if ("4" == value) {
            view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(true);
            view.getCmp("prmContractCDto->innerPurchaseReqId").allowBlank = false;
        } else {
            view.getCmp("prmContractCDto->innerPurchaseReqId").sotValue(null);
            view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(false);
            view.getCmp("prmContractCDto->innerPurchaseReqId").allowBlank = true;
        }
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        if (!me.workFlowTaskId) {
            var taxType = view.getCmp("prmContractCDto->prmCodeType").gotValue();
            var errorMsg = "";
            var cdmFileRelationDtoItems = view.getCmp("cdmFileRelationDto").store.data.items;
            if (Scdp.ObjUtil.isEmpty(cdmFileRelationDtoItems)) {
                errorMsg = Erp.Const.BREAK_LINE + "合同附件不能为空！";
            }
            if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                return true;
            } else {
                Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                return false;
            }
        }
        if (Erp.ArrayUtil.anyContains(me.workFlowFormData, "wf_erp_enable_taxType=1")) {
            var taxType = view.getCmp("prmContractCDto->prmCodeType").gotValue();
            if (Scdp.ObjUtil.isEmpty(taxType)) {
                Scdp.MsgUtil.warn("提交前，请补充代号类型！");
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
        var type = view.getCmp("prmContractCDto->projectSourceType").gotValue();
        if (type == "1" || type == "2") {
            var callback = function (subView, isCancel) {
                var grid = subView.getQueryPanel().ownerCt.getCmp('resultPanel');
                var selectedRecords = grid.getSelectedRecords();
                if (selectedRecords.length > 0) {
                    var rowData = selectedRecords[0].data;
                    view.getCmp("prmContractCDto->operateBusinessBidInfoId").sotValue(rowData.uuid);
                }
                return true;
            };
            var controller;
            var sourceType;
            sourceType = type == '1' ? '2' : "1";
            if (sourceType == "1") {
                controller = Ext.create("Businessbidinfo.controller.BusinessbidinfoQueryController");
            } else if (sourceType == "2") {
                controller = Ext.create("Bidcontractplan.controller.BidcontractplanQueryController");
            }
            var winActual = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '项目来源选择', "确认");
            var queryExtraParams = {exclude: '1'};
            controller.view.getConditionPanel().queryExtraParams = queryExtraParams;
            var childView = winActual.down('IView');

            childView.getCmp("queryPanel->projectType").sotValue(sourceType);
            childView.getCmp("queryPanel->projectType").originalValue = childView.getCmp("queryPanel->projectType").gotValue();
        }
    }
});
