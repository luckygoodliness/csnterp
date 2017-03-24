Ext.define("Invoice.controller.FadFailyClaimsController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Invoice.view.FadFailyClaimsView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'taxRate', name: 'change', fn: 'taxRateChange'},
        {cid: 'fadCashReqInvoiceDto', name: 'edit', fn: 'fadCashReqInvoiceGridChange'},
        {cid: 'renderPerson', name: 'change', fn: 'renderPersonChange'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto',
    queryAction: 'invoice-query',
    loadAction: 'failyinvoice-load',
    addAction: 'failyinvoice-add',
    modifyAction: 'failyinvoice-modify',
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
    },
    beforeSave: function () {
        var me = this;
        var result = true;
        result = me.validation();
        return result;
    },
    afterSave: function (retData) {
        var me = this;
        me.updateClearanceMoney();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.updateClearanceMoney();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
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
            Scdp.MsgUtil.info("请先选择报销人！");
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
        var param = {renderPerson: renderPerson, renderPersonName: me.view.getCmp("fadInvoiceDto->renderPersonName").gotValue(), notInRow: notInRow, reqType: 1};
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
        this.validation();
    },

    validation: function () {//前台校验
        var me = this.view;
        var expensesMoney = me.getCmp("fadInvoiceDto->expensesMoney").value;
        var totalClearanceMoney = me.getCmp("fadInvoiceDto->totalClearanceMoney").value;
        var invoiceTotalValue = me.getCmp("fadInvoiceDto->invoiceTotalValue").value;
        if (expensesMoney > invoiceTotalValue) {
            Scdp.MsgUtil.info("报销金额不能比发票金额大！");
            return false;
        }else  if (totalClearanceMoney > expensesMoney) {
            Scdp.MsgUtil.info("核销金额合计不能比报销金额大！");
            return false;
        }
        return true;
    },
    initialize: function () {
        this.view.getCmp("expensesType").sotValue(1);
//        this.view.getCmp("subjectCode").sotValue(2);
    }
});