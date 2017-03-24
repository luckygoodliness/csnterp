Ext.define("Invoice.controller.NonFailyClaimsController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Invoice.view.NonFailyClaimsView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'fadInvoiceDto->payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'taxRate', name: 'change', fn: 'taxRateChange'},
        {cid: 'fadCashReqInvoiceDto', name: 'edit', fn: 'fadCashReqInvoiceGridChange'},
        {cid: 'fadInvoiceDto->bankId', name: 'change', fn: 'bankIdChange'},
        {cid: 'fadInvoiceDto->invoiceType', name: 'change', fn: 'invoiceTypeChange'},
        {cid: 'fadInvoiceDto->invoiceTotalValue', name: 'change', fn: 'invoiceTotalValueChange'},
        {cid: 'fadInvoiceDto->expensesMoney', name: 'change', fn: 'expensesMoneyChange'},
        {cid: 'fadInvoiceDto->renderPerson', name: 'change', fn: 'renderPersonChange'},
        {cid: 'fadInvoiceDto->subjectCode', name: 'blur', fn: 'subjectNameChange'},
        {cid: 'fadInvoiceDto->fadYear', name: 'change', fn: 'fadYearChange'},
        {cid: 'Btn_annul', name: 'click', fn: 'clickAnnul'},
        {cid: 'Btn_certificate', name: 'click', fn: 'clickCertificate'},
        {cid: 'toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'expensePaste', name: 'click', fn: 'doPrintExpensePaste'},
        {cid: 'printInvoiceReceipt', name: 'click', fn: 'doPrintInvoiceReceipt'},
        {cid: 'excelSubmit', name: 'click', fn: 'clickedExecelSubmit'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto',
    queryAction: 'invoice-query',
    loadAction: 'failyinvoice-load',
    addAction: 'failyinvoice-add',
    modifyAction: 'failyinvoice-modify',
    deleteAction: 'invoice-delete',
    exportXlsAction: "invoice-exportxls",
    //M4_C8_F1_发票内容导入
    //发票内容excel导入明细功能
    clickedExecelSubmit : function(){
        var me = this;
        var formView = Ext.widget("JForm", {
            height: 150,
            width: 500,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            cid: 'uploadForm',
            items: [
                {
                    xtype: 'JFile',
                    fieldLabel: '上传文件名',
                    cid: 'uploadFile'
                }
            ]
        });
        var callBack = function (subView) {
            var uploadFile = subView.getCmp("uploadFile");
            //var uploadData = [];
            if (Scdp.ObjUtil.isEmpty(uploadFile.gotValue())) {
                Scdp.MsgUtil.info("Please select file!");
                return;
            }
            Scdp.doAction("prmfailyclaims-importexcel", null, function (result) {
                if (result.succ) {
                    me.view.getCmp("fadInvoiceMaterialDto").addRowItems(result.resData);
                } else {
                    Erp.Util.showLogView(Erp.I18N.EXCEL_UPLOAD_FAILURE + Erp.Const.BREAK_LINE + result.errorMsgLog);
                }
                win.close();
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', 'Excel导入', "上传文件");
    },
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    taxRateChange: function () {//计算税率
        var invoiceTotalValue = this.view.getCmp("fadInvoiceDto->invoiceTotalValue").gotValue();
        var taxRate = this.view.getCmp("fadInvoiceDto->taxRate").gotValue();
        var taxMoney = Erp.MathUtil.multiNumber(invoiceTotalValue, taxRate) / (1 + parseFloat(taxRate));
        var noTaxMoney = Erp.MathUtil.minusNumber(invoiceTotalValue, taxMoney);
        if (!isNaN(taxMoney) && taxMoney != null) {
            this.view.getCmp("fadInvoiceDto->taxMoney").sotValue(taxMoney);
        }
        if (!isNaN(taxMoney) && noTaxMoney != null) {
            this.view.getCmp("fadInvoiceDto->noTaxMoney").sotValue(noTaxMoney);
        }
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.initialize();
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
        me.updateVirtualMoneyFiled();
        return true;
    },
    afterModify: function () {
        var me = this;
        me.taxRateChange();
        me.fadCashReqInvoiceGridChange();
        me.setSupplierCmpEditable();
        var state = me.view.getCmp("fadInvoiceDto->state").gotValue();
        if (state == '1') {
            me.setFieldEditable();
        }
    },
    erpBeforeSave: function () {
        var me = this;
        var result = true;
        result = me.validation();
        return result;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.updateVirtualMoneyFiled();
        if (Scdp.ObjUtil.isNotEmpty(retData.invoiceReqNo)) {
            me.view.getCmp("fadInvoiceDto->invoiceReqNo").sotValue(retData.invoiceReqNo);
        }
        me.updateClearanceMoney();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.taxRateChange();
        me.callParent(arguments);
        me.setBtnState();
        me.updateVirtualMoneyFiled();
        me.controlBtnCertificate();
        //me.view.hideMaterialTab(me.view);
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.taxRateChange();
        me.updateVirtualMoneyFiled();
        me.setBtnState();
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
    updateVirtualMoneyFiled: function () {
        var me = this;
        me.updateClearanceMoney();
        me.updatePayCash();
    },
    renderPersonChange: function () {
        this.clearFadCashReqInvoiceGrid();
    },
    clearFadCashReqInvoiceGrid: function () {
        var fadCashReqInvoiceGrid = this.view.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.store.removeAll();
        this.updateClearanceMoney();
    },
    fadYearChange: function () {
        var me = this;
        var view = me.view;
        var fadYearCmp = view.getCmp("fadInvoiceDto->fadYear");
        var renderPersonCmp = view.getCmp("fadInvoiceDto->renderPerson");
        var subjectCodeCmp = view.getCmp("fadInvoiceDto->subjectCode");
        subjectCodeCmp.putValue();
        var fadYear = fadYearCmp.gotValue();
        var renderPerson = renderPersonCmp.gotValue();
        if (Scdp.ObjUtil.isEmpty(fadYear) || Scdp.ObjUtil.isEmpty(renderPerson)) {
            subjectCodeCmp.sotEditable(false);
        } else {
            subjectCodeCmp.sotEditable(true);
        }
    },
    setBtnState: function () {
        var me = this;
        var view = me.view;
        var stateCmp = me.view.getCmp("fadInvoiceDto->state");
        if (stateCmp.gotValue() == 2) {
            view.getCmp("Btn_annul").setDisabled(false);
        } else {
            view.getCmp("Btn_annul").setDisabled(true);
        }
        if (stateCmp.gotValue() == 0 || stateCmp.gotValue() == 5) {
            view.getCmp("deleteBtn").setDisabled(false);
        } else {
            view.getCmp("deleteBtn").setDisabled(true);
        }
    },
    //点击废止按钮
    clickAnnul: function () {
        var me = this;
        var uuid = this.view.getCmp('fadInvoiceDto->uuid').gotValue();
        if (uuid == "") {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        Scdp.MsgUtil.confirm("是否废止该报销？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("invoice-annul", postData, function (result) {
                    me.view.getCmp("fadInvoiceDto->state").sotValue("3");
                    Scdp.MsgUtil.info("流水号" + result.invoiceReqNo + "废止成功!");
                })
            }
        });
    },
    subjectNameChange: function (a, b) {
        var me = this;
        var view = me.view;
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.store.removeAll();
        var subjectName = view.getCmp("fadInvoiceDto->subjectName").gotValue();
        if (subjectName == "业务活动费") {
            view.getCmp("fadInvoiceDto->tripReason").emptyText = "参考格式：招待XX地区（或项目）领导（或业主）";
        } else {
            view.getCmp("fadInvoiceDto->tripReason").emptyText = " ";
        }
        view.getCmp("fadInvoiceDto->tripReason").applyEmptyText();
        //view.hideMaterialTab(view);
    },
    pickCashReq: function () {//相关合同弹出窗
        var me = this;
        var view = me.view;
        var renderPerson = me.view.getCmp("fadInvoiceDto->renderPerson").gotValue();
        var subjectId = me.view.getCmp("fadInvoiceDto->subjectId").gotValue();
        if (renderPerson == null || renderPerson == '') {
            Scdp.MsgUtil.info("请先选择核销人！");
            return false;
        }
        if (subjectId == null || subjectId == '') {
            Scdp.MsgUtil.info("请先选择费用科目！");
            return false;
        }
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length > 0) {
//                var supplierCode = '';
//                var supplierName = '';
//                var taxRegistrationNo = '';
                var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
//
                Ext.Array.each(selectedRecords, function (a) {
                    var rowData = a.data;
                    var c = fadCashReqInvoiceGrid.getStore(), d = Ext.ModelManager.create({}, fadCashReqInvoiceGrid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                        d.set("fadCashReqId", rowData.uuid);
                        d.set("runningNo", rowData.runningNo);
                        d.set("cashVoucher", rowData.financeNo);
                        d.set("reqMoney", rowData.money);
                        d.set("clearanceMoney", rowData.money - rowData.clearancedMoney);
                    }
                    d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);
                    fadCashReqInvoiceGrid.store.insert(c.getCount(), d);
                    fadCashReqInvoiceGrid.getSelectionModel().select(c.getCount() - 1);
                });
                me.fadCashReqInvoiceGridChange();
                return true;
            } else {
                return true;
            }
        };
        var notInRow = "";
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fadCashReqInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fadCashReqInvoiceGrid.getStore().getAt(i).data;
                notInRow = notInRow + "'" + rowData.fadCashReqId + "',"
            }
        }
        notInRow = notInRow + "'.'";
        var queryController = Ext.create("Cashreq.controller.CashreqQueryController");
        var param = {
            renderPerson: renderPerson,
            renderPersonName: me.view.getCmp("fadInvoiceDto->renderPersonDesc").gotValue(),
            notInRow: notInRow,
            reqType: 1,
            isProject: 0,
            subjectId: subjectId
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', "未核销请款记录", null, true);
    },
    fadCashReqInvoiceGridChange: function () {//请款核销grid改变时
        this.updateClearanceMoney();//更新原请款金额、核销总额
        this.updatePayCash();
    },
    updateClearanceMoney: function () {//更新原请款金额、核销总额
        var me = this;
        var view = me.view;
        var totalClearanceMoney = 0;
        var cashReqMoney = 0;
        var fontractInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
                totalClearanceMoney = totalClearanceMoney + rowData.clearanceMoney;
                cashReqMoney = cashReqMoney + rowData.reqMoney;
            }
        }
        view.getCmp("fadInvoiceDto->totalClearanceMoney").sotValue(totalClearanceMoney);//更新核销金额合计
        view.getCmp("fadInvoiceDto->cashReqMoney").sotValue(cashReqMoney);//更新原请款金额合计
        //this.validation();
    },
    invoiceTypeChange: function () {
        var me = this;
        var view = me.view;
        var invoiceType = view.getCmp("fadInvoiceDto->invoiceType").value;
        var taxRateCmp = view.getCmp("fadInvoiceDto->taxRate");
        var taxRateDescCmp = view.getCmp("fadInvoiceDto->taxRateDesc");
        if (invoiceType == "0") {
            taxRateCmp.sotValue("0.17");
            taxRateDescCmp.sotValue("17%");
        }
    },
    invoiceTotalValueChange: function () {
        var view = this.view;
        var invoiceTotalValue = view.getCmp("fadInvoiceDto->invoiceTotalValue").gotValue();
        view.getCmp("fadInvoiceDto->expensesMoney").sotValue(invoiceTotalValue);//更新报现金额
        this.taxRateChange();
        this.updatePayCash();
    },
    expensesMoneyChange: function () {
        this.updatePayCash();
    },
    updatePayCash: function () {//更新补发金额
        var me = this.view;
        var expensesMoney = me.getCmp("fadInvoiceDto->expensesMoney").gotValue();
        var totalClearanceMoney = me.getCmp("fadInvoiceDto->totalClearanceMoney").gotValue();
        me.getCmp("fadInvoiceDto->payCash").sotValue(expensesMoney - totalClearanceMoney);//更新补发金额
    },
    setFieldEditable: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadInvoiceDto").sotEditable(false);
        view.getCmp("fadInvoiceDto->expensesMoney").sotEditable(true);
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.getCmp("toolbar->addRowBtn").disable();
        fadCashReqInvoiceGrid.getCmp("toolbar->delRowBtn").disable();
        fadCashReqInvoiceGrid.on('beforeedit', function (editor, eventObj) {
            var field = eventObj.field;
            if (field == 'clearanceMoney' || Scdp.Const.UI_INFO_STATUS_NEW == view.getUIStatus()) {
                return true;
            } else {
                return false;
            }
        });
    },
    validation: function () {//前台校验
        var me = this;
        var view = me.view;
        var result = false;
        var invoiceTotalValue = view.getCmp("fadInvoiceDto->invoiceTotalValue").gotValue();
        var expensesMoney = view.getCmp("fadInvoiceDto->expensesMoney").gotValue();
        if (invoiceTotalValue == 0) {
            Scdp.MsgUtil.info("报销金额不能为0！");
            return result;
        }
        if (expensesMoney > invoiceTotalValue) {
            Scdp.MsgUtil.info("核准金额不能比报销金额金额大！");
            return result;
        }

        //当冲请款为勾选时，必须选择已请款记录
        var isWriteOff1 = view.getCmp("isWriteOff").gotValue();
        if (isWriteOff1){
            var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
            var count = fadCashReqInvoiceGrid.getStore().getCount();
            if (count == 0){
                Scdp.MsgUtil.info("冲请款已勾选，请选择已请款记录！");
                return false;
            }
        }

        var totalClearanceMoney = view.getCmp("fadInvoiceDto->totalClearanceMoney").gotValue();
        if (totalClearanceMoney > expensesMoney) {
            Scdp.MsgUtil.info("核销金额合计不能比核准金额大！");
            return result;
        }
        var subjectName = view.getCmp("subjectName").gotValue();
        if ((subjectName == '固定资产添置' || subjectName == '低值易耗品') && invoiceTotalValue > 5000) {
            Scdp.MsgUtil.info(subjectName + "单笔报销金额不能大于5千元！");
            return result;
        }
        //*****************后台校验开始*******************//
        var payCash = view.getCmp("fadInvoiceDto->payCash").gotValue();
        if (payCash > 0) {
            result = me.validationInBackground();//后台校验
            return result;
        }
        //******************后台校验结束******************//
        return true;
    },
    validationInBackground: function () {//后台校验
        var me = this;
        var view = me.view;
        var subjectId = view.getCmp("fadInvoiceDto->subjectId").gotValue();
        var userCode = view.getCmp("fadInvoiceDto->renderPerson").gotValue();
        var notInRow = "";
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fadCashReqInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fadCashReqInvoiceGrid.getStore().getAt(i).data;
                notInRow = notInRow + "'" + rowData.fadCashReqId + "',"
            }
        }
        notInRow = notInRow + "'.'";
        var postData = {subjectId: subjectId, userCode: userCode, notInRow: notInRow};
        var validateResult = Scdp.doAction("invoice-Validation", postData, null, null, true, false);
        if (validateResult.rownum > 0) {
            Scdp.MsgUtil.confirm("存在可以核销请款，是否继续保存？", function (e) {
                if ("yes" == e) {
                    me.doErpSave();
                }
            });
            return false;
        } else {
            return true;
        }
    },
    payStyleChange: function () {
        var me = this;
        me.setSupplierCmpEditable();
    },
    setSupplierCmpEditable: function () {
        var me = this;
        //付款方式
        var payStyleValue = me.view.getCmp("fadInvoiceDto->payStyle").gotValue();
        if (payStyleValue == "0" || payStyleValue == "2") {//如果付款方式为现金、电汇时 收款单位可以为空
            me.view.getCmp("fadInvoiceDto->supplierName").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->supplierName").sotValue();
            me.view.getCmp("fadInvoiceDto->supplierName").sotEditable(false);
            me.view.getCmp("fadInvoiceDto->bank").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->bank").sotValue();
            me.view.getCmp("fadInvoiceDto->bankId").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->bankId").sotValue();
            me.view.getCmp("fadInvoiceDto->payee").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->payee").sotEditable(true);
            me.view.getCmp("fadInvoiceDto->payee").sotValue(me.view.getCmp("fadInvoiceDto->renderPersonDesc").gotValue());
        } else {
            me.view.getCmp("fadInvoiceDto->supplierName").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->supplierName").sotEditable(true);
            me.view.getCmp("fadInvoiceDto->bank").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->bankId").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->payee").sotValue();
            me.view.getCmp("fadInvoiceDto->payee").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->payee").sotEditable(false);
        }
    },
    initialize: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadInvoiceDto->supplierName").setReadOnly(true);
        view.getCmp("fadInvoiceDto->payee").setReadOnly(true);
        view.getCmp("fadInvoiceDto->expensesType").sotValue(1);
        view.getCmp("fadInvoiceDto->invoiceNum").sotValue(1);
        view.getCmp("fadInvoiceDto->state").sotValue("0");
        view.getCmp("fadInvoiceDto->fadYear").sotValue(new Date().getFullYear().toString());
        view.getCmp("fadInvoiceDto->renderPerson").putValue(Scdp.getCurrentUserId());
        //view.getCmp("fadInvoiceDto->renderPerson").sotValue(Scdp.getCurrentUserId());
        //view.getCmp("fadInvoiceDto->renderPersonName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp("fadInvoiceDto->officeId").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
    },
    doPrintExpensePaste: function () {
        var me = this;
        var view = me.view;
        var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
        var param = {invoiceReqNo: invoiceReqNo.gotValue()};
        Scdp.printReport("erp/prm/FadInvoiceExpensePaste.cpt", [param], false, "pdf");
    },
    doPrintInvoiceReceipt: function () {
        var me = this;
        var view = me.view;
        var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
        var param = {invoiceReqNo: invoiceReqNo.gotValue()};
        Scdp.printReport("erp/prm/FadCashReqMaterial.cpt", [param], false, "pdf");
    },
    //点击生成凭证
    clickCertificate: function () {
        var me = this;
        var uuid = this.view.getCmp('fadInvoiceDto->uuid').gotValue();
        var expensesType = this.view.getCmp('fadInvoiceDto->expensesType').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        Scdp.MsgUtil.confirm("是否生成凭证？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid, expensesType: expensesType};
                Scdp.doAction("invoice-createcertificate", postData, function (result) {
                    Scdp.MsgUtil.info("生成凭证成功!");
                    Erp.Util.gotoCertificateModule(result.fadCertificateUuid);
                })
            }
        });
    },
    //控制生成凭证按钮的显示
    controlBtnCertificate: function () {
        var me = this;
        var state = me.view.getCmp("fadInvoiceDto->state").gotValue();
        me.view.getCmp("Btn_certificate").setDisabled(true);
        me.view.getCmp("toCertificate").setDisabled(true);
        if (Scdp.ObjUtil.isNotEmpty(state)) {
            if ("2" == state) {
                me.view.getCmp("Btn_certificate").setDisabled(false);
            }
            else if ("8" == state || "4" == state) {
                me.view.getCmp("toCertificate").setDisabled(false);
            }
        }
    },

    //调出凭证
    toCertificate: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp('fadInvoiceDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    },
    //银行账号改变，自动带出银行名称
    bankIdChange: function (obj) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(obj.displayTplData)) {
            me.view.getCmp("fadInvoiceDto->bank").sotValue(obj.displayTplData[0].codedesc);
            var bank = me.view.getCmp("fadInvoiceDto->bank");
            bank.sotValue(obj.displayTplData[0].codedesc);
        }
    }
});