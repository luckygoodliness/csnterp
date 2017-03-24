Ext.define("Invoice.controller.FadBusinessTripClaimsController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Invoice.view.FadBusinessTripClaimsView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'fadCashReqInvoiceDto', name: 'edit', fn: 'fadCashReqInvoiceGridChange'},
        {cid: 'fadInvoiceSubsidyDto', name: 'edit', fn: 'fadInvoiceSubsidyGridChange'},
        {cid: 'fadInvoiceTravelDto', name: 'edit', fn: 'fadInvoiceTravelGridChange'},
        {cid: 'tripBeginDate', name: 'change', fn: 'tripDateChange'},
        {cid: 'tripEndDate', name: 'change', fn: 'tripDateChange'},
        {cid: 'renderPerson', name: 'change', fn: 'renderPersonChange'}
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
        me.updateSubsidyMoney();
        me.updateClearanceMoney();
    },
    beforeSave: function () {
        var me = this;
        var result = true;
        result = me.validation();
        return result;
    },
    afterSave: function (retData) {
        var me = this;
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
        me.updateSubsidyMoney();
        me.updateClearanceMoney();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.updateSubsidyMoney();
        me.updateClearanceMoney();
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
    renderPersonChange: function () {
        this.clearFadCashReqInvoiceGrid();
    },
    clearFadCashReqInvoiceGrid: function () {
        var fadCashReqInvoiceGrid = this.view.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.store.removeAll();
        this.updateClearanceMoney();
    },
    pickCashReq: function () {//相关合同弹出窗
        var me = this;
        var view = me.view;

        var renderPerson = me.view.getCmp("fadInvoiceDto->renderPerson").gotValue();
        if (renderPerson == null || renderPerson == '') {
            Scdp.MsgUtil.info("请先选择出差人！");
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
        var param = {renderPerson: renderPerson, renderPersonName: me.view.getCmp("fadInvoiceDto->renderPersonName").gotValue(), notInRow: notInRow, reqType: 2};
        queryController.actionParams = param;

        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');

    },
    fadCashReqInvoiceGridChange: function () {//请款核销grid改变时
        this.updateClearanceMoney();//更新原请款金额、核销总额
    },
    updateClearanceMoney: function () {//更新原请款金额、核销总额
        var totalClearanceMoney = 0
        var cashReqMoney = 0
        var fontractInvoiceGrid = this.view.getCmp("fadCashReqInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
                totalClearanceMoney = totalClearanceMoney + rowData.clearanceMoney;
                cashReqMoney = cashReqMoney + rowData.reqMoney;
            }
        }
        this.view.getCmp("fadInvoiceDto->totalClearanceMoney").sotValue(totalClearanceMoney);//更新核销金额合计
        this.view.getCmp("fadInvoiceDto->cashReqMoney").sotValue(cashReqMoney);//更新原请款金额合计
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
        var subsidyMoney = 0
        var fadInvoiceSubsidyGrid = this.view.getCmp("fadInvoiceSubsidyDto");
        var count = fadInvoiceSubsidyGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fadInvoiceSubsidyGrid.getStore().getAt(i).data;
                subsidyMoney = subsidyMoney + rowData.money;
            }
        }
        this.view.getCmp("fadInvoiceDto->subsidyMoney").sotValue(subsidyMoney);//更新补助合计
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
        var invoiceTotalValue = 0
        var expensesMoney = 0
        var fadInvoiceTravelGrid = this.view.getCmp("fadInvoiceTravelDto");
        var count = fadInvoiceTravelGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var row = fadInvoiceTravelGrid.getStore().getAt(i)
                var rowData = row.data;
                invoiceTotalValue = invoiceTotalValue + rowData.invoiceMoney;
                expensesMoney = expensesMoney + rowData.approvedMoney;
            }
        }
        this.view.getCmp("fadInvoiceDto->invoiceTotalValue").sotValue(invoiceTotalValue);//更新发票总金额
        this.view.getCmp("fadInvoiceDto->expensesMoney").sotValue(expensesMoney);//更新报销金额
    },

    tripDateChange: function (a, b) {
        var tripBeginDate = this.view.getCmp("tripBeginDate").value
        var tripEndDate = this.view.getCmp("tripEndDate").value

        if (tripBeginDate != null && tripEndDate != null) {
            this.view.getCmp("fadInvoiceDto->tripDays").sotValue((tripEndDate - tripBeginDate) / 86400000 + 1);
        }
//        this.view.getCmp("subjectCode").sotValue(2);
    },
    validation: function () {//前台校验
        var me = this.view;
        var tripDays = me.getCmp("fadInvoiceDto->tripDays").value;
        if (tripDays <= 0) {
            Scdp.MsgUtil.info("请检查出差日期是否有误！");
            return false;
        }
        var expensesMoney = me.getCmp("fadInvoiceDto->expensesMoney").value;
        var totalClearanceMoney = me.getCmp("fadInvoiceDto->totalClearanceMoney").value;
        if (totalClearanceMoney > expensesMoney) {
            Scdp.MsgUtil.info("核销金额合计不能比报销金额合计大！");
            return false;
        }
        return true
    },
    initialize: function () {
        this.view.getCmp("expensesType").sotValue(2);
//        this.view.getCmp("subjectCode").sotValue(2);

    }
});