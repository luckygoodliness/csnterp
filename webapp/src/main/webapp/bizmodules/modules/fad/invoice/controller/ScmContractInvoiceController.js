Ext.define("Invoice.controller.ScmContractInvoiceController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Invoice.view.ScmContractInvoiceView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'pickContract', name: 'click', fn: 'pickContract'},
        {cid: 'taxRate', name: 'change', fn: 'taxRateChange'},
        {cid: 'payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'fadInvoiceDto->bankId', name: 'change', fn: 'bankIdChange'},
        {cid: 'fadInvoiceDto->invoiceType', name: 'change', fn: 'invoiceTypeChange'},
        {cid: 'scmContractInvoiceDto', name: 'edit', fn: 'contractInvoiceGridChange'},
        {cid: 'batchWorkFlowCommit', name: 'click', fn: 'batchWorkFlowCommit'},
        {cid: 'batchExpensePaste', name: 'click', fn: 'batchExpensePaste'},
        {cid: 'Btn_annul', name: 'click', fn: 'clickAnnul'},
        {cid: 'Btn_certificate', name: 'click', fn: 'clickCertificate'},
        {cid: 'toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'expensePaste', name: 'click', fn: 'doPrintExpensePaste'},
        {cid: 'printMaterialRep', name: 'click', fn: 'doPrintMaterialRep'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.invoice.dto.FadInvoiceDto',
    queryAction: 'invoice-query',
    loadAction: 'invoice-load',
    addAction: 'invoice-add',
    modifyAction: 'invoice-modify',
    deleteAction: 'invoice-delete',
    exportXlsAction: "invoice-exportxls",
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.afterChangeUIStatus();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.afterChangeUIStatus();
        me.initialize();
        //me.setRenderPersonNameState();
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
        var view = me.view;
        view.afterChangeUIStatus();
        me.taxRateChange();
        var supplierCode = view.getCmp("fadInvoiceDto->supplierId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(supplierCode)) {
            view.getCmp("fadInvoiceDto->supplierName").sotEditable(false);
        }
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(retData.invoiceReqNo)) {
            me.view.getCmp("fadInvoiceDto->invoiceReqNo").sotValue(retData.invoiceReqNo);
        }
        var viewUuid = me.view.getCmp("fadInvoiceDto->uuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(viewUuid) && viewUuid != null && viewUuid != '') {
            me.loadItem(viewUuid);
        }
        view.afterChangeUIStatus();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.setBtnState();
        me.taxRateChange();
        me.controlBtnCertificate();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        var view = me.view;
        view.afterChangeUIStatus();
        me.taxRateChange();
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        var view = me.view;
        view.afterChangeUIStatus();
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
    payStyleChange: function () {
        var me = this;
        me.setRenderPersonNameState();

    },
    setRenderPersonNameState: function () {
        var me = this;
        var view = me.view;
        var payStyleValue = view.getCmp("fadInvoiceDto->payStyle").value;
        //var payeeCmp = view.getCmp("fadInvoiceDto->payee");
        //if (payStyle == "0" || payStyle == "2") {
        //    payeeCmp.sotEditable(true);
        //    payeeCmp.allowBlank = false;
        //} else {
        //    payeeCmp.sotValue();
        //    payeeCmp.sotEditable(false);
        //    payeeCmp.allowBlank = true;
        //}
        if (payStyleValue == "0" || payStyleValue == "2") {//如果付款方式为现金、电汇时 开户银行与银行账号可以为空
            me.view.getCmp("fadInvoiceDto->bank").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->bankId").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->payee").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->payee").sotEditable(true);
        } else {
            me.view.getCmp("fadInvoiceDto->bank").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->bankId").allowBlank = false;
            me.view.getCmp("fadInvoiceDto->payee").sotValue();
            me.view.getCmp("fadInvoiceDto->payee").allowBlank = true;
            me.view.getCmp("fadInvoiceDto->payee").sotEditable(false);
        }
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
    pickContract: function () {//相关合同弹出窗
        var me = this;
        var view = me.view;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            //var grid = subView.getQueryPanel().down("JQGrid");
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length > 0) {
                if (selectedRecords.length != 1) {
                    var supplier = '';
                    var subjectCode = '';
                    var supplierNull = false;
                    var isSameSupplier = true;
                    //var isSameSubjectCode = true;
                    //var isSameContractNature = true;
                    Ext.Array.each(selectedRecords, function (a) {
                        var rowData = a.data;
                        if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                            if (Scdp.ObjUtil.isEmpty(rowData.supplierCode)) {
                                supplierNull = true;
                            } else if (Scdp.ObjUtil.isNotEmpty(supplier) && supplier != rowData.supplierCode) {
                                isSameSupplier = false;
                            }
                            supplier = rowData.supplierCode;
                        }
                    });
                    if (!isSameSupplier) {
                        Scdp.MsgUtil.info("选择失败，所选合同属于不同供应商！");
                        return false;
                    }
                    if (supplierNull) {
                        Scdp.MsgUtil.confirm("存在供应商为空的合同，是否继续添加？", function (e) {
                            if ("yes" == e) {
                                me.addData(selectedRecords, me);
                                me.getMaxTaxRate(selectedRecords, me);
                                subView.up("window").close();
                            } else {
                                return false;
                            }
                        });
                    } else {
                        me.addData(selectedRecords, me);
                        me.getMaxTaxRate(selectedRecords, me);
                        return true;
                    }
                } else {
                    me.addData(selectedRecords, me);
                    me.getMaxTaxRate(selectedRecords, me);
                    return true;
                }

            } else {
                me.getMaxTaxRate(selectedRecords, me);
                return true;
            }
        };
        var notInRow = "";
        var fontractInvoiceGrid = view.getCmp("scmContractInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
                notInRow = notInRow + "'" + rowData.scmContractId + "',";
            }
        }
        notInRow = notInRow + "'.'";
        var queryController = Ext.create("Scmcontract.controller.ScmPickContractController");
        var param = {
            supplierId: me.view.getCmp("fadInvoiceDto->supplierId").gotValue(),
            supplierName: me.view.getCmp("fadInvoiceDto->supplierName").gotValue(),
            notInRow: notInRow
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '合同选择', null, true);
    },


    addData: function (selectedRecords, controller) {
        var me = controller;
        var view = me.view;
        var supplierCode = '';
        var supplierName = '';
        var bank = '';
        var bankId = '';
        var supplierIdCmp = view.getCmp("fadInvoiceDto->supplierId");
        var supplierNameCmp = view.getCmp("fadInvoiceDto->supplierName");
        var bankCmp = view.getCmp("fadInvoiceDto->bank");
        var bankIdCmp = view.getCmp("fadInvoiceDto->bankId");
        var fontractInvoiceGrid = view.getCmp("scmContractInvoiceDto");
        Ext.Array.each(selectedRecords, function (a) {
            var rowData = a.data;
            var c = fontractInvoiceGrid.getStore(),
                d = Ext.ModelManager.create({}, fontractInvoiceGrid.store.model.modelName);
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                if (Scdp.ObjUtil.isNotEmpty(rowData.supplierCode)) {
                    supplierCode = rowData.supplierCode;
                    supplierName = rowData.supplierNewName;
                    bank = rowData.bank;
                    bankId = rowData.bankId;
                }
                d.set("scmContractId", rowData.uuid);
                d.set("scmContractCode", rowData.scmContractCode);
                d.set("projectName", rowData.projectName);

                /*不改变原有显示逻辑：
                //取标记位isStamp
                var postData = {
                    fadInvoiceUuid: me.view.getCmp('fadInvoiceDto->uuid').gotValue(),
                    scmContractCode: rowData.scmContractCode
                };
                var actionResult = Scdp.doAction("invoice-getpurchasedetailstampprojectcode", postData, null, null, false, false);
                var scmContractMapList = actionResult.scmContractMapList;

                //如果标记位isStamp为1，则将标记源的课题代号覆盖到合同关联的采购明细的项目代号。
                Ext.Array.each(scmContractMapList, function (item) {
                            if (item.isStamp == "1") {
                                d.set("fadSubjectCode", item.projectCode);
                            }
                            else {
                                d.set("fadSubjectCode", rowData.subjectCodeQ);
                            }
                });*/
                d.set("fadSubjectCode", rowData.subjectCodeQ);
                d.set("subjectName", rowData.subjectName);
                d.set("officeName", rowData.orgName);
                d.set("officeId", rowData.officeId);
                d.set("contractTotalValue", rowData.amount);
                d.set("fadInvoiceMoneyL", rowData.fadInvoiceMoneyL);
                d.set("fadInvoiceMoney", rowData.fadInvoiceMoney);
                d.set("amount", rowData.fadInvoiceMoneyU);
                d.set("projectId", rowData.projectId);
            }
            d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);
            fontractInvoiceGrid.store.insert(c.getCount(), d);
            fontractInvoiceGrid.getSelectionModel().select(c.getCount() - 1)
        });

        if (Scdp.ObjUtil.isNotEmpty(supplierCode)) {
            supplierIdCmp.sotValue(supplierCode);
            supplierNameCmp.sotValue(supplierName);
            bankCmp.sotValue(bank);
            bankIdCmp.sotValue(bankId);
            supplierNameCmp.sotEditable(false);
            //bankCmp.sotEditable(false);
            //bankIdCmp.sotEditable(false);
        } else {
            supplierNameCmp.sotEditable(true);
            //bankCmp.sotEditable(true);
            //bankIdCmp.sotEditable(true);
        }

        me.updateString();
        me.contractInvoiceGridChange();
    },
    getMaxTaxRate: function (selectedRecords, controller) {
        var me = controller;

        //这里求出【项目税率】
        var selectedRecordsSelectionUuidList = [];
        for (var i = 0; i < selectedRecords.length; i++) {
            selectedRecordsSelectionUuidList.push(selectedRecords[i].data.uuid);
        }

        var postData = {
            selectedRecordsSelectionUuidList: selectedRecordsSelectionUuidList
        };
        Scdp.doAction("invoice-getmaxtaxrate", postData, function (result) {
            if (
                (Erp.Util.isNullReturnEmpty(result.maxTaxRate).length > 0)
                &&
                (Erp.Util.isNullReturnEmpty(result.maxTaxRateDesc).length > 0)
            ) {
                me.view.getCmp("fadInvoiceDto->taxRate").sotValue(result.maxTaxRate);
                me.view.getCmp("fadInvoiceDto->taxRateDesc").sotValue(result.maxTaxRateDesc);
                me.taxRateChange();
            }
        });
    },
    afterDeleteRow: function () {//删除明细数据后动作
        var me = this;
        var view = me.view;
        var fontractInvoiceGrid = this.view.getCmp("scmContractInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count == 0) {
            view.getCmp("fadInvoiceDto->supplierId").sotValue("");
            view.getCmp("fadInvoiceDto->supplierName").sotValue("");
            //view.getCmp("fadInvoiceDto->taxRegistrationNo").sotValue("");
            view.getCmp("fadInvoiceDto->officeId").sotValue("");
            //view.getCmp("fadInvoiceDto->prmProjectMainId").putValue("");
            //view.getCmp("fadInvoiceDto->projectName").sotValue("");
            //view.getCmp("fadInvoiceDto->subjectId").sotValue("");
            view.getCmp("fadInvoiceDto->subjectName").sotValue("");
            //view.getCmp("fadInvoiceDto->subjectCode").sotValue("");
            view.getCmp("fadInvoiceDto->fadSubjectCode").sotValue("");
            view.getCmp("fadInvoiceDto->scmContractCode").sotValue("");
            view.getCmp("fadInvoiceDto->bank").sotValue("");
            view.getCmp("fadInvoiceDto->bankId").sotValue("");
//            view.getCmp("fadInvoiceDto->renderPerson").sotValue("");
//            view.getCmp("fadInvoiceDto->renderPersonName").sotValue("");
        } else if (count > 0) {
            me.updateString();
        }
        this.contractInvoiceGridChange();
    },
    updateString: function () {//更新用逗号隔开的字段
        var me = this;
        var view = me.view;
        var fontractInvoiceGrid = view.getCmp("scmContractInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        var scmContractCode = '';
        var fadSubjectCode = '';
        var subjectName = '';
        var officeId = '';
        var officeName = '';
        for (var i = 0; i < count; i++) {
            var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
            if (i == 0) {
                scmContractCode = rowData.scmContractCode;
                fadSubjectCode = rowData.fadSubjectCode;
                subjectName = rowData.subjectName;
                officeId = rowData.officeId;
                officeName = rowData.officeName;
            } else {
                scmContractCode += ',' + rowData.scmContractCode;
                if (Scdp.ObjUtil.isNotEmpty(fadSubjectCode) && fadSubjectCode.indexOf(rowData.fadSubjectCode) < 0) {
                    fadSubjectCode += ',' + rowData.fadSubjectCode;
                }
                if (Scdp.ObjUtil.isNotEmpty(subjectName) && subjectName.indexOf(rowData.subjectName) < 0) {
                    subjectName += ',' + rowData.subjectName;
                }
                if (Scdp.ObjUtil.isNotEmpty(officeName) && officeName.indexOf(rowData.officeName) < 0) {
                    officeName += ',' + rowData.officeName;
                }
                if (Scdp.ObjUtil.isNotEmpty(officeId) && officeId.indexOf(rowData.officeId) < 0) {
                    officeId += ',' + rowData.officeId;
                }
            }
        }
        view.getCmp("fadInvoiceDto->scmContractCode").sotValue(scmContractCode);
        view.getCmp("fadInvoiceDto->fadSubjectCode").sotValue(fadSubjectCode);
        view.getCmp("fadInvoiceDto->subjectName").sotValue(subjectName);
        view.getCmp("fadInvoiceDto->officeName").sotValue(officeName);
        view.getCmp("fadInvoiceDto->officeId").sotValue(officeId);
    },
    validation: function (selectedRecords) {//校验选择的是否是同一供应商
        var supplier = '';
        var isSame = true;
        Ext.Array.each(selectedRecords, function (a) {
            var rowData = a.data;
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                if (supplier != '' && supplier != rowData.supplierCode) {
                    isSame = false;
                }
                supplier = rowData.supplierCode;
            }
        });
        return isSame;
    },
    contractInvoiceGridChange: function () {//更新发票总额
        var invoiceValue = 0;
        var fontractInvoiceGrid = this.view.getCmp("scmContractInvoiceDto");
        var count = fontractInvoiceGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = fontractInvoiceGrid.getStore().getAt(i).data;
                invoiceValue = Erp.MathUtil.plusNumber(invoiceValue, rowData.amount);
            }
        }
        this.view.getCmp("fadInvoiceDto->invoiceTotalValue").sotValue(invoiceValue);
        this.view.getCmp("fadInvoiceDto->expensesMoney").sotValue(invoiceValue);
        this.taxRateChange();
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
    initialize: function () {
        var view = this.view;
        view.getCmp("fadInvoiceDto->invoiceNum").sotValue(1);
        view.getCmp("fadInvoiceDto->renderPerson").putValue(Scdp.getCurrentUserId());
        //view.getCmp("fadInvoiceDto->renderPerson").sotValue(Scdp.getCurrentUserId());
        //view.getCmp("fadInvoiceDto->renderPersonName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp("fadInvoiceDto->state").sotValue("0");
        view.getCmp("fadInvoiceDto->payStyle").sotValue("1");
        view.getCmp("fadInvoiceDto->expensesType").sotValue(0);
    },
    //doPrintExpensePaste: function () {
    //    var me = this;
    //    var view = me.view;
    //    var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
    //    var param = {invoiceReqNo: invoiceReqNo.gotValue()};
    //    var printFn = function () {
    //        var contractItem = view.getCmp("scmContractInvoiceDto").store.data.items;
    //        var projectIds = [];
    //        for (var i = 0; i < contractItem.length; i++) {
    //            var projectId = contractItem[i].data.projectId;
    //            if (projectIds.indexOf(projectId) == -1) {
    //                projectIds.push(projectId);
    //            }
    //        }
    //        var j = 1;
    //        Ext.Array.each(projectIds, function (item) {
    //            (function (item, cnt) {
    //                setTimeout(function () {j
    //                    var paramReport = {invoiceReqNo: invoiceReqNo.gotValue(), projectId: item};
    //                    Scdp.printReport("erp/prm/MaterialReceipt.cpt", [paramReport], false, "pdf");
    //                }, Scdp.Const.REPORT_DEFAULT_PRINT_INTERVAL * cnt);
    //            })(item, j);
    //            j += 1;
    //        });
    //    };
    //    var task = new Ext.util.DelayedTask(printFn);
    //    task.delay(500);
    //    Scdp.printReport("erp/prm/FadInvoiceExpensePaste.cpt", [param], false, "pdf");
    //},
    doPrintExpensePaste: function () {
        var me = this;
        var view = me.view;
        //取标记位isStamp
        var postData = {
            fadInvoiceUuid: me.view.getCmp('fadInvoiceDto->uuid').gotValue()
        };
        var actionResult = Scdp.doAction("invoice-getprmpurchasereqdetailstamp", postData, null, null, false, false);
        var scmContractMapList = actionResult.scmContractMapList;
        var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
        var param = {invoiceReqNo: invoiceReqNo.gotValue()};
        var printFn = function () {
            var j = 1;
            Ext.Array.each(scmContractMapList, function (item) {
                (function (item, cnt) {
                    setTimeout(function () {
                        var paramReport = {invoiceReqNo: invoiceReqNo.gotValue(), scmContractId: item.scmContractId};
                        if (item.isStamp == "1") {
                            Scdp.printReport("erp/prm/MaterialReceiptStamp.cpt", [paramReport], false, "pdf");
                        }
                        else {
                            Scdp.printReport("erp/prm/MaterialReceipt.cpt", [paramReport], false, "pdf");
                        }
                    }, Scdp.Const.REPORT_DEFAULT_PRINT_INTERVAL * (cnt + scmContractMapList.length));
                })(item, j);
                j += 1;
            });
        };
        var task = new Ext.util.DelayedTask(printFn);
        task.delay(500);
        //标记源的课题代号为空时取原始课题代号；标记源课题代号不为空时及取对应值，通过sql做了判断处理。
        Scdp.printReport("erp/prm/FadInvoiceExpensePasteStamp.cpt", [param], false, "pdf");
    },

    //doPrintMaterialRep: function () {
    //    var me = this;
    //    var view = me.view;
    //    var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
    //    var contractItem = view.getCmp("scmContractInvoiceDto").store.data.items;
    //    var projectIds = [];
    //    for (var i = 0; i < contractItem.length; i++) {
    //        var projectId = contractItem[i].data.projectId;
    //        if (projectIds.indexOf(projectId) == -1) {
    //            projectIds.push(projectId);
    //        }
    //    }
    //    var j = 1;
    //    Ext.Array.each(projectIds, function (item) {
    //        (function (item, cnt) {
    //            setTimeout(function () {
    //                var paramReport = {invoiceReqNo: invoiceReqNo.gotValue(), projectId: item};
    //                Scdp.printReport("erp/prm/MaterialReceipt.cpt", [paramReport], false, "pdf");
    //            }, Scdp.Const.REPORT_DEFAULT_PRINT_INTERVAL * (cnt + projectIds.length));
    //        })(item, j);
    //        j += 1;
    //    });
    //},
    doPrintMaterialRep: function () {
        var me = this;
        var view = me.view;

        //取标记位isStamp
        var postData = {
            fadInvoiceUuid: me.view.getCmp('fadInvoiceDto->uuid').gotValue()
        };
        var actionResult = Scdp.doAction("invoice-getprmpurchasereqdetailstamp", postData, null, null, false, false);
        var scmContractMapList = actionResult.scmContractMapList;
        var invoiceReqNo = view.getCmp("fadInvoiceDto->invoiceReqNo");
        var j = 1;
        Ext.Array.each(scmContractMapList, function (item) {
            (function (item, cnt) {
                setTimeout(function () {
                    var paramReport = {invoiceReqNo: invoiceReqNo.gotValue(), scmContractId: item.scmContractId};
                    if (item.isStamp == "1") {
                        Scdp.printReport("erp/prm/MaterialReceiptStamp.cpt", [paramReport], false, "pdf");
                    }
                    else {
                        Scdp.printReport("erp/prm/MaterialReceipt.cpt", [paramReport], false, "pdf");
                    }
                }, Scdp.Const.REPORT_DEFAULT_PRINT_INTERVAL * (cnt + scmContractMapList.length));
            })(item, j);
            j += 1;
        });
    },


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
    //银行账号改变，自动带出银行名称
    bankIdChange: function (obj) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(obj.displayTplData)) {
            me.view.getCmp("fadInvoiceDto->bank").sotValue(obj.displayTplData[0].codedesc);
            var bank = me.view.getCmp("fadInvoiceDto->bank");
            bank.sotValue(obj.displayTplData[0].codedesc);
        }
    },
    //批量提交
    batchWorkFlowCommit: function () {
        var me = this;
        var fadInvoiceGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadInvoiceGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要提交的行!");
        }
        else {
            var isContinue = true;
            for (var i = 0; i < fadInvoiceGridSelection.length; i++) {
                //if (fadInvoiceGridSelection[i].data.stateName == "已生成凭证") {
                //    isContinue = false;
                //    Scdp.MsgUtil.warn("请勿选择【工作流状态】为【已生成凭证】的【发票】!");
                //    return;
                //}
                //else if (fadInvoiceGridSelection[i].data.stateName == "已提交") {
                //    isContinue = false;
                //    Scdp.MsgUtil.warn("请勿选择【工作流状态】为【已提交】的【发票】!");
                //    return;
                //}
                //else if (fadInvoiceGridSelection[i].data.stateName == "已审核") {
                //    isContinue = false;
                //    Scdp.MsgUtil.warn("请勿选择【工作流状态】为【已审核】的【发票】!");
                //    return;
                //}
                //else if (fadInvoiceGridSelection[i].data.stateName == "已发NC") {
                //    isContinue = false;
                //    Scdp.MsgUtil.warn("请勿选择【工作流状态】为【已发NC】的【发票】!");
                //    return;
                //}
                //else if (fadInvoiceGridSelection[i].data.stateName == "作废") {
                //    isContinue = false;
                //    Scdp.MsgUtil.warn("请勿选择【工作流状态】为【作废】的【发票】!");
                //    return;
                //}
                if (fadInvoiceGridSelection[i].data.stateName != "新增") {
                    isContinue = false;
                    Scdp.MsgUtil.warn("请选择【工作流状态】为【新增】的【发票】!");
                    return;
                }
                else {
                    isContinue = true;
                }
            }

            if (isContinue) {
                Scdp.MsgUtil.confirm("是否提交发票?请确认!", function (e) {
                    if (e == "yes") {
                        var fadInvoiceGridSelectionUuidList = [];
                        var fadInvoiceGridSelectionInvoiceReqNoList = [];
                        var fadInvoiceGridSelectionCreateTimeList = [];
                        var fadInvoiceGridSelectionTblVersionList = [];
                        for (var i = 0; i < fadInvoiceGridSelection.length; i++) {
                            fadInvoiceGridSelectionUuidList.push(fadInvoiceGridSelection[i].data.uuid);
                            fadInvoiceGridSelectionInvoiceReqNoList.push(fadInvoiceGridSelection[i].data.invoiceReqNo);
                            fadInvoiceGridSelectionCreateTimeList.push(fadInvoiceGridSelection[i].data.createTime);
                            fadInvoiceGridSelectionTblVersionList.push(fadInvoiceGridSelection[i].data.tblVersion);
                        }

                        /*for (var i = 0; i < fadInvoiceGridSelectionUuidList.length; i++) {
                         for (var y = i; y < fadInvoiceGridSelectionUuidList.length; y++) {
                         if (fadInvoiceGridSelectionCreateTimeList[i] > fadInvoiceGridSelectionCreateTimeList[y]) {
                         var fadInvoiceGridSelectionUuid = fadInvoiceGridSelectionUuidList[i];
                         var fadInvoiceGridSelectionInvoiceReqNo = fadInvoiceGridSelectionInvoiceReqNoList[i];
                         var fadInvoiceGridSelectionCreateTime = fadInvoiceGridSelectionCreateTimeList[i];
                         var fadInvoiceGridSelectionTblVersion = fadInvoiceGridSelectionTblVersionList[i];

                         fadInvoiceGridSelectionUuidList[i] = fadInvoiceGridSelectionUuidList[y];
                         fadInvoiceGridSelectionInvoiceReqNoList[i] = fadInvoiceGridSelectionInvoiceReqNoList[y];
                         fadInvoiceGridSelectionCreateTimeList[i] = fadInvoiceGridSelectionCreateTimeList[y];
                         fadInvoiceGridSelectionTblVersionList[i] = fadInvoiceGridSelectionTblVersionList[y];

                         fadInvoiceGridSelectionUuidList[y] = fadInvoiceGridSelectionUuid;
                         fadInvoiceGridSelectionInvoiceReqNoList[y] = fadInvoiceGridSelectionInvoiceReqNo;
                         fadInvoiceGridSelectionCreateTimeList[y] = fadInvoiceGridSelectionCreateTime;
                         fadInvoiceGridSelectionTblVersionList[y] = fadInvoiceGridSelectionTblVersion;
                         }
                         }
                         }*/

                        var postData = {
                            fadInvoiceGridSelectionUuidList: fadInvoiceGridSelectionUuidList,
                            fadInvoiceGridSelectionInvoiceReqNoList: fadInvoiceGridSelectionInvoiceReqNoList,
                            fadInvoiceGridSelectionTblVersionList: fadInvoiceGridSelectionTblVersionList
                        };
                        Scdp.doAction("invoice-batchworkflowcommit", postData, function (result) {
                            if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                                //Scdp.MsgUtil.warn(result.errorMsg);
                                alert(result.errorMsg);
                            }
                            else {
                                //me.doReset();
                                me.doQuery();
                                Scdp.MsgUtil.info("提交发票成功!");
                            }
                        });
                    }
                });
            }
        }
    },
    //批量打印
    batchExpensePaste: function () {
        var me = this;
        var fadInvoiceGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadInvoiceGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要打印的行!");
        }
        else {
            var param = {invoiceReqNo: ''};
            for (var i = 0; i < fadInvoiceGridSelection.length; i++) {
                var invoiceReqNo = fadInvoiceGridSelection[i].data.invoiceReqNo;
                if (Erp.Util.isNullReturnEmpty(param.invoiceReqNo).length > 0) {
                    param.invoiceReqNo = param.invoiceReqNo + '|' + invoiceReqNo;
                }
                else {
                    param.invoiceReqNo = invoiceReqNo;
                }
            }
            if (Erp.Util.isNullReturnEmpty(param.invoiceReqNo).length > 0) {
                param.invoiceReqNo = '|' + param.invoiceReqNo + '|';
                Scdp.printReport("erp/prm/FadInvoice_batch_main.cpt", [param], false, "pdf");
            }
        }
    }
});