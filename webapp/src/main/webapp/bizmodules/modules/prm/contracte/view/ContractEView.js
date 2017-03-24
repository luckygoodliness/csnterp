Ext.define('ContractE.view.ContractEView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/contractr',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 125,
    //epHeight: 220,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'contract-r-query-layout.xml',
    editLayoutFile: 'contract-r-edit-layout.xml',
    //extraLayoutFile: 'contract-extra-layout.xml',
    bindings: ['prmContractCDto', 'prmContractCDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Contract_Revised',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
//        me.getCmp("queryPanel->contractStatus").sotValue("REVISE");
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
        me.getCmp("prmContractCDto->bidInfoId").setDisabled(true);
        me.initHeader();
        me.initFileGrid();
        me.initQueryPanel();
    },
    initQueryPanel: function () {
        var me = this;
    },
    initHeader: function () {
        var me = this;
        var contractForm = me.getCmp("prmContractCDto");
        var prmContractCIdCmp = contractForm.getCmp("prmContractId");
        prmContractCIdCmp.afterSotValue = function (obj) {
            if (Scdp.ObjUtil.isNotEmpty(prmContractCIdCmp.gotValue())) {
                var postData = {};
                postData.uuid = prmContractCIdCmp.gotValue();
                Scdp.doAction("wrapper-contract-to-revise", postData, function (result) {
                    if (result) {
                        var operateObj = result;
                        me.sotValue(operateObj, true);
                        var affiliatedInstitutions = operateObj.prmContractCDto.affiliatedInstitutions;
                        if (Scdp.ObjUtil.isNotEmpty(affiliatedInstitutions)) {
                            me.getCmp("prmContractCDto->affiliatedInstitutions").sotValue(affiliatedInstitutions);
                        } else {
                            var officeId = me.getCmp("prmContractCDto->contractorOffice").gotValue();
                            if (Scdp.ObjUtil.isNotEmpty(officeId)) {
                                me.getCmp("prmContractCDto->affiliatedInstitutions").sotValue(officeId + "_DIRECTLY");
                            }
                        }
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
                }, null, true);
            }
        };
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

        if (Scdp.Const.UI_INFO_STATUS_NEW == status || Scdp.Const.UI_INFO_STATUS_MODIFY == status) {
            var wfData = view.controller.workFlowFormData;
            if (Erp.ArrayUtil.anyContains(wfData, "wf_erp_enable_taxType=1")) {
                view.sotEditable(false);
                view.getCmp("prmContractCDto->taxType").sotEditable(true);
            } else {
                view.getCmp("prmContractCDto->taxType").sotEditable(false);
            }

        }
    }
});