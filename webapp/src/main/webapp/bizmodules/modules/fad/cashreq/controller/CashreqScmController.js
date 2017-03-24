Ext.define("Cashreq.controller.CashreqScmController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Cashreq.view.CashreqScmView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'editToolbar->trash', name: 'click', fn: 'trashClick'},
        {cid: 'fadCashReqDto->payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'fadCashReqDto->electricCommercialStore', name: 'blur', fn: 'electricCommercialStoreBlur'},
        {cid: 'editToolbar->certificate', name: 'click', fn: 'certificateClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'editToolbar->editBtn', name: 'click', fn: 'modifyBtnClick'},
        {cid: 'payeeAccount', name: 'change', fn: 'payeeAccountChange'},
        {cid: 'expenseRequest', name: 'click', fn: 'doPrintExpenseRequest'},
        {cid: 'fadCashReqDto->fileUpload', name: 'click', fn: 'doFileUpload'},
        {cid: 'fadCashReqDto->fileDownload', name: 'click', fn: 'doFileDownload'},
        {cid: 'fadCashReqDto->fileDelete', name: 'click', fn: 'dofileDelete'}
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
        var view = me.view;
        me.callParent(arguments);
        view.getCmp("fadCashReqDto->fileUpload").disable();
        view.getCmp("fadCashReqDto->fileDelete").disable();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->reqType").sotValue("0");
        view.getCmp("fadCashReqDto->applyDate").sotValue(new Date());
        view.getCmp("fadCashReqDto->electricCommercialStore").filterConditions = {selfconditions: " office_id = '" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "'"};
        view.getCmp("fadCashReqDto->jdName").filterConditions = {selfconditions: " 1=1"}
        this.fillApplier();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->runningNo").sotValue("");
        view.getCmp("fadCashReqDto->state").sotValue("0");
        view.getCmp("fadCashReqDto->applyDate").sotValue(new Date());
        this.fillApplier();
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
    },
    modifyBtnClick: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
        var fadPayReqHUuid = view.getCmp("fadCashReqDto->fadPayReqHUuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(fadPayReqHUuid) && state == '2') {
            me.loadItem(uuid, "modify", me.afterModify);
        }
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var payStyle = view.getCmp("fadCashReqDto->payStyle").gotValue();
        if (payStyle != "0" && payStyle != "2") {
            var payeeName = view.getCmp("fadCashReqDto->payeeName").gotValue();
            var payeeBankName = view.getCmp("fadCashReqDto->payeeBankName").gotValue();
            var payeeAccount = view.getCmp("fadCashReqDto->payeeAccount").gotValue();

            if (Scdp.ObjUtil.isEmpty(payeeName) || Scdp.ObjUtil.isEmpty(payeeBankName) || Scdp.ObjUtil.isEmpty(payeeAccount)) {
                Scdp.MsgUtil.warn("当前申请非个人收款，请输入收款单位名称、收款开户银行、收款单位账号!");
                return false;
            }
        }
        var electricCommercialStore = me.view.getCmp("fadCashReqDto->electricCommercialStore").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(electricCommercialStore)) {
            var jdName = view.getCmp("fadCashReqDto->jdName").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdName)) {
                Scdp.MsgUtil.warn("请输完整商城信息！用户名不能为空！");
                return false;
            }
            var jdPassword = view.getCmp("fadCashReqDto->jdPassword").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdPassword)) {
                Scdp.MsgUtil.warn("请输完整商城信息！密码不能为空！");
                return false;
            }

            var jdOrderNo = view.getCmp("fadCashReqDto->jdOrderNo").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdOrderNo)) {
                Scdp.MsgUtil.warn("请输完整商城信息！订单号不能为空！");
                return false;
            }

            var jdOrderDate = view.getCmp("fadCashReqDto->jdOrderDate").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdOrderDate)) {
                Scdp.MsgUtil.warn("请输完整商城信息！下单时间不能为空！");
                return false;
            }

            var jdLastDate = view.getCmp("fadCashReqDto->jdLastDate").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdLastDate)) {
                Scdp.MsgUtil.warn("请输完整商城信息！最后支付时间不能为空！");
                return false;
            }
            if (jdOrderDate > jdLastDate) {
                Scdp.MsgUtil.warn("最后支付时间不能早于下单时间！");
                return false;
            }
        }
        return true;
    },
    afterSave: function (result) {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
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
        var view = me.view;
        me.callParent(arguments);
        view.getCmp("editPanel->trash").setDisabled(true);
        view.getCmp("editPanel->certificate").setDisabled(true);
        view.getCmp("editPanel->toCertificate").setDisabled(true);
        view.getCmp("editPanel->editBtn").setDisabled(true);
        view.getCmp("fadCashReqDto->fileUpload").disable();
        view.getCmp("fadCashReqDto->fileDelete").disable();
        var state = view.getCmp("fadCashReqDto->state").gotValue()
        var operator = view.getCmp("fadCashReqDto->operatorId").gotValue();

        if (state == "0") {
            if (Scdp.getCurrentUserId() == operator) {
                view.getCmp("fadCashReqDto->fileUpload").setDisabled(false);
                view.getCmp("fadCashReqDto->fileDelete").setDisabled(false);
            }
        } else if (state == "2") {
            view.getCmp("editPanel->trash").setDisabled(false);
            view.getCmp("editPanel->certificate").setDisabled(false);
            if (Scdp.ObjUtil.isNotEmpty(view.getCmp("fadCashReqDto->fadPayReqHUuid").gotValue())) {
                view.getCmp("editPanel->editBtn").setDisabled(false);
            }
        }
        else if (state == "4" || state == "8") {
            view.getCmp("editPanel->toCertificate").setDisabled(false);
        }
    },
    payStyleChange: function (a, b) {
        var view = this.view;
//        if (b == "0" || b == "2") {
//            view.getCmp("fadCashReqDto->payeeName").sotValue('');
//            view.getCmp("fadCashReqDto->payeeBankName").sotValue('');
//            view.getCmp("fadCashReqDto->payeeAccount").sotValue('');
//        }
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
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
    payeeAccountChange: function (obj) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(obj.displayTplData) && obj.displayTplData[0].codedesc) {
            var bank = me.view.getCmp("fadCashReqDto->payeeBankName");
            bank.sotValue(obj.displayTplData[0].codedesc);
        }
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
    //填充申请人
    fillApplier: function () {
        this.view.getCmp("fadCashReqDto->operatorId").sotValue(Scdp.getCurrentUserId());
        this.view.getCmp("fadCashReqDto->operatorName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        this.view.getCmp("fadCashReqDto->operateTime").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
    },
    doPrintExpenseRequest: function () {
        var me = this;
        var view = me.view;
        var runningNo = view.getCmp("fadCashReqDto->runningNo").gotValue();
        var reqType = view.getCmp("fadCashReqDto->reqType").gotValue();
        var isAdvancePayment = view.getCmp("fadCashReqDto->isAdvancePayment").gotValue();
        var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
        if (isAdvancePayment == '1') {
            uuid = view.getCmp("fadCashReqDto->purchaseContractId").gotValue();
        }
        var reqHUuid = view.getCmp("fadCashReqDto->fadPayReqHUuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(reqHUuid)) {
            uuid = reqHUuid;
        }
        var param = {runningNo: runningNo, uuid: uuid};
        if (reqType == 2) {//差旅
            Scdp.printReport("erp/prm/AccountStatementTripScm.cpt", [param], false, "pdf");
        } else {
            Scdp.printReport("erp/prm/AccountStatementOthersScm.cpt", [param], false, "pdf");
        }
    },
    electricCommercialStoreBlur: function (a, b) {
//        var me = this;
//        var view = me.view;
//        if (a.lastValue != a.oldValue) {
//            view.getCmp("fadCashReqDto->payeeName").putValue(a.lastValue);
//            view.getCmp("fadCashReqDto->payStyle").sotValue('1');
//        }
    },
    doFileUpload: function () {
        var me = this;
        var form = me;
        var fileClassify = "";
        Erp.FileUtil.erpFileUpload(form, fileClassify, me.initSubFileUploadData);
    },
    initSubFileUploadData: function (form, obj) {
        var me = form;
        var view = me.view;
        var fileName = obj.fileName

        view.getCmp("fadCashReqDto->fileId").sotValue(obj.uuid);
        var dataId = view.getCmp("fadCashReqDto->uuid").gotValue();
        var fileId = view.getCmp("fadCashReqDto->fileId").gotValue();
        var param = {dataId: dataId, fileId: fileId, actionType: 'upload'};
        Scdp.doAction("cashreq-attachment", param, function (result) {
            if (result.result == true) {
                view.getCmp("fadCashReqDto->attachmentFileName").sotValue(fileName);
            }
        });
    }, doFileDownload: function (fileGrid) {
        var me = this;
        var view = me.view;

        var fileName = view.getCmp("fadCashReqDto->attachmentFileName").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(fileName)) {
            var uuids = [];
            var fileId = view.getCmp("fadCashReqDto->fileId").gotValue();
            var postdata = {};
            uuids.push(fileId);
            postdata.fileList = uuids;
            var ret = Scdp.doAction("erp-common-file-download", postdata, null, null, false, false);
            var urlList = ret.URL_LIST;
            Ext.each(urlList, function (item) {
                window.open(item);
            })
        } else {
            Scdp.MsgUtil.warn("当前申请没有附件关联信息！");
        }
    }, dofileDelete: function (fileGrid) {
        var me = this;
        var view = me.view;
        var fileName = view.getCmp("fadCashReqDto->attachmentFileName").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(fileName)) {
            var dataId = view.getCmp("fadCashReqDto->uuid").gotValue();
            var fileId = view.getCmp("fadCashReqDto->fileId").gotValue();
            var param = {dataId: dataId, fileId: fileId, actionType: 'delete'};
            Scdp.doAction("cashreq-attachment", param, function (result) {
                if (result.result == true) {
                    view.getCmp("fadCashReqDto->attachmentFileName").sotValue("");
                }
            });
        } else {
            Scdp.MsgUtil.warn("当前申请没有附件关联信息！");
        }
    }
});