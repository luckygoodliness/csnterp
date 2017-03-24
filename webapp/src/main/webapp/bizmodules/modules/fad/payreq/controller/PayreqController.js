Ext.define("Payreq.controller.PayreqController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Payreq.view.PayreqView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto',
    queryAction: 'payreq-query',
    loadAction: 'payreq-load',
    addAction: 'payreq-add',
    modifyAction: 'payreq-modify',
    deleteAction: 'payreq-delete',
    exportXlsAction: "payreq-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        var role = Erp.Util.getCurrentUserRoleName();
        me.isBizVp = false;
        me.isScmVp = false;
        me.isComVp = false;
        if (Scdp.ObjUtil.isNotEmpty(role)) {
            me.isBizVp = role.ROLE.indexOf("事业部分管支付领导") > -1;
            me.isScmVp = role.ROLE.indexOf("供应链部主任") > -1;
            me.isComVp = role.ROLE.indexOf("公司领导") > -1;
        }

        me.view.getCmp("fadPayReqCDto").on('select', function (grid, record, index) {
            setToCertificate(me.view.getCmp("fadPayReqCDto"), me);
        });
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        var reqType = view.getCmp("fadPayReqHDto->reqType");
        reqType.sotValue("1");
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
        var resNos = "";
        me.view.getCmp("fadPayReqCDto").clearFilter();
        var recordModify = me.view.getCmp("fadPayReqCDto").store.getModifiedRecords();
        for (var i = 0; i < recordModify.length; i++) {
            var a = recordModify[i];
            var unPaidMoney = Erp.MathUtil.minusNumber(a.data.scmContractAmount, a.data.scmContractPaidMoney);
            if (Erp.MathUtil.minusNumber(unPaidMoney, a.data.auditMoney) < 0) {
                var index = me.view.getCmp("fadPayReqCDto").store.indexOf(a);
                resNos = resNos + (index + 1) + ",";
            }
        }

        if (resNos != "") {
            resNos = resNos.substr(0, resNos.length - 1);
            Scdp.MsgUtil.warn("序号 " + resNos + " 支付金额大于合同未付金额,保存失败！")
            return false;
        }
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        var view = me.view;
        if (Scdp.ObjUtil.isNotEmpty(retData.reqno)) {
            view.getCmp("fadPayReqHDto->reqno").sotValue(retData.reqno);
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
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if (state != '2') {
            if (view.getCmp("editPanel->payBtn")) {
                view.getCmp("editPanel->payBtn").setDisabled(true);
            }
            if (view.getCmp("editPanel->cashReqBtn")) {
                view.getCmp("editPanel->cashReqBtn").setDisabled(true);
            }
        } else {
            if (view.getCmp("editPanel->payBtn")) {
                view.getCmp("editPanel->payBtn").setDisabled(false);
            }
            if (view.getCmp("editPanel->cashReqBtn")) {
                view.getCmp("editPanel->cashReqBtn").setDisabled(false);
            }
        }
        view.getCmp("fadPayReqCDto").setCurRecord(0);
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
        var view = me.view;
        var rtnFlag = true;
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if (state != "0") {
            Scdp.MsgUtil.warn("非新增状态的支付，删除失败！")
            return false;
        } else {
            var store = view.getCmp("fadPayReqCDto").store;
            if (store.getCount() > 0) {
                for (var i = 0; i < store.getCount(); i++) {
                    var data = store.getAt(i).data;
                    if (data.state != "0") {
                        rtnFlag = false;
                        break;
                    }
                }
            }
            if (!rtnFlag) {
                Scdp.MsgUtil.warn("存在非新增状态的支付明细，删除失败！！")
            }
            return rtnFlag;
        }
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

    btnSearchClick: function () {
        var me = this;
        var view = me.view;
        var store = view.getCmp("fadPayReqCDto").store;
        store.clearFilter();
        var callBack = function (subView) {
            var orgName = subView.getCmp('officeIdPlusQDesc').gotValue();
            var departmentName = subView.getCmp('departmentNameQDesc').gotValue();
            var projectMainName = subView.getCmp('projectIdQ').gotValue();
            var supplierName = subView.getCmp('supplierName').gotValue();
            var checkedInvoiceEnough = subView.getCmp('checkedInvoiceEnough').gotValue();
            var isCheckedMoneyEnough = subView.getCmp('isCheckedMoneyEnough').gotValue();

            var fadInvoiceMoneyQfieldFrom = subView.getCmp('fadInvoiceMoneyQfieldFrom').gotValue();
            var fadInvoiceMoneyQfieldTo = subView.getCmp('fadInvoiceMoneyQfieldTo').gotValue();
            var scmPaidRateQfieldFrom = subView.getCmp('scmPaidRateQfieldFrom').gotValue();
            var scmPaidRateQfieldTo = subView.getCmp('scmPaidRateQfieldTo').gotValue();

            var auditMoneyQfieldFrom = subView.getCmp('auditMoneyQfieldFrom').gotValue();
            var auditMoneyQfieldTo = subView.getCmp('auditMoneyQfieldTo').gotValue();
            var needToPayMimusAuditMoneyQfieldFrom = subView.getCmp('needToPayMimusAuditMoneyQfieldFrom').gotValue();
            var needToPayMimusAuditMoneyQfieldTo = subView.getCmp('needToPayMimusAuditMoneyQfieldTo').gotValue();
            var stateQ = subView.getCmp('stateQ').gotValue();
            store.filter([
                {
                    filterFn: function (item) {
                        var flag = true;
                        if (flag && Scdp.ObjUtil.isNotEmpty(supplierName)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("supplierName")) && item.get("supplierName").indexOf(supplierName) >= 0
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(orgName)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("orgName")) && item.get("orgName").indexOf(orgName) >= 0
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(departmentName)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("departmentName")) && item.get("departmentName").indexOf(departmentName) >= 0
                        }

                        if (flag && Scdp.ObjUtil.isNotEmpty(projectMainName)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("projectMainName")) && item.get("projectMainName").indexOf(projectMainName) >= 0
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(fadInvoiceMoneyQfieldFrom)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("scmContractSupplierUnPaidMoney")) && item.get("scmContractSupplierUnPaidMoney") >= fadInvoiceMoneyQfieldFrom;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(fadInvoiceMoneyQfieldTo)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("scmContractSupplierUnPaidMoney")) && item.get("scmContractSupplierUnPaidMoney") <= fadInvoiceMoneyQfieldTo;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(scmPaidRateQfieldFrom)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("scmPaidRate")) && item.get("scmPaidRate") >= scmPaidRateQfieldFrom;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(scmPaidRateQfieldTo)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("scmPaidRate")) && item.get("scmPaidRate") <= scmPaidRateQfieldTo;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(auditMoneyQfieldFrom)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("auditMoney")) && item.get("auditMoney") >= auditMoneyQfieldFrom;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(auditMoneyQfieldTo)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("auditMoney")) && item.get("auditMoney") <= auditMoneyQfieldTo;
                        }

                        if (flag && Scdp.ObjUtil.isNotEmpty(needToPayMimusAuditMoneyQfieldFrom)) {
                            flag = flag && Erp.MathUtil.minusNumber(item.get("scmContractNeedToPay"), item.get("auditMoney")) >= needToPayMimusAuditMoneyQfieldFrom;

                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(needToPayMimusAuditMoneyQfieldTo)) {
                            flag = flag && Erp.MathUtil.minusNumber(item.get("scmContractNeedToPay"), item.get("auditMoney")) <= needToPayMimusAuditMoneyQfieldTo;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(stateQ)) {
                            flag = flag && Scdp.ObjUtil.isNotEmpty(item.get("state")) && item.get("state") == stateQ;
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(checkedInvoiceEnough)) {
                            if (checkedInvoiceEnough == '1') {
                                flag = flag && Erp.MathUtil.plusNumber(item.get("invoiceScmNeedCheck"), item.get("scmContractNeedToPay")) >= item.get("auditMoney");
                            } else {
                                flag = flag && Erp.MathUtil.plusNumber(item.get("invoiceScmNeedCheck"), item.get("scmContractNeedToPay")) < item.get("auditMoney");
                            }
                        }
                        if (flag && Scdp.ObjUtil.isNotEmpty(isCheckedMoneyEnough)) {
                            var paid = Scdp.ObjUtil.isEmpty(item.get("scmContractPaidMoney")) ? 0 : item.get("scmContractPaidMoney");
                            var checked = Scdp.ObjUtil.isEmpty(item.get("scmContractCheckedAmount")) ? 0 : item.get("scmContractCheckedAmount");
                            var applyed = Scdp.ObjUtil.isEmpty(item.get("auditMoney")) ? 0 : item.get("auditMoney");

                            if (isCheckedMoneyEnough == '1') {
                                flag = flag && (item.get("contractNature") !== "0" ||
                                    (Erp.MathUtil.minusNumber(Erp.MathUtil.minusNumber(checked, paid), applyed) >= 0));
                            } else {
                                flag = flag && item.get("contractNature") === "0" &&
                                    (Erp.MathUtil.minusNumber(Erp.MathUtil.minusNumber(checked, paid), applyed) < 0);
                            }
                        }

                        return flag;
                    }
                }
            ]);
            return true;
        };
        var form = Ext.widget("JForm", {
            height: 350,
            width: 600,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JHidden',
                    allowBlank: true,
                    fieldLabel: '所属部门',
                    cid: 'officeIdPlusQDesc',
                    emptyText: '所属部门'
                },
                {
                    xtype: 'JErpDepartMent',
                    descField: 'shortCode',
                    allowBlank: true,
                    fieldLabel: '所属部门',
                    cid: 'officeIdPlusQ',
                    emptyText: '所属部门'
                },
                {
                    xtype: 'JHidden',
                    allowBlank: true,
                    fieldLabel: '申请部门',
                    cid: 'departmentNameQDesc',
                    emptyText: '所属部门'
                },
                {
                    xtype: 'JErpDepartMent',
                    descField: 'shortCode',
                    allowBlank: true,
                    fieldLabel: '申请部门',
                    cid: 'departmentNameQ',
                    emptyText: '所属部门'
                },
                {
                    xtype: 'JText',
                    allowBlank: true,
                    fieldLabel: '项目名称',
                    cid: 'projectIdQ',
                    emptyText: '项目名称'
                },
                {
                    xtype: 'JSupplierGrid',
                    allowBlank: true,
                    fieldLabel: '供应商',
                    cid: 'supplierName',
                    emptyText: '供应商'
                },
                {
                    xtype: 'JFromTo',
                    subXtype: 'JDec',
                    fieldLabel: '总应付账款(元)',
                    minValue: '0.00',
                    cid: 'fadInvoiceMoneyQ'
                },
                {
                    xtype: 'JFromTo',
                    subXtype: 'JDec',
                    minValue: '0.00',
                    fieldLabel: '合同已付比例(%)',
                    cid: 'scmPaidRateQ'
                },
                {
                    xtype: 'JFromTo',
                    subXtype: 'JDec',
                    fieldLabel: '终审金额(元)',
                    minValue: '0.00',
                    cid: 'auditMoneyQ'
                },
                {
                    xtype: 'JFromTo',
                    subXtype: 'JDec',
                    fieldLabel: '应付款减终审(元)',
                    cid: 'needToPayMimusAuditMoneyQ'
                },
                {
                    xtype: 'JBolCombo',
                    labelWidth: 150,
                    fieldLabel: '(应付+待核)是否大于终审',
                    cid: 'checkedInvoiceEnough'
                },
                {
                    xtype: 'JCombo',
                    comboType: 'scdp_fmcode',
                    codeType: 'FAD_BILL_STATE',
                    displayDesc: true,
                    fieldLabel: '状态',
                    cid: 'stateQ'
                },
                {
                    xtype: 'JBolCombo',
                    labelWidth: 150,
                    fieldLabel: '到货确认金额是否足够',
                    cid: 'isCheckedMoneyEnough'
                }
            ]
        });
        Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '检索窗口', "查询");
    },

    exportDetailBtnClick: function () {
        var me = this;
        var view = me.view;
        var exportFileName = view.menuText + view.getCmp("fadPayReqHDto->reqno").gotValue();

        var tmpColumns = view.getCmp("fadPayReqCDto").columns;
        var columns = Ext.Array.filter(tmpColumns, function (column) {
            if (column.hidden === false && column.dataIndex) {
                return true;
            } else {
                return false;
            }
        });
        var queryResultColumnNames = [];
        var queryResultColumnFriendlyNames = [];
        var queryResultColumnWidths = [];
        for (var i = 0; i < columns.length; i++) {
            queryResultColumnNames.push(columns[i].dataIndex);
            queryResultColumnFriendlyNames.push(columns[i].text);
            queryResultColumnWidths.push(columns[i].width.toString());
        }

        var items = [];

        var store = view.getCmp("fadPayReqCDto").store.data.items;
        for (var i = 0; i < store.length; i++) {
            items.push(store[i].raw);
        }

        var param = {exportFileName: exportFileName,
            root: items,
            hideSeq: true,
            rootSize: items.length.toString(),
            queryResultColumnNames: queryResultColumnNames,
            queryResultColumnWidths: queryResultColumnWidths,
            queryResultColumnFriendlyNames: queryResultColumnFriendlyNames };
        Scdp.doPost("payreq-detail-export", param, function (result) {
        });
    },
    doPrintExpenseRequest: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();

        var param = {uuid: uuid};
        Scdp.printReport("erp/pay/PayReq.cpt", [param], false, "pdf");
    },

    btnSendMsgClick: function () {
        var me = this;
        var view = me.view;
        Scdp.MsgUtil.confirm("是否确认发送到货确认操作提示消息？<br>（只对选中的到货确认金额不足的合同）", function (e) {
            if ("yes" == e) {
                var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();
                var cids = [];
                var selectedRecords = view.getCmp("fadPayReqCDto").getSelectedRecords();
                Ext.Array.each(selectedRecords, function (a) {
                    cids.push(a.data.uuid);
                });
                var param = {action: "confirmationOfArrival", uuid: uuid, fadUUids: cids};
                Scdp.doAction("payreq-msg", param, function (result) {
                    Scdp.MsgUtil.info("发送成功！");
                });
            }
        });
    },

    doPay: function (a, b) {
        var me = this;
        var view = this.view;

        var selectedFadPayReqC = view.getCmp("fadPayReqCDto").getSelectedRecords();
        var hasAmoutNotEnough = true;
        var payData = [];
        for (var i = 0; i < selectedFadPayReqC.length; i++) {
            if (selectedFadPayReqC[i].data.state != "4" && selectedFadPayReqC[i].data.auditMoney > 0) {
                payData.push(selectedFadPayReqC[i].data.uuid);
                if (hasAmoutNotEnough && (selectedFadPayReqC[i].data.auditMoney > selectedFadPayReqC[i].data.scmContractNeedToPay )) {
                    hasAmoutNotEnough = false;
                }
            }
        }
        var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();
        if (!hasAmoutNotEnough) {
            Scdp.MsgUtil.showYesNoCancel("应付账款不足，是否自动生成费用请款信息？", function (e) {
                if (e != "cancel") {
                    var param = {payData: payData, uuid: uuid, reqRelated: e};
                    if (payData.length > 0) {
                        Scdp.doAction("payreq-pay", param, function (result) {
                            me.loadItem(uuid);
                            Scdp.MsgUtil.info("操作成功！");
                        });
                    }
                }
            });
        }
        else {
            var param = {payData: payData, uuid: uuid, reqRelated: "no"};
            if (payData.length > 0) {
                Scdp.doAction("payreq-pay", param, function (result) {
                    me.loadItem(uuid);
                    Scdp.MsgUtil.info("操作成功！");
                    if (Scdp.ObjUtil.isNotEmpty(result.fadCertificateUuid)) {
                        Erp.Util.gotoCertificateModule(result.fadCertificateUuid);
                    }
                });
            }
        }
    },


    btnAnalysisClick: function () {
        var me = this;
        var view = this.view;
        var selectedFadPayReqC = view.getCmp("fadPayReqCDto").getSelectedRecords();
        var supplierName = "";
        if (selectedFadPayReqC.length > 0) {
            supplierName = selectedFadPayReqC[0].data.supplierName;
        }
        var param = {supplierName: supplierName};
        var menuCode = 'SCM_PAYMENT_ANALYSIS';
        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    },
    doCashReq: function (a, b) {
        var me = this;
        var view = this.view;
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if (state == "2") {
            var selectedFadPayReqC = view.getCmp("fadPayReqCDto").getSelectedRecords();
            var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();
            var data = [];
            for (var i = 0; i < selectedFadPayReqC.length; i++) {
                data.push(selectedFadPayReqC[i].data.uuid);
            }
            var param = {reqData: data, uuid: uuid};
            Scdp.doAction("payreq-cashreq", param, function (result) {
                me.loadItem(uuid);
            });
        }
    },
