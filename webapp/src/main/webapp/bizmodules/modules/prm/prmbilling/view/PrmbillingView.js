Ext.define('Prmbilling.view.PrmbillingView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmbilling',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 270,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmbilling-query-layout.xml',
    editLayoutFile: 'prmbilling-edit-layout.xml',
    //extraLayoutFile: 'prmbilling-extra-layout.xml',
    bindings: ['prmBillingDto', 'prmBillingDto.prmBillingDetailDto', 'prmBillingDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: false,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Invoice',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        me.getCmp('editToolbar->copyAddBtn').hide();
    },
    addOtherButton: function () {
        var me = this;
        me.getEditToolbar().add({
            text: '凭证生成',
            cid: 'certificate',
            iconCls: 'erp-voucher',
            disabled: "true"
        });

        me.getEditToolbar().add({
            text: '凭证',
            cid: 'toCertificate',
            iconCls: 'erp-voucher',
            disabled: "true"
        });

        me.getEditToolbar().add({
            text: '开票设定',
            cid: 'invoiceNoDateSet',
            iconCls: "temp_icon_16",
            disabled: "true"
        });

        me.getEditToolbar().add({
            text: '开票保存',
            cid: 'invoiceNoDateSave',
            iconCls: "temp_icon_16",
            disabled: "true"
        });

        me.getEditToolbar().add({
            text: '打印开票申请',
            cid: 'prmBillingPrint',
            iconCls: "printer_icon_16"
        });

        me.getEditToolbar().add({
            text: '生成收款计划',
            cid: 'prmMakeReceiptsBtn',
            iconCls: "temp_icon_16"
        });
        //M3_C11_F5_关联合同
        me.getEditToolbar().add({
            text: '关联合同',
            cid: 'cognateContract',
            iconCls: 'erp-voucher'
        });
        me.getQueryToolbar().add({
            text: '开票',
            cid: 'prmBillingInvoiceBtn',
            iconCls: "temp_icon_16"
        })
        //M3_NC2_F1_开票申请作废
        me.getQueryToolbar().add({
            text: '作废',
            cid: 'prmInvalidInvoiceBtn',
            iconCls: "temp_icon_16"
        })

        //me.getCmp("editPanel->invoiceNoDateSave").hide();
    },
    afterInit: function () {
        var me = this;
        me.addOtherButton();
        me.initBillingGrid();
        me.initCustomerInvoice();
        me.getCmp("editToolbar->cognateContract").setDisabled(true);
        me.getCmp("editToolbar->prmMakeReceiptsBtn").setDisabled(true);
        me.getCmp("prmBillingDto->customerInvoiceName").sotCodeAndDesc = me.sotCodeAndDescInvoiceName;
    },

    initBillingGrid: function () {
        var view = this;

        var detailGrid = view.getCmp("prmBillingDetailDto");
        detailGrid.on('beforeedit', function (editor, eventObj) {
            var grid = eventObj.grid;
            var record = eventObj.record;
            var amount = record.get("amount");
            var price = record.get("price");
            return grid.editable;
        });
        detailGrid.afterEditGrid = view.changeBudgetDetail;
        detailGrid.afterDeleteRow = function () {
            var contractInvoiceController = Scdp.getActiveModule().controller;
            contractInvoiceController.view.totalBudgetDetail();
        };
        detailGrid.afterCopyAddRow = function () {
            var contractInvoiceController = Scdp.getActiveModule().controller;
            contractInvoiceController.view.totalBudgetDetail();
        };
    },
    changeBudgetDetail: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }
        var grid = eventObj.grid;
        var view = grid.up("IView");
        var record = eventObj.record;
        view.totalBudgetDetail();
    },
    totalBudgetDetail: function () {
        var view = this;
        var billingDetailGrid = view.getCmp("prmBillingDetailDto");
        var store = billingDetailGrid.getStore();
        var originalMoney = 0;
        var i = 0;
        Ext.Array.each(store.data.items, function (record) {
            var totalValue = record.get("totalValue");
            if (Scdp.ObjUtil.isEmpty(totalValue)) {
                totalValue = 0;
            }
            var amount = record.get("amount");
            var price = record.get("price");
            var total = amount * price;
            store.getAt(i).set("totalValue", total);
            originalMoney += total;
            i++;
        });
        view.getCmp('prmBillingDto->originalMoney').sotValue(originalMoney);
        var exchangeRate = view.getCmp('prmBillingDto->exchangeRate').gotValue();
        if (exchangeRate != null || exchangeRate != '') {
            var invoiceMoney = originalMoney * exchangeRate;
            view.getCmp('prmBillingDto->invoiceMoney').sotValue(invoiceMoney);
        }
        else {
            view.getCmp('prmBillingDto->invoiceMoney').sotValue(originalMoney);
        }
    },
    initCustomerInvoice: function () {
        var view = this;
        var prmBillingForm = view.getCmp("prmBillingDto");
        var customerInvoiceId = prmBillingForm.getCmp("customerInvoiceId");
        var bankAccountCmp = prmBillingForm.getCmp("bankAccount");
        var bankNameCmp = prmBillingForm.getCmp("bankName");
        var projectNameCmp = prmBillingForm.getCmp("projectName");
        customerInvoiceId.afterSotValue = function () {
            var supplierCode = customerInvoiceId.gotValue();
            if (Scdp.ObjUtil.isNotEmpty(supplierCode)) {

                var postData = {supplierCode: supplierCode};
                Scdp.doAction("prmbilling-querycustomerbankinfo", postData, function (result) {
                    if (Scdp.ObjUtil.isNotEmpty(result)) {
                        bankAccountCmp.reload(prmBillingForm);
                        bankAccountCmp.sotValue(result.bankId);
                        bankNameCmp.sotValue(result.bankName);
                    }
                });
            } else {
                bankAccountCmp.reload(prmBillingForm);
                bankAccountCmp.sotValue("");
                bankNameCmp.sotValue("");
            }
        }
        projectNameCmp.afterSotValue = function () {
            prmBillingForm.getCmp("customerName").sotValue();
            prmBillingForm.getCmp("prmContractId").sotValue();
        }
    },


    sotCodeAndDescInvoiceName: function (code, desc, needStartEdit, recordData) {
        var me = this;
        if (me.isInForm()) {
            var upForm = me.getUpForm();
            var descComp = upForm.getCmp(me.getDescCid());
            if (Scdp.ObjUtil.isNotEmpty(descComp)) {
                descComp.sotValue(desc);
            }
            me.sotValue(code);
            me.oldValue = me.gotRawValue();

            if (Scdp.ObjUtil.isEmpty(code) || Scdp.ObjUtil.isEmpty(recordData)) {
                Ext.Array.each(me.refer, function (c) {
                    var f = upForm.getCmp(c.refField);
                    if (f) f.sotValue(null);
                });
            } else {
                Ext.Array.each(me.refer, function (c) {
                    var f = upForm.getCmp(c.refField);
                    if (f && (f.isXType("JQueryField") || f.isXType("JTreeField") || f.isXType("JGridField")
                        || f.isXType("JComboTreeField"))) {
                        f.putValue(me.mergeValues(recordData, c.valueField));
                    } else if (f) {
                        f.sotValue(me.mergeValues(recordData, c.valueField));
                        var view = me.up("IView");
                        view.sotTaxNoEditable(view);
                    }
                });
            }
        }
    },
    sotTaxNoEditable: function (view) {
        var me = this;
        var taxNoCmp = view.getCmp("prmBillingDto->taxNo");
        if (Scdp.ObjUtil.isNotEmpty(taxNoCmp.gotValue())) {
            taxNoCmp.sotEditable(false);
        } else {
            taxNoCmp.sotEditable(true);
        }
    }
    //initCustomer: function () {
    //    var me = this;
    //    var prmBillingForm = me.getCmp("prmBillingDto");
    //    var customerNameCmp=prmBillingForm.getCmp("customerName");
    //    var prmProjectMainId=prmBillingForm.getCmp("prmProjectMainId").gotValue();
    //    customerNameCmp.filterConditions={selfconditions:" UUID IN(SELECT D.CUSTOMER_ID FROM PRM_CONTRACT_DETAIL D WHERE D.PRM_PROJECT_MAIN_ID = '"+prmProjectMainId+"'"};
    //}

});