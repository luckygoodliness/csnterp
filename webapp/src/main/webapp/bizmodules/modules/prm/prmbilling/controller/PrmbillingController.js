var isCognateContract = false;//M3_C11_F5_关联合同
Ext.define("Prmbilling.controller.PrmbillingController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmbilling.view.PrmbillingView',
    uniqueValidateFields: [],
    extraEvents: [
        //{cid: 'projectName', name: 'blur', fn: 'fill'},
        {cid: 'prmBillingDto->exchangeRate', name: 'change', fn: 'changeMoney'},
        {cid: 'prmBillingDto->invoiceMoney', name: 'change', fn: 'calTaxMoney'},
        {cid: 'prmBillingDto->taxRateName', name: 'change', fn: 'taxRateNameChange'},
        {cid: 'prmBillingDto->invoiceType', name: 'change', fn: 'taxRateChange'},
        //{cid: 'prmBillingDto->prmProjectMainId', name: 'change', fn: 'prmProjectMainIdChange'},
        {cid: 'prmBillingDto->bankAccount', name: 'change', fn: 'bankAccountChange'},
        {cid: 'editToolbar->certificate', name: 'click', fn: 'certificateClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'editToolbar->invoiceNoDateSet', name: 'click', fn: 'invoiceNoDateSetClick'},
        {cid: 'editToolbar->invoiceNoDateSave', name: 'click', fn: 'invoiceNoDateSaveClick'},
        {cid: 'editToolbar->prmBillingPrint', name: 'click', fn: 'prmBillingPrintClick'},
        {cid: 'locationType', name: 'change', fn: 'setPaymentStatus'},
        {cid: 'prmMakeReceiptsBtn', name: 'click', fn: 'fnMakeReceipts'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'prmBillingInvoiceBtn', name: 'click', fn: 'fnBillingInv'},
        {cid: 'prmInvalidInvoiceBtn', name: 'click', fn: 'fnInvalidInv'}, //M3_NC2_F1_开票申请作废
        {cid: 'cognateContract', name: 'click', fn: 'cognateContractFn'}//M3_C11_F5_关联合同
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto.PrmBillingDto',
    queryAction: 'prmbilling-query',
    loadAction: 'prmbilling-load',
    addAction: 'prmbilling-add',
    modifyAction: 'prmbilling-modify',
    deleteAction: 'prmbilling-delete',
    fillAction: 'prmbilling-fill',
    exportXlsAction: "prmbilling-exportxls",

    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        var uuid = me.view.getCmp("uuid").gotValue();
        me.controlFileButton();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmBillingDto->state").sotValue("0");
        me.controlFileButton();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var invoiceType = me.view.getCmp('invoiceType').gotValue();
        var taxRateName = me.view.getCmp("taxRateName");
        if (me.workFlowFormData.indexOf("wf_enable_modify_field=taxRateName") > -1) {
            me.view.sotEditable(false);
            taxRateName.sotEditable(true);
        }
        if (invoiceType == 3) {
            taxRateName.sotEditable(false);
        } else {
            taxRateName.sotEditable(true);
        }
        me.setPaymentStatus();

        me.view.getCmp("prmBillingDto->projectName").sotEditable(false);
        me.view.getCmp("prmBillingDto->taxNo").sotEditable(false);


        //M3_C7_F4_关联合同
        if (isCognateContract) {
            me.view.getCmp("prmBillingDto").sotEditable(false);
            me.view.getCmp("prmBillingDto->prmContractId").sotEditable(true);
            me.view.getCmp("prmBillingDto->expectReceiveDate").sotEditable(true);
            me.view.getCmp("prmBillingDetailDto").sotEditable(false);
            me.view.getCmp("cdmFileRelationDto").sotEditable(false);
//            Ext.Array.each(me.view.getCmp("prmBillingDto").items.items, function (a) {
//                if(a.cid =="prmContractId")
//                    a.sotEditable(true);
//                else
//                    a.sotEditable(false);
//            })
        } else {
            me.controlFileButton();
        }
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        //var invoiceType = view.getCmp("prmBillingDto->invoiceType").gotValue();
        //if (invoiceType == 0 || invoiceType == 1) {
        //    var phone = view.getCmp("prmBillingDto->phone").gotValue();
        //    if (Scdp.ObjUtil.isEmpty(phone)) {
        //        Scdp.MsgUtil.warn("票据类型为增值税情况下，联系电话不能为空！");
        //        return false;
        //    }
        //}
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        //me.doQuery();
        //M3_C7_F4_关联合同
        if (isCognateContract) {
            me.view.getCmp('prmBillingDto->prmContractId').refer.push({
                refField: "customerInvoiceName",
                valueField: "customerName"
            })
            isCognateContract = false;
        }
        me.controlFileButton();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        var uuid = me.view.getCmp("uuid").gotValue();
        me.view.getCmp("editPanel->certificate").setDisabled(true);
        me.view.getCmp("editPanel->toCertificate").setDisabled(true);
        me.view.getCmp("editPanel->invoiceNoDateSet").setDisabled(true);
        me.view.getCmp("editPanel->invoiceNoDateSave").setDisabled(true);
        if (me.view.getCmp("prmBillingDto->state").gotValue() == "2") {
            me.view.getCmp("editPanel->certificate").setDisabled(false);
            me.view.getCmp("editPanel->invoiceNoDateSet").setDisabled(false);
        }
        else if (me.view.getCmp("prmBillingDto->state").gotValue() == "4" || me.view.getCmp("prmBillingDto->state").gotValue() == "8") {
            me.view.getCmp("editPanel->toCertificate").setDisabled(false);
        }

        //M3_C7_F4_关联合同
        var state = me.view.getCmp("prmBillingDto->state").gotValue();
        var prmContractId = me.view.getCmp("prmBillingDto->prmContractId").gotValue();
        if (state == "2") {
            me.view.getCmp("editToolbar->prmMakeReceiptsBtn").setDisabled(false);
            if (Scdp.ObjUtil.isEmpty(prmContractId)) {
                me.view.getCmp("cognateContract").setDisabled(false);
            }
        } else {
            me.view.getCmp("editToolbar->prmMakeReceiptsBtn").setDisabled(true);
            me.view.getCmp("cognateContract").setDisabled(true);
        }
        me.controlFileButton();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        //M3_C7_F4_关联合同
        isCognateContract = false;
        me.controlFileButton();
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    },
    doAdd: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp('originalMoney').sotValue(0);
        me.view.getCmp('invoiceMoney').sotValue(0);
        me.view.getCmp('exchangeRate').sotValue(1);
        me.view.getCmp('invoiceCurrency').sotValue('CNY');
        me.view.getCmp('originalCurrency').sotValue('CNY');
        me.view.getCmp('invoiceCurrencyName').sotValue('人民币元');
        me.view.getCmp('originalCurrencyName').sotValue('人民币元');

        //M3_NC2_F1_开票申请作废
        me.view.getCmp('billType').sotValue(0);

    },
//    doModify: function(){
//        var me = this;
//        me.callParent(arguments);
//
//},
//    fill: function () {
//        var me = this;
//        var view = me.view;
//        var projectName = view.getCmp("prmBillingDto->projectName");
//        if (!projectName.isEditable()) {
//            return;
//        }
//        var customerId = this.view.getCmp('prmBillingDto->customerId').gotValue();
//        if (Scdp.ObjUtil.isNotEmpty(customerId)) {
//            var postData = {customerId: customerId};
//            Scdp.doAction("prmbilling-fill", postData, function (out) {
//                me.view.getCmp('customerInvoiceName').putValue(out.result.customerInvoiceName);
//                me.view.getCmp('customerLocation').sotValue(out.result.customerLocation);
//                me.view.getCmp('taxNo').sotValue(out.result.taxNo);
//                me.view.getCmp('bankName').sotValue(out.result.bankName);
//                me.view.getCmp('bankAccount').sotValue(out.result.bankAccount);
//                me.view.getCmp('phone').sotValue(out.result.phone);
//            });
//        } else {
//            me.view.getCmp('customerInvoiceName').sotValue(null);
//            me.view.getCmp('customerLocation').sotValue(null);
//            me.view.getCmp('taxNo').sotValue(null);
//            me.view.getCmp('bankName').sotValue(null);
//            me.view.getCmp('bankAccount').sotValue(null);
//        }
//    },
    changeMoney: function () {
        var me = this;
        var originalMoney = me.view.getCmp('originalMoney').gotValue();
        var exchangeRate = me.view.getCmp('exchangeRate').gotValue();
        var invoiceMoney = originalMoney * exchangeRate;
        me.view.getCmp('invoiceMoney').sotValue(invoiceMoney);
    },
    taxRateNameChange: function () {
        var me = this;
        var taxRate = me.view.getCmp('taxRateName').gotValue();
        me.view.getCmp('taxRate').sotValue(taxRate);
        me.calTaxMoney();
    },
    calTaxMoney: function () {
        var me = this;
        var taxRate = me.view.getCmp('taxRate').gotValue();
        if (taxRate.indexOf("%") > 0)
            taxRate = Number(taxRate.replace("%", "")) / 100
        var invoiceMoney = me.view.getCmp('invoiceMoney').gotValue();
        if (Scdp.ObjUtil.isNotEmpty(taxRate) && Scdp.ObjUtil.isNotEmpty(invoiceMoney)) {
            var netMoney = Number(invoiceMoney) / (1 + Number(taxRate));
            var taxMoney = (Number(invoiceMoney) / (1 + Number(taxRate))) * Number(taxRate);
            me.view.getCmp('netMoney').sotValue(netMoney);
            me.view.getCmp('taxMoney').sotValue(taxMoney);
        }
    },
    taxRateChange: function () {
        var me = this;
        var invoiceType = me.view.getCmp('invoiceType').gotValue();
        var taxRateName = me.view.getCmp("taxRateName");
        if (invoiceType == 3) {
            taxRateName.sotValue("0%");
            taxRateName.sotEditable(false);
            me.view.getCmp("taxRate").sotValue("0.000");
        } else {
            taxRateName.sotEditable(true);
        }
        var bankNameC = me.view.getCmp("prmBillingDto->bankName");
        var bankAccountC = me.view.getCmp("prmBillingDto->bankAccount");
        var taxNoC = me.view.getCmp("prmBillingDto->taxNo");
        var customerLocationC = me.view.getCmp("prmBillingDto->customerLocation");
        var phoneC = me.view.getCmp("prmBillingDto->phone");
        if (invoiceType == 0) {
            bankNameC.allowBlank = false;
            bankAccountC.allowBlank = false;
            taxNoC.allowBlank = false;
            customerLocationC.allowBlank = false;
            if (bankNameC.labelEl.dom.innerHTML.indexOf("*") < 0) {
                bankNameC.labelEl.dom.innerHTML = bankNameC.labelEl.dom.innerHTML.replace('</div>', '*</div>');
                bankAccountC.labelEl.dom.innerHTML = bankAccountC.labelEl.dom.innerHTML.replace('</div>', '*</div>');
                taxNoC.labelEl.dom.innerHTML = taxNoC.labelEl.dom.innerHTML.replace('</div>', '*</div>');
                customerLocationC.labelEl.dom.innerHTML = customerLocationC.labelEl.dom.innerHTML.replace('</div>', '*</div>');
            }
        } else {
            bankNameC.allowBlank = true;
            bankAccountC.allowBlank = true;
            taxNoC.allowBlank = true;
            customerLocationC.allowBlank = true;
            bankNameC.labelEl.dom.innerHTML = bankNameC.labelEl.dom.innerHTML.replace('*', '');
            bankAccountC.labelEl.dom.innerHTML = bankAccountC.labelEl.dom.innerHTML.replace('*', '');
            taxNoC.labelEl.dom.innerHTML = taxNoC.labelEl.dom.innerHTML.replace('*', '');
            customerLocationC.labelEl.dom.innerHTML = customerLocationC.labelEl.dom.innerHTML.replace('*', '');
        }
        if (invoiceType == 0 || invoiceType == 1) {
            phoneC.allowBlank = false;
            if (phoneC.labelEl.dom.innerHTML.indexOf("*") < 0) {
                phoneC.labelEl.dom.innerHTML = phoneC.labelEl.dom.innerHTML.replace('</div>', '*</div>');
            }
        } else {
            phoneC.allowBlank = true;
            phoneC.labelEl.dom.innerHTML = phoneC.labelEl.dom.innerHTML.replace('*', '');
        }
    },

    //银行账号改变，自动带出银行名称且不能编辑，如果是新银行账号则可以编辑。
    bankAccountChange: function (obj) {
        var me = this;
        var bankName = me.view.getCmp("prmBillingDto->bankName");
        if (Scdp.ObjUtil.isNotEmpty(obj.displayTplData)) {
            me.view.getCmp("prmBillingDto->bankName").sotValue(obj.displayTplData[0].codedesc);
            bankName.sotValue(obj.displayTplData[0].codedesc);
            bankName.sotEditable(false);
        } else {
            //bankName.sotValue("");
            bankName.sotEditable(true);
        }
    },
    //prmProjectMainIdChange: function () {
    //    var me = this;
    //    var view = me.view;
    //    var prmBillingForm = view.getCmp("prmBillingDto");
    //    var customerNameCmp = prmBillingForm.getCmp("customerInvoiceName");
    //    var prmProjectMainId = prmBillingForm.getCmp("prmProjectMainId").gotValue();
    //    customerNameCmp.filterConditions = {selfconditions: " UUID IN(SELECT D.CUSTOMER_ID FROM PRM_CONTRACT_DETAIL D WHERE D.PRM_PROJECT_MAIN_ID = '" + prmProjectMainId + "'"};
    //    //customerNameCmp.reload(prmBillingForm);
    //
    //},
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var isPreProject = view.getCmp("prmBillingDto->isPreProject").gotValue();
        var prmContractId = view.getCmp("prmBillingDto->prmContractId").gotValue();
        var customerName = view.getCmp("prmBillingDto->customerName").gotValue();
        var prmProjectMainId = view.getCmp("prmBillingDto->prmProjectMainId").gotValue();
        var errorMsg = "";
        if (isPreProject != "1") {
            if (Scdp.ObjUtil.isEmpty(prmContractId)) {
                errorMsg += Erp.Const.BREAK_LINE + "非预立项项目“合同名称”" + Erp.I18N.CAN_NOT_EMPTY;
            }
            if (Scdp.ObjUtil.isEmpty(customerName)) {
                errorMsg += Erp.Const.BREAK_LINE + "非预立项项目“业主单位”" + Erp.I18N.CAN_NOT_EMPTY;
            }
            //M3_C11_F5_关联合同
            Scdp.doAction("receipts-billingisnull", {prmProjectMainId: prmProjectMainId}, function (res) {
                if (Scdp.ObjUtil.isNotEmpty(res) && res.result > 0) {
                    errorMsg += Erp.Const.BREAK_LINE + "该项目存在未关联合同的开票申请和项目收款数据,请先处理";
                }
            }, false, false, false, null);
        }
        if (!me.workFlowTaskId) {
            if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                return true;
            } else {
                Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                return false;
            }
        } else {
            return true;
        }
    },
    certificateClick: function (obj) {
        var me = this;
        var view = me.view;
        var state = view.getCmp("prmBillingDto->state").gotValue();
        if (state != "2") {
            Scdp.MsgUtil.warn("只有审批通过的单据才能生成凭证！");
        }
        else if (!(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceNo").gotValue()).length > 0)) {
            Scdp.MsgUtil.warn("发票号不能为空!");
        }
        else if (!(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceDate").gotValue()).length > 0)) {
            Scdp.MsgUtil.warn("开票日期不能为空!");
        }
        else {
            Scdp.MsgUtil.confirm("是否生成凭证？", function (e) {
                if ("yes" == e) {
                    var uuid = view.getCmp("prmBillingDto->uuid").gotValue();
                    var param = {uuid: uuid};
                    Scdp.doAction("billing-certificate-create", param, function (result) {
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
            });
        }
    },

    //调出凭证
    toCertificate: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp('prmBillingDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    },
    invoiceNoDateSetClick: function () {
        var me = this;
        if (me.view.getCmp("prmBillingDto->state").gotValue() == "2") {
            //me.view.getCmp("prmBillingDto->invoiceNo").allowBlank = false;
            //me.view.getCmp("prmBillingDto->invoiceDate").allowBlank = false;
            me.view.getCmp("prmBillingDto->invoiceDate").sotValue(Erp.Util.getDateForStandard(me.view.getCmp("prmBillingDto->invoiceDate").gotValue()));
            me.view.getCmp("prmBillingDto->invoiceNo").sotEditable(true);
            me.view.getCmp("prmBillingDto->invoiceDate").sotEditable(true);

            me.view.getCmp("prmBillingDto->taxMoney").sotEditable(true);
            me.view.getCmp("prmBillingDto->netMoney").sotEditable(true);

            me.view.getCmp("prmBillingDto->taxMoney").setReadOnly(false);
            me.view.getCmp("prmBillingDto->netMoney").setReadOnly(false);

            me.view.getCmp("editPanel->invoiceNoDateSet").setDisabled(true);
            me.view.getCmp("addNew1Btn").setDisabled(true);
            me.view.getCmp("editPanel->addNew2Btn").setDisabled(true);
            me.view.getCmp("editPanel->copyAddBtn").setDisabled(true);
            me.view.getCmp("editPanel->certificate").setDisabled(true);
            me.view.getCmp("editPanel->invoiceNoDateSave").setDisabled(false);
            //me.view.getCmp("editPanel->saveBtn").setDisabled(false);
        }
    },
    //打印开票申请单
    prmBillingPrintClick: function () {
        var me = this;
        var view = me.view;
        var billingUuid = view.getCmp("prmBillingDto->uuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(billingUuid)) {
            var param = {billingUuid: billingUuid};
            Scdp.printReport("erp/prm/开票申请.cpt", [param], false, "pdf");
        }
    },
    invoiceNoDateSaveClick: function () {
        var me = this;
        if (me.view.getCmp("prmBillingDto->state").gotValue() == "2") {
            /*if (!(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceNo").gotValue()).length > 0)) {
             Scdp.MsgUtil.warn("发票号不能为空!");
             }
             else if (!(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceDate").gotValue()).length > 0)) {
             Scdp.MsgUtil.warn("开票日期不能为空!");
             }*/

            if ((parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->taxMoney").gotValue()))) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->netMoney").gotValue())))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceMoney").gotValue())))) {
                Scdp.MsgUtil.warn("【税额】加【不含税发票金额】必须与【开票金额】相等!");
            }
            else {
                var prmBillingUuid = Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->uuid").gotValue());
                var tblVersion = Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->tblVersion").gotValue());
                var invoiceNo = Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceNo").gotValue());
                var invoiceDate = Erp.Util.isNullReturnEmpty(me.view.getCmp("prmBillingDto->invoiceDate").gotValue());
                var postData = {
                    prmBillingUuid: prmBillingUuid,
                    tblVersion: tblVersion,
                    invoiceNo: invoiceNo,
                    invoiceDate: invoiceDate
                };
                Scdp.doAction("prmbilling-invoiceNoDateSave", postData, function (result) {
                    if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                        Scdp.MsgUtil.warn(result.errorMsg);
                    }
                    else {
                        me.view.getCmp("prmBillingDto->invoiceNo").sotEditable(false);
                        me.view.getCmp("prmBillingDto->invoiceDate").sotEditable(false);

                        me.view.getCmp("prmBillingDto->taxMoney").sotEditable(false);
                        me.view.getCmp("prmBillingDto->netMoney").sotEditable(false);

                        me.view.getCmp("prmBillingDto->taxMoney").setReadOnly(true);
                        me.view.getCmp("prmBillingDto->netMoney").setReadOnly(true);

                        me.view.getCmp("editPanel->invoiceNoDateSave").setDisabled(true);
                        //me.view.controller.doSave();
                        me.view.getCmp("addNew1Btn").setDisabled(false);
                        me.view.getCmp("editPanel->addNew2Btn").setDisabled(false);
                        me.view.getCmp("editPanel->copyAddBtn").setDisabled(false);
                        me.view.getCmp("editPanel->certificate").setDisabled(false);
                        me.view.getCmp("editPanel->invoiceNoDateSet").setDisabled(false);
                        //me.view.getCmp("prmBillingDto->invoiceNo").allowBlank = true;
                        //me.view.getCmp("prmBillingDto->invoiceDate").allowBlank = true;
                        //Scdp.MsgUtil.info("保存开票成功!");
                    }
                });
            }
        }
    },
    setPaymentStatus: function () {
        var me = this;
        var view = me.view;
        var locationType = view.getCmp("prmBillingDto->locationType").gotValue();
        var paymentType = view.getCmp("prmBillingDto->paymentType");
        if (locationType == "W") {
            paymentType.sotEditable(true);
            paymentType.allowBlank = false;
        } else {
            paymentType.sotValue(null);
            paymentType.sotEditable(false);
            paymentType.allowBlank = true;
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmBillingDto->reqOffice').gotValue();
        return processDeptCode;
    },
    fnMakeReceipts: function () {
        //跳转
        //目标页面的代码
        var me = this;
        //var selecte = me.view.getResultPanel().getSelectionModel().getSelection();
        //if (selecte.length == 0) {
        //    Scdp.MsgUtil.warn("请选择一条记录");
        //    return;
        //}
        //if (selecte.length > 1) {
        //    Scdp.MsgUtil.warn("只能选择一条记录");
        //    return;
        //}
        var menuCode = 'RECEIPTS';
        var param = {};
        param.projectCode = me.view.getCmp("prmBillingDto->projectCode").gotValue();
        param.projectName = me.view.getCmp("prmBillingDto->projectName").gotValue();
        param.customerName = me.view.getCmp("prmBillingDto->customerName").gotValue();
        param.invoiceMoney = me.view.getCmp("prmBillingDto->invoiceMoney").gotValue();
        param.prmProjectMainId = me.view.getCmp("prmBillingDto->prmProjectMainId").gotValue();
        param.customerId = me.view.getCmp("prmBillingDto->customerId").gotValue();
        param.estimateDate = me.view.getCmp("prmBillingDto->expectReceiveDate").gotValue();
        param.flag = "1";
        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "";
        var fileObjConfig = {};
        fileObjConfig.regex = /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)(\w)?)$/i;
        fileObjConfig.regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt';
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData, null, fileObjConfig);
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
    },
    //文件按钮的禁用启用
    controlFileButton: function () {
        var me = this;
        var status = Ext.getCmp("editStatus").getValue();
        if (Scdp.ObjUtil.isEmpty(status)) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            return false;
        }
        if (status == Scdp.I18N.VIEW_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(false);
        } else if (status == Scdp.I18N.MODIFY_STATUS || status == Scdp.I18N.ADD_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(false);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(false);
        }
    },
    fnBillingInv: function () {
        var me = this;
        var select = me.view.getResultPanel().getSelectionModel().getSelection();
        if (select.length == 0) {
            Scdp.MsgUtil.warn("请选择记录");
            return;
        }
        var uuids = "";
        var flag = false;
        Ext.Array.each(select, function (a) {
            if (a.data.cdmBillStateCombo != "已审核") {
                flag = true;
                return;
            }
            uuids += "'" + a.data.uuid + "',";
        });
        if (flag) {
            Scdp.MsgUtil.warn("存在流程状态不是“已审核”的记录，请重新选择");
            return;
        }
        Scdp.MsgUtil.confirm("是否确认开票？", function (e) {
            if ("yes" == e) {
                Scdp.doAction("prmbilling-billingInv", {uuids: uuids.substr(0, uuids.length - 1)}, function (result) {
                    if (result.success)
                        Scdp.MsgUtil.info("开票成功");
                });
            }
        });

    },
    //M3_NC2_F1_开票申请作废
    fnInvalidInv: function () {
        var me = this;
        var selecte = me.view.getResultPanel().getSelectionModel().getSelection();
        if (selecte.length == 0) {
            Scdp.MsgUtil.warn("请选择一条记录");
            return;
        }
        if (selecte.length > 1) {
            Scdp.MsgUtil.warn("只能选择一条记录");
            return;
        }
        if (selecte[0].data.cdmBillStateCombo != "已审核") {
            Scdp.MsgUtil.warn("该记录不是已审核状态，不能进行操作。");
            return;
        }
        var menuCode = 'PRMINVALID';
        var param = {};
        param.uuid = selecte[0].data.uuid;
        param.flag = "1";
        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    },
    //M3_C11_F5_关联合同
    cognateContractFn: function () {
        var me = this;
        me.view.getCmp("editToolbar->modifyBtn").focus();
        me.view.getCmp("editToolbar->modifyBtn").fireEvent("click");
        me.view.getCmp('prmBillingDto->prmContractId').refer.splice(2, 1);
        isCognateContract = true;
    }
});