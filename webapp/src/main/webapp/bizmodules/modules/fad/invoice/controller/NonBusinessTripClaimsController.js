Ext.define("Invoice.controller.NonBusinessTripClaimsController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Invoice.view.NonBusinessTripClaimsView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'fadInvoiceDto->payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'fadCashReqInvoiceDto', name: 'edit', fn: 'fadCashReqInvoiceGridChange'},
        {cid: 'fadInvoiceSubsidyDto', name: 'edit', fn: 'fadInvoiceSubsidyGridChange'},
        {cid: 'fadInvoiceTravelDto', name: 'edit', fn: 'fadInvoiceTravelGridChange'},
        {cid: 'fadInvoiceDto->bankId', name: 'change', fn: 'bankIdChange'},
        {cid: 'fadInvoiceDto->tripBeginDate', name: 'change', fn: 'tripDateChange'},
        {cid: 'fadInvoiceDto->tripEndDate', name: 'change', fn: 'tripDateChange'},
        {cid: 'fadInvoiceDto->renderPerson', name: 'change', fn: 'renderPersonChange'},
        {cid: 'fadInvoiceDto->fadYear', name: 'change', fn: 'fadYearChange'},
        {cid: 'Btn_annul', name: 'click', fn: 'clickAnnul'},
        {cid: 'Btn_certificate', name: 'click', fn: 'clickCertificate'},
        {cid: 'toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'expensePaste', name: 'click', fn: 'doPrintExpensePaste'},
        {cid: 'businessTripPrint', name: 'click', fn: 'doPrintBusinessTrip'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto',
    queryAction: 'invoice-query',
    loadAction: 'tripinvoice-load',
    addAction: 'tripinvoice-add',
    modifyAction: 'tripinvoice-modify',
    deleteAction: 'invoice-delete',
    exportXlsAction: "invoice-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.afterOfficeIdSotValue();
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
        return true;
    },
    afterModify: function () {
        var me = this;
        me.updateVirtualMoneyFiled();
        me.setSupplierNameCmpEditable();
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
        if (Scdp.ObjUtil.isNotEmpty(retData.invoiceReqNo)) {
            me.view.getCmp("fadInvoiceDto->invoiceReqNo").sotValue(retData.invoiceReqNo);
        }
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.setBtnState();
        me.updateVirtualMoneyFiled();
        me.controlBtnCertificate();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
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
        me.updateSubsidyMoney();
        me.updateClearanceMoney();
        me.updatePayCash();
        me.updateRemainBudget();
    },
    renderPersonChange: function () {
        this.clearFadCashReqInvoiceGrid();
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
    clearFadCashReqInvoiceGrid: function () {
        var fadCashReqInvoiceGrid = this.view.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.store.removeAll();
        this.updateClearanceMoney();
    },
    pickCashReq: function () {//相关合同弹出窗
        var me = this;
        var view = me.view;
        var renderPersonCmp = view.getCmp("fadInvoiceDto->renderPerson");
        var renderPerson = renderPersonCmp.gotValue()
        var fadYearCmp = view.getCmp("fadInvoiceDto->fadYear");
        var fadYear = fadYearCmp.gotValue();
        if (Scdp.ObjUtil.isEmpty(renderPerson)) {
            Scdp.MsgUtil.info("请先选择出差人！");
            me.view.getCmp("fadInvoiceDto->renderPerson").focus();
            return false;
        }
        if (Scdp.ObjUtil.isEmpty(fadYear)) {
            Scdp.MsgUtil.info("请先选择预算年！");
            fadYearCmp.focus();
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
        var notInRow = ""
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fadCashReqInvoiceGrid.getStore().getCount()
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
            subjectId: view.getCmp("fadInvoiceDto->subjectId").gotValue(),
            renderPersonName: me.view.getCmp("fadInvoiceDto->renderPersonDesc").gotValue(),
            notInRow: notInRow,
            reqType: 2,
            isProject: 0
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
        var totalClearanceMoney = 0
        var cashReqMoney = 0
        var fontractInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
                totalClearanceMoney = Erp.MathUtil.plusNumber(totalClearanceMoney ,rowData.clearanceMoney,0);
                cashReqMoney = Erp.MathUtil.plusNumber(cashReqMoney ,rowData.reqMoney,0);
            }
        }
        view.getCmp("fadInvoiceDto->totalClearanceMoney").sotValue(totalClearanceMoney);//更新核销金额合计
        view.getCmp("fadInvoiceDto->cashReqMoney").sotValue(cashReqMoney);//更新原请款金额合计

    },
    fadInvoiceSubsidyGridChange: function (a, b) {//补助grid改变时
        var fadInvoiceSubsidyGrid = this.view.getCmp("fadInvoiceSubsidyDto");
        var editrow = fadInvoiceSubsidyGrid.getStore().getAt(b.rowIdx);
        if ((b.field == "tripDays" || b.field == "standard") && b.originalValue != b.value) {//自动计算补助金额
            editrow.set("money", editrow.data.tripDays * editrow.data.standard);
        }
        this.updateSubsidyMoney();//更新补助合计
    },

    updateSubsidyMoney: function () {//更新补助合计
        var me = this;
        var view = me.view;
        var subsidyMoney = 0;
        var fadInvoiceSubsidyGrid = this.view.getCmp("fadInvoiceSubsidyDto");
        var count = fadInvoiceSubsidyGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fadInvoiceSubsidyGrid.getStore().getAt(i).data;
                subsidyMoney = Erp.MathUtil.plusNumber(subsidyMoney ,rowData.money,0);
            }
        }
        view.getCmp("fadInvoiceDto->subsidyMoney").sotValue(subsidyMoney);//更新补助合计
        me.updateExpensesMoney();
        me.updatePayCash();
    },
    fadInvoiceTravelGridChange: function (a, b) {//交通及住宿费grid改变时
        var fadInvoiceTravelGrid = this.view.getCmp("fadInvoiceTravelDto");
        var editrow = fadInvoiceTravelGrid.getStore().getAt(b.rowIdx);
        if (b.field == "invoiceMoney" && b.originalValue != b.value) {//当修改发票金额时自动赋值核准金额
            editrow.set("approvedMoney", b.value);
        }
        if (b.field == "approvedMoney" && b.originalValue != b.value) {//核准金额不能比发票金额大
            var invoiceMoney = editrow.data.invoiceMoney;
            if (b.value > invoiceMoney) {
                Scdp.MsgUtil.info("核准金额不能比发票金额大！");
//                b.value=b.originalValue;
                editrow.set("approvedMoney", invoiceMoney);
            }
        }
        this.updateExpensesMoney();//更新发票总金额，更新报销金额
    },
    updateExpensesMoney: function () {//更新发票总金额，更新报销金额
        var me = this;
        var view = me.view;
        var invoiceTotalValue = 0;
        var expensesMoney = 0;
        var fadInvoiceTravelGrid = this.view.getCmp("fadInvoiceTravelDto");
        var count = fadInvoiceTravelGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var row = fadInvoiceTravelGrid.getStore().getAt(i)
                var rowData = row.data;
                invoiceTotalValue = Erp.MathUtil.plusNumber(invoiceTotalValue ,rowData.invoiceMoney,0);
                expensesMoney = Erp.MathUtil.plusNumber(expensesMoney ,rowData.approvedMoney,0);
            }
        }
        var subsidyMoney = view.getCmp("fadInvoiceDto->subsidyMoney").gotValue();//补助总额
        view.getCmp("fadInvoiceDto->invoiceTotalValue").sotValue(Erp.MathUtil.plusNumber(invoiceTotalValue ,subsidyMoney,0));//更新报销总金额
        view.getCmp("fadInvoiceDto->expensesMoney").sotValue(Erp.MathUtil.plusNumber(expensesMoney ,subsidyMoney,0));//更新核准金额
        me.updatePayCash();
    },
    updatePayCash: function () {//更新报现金额
        var view = this.view;
        var expensesMoney = view.getCmp("fadInvoiceDto->expensesMoney").gotValue();
        var totalClearanceMoney = view.getCmp("fadInvoiceDto->totalClearanceMoney").gotValue();
        var payCash = expensesMoney - totalClearanceMoney;
        view.getCmp("fadInvoiceDto->payCash").sotValue(payCash);
    },
    tripDateChange: function (a, b) {
        var tripBeginDate = this.view.getCmp("tripBeginDate").gotValue();
        var tripEndDate = this.view.getCmp("tripEndDate").gotValue();
        if (tripBeginDate != null && tripEndDate != null) {
            this.view.getCmp("fadInvoiceDto->tripDays").sotValue((tripEndDate - tripBeginDate) / 86400000 + 1);
        }
    },
    setFieldEditable: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadInvoiceDto").sotEditable(false);
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

        var fadInvoiceTravelGrid = view.getCmp("fadInvoiceTravelDto");
        fadInvoiceTravelGrid.getCmp("toolbar->addRowBtn").disable();
        fadInvoiceTravelGrid.getCmp("toolbar->delRowBtn").disable();
        fadInvoiceTravelGrid.getCmp("toolbar->copyAddRowBtn").disable();
        fadInvoiceTravelGrid.on('beforeedit', function (editor, eventObj) {
            var field = eventObj.field;
            if (field == 'approvedMoney' || Scdp.Const.UI_INFO_STATUS_NEW == view.getUIStatus()) {
                return true;
            } else {
                return false;
            }
        });

        //Ext.apply(fadInvoiceTravelGrid, {
        //    rowColorConfigFn: function (record) {
        //        //被其他选中
        //        //if (index.field == "approvedMoney") {
        //            return 'erp-grid-edit-color-red';
        //        //}z
        //    }
        //});

        var fadInvoiceSubsidyGrid = view.getCmp("fadInvoiceSubsidyDto");
        fadInvoiceSubsidyGrid.getCmp("toolbar->addRowBtn").disable();
        fadInvoiceSubsidyGrid.getCmp("toolbar->delRowBtn").disable();
        fadInvoiceSubsidyGrid.getCmp("toolbar->copyAddRowBtn").disable();
        fadInvoiceSubsidyGrid.on('beforeedit', function (editor, eventObj) {
            var field = eventObj.field;
            if (field == 'standard' || Scdp.Const.UI_INFO_STATUS_NEW == view.getUIStatus()) {
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
        if (invoiceTotalValue == 0) {
            Scdp.MsgUtil.info("报销金额不能为0！");
            return result;
        }
        var tripDays = view.getCmp("fadInvoiceDto->tripDays").gotValue();
        if (tripDays <= 0) {
            Scdp.MsgUtil.info("请检查出差日期是否有误！");
            return result;
        }

        //M4_C9_F1_选择冲请款
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

        var expensesMoney = view.getCmp("fadInvoiceDto->expensesMoney").gotValue();
        var totalClearanceMoney = view.getCmp("fadInvoiceDto->totalClearanceMoney").gotValue();
        if (totalClearanceMoney > expensesMoney) {
            Scdp.MsgUtil.info("核销金额合计不能比报销金额合计大！");
            return result;
        }
        //*****************后台校验开始*******************//
        var payCash = view.getCmp("fadInvoiceDto->payCash").gotValue();
        if (payCash > 0) {
            result = me.validationInBackground();//后台校验
            return result;
        }
        //******************后台校验结束******************//
        return true
    },
    validationInBackground: function () {//后台校验
        var me = this;
        var view = me.view;
        var subjectId = view.getCmp("fadInvoiceDto->subjectId").gotValue();
        var userCode = view.getCmp("fadInvoiceDto->renderPerson").gotValue();
        var notInRow = ""
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fadCashReqInvoiceGrid.getStore().getCount()
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
        me.setSupplierNameCmpEditable();
    },
    setSupplierNameCmpEditable: function () {
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
        view.getCmp("fadInvoiceDto->expensesType").sotValue(2);
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
        var printFn = function () {
            Scdp.printReport("erp/prm/BusinessTripClaims.cpt", [param], false, "pdf");
        };
        var task = new Ext.util.DelayedTask(printFn);
        task.delay(3000);
        Scdp.printReport("erp/prm/FadInvoiceExpensePaste.cpt", [param], false, "pdf");

    },
    doPrintBusinessTrip: function () {
        var me = this;
        var view = me.view;
        var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
        var param = {invoiceReqNo: invoiceReqNo.gotValue()};
        Scdp.printReport("erp/prm/BusinessTripClaims.cpt", [param], false, "pdf");
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
    updateRemainBudget: function (postData) {//更新当前可用金额
        var me = this;
        me.fadYearChange();
    },
    afterOfficeIdSotValue: function (postData) {//更新当前可用金额
        var view = this.view;
        var officeIdCmd = view.getCmp("fadInvoiceDto->officeId");
        var fadYearCmd = view.getCmp("fadInvoiceDto->fadYear");
        officeIdCmd.afterSotValue = function () {
            var officeId = officeIdCmd.gotValue();
            var fadYear = fadYearCmd.gotValue();
//        var projectId = view.getCmp("fadInvoiceDto->prmProjectMainId").value;
            if (Scdp.ObjUtil.isNotEmpty(officeId) && Scdp.ObjUtil.isNotEmpty(fadYear)) {
                var postData = {isPrm: "0", officeId: officeId, fadYear: fadYear};
                Scdp.doAction("invoice-getremainbudget", postData, function (retData) {
                    view.getCmp("fadInvoiceDto->remainBudget").sotValue(retData.remainBudget);
                    view.getCmp("fadInvoiceDto->subjectId").sotValue(retData.subjectId);
                });
            }
        }
    },
    fadYearChange: function () {
        var me = this;
        var view = this.view;
        var officeIdCmd = view.getCmp("fadInvoiceDto->officeId");
        var fadYearCmd = view.getCmp("fadInvoiceDto->fadYear");
        var officeId = officeIdCmd.gotValue();
        var fadYear = fadYearCmd.gotValue();
        if (Scdp.ObjUtil.isNotEmpty(officeId) && Scdp.ObjUtil.isNotEmpty(fadYear)) {
            var postData = {isPrm: "0", officeId: officeId, fadYear: fadYear};
            Scdp.doAction("invoice-getremainbudget", postData, function (retData) {
                view.getCmp("fadInvoiceDto->remainBudget").sotValue(retData.remainBudget);
                view.getCmp("fadInvoiceDto->subjectId").sotValue(retData.subjectId);
            });
        }
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