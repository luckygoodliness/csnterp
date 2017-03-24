Ext.define("Cashclearance.controller.CashclearanceController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Cashclearance.view.CashclearanceView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'Btn_annul', name: 'click', fn: 'clickAnnul'},
        {cid: 'Btn_certificate', name: 'click', fn: 'clickCertificate'},
        {cid: 'toCertificate', name: 'click', fn: 'toCertificate'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.cashclearance.dto.FadCashClearanceDto',
    queryAction: 'cashclearance-query',
    loadAction: 'cashclearance-load',
    addAction: 'cashclearance-add',
    modifyAction: 'cashclearance-modify',
    deleteAction: 'cashclearance-delete',
    exportXlsAction: "cashclearance-exportxls",
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
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.callParent(arguments);
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
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(retData.runningNo)) {
            me.view.getCmp("fadCashClearanceDto->runningNo").sotValue(retData.runningNo);
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
        me.controlBtnCertificate();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
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
    setBtnState: function () {
        var me = this;
        var view = me.view;
        var stateCmp = me.view.getCmp("fadCashClearanceDto->state");
        if (stateCmp.value == 2) {
            view.getCmp("Btn_annul").setDisabled(false);
        } else {
            view.getCmp("Btn_annul").setDisabled(true);
        }
        if (stateCmp.value == 0 || stateCmp.value == 5) {
            view.getCmp("deleteBtn").setDisabled(false);
        } else {
            view.getCmp("deleteBtn").setDisabled(true);
        }
    },
    //点击废止按钮
    clickAnnul: function () {
        var me = this;
        var uuid = this.view.getCmp('fadCashClearanceDto->uuid').gotValue();
        if (uuid == "") {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        Scdp.MsgUtil.confirm("是否废止该报销？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("cashclearance-annul", postData, function (result) {
                    me.view.getCmp("fadCashClearanceDto->state").sotValue("3");
                    Scdp.MsgUtil.info("流水号" + result.invoiceReqNo + "废止成功!");
                })
            }
        });
    },
    pickCashReq: function () {
        var me = this;
        var view = me.view;
        var renderPerson = me.view.getCmp("fadCashClearanceDto->clearancePerson").gotValue();
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
            if (selectedRecords.length == 1 || (selectedRecords.length > 0 && me.validation(selectedRecords))) {
                var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
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
            } else if (selectedRecords.length > 0) {
                Scdp.MsgUtil.info("选择失败，所选请款属于不同费用科目！");
                return false;
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
            renderPersonName: me.view.getCmp("fadCashClearanceDto->clearancePersonName").gotValue(),
            notInRow: notInRow
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
    },
    validation: function (selectedRecords) {//校验选择的是否是同费用科目
        var budgetCUuid = '';
        var isSame = true;
        Ext.Array.each(selectedRecords, function (a) {
            var rowData = a.data;
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                if (budgetCUuid != '' && budgetCUuid != rowData.budgetCUuid) {
                    isSame = false;
                }
                budgetCUuid = rowData.budgetCUuid;
            }
        });
        return isSame;
    },
    fadCashReqInvoiceGridChange: function () {//请款核销grid改变时
        var me = this;
        me.updateClearanceMoney();//更新原请款金额、核销总额
    },
    updateClearanceMoney: function () {//更新原请款金额、核销总额
        var me = this;
        var view = me.view;
        var totalClearanceMoney = 0;
        var fontractInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
                totalClearanceMoney = totalClearanceMoney + rowData.clearanceMoney;
            }
        }
        view.getCmp("fadCashClearanceDto->totalMoney").sotValue(totalClearanceMoney);//更新核销金额合计
    },
    //点击生成凭证
    clickCertificate: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp('fadCashClearanceDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        Scdp.MsgUtil.confirm("是否生成凭证？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("cashclearance-createcertificate", postData, function (result) {
                    Scdp.MsgUtil.info("生成凭证成功!");
                    Erp.Util.gotoCertificateModule(result.fadCertificateUuid);
                })
            }
        });
    },
    //控制生成凭证按钮的显示
    controlBtnCertificate: function () {
        var me = this;
        var state = me.view.getCmp("fadCashClearanceDto->state").gotValue();
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
        var uuid = view.getCmp('fadCashClearanceDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    }
});