//调出凭证
    toCertificate: function () {
        var me = this;
        var view = me.view;
        var selectedFadPayReqC = view.getCmp("fadPayReqCDto").getCurRecord();
        if (Scdp.ObjUtil.isEmpty(selectedFadPayReqC)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var uuid = selectedFadPayReqC.data.uuid;
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    },

    needContractAmountBiggerThenPaidMoney: function () {
        return '1';
    },
//相关合同弹出窗
    pickContract: function () {
        var me = this;
        var view = me.view;
        var notInRow = ""
        var dtoGrid = view.getCmp("fadPayReqCDto");
        for (var i = 0; i < dtoGrid.getStore().getCount(); i++) {
            if (Scdp.ObjUtil.isNotEmpty(dtoGrid.getStore().getAt(i).data.scmContractId))
                notInRow = notInRow + "'" + dtoGrid.getStore().getAt(i).data.scmContractId + "',"
        }

        var snapshot = me.view.getCmp("fadPayReqCDto").store.snapshot;
        if (Scdp.ObjUtil.isNotEmpty(snapshot)) {
            for (var j = 0; j < snapshot.items.length; j++) {
                notInRow = notInRow + "'" + snapshot.items[j].data.scmContractId + "',";
            }
        }
        notInRow = notInRow + "'.'";
        var needContractAmountBiggerThenPaidMoney = me.needContractAmountBiggerThenPaidMoney();
        var param = {notInRow: notInRow, needContractAmountBiggerThenPaidMoney: needContractAmountBiggerThenPaidMoney };
        var queryController = Ext.create("Scmcontract.controller.ScmPayPickContractController");

        queryController.actionParams = param;
        var billstate = view.getCmp("fadPayReqHDto->state").gotValue();
        var reqType = view.getCmp("fadPayReqHDto->reqType").gotValue();
        var statec = "0";
        if (reqType == "1") {
            if (!billstate || billstate == "0") {
                statec = (me.isScmVp || me.isBizVp) ? "7" : "0";
            }
            else if (billstate == "6") {
                statec = "1";
            }
        }

        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().ownerCt.getCmp('resultPanel');
            var selectedRecords = grid.getSelectedRecords();
            if (selectedRecords.length > 0) {
                var dtoGrid = view.getCmp("fadPayReqCDto");
                var fillMethod = subView.getQueryPanel().getCmp("fadMoneyFill").gotValue();
                var supplierCodeS = subView.getCmp('supplierCodeS').gotValue();

                var createDate = Ext.Date.format(new Date(), 'Y-m-d')
                var createByName = Scdp.CacheUtil.get(Scdp.Const.USER_NAME);
                var c = dtoGrid.getStore();
                var storeCount = dtoGrid.getStore().data.length;
                var lst = [];
                Ext.Array.each(selectedRecords, function (a) {
                    var rowData = a.data;
                    var store = dtoGrid.getStore();
                    storeCount = storeCount + 1;
                    var d = Ext.ModelManager.create({}, dtoGrid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                        d.set("state", statec);
                        d.set("orgName", rowData.orgName);
                        d.set("scmContractId", rowData.uuid);
                        d.set("scmContractCode", rowData.scmContractCode);

                        d.set("supplierId", rowData.supplierCode);
                        d.set("supplierName", rowData.supplierName);

                        d.set("projectMainId", rowData.projectId);
                        d.set("projectCode", rowData.fadSubjectCode);
                        d.set("projectMainName", rowData.projectName);
                        d.set("prmReceiptRate", rowData.prmReceiptRate);

                        d.set("scmContractAmount", rowData.amount);
                        d.set("scmContractPaidMoney", rowData.scmPaidMoney);
                        d.set("scmContractUnPaidMoney", rowData.scmUnpaidMoney);
                        d.set("scmPaidRate", rowData.scmPaidRate);

                        d.set("scmContractFadInvoiceMoney", rowData.fadInvoiceMoney);
                        d.set("scmContractNeedToPay", rowData.scmNeedPayMoney);

                        d.set("scmContractCheckedAmount", rowData.checkedMoney);
                        d.set("scmContractSupplierUnPaidMoney", rowData.scmSupplierUnPaidMoney);

                        d.set("scmContractCheckedAmount", rowData.checkedMoney);
                        if (Scdp.ObjUtil.isNotEmpty(fillMethod)) {
                            var reqMoney = 0;
                            var nonPaid = Erp.MathUtil.minusNumber(rowData.amount, rowData.scmPaidMoney);
                            if (fillMethod == "0") {
                                reqMoney = 0;
                            } else if (fillMethod == "1") {
                                var reqMoneyReferToPrmReceiptRate = Erp.MathUtil.divideNumber(Erp.MathUtil.multiNumber(rowData.prmReceiptRate, rowData.amount), 100);
                                reqMoney = Erp.MathUtil.minusNumber(reqMoneyReferToPrmReceiptRate, rowData.scmPaidMoney);
                            } else if (fillMethod == "2") {
                                reqMoney = Erp.MathUtil.minusNumber(rowData.fadInvoiceMoney, rowData.scmPaidMoney);
                            } else if (fillMethod == "3") {
                                reqMoney = nonPaid;
                            }
                            reqMoney = reqMoney > 0 ? reqMoney : 0;
                            var reqMoneyRate = 0;
                            if (Scdp.ObjUtil.isNotEmpty(rowData.amount)) {
                                reqMoneyRate = Erp.MathUtil.divideNumber(
                                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(reqMoney, rowData.scmPaidMoney, false), 100),
                                    rowData.amount);
                            }

                            d.set("scmContractExpectPaid", reqMoney + rowData.scmPaidMoney);

                            if (me.actionParams.type == "project") {
                                d.set("reqMoney", reqMoney);
                                d.set("reqMoneyRate", reqMoneyRate);
                                d.set("approveMoney", reqMoney);
                                d.set("approveMoneyRate", reqMoneyRate);
                            }
                            else {
                                d.set("purchaseManagerMoney", reqMoney);
                                d.set("purchaseManagerMoneyRate", reqMoneyRate);
                            }
                            d.set("purchaseChiefMoney", reqMoney);
                            d.set("purchaseChiefMoneyRate", reqMoneyRate);
                            d.set("auditMoney", reqMoney);
                            d.set("auditMoneyRate", reqMoneyRate);
                        }
                    }
                    d.set("createBy", Scdp.getCurrentUserId());
                    d.set("createByName", createByName);
                    d.set("departmentName", Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME));
                    d.set("createTime", createDate);

                    d.set("seqNo", storeCount);
                    supplierCodeS = supplierCodeS + ",'" + rowData.uuid + "'";
                    lst.push(d);
                });
                dtoGrid.store.insert(c.getCount(), lst);
                grid.getStore().remove(grid.getSelectionModel().getSelection());
                subView.getCmp('supplierCodeS').sotValue(supplierCodeS);
            }
        }
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
    }

})
;
//自定义方法
function setToCertificate(fadPayReqCDto, me) {
    var fadPayReqCGridRowDataCur = fadPayReqCDto.getCurRecord();
    if (fadPayReqCGridRowDataCur == null) {
        me.view.getCmp("editPanel->toCertificate") && me.view.getCmp("editPanel->toCertificate").setDisabled(true);
        return;
    }

    if (fadPayReqCGridRowDataCur.get("state") == "4" || fadPayReqCGridRowDataCur.get("state") == "8") {
        me.view.getCmp("editPanel->toCertificate") && me.view.getCmp("editPanel->toCertificate").setDisabled(false);
    }
    else {
        me.view.getCmp("editPanel->toCertificate") && me.view.getCmp("editPanel->toCertificate").setDisabled(true);
    }
}