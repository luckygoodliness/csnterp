Ext.define("Cashreq.controller.CashreqDepositController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Cashreq.view.CashreqDepositView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'editToolbar->trash', name: 'click', fn: 'trashClick'},
        {cid: 'fadCashReqDto->payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'editToolbar->certificate', name: 'click', fn: 'certificateClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'depositCharacteristic', name: 'change', fn: 'depositCharacteristicChange'},
        {cid: 'expenseRequest', name: 'click', fn: 'doPrintExpenseRequest'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto',
    queryAction: 'cashreq-query',
    loadAction: 'cashreq-load',
    addAction: 'cashreq-add',
    modifyAction: 'cashreq-modify',
    deleteAction: 'cashreq-delete',
    exportXlsAction: "cashreq-exportxls",
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
        var subjectName = view.getCmp("fadCashReqDto->subjectName");
        var reqType = view.getCmp("fadCashReqDto->reqType");
        view.getCmp("fadCashReqDto->applyDate").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
        subjectName.sotValue("保证金申请");
        reqType.sotValue("3");
        view.getCmp("fadCashReqDto->state").sotValue("0");
        view.getCmp("fadCashReqDto->isProject").sotValue(0);
        view.getCmp("fadCashReqDto->payStyle").sotValue("1");
        me.fillApplier();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->runningNo").sotValue("");
        view.getCmp("fadCashReqDto->applyDate").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
        view.getCmp("fadCashReqDto->isProject").sotValue(0);
        view.getCmp("fadCashReqDto->payStyle").sotValue("1");
        me.fillApplier();
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->officeId").setReadOnly(true);
        view.getCmp("fadCashReqDto->staffId").setReadOnly(true);
        var bidInfo = view.getCmp("fadCashReqDto->operateBusinessBidInfoId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(bidInfo)) {
            view.getCmp("fadCashReqDto->money").setReadOnly(true);
            view.getCmp("fadCashReqDto->fadSubjectCode").setReadOnly(true);
        }
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var applyDate = view.getCmp("fadCashReqDto->applyDate").gotValue();
        var squareDate = view.getCmp("fadCashReqDto->squareDate").gotValue();
        var preclearDate = view.getCmp("fadCashReqDto->preclearDate").gotValue();
        var depositType = view.getCmp("fadCashReqDto->depositType").gotValue();

        if (depositType == "投标资审保证金") {
            var operateBusinessBidInfoId = view.getCmp("fadCashReqDto->operateBusinessBidInfoId").gotValue();
            if (Scdp.ObjUtil.isEmpty(operateBusinessBidInfoId)) {
                Scdp.MsgUtil.warn("保存失败！<br\>投标资审保证金必须关联投标信息!");
                return false;
            }
        } else if (depositType == "现金履约保证金") {
            var fadSubjectCode = view.getCmp("fadCashReqDto->fadSubjectCode").gotValue();
            if (Scdp.ObjUtil.isEmpty(fadSubjectCode)) {
                Scdp.MsgUtil.warn("保存失败！<br\>现金履约保证金必须关联项目信息!");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(applyDate) && Scdp.ObjUtil.isNotEmpty(squareDate)) {
            if (applyDate > squareDate) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计结算时间不能早于请款时间!");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(squareDate) && Scdp.ObjUtil.isNotEmpty(applyDate)) {
            if (squareDate > preclearDate) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计核销时间不能早于预计结算时间!");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(preclearDate) && Scdp.ObjUtil.isNotEmpty(preclearDate)) {
            if (preclearDate > Ext.Date.add(preclearDate, Ext.Date.MONTH, 6)) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计核销时间不能晚于请款后半年!");
                return false;
            }
        }
        return true;
    },
    certificateClick: function (obj) {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        if (state != "2") {
            Scdp.MsgUtil.warn("只有审批通过的单据才能生成凭证！");
        } else {
            var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
            var param = {uuid: uuid};
            Scdp.doAction("certificate-create", param, function (result) {
                if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                    me.loadItem(uuid);
                    Scdp.MsgUtil.info("操作成功！");
                    Erp.Util.gotoCertificateModule(result.fadCertificateUuid);
                }
                else {
                    Scdp.MsgUtil.warn("操作失败！" + result.msg);
                }
            });
        }
    },

    //调出凭证
    toCertificate: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp('fadCashReqDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    },
    afterSave: function (result) {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
        if (Scdp.ObjUtil.isNotEmpty(result.runningNo)) {
            me.view.getCmp("fadCashReqDto->runningNo").sotValue(result.runningNo);
        }
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
        view.getCmp("editPanel->trash").setDisabled(true);
        view.getCmp("editPanel->certificate").setDisabled(true);
        view.getCmp("editPanel->toCertificate").setDisabled(true);
        if (view.getCmp("fadCashReqDto->state").gotValue() == "2") {
            view.getCmp("editPanel->trash").setDisabled(false);
            view.getCmp("editPanel->certificate").setDisabled(false);
        }
        else if (view.getCmp("fadCashReqDto->state").gotValue() == "4" || view.getCmp("fadCashReqDto->state").gotValue() == "8") {
            view.getCmp("editPanel->toCertificate").setDisabled(false);
        }
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->money").setReadOnly(true);
        view.getCmp("fadCashReqDto->fadSubjectCode").setReadOnly(true);
        me.callParent(arguments);

    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        var view = me.view;
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

    trashClick: function (obj) {
        var me = this;
        var view = this.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        if (state != '2') {
            Scdp.MsgUtil.warn("只有审批通过的单据才能作废！");
        } else {
            Scdp.MsgUtil.confirm("是否确认作废？", function (e) {
                if (e == "yes") {
                    var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
                    var actionType = "trash";
                    var param = {actionType: actionType, uuid: uuid};
                    Scdp.doAction("cashreq-work", param, function (result) {
                        if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                            me.loadItem(uuid);
                            Scdp.MsgUtil.info("操作成功！");
                        }
                        else {
                            Scdp.MsgUtil.warn("操作失败！");
                        }
                    });
                }
            });
        }
    },

    payStyleChange: function (a, b) {
        var view = this.view;
        if (b == "0" || b == "2") {
            view.getCmp("fadCashReqDto->payeeName").sotValue('');
            view.getCmp("fadCashReqDto->payeeBankName").sotValue('');
        }
    },

    //填充申请人
    fillApplier: function () {
        this.view.getCmp("fadCashReqDto->operatorId").sotValue(Scdp.getCurrentUserId());
        this.view.getCmp("fadCashReqDto->staffId").putValue(Scdp.getCurrentUserId());
        this.view.getCmp("fadCashReqDto->officeId").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));

        this.view.getCmp("fadCashReqDto->operatorName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        this.view.getCmp("fadCashReqDto->operateTime").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
    },

    depositCharacteristicChange: function (a, b, c, d) {

    },
    //M7_C5_F3_批量打印
    doPrintExpenseRequest: function () {
        var me = this;
        var view = me.view;
        var uuids = "";
        var reqType = "";
        var grid = view.getQueryPanel().getCmp("resultPanel");
        var selectedRecords = grid.getSelectionModel().getSelection();
        if (selectedRecords.length > 0) {
            reqType = selectedRecords[0].data.reqType;
            var flag = false;
            for (var i = 0; i < selectedRecords.length; i++) {
                uuids += selectedRecords[i].data.uuid + ",";
                if (reqType == "2") {
                    if (reqType != selectedRecords[i].data.reqType) {
                        flag = true;
                    }
                } else {
                    if ("2" == selectedRecords[i].data.reqType) {
                        flag = true;
                    }
                }
            }
            if (flag) {
                Scdp.MsgUtil.warn("选中的打印数据中的类型不相同");
                return;
            }
            var param = {
                uuid: uuids.substr(0, uuids.length - 1)
            }
            if (reqType == 2) {
                Scdp.printReport("erp/prm/AccountStatementTrip.cpt", [param], false, "pdf");
            } else {
                Scdp.printReport("erp/prm/AccountStatementOthers.cpt", [param], false, "pdf");
            }
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('fadCashReqDto->officeId').gotValue();
        return processDeptCode;
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        var fileGrid = view.getCmp("cdmFileRelationDto");
        if (Scdp.ObjUtil.isEmpty(state) || state == '0') {
            if (fileGrid.getStore().getCount() == 0) {
                Scdp.MsgUtil.warn("附件信息不能为空，请上传附件信息！");
                return false;
            }
        }
        return true;
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
    }
});