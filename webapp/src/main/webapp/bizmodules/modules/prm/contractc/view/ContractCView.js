Ext.define('ContractC.view.ContractCView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/contractc',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 125,
    //epHeight: 220,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'contract-c-query-layout.xml',
    editLayoutFile: 'contract-c-edit-layout.xml',
    //extraLayoutFile: 'contract-extra-layout.xml',
    bindings: ['prmContractCDto', 'prmContractCDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Contract_Management',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
        me.initHeader();
        me.initFileGrid();
        me.initQueryPanel();
    },
    initQueryPanel: function () {
        var me = this;
        me.getCmp("queryPanel->contractStatus").sotValue("NEW");
    },
    initHeader: function () {
        var me = this;
        var contractForm = me.getCmp("prmContractCDto");
        var opBizIdCmp = contractForm.getCmp("operateBusinessBidInfoId");
        opBizIdCmp.afterSotValue = function (obj) {
            if (Scdp.ObjUtil.isNotEmpty(opBizIdCmp.gotValue())) {
                var postData = {};
                postData.uuid = opBizIdCmp.gotValue();
                Scdp.doAction("prm-contract-wrapper-operate-info", postData, function (result) {
                    if (result) {
                        var operateObj = result;
                        var projectType = operateObj.projectType;
                        contractForm.sotValue(operateObj, false, true);
                        var officeId = me.getCmp("prmContractCDto->contractorOffice").gotValue();
                        if (officeId != null) {
                            me.getCmp("prmContractCDto->affiliatedInstitutions").sotValue(officeId+"_DIRECTLY");
                        }
                        if ("2" == projectType) {
                            var fileDtos = operateObj.cdmFileRelationDto;
                            if (fileDtos) {
                                var fileGrid = me.getCmp("cdmFileRelationDto");
                                Ext.Array.each(fileDtos, function (item) {
                                    Scdp.wrapDataForRemoveSystemFields(item);
                                    item["cdmFileType"] = fileGrid.cdmFileType;
                                    fileGrid.addRowItem(item, false);
                                });
                            }
                        }
                        me.setComponentBlankState();
                        me.setComponentEditState();
                    }
                }, null, true);
            }
        };
        var customerId = contractForm.getCmp("customerId");

        customerId.afterSotValue = function (obj) {
            if (Scdp.ObjUtil.isEmpty(obj.gotValue())) {
                contractForm.getCmp("innerPurchaseReqId").sotEditable(true);
            }
        };
        me.initContractNowMoney();
    },
    initContractNowMoney: function () {
        var view = this;
        view.getCmp("prmContractCDto->contractNowMoney").setVisible(false);
        view.getCmp("prmContractCDto->contractNowMoney").allowBlank = true;
    },
    initFileGrid: function () {
        var me = this;
        var fileGrid = me.getCmp("cdmFileRelationDto");
        var fileToolbar = fileGrid.getEditToolbar();
        me.getCmp("cdmFileRelationDto->rowMoveTopBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveBottomBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveUpBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveDownBtn").setVisible(false);

        fileToolbar.add({
            xtype: 'button',
            cid: 'filePreview',
            iconCls: 'file_preview_icon'
        });
        fileToolbar.add({
            xtype: 'button',
            cid: 'fileDownload',
            iconCls: 'file_download_icon'
        });

        fileGrid.doAddRow = function () {
            var me = this;
            Erp.FileUtil.erpFileUpload(me);
        };
    },
    UIStatusChanged: function (view, newStatus) {
        var fileDownloadBtn = view.getCmp("cdmFileRelationDto->fileDownload");
        var filePreviewBtn = view.getCmp("cdmFileRelationDto->filePreview");
        if (fileDownloadBtn) {
            fileDownloadBtn.setDisabled(false);
        }
        if (filePreviewBtn) {
            filePreviewBtn.setDisabled(false);
        }
        view.afterChangeUIStatus(newStatus);
    },
    afterChangeUIStatus: function (status) {
        var view = this;
        if (!status) {
            status = view.getUIStatus();
        }
        var bidInfoIdStatus = false;
        if (Scdp.Const.UI_INFO_STATUS_NEW == status || Scdp.Const.UI_INFO_STATUS_MODIFY == status) {
            var wfData = view.controller.workFlowFormData;
            if (Erp.ArrayUtil.anyContains(wfData, "wf_erp_enable_taxType=1")) {
                view.sotEditable(false);
                view.getCmp("prmContractCDto->prmCodeType").sotEditable(true);
            } else {
                view.getCmp("prmContractCDto->prmCodeType").sotEditable(false);
            }
            bidInfoIdStatus = view.getCmp("prmContractCDto->state").gotValue() == "0"
                && (view.getCmp("prmContractCDto->projectSourceType").gotValue() == "1"
                    || view.getCmp("prmContractCDto->projectSourceType").gotValue() == "2");
        }
        view.getCmp("prmContractCDto->bidInfoId").setDisabled(!bidInfoIdStatus);
        view.setComponentBlankState();
        view.setComponentEditState();
    },
    setComponentBlankState: function () {
        var view = this;
        var projectSourceTypeIs2 = view.getCmp("prmContractCDto->projectSourceType").gotValue() == '2'
        view.getCmp("prmContractCDto->projectManager").allowBlank = !projectSourceTypeIs2
        view.getCmp("prmContractCDto->generalEngineer").allowBlank = !projectSourceTypeIs2
    },
    setComponentEditState: function () {
        var view = this;
        var state = view.getCmp("prmContractCDto->state").gotValue()
        var status = view.getUIStatus();
        if (state == "0" || state == "5") {
            if (Scdp.Const.UI_INFO_STATUS_NEW == status || Scdp.Const.UI_INFO_STATUS_MODIFY == status) {
                var projectSourceTypeIs2 = view.getCmp("prmContractCDto->projectSourceType").gotValue() == '2'
                var projectSourceTypeIs1 = view.getCmp("prmContractCDto->projectSourceType").gotValue() == '1'
                var projectSourceTypeIs4 = view.getCmp("prmContractCDto->projectSourceType").gotValue() == '4'
                view.getCmp("prmContractCDto->contractName").sotEditable(!projectSourceTypeIs1);
                view.getCmp("prmContractCDto->projectName").sotEditable(!projectSourceTypeIs2);
                view.getCmp("prmContractCDto->innerPurchaseReqId").sotEditable(projectSourceTypeIs4);
            }
        }
    }
});