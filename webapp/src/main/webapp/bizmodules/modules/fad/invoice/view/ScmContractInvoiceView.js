Ext.define('Invoice.view.ScmContractInvoiceView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/invoice',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'scmContractInvoice-query-layout.xml',
    editLayoutFile: 'scmContractInvoice-edit-layout.xml',
    //extraLayoutFile: 'invoice-extra-layout.xml',
    bindings: ['fadInvoiceDto', 'fadInvoiceDto.scmContractInvoiceDto', 'fadInvoiceDto.fadCashReqInvoiceDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_Contract_Invoice',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterChangeUIStatus: function () {
        var me = this;
        var uiStatus = me.getUIStatus();
//        if (Scdp.Const.UI_INFO_STATUS_NEW == uiStatus||Scdp.Const.UI_INFO_STATUS_MODIFY == uiStatus) {
//            var receiptStatusCmp = me.getCmp("fadInvoiceDto->status");
//            if ("CONFIRMED" == receiptStatusCmp.gotValue()) {
//                me.getCmp('editToolbar->deleteBtn').setDisabled(true);
//            }
//        };
        var pickReceiptBtn = me.getCmp("fadInvoiceDto->pickContract");
        if (Scdp.Const.UI_INFO_STATUS_NEW == uiStatus || Scdp.Const.UI_INFO_STATUS_MODIFY == uiStatus) {
            pickReceiptBtn.setDisabled(false);
        } else {
            pickReceiptBtn.setDisabled(true);
        }
    },
    afterInit: function () {
        var me = this;

        me.resetToolbar();
        me.resetDetailToolbar();
        me.initSupplier();

        var grid = me.getCmp("scmContractInvoiceDto");
        grid.beforeEditGrid = function (eventObj) {
            //todo
            return false;
        };
        grid.rowColorConfigFn = me.rowColorConfigFn;

        var queryToolbar = me.getQueryToolbar();
        queryToolbar.add({
            text: '提交',
            cid: 'batchWorkFlowCommit',
            iconCls: 'workFlowCommit_Btn'
            //disabled: "true"
        });
        queryToolbar.add({
            text: '打印',
            cid: 'batchExpensePaste',
            iconCls: 'printer_icon_16'
            //disabled: "true"
        });

        var editToolbar = me.getEditToolbar();
        editToolbar.add(
            {
                xtype: 'button',
                text: '废止',
                cid: 'Btn_annul',
                iconCls: 'erp-trash'
                //disabled: "true"
            },
            {
                xtype: 'button',
                text: '生成凭证',
                cid: 'Btn_certificate',
                iconCls: 'erp-voucher',
                disabled: "true"
            },
            {
                xtype: 'button',
                text: '凭证',
                cid: 'toCertificate',
                iconCls: 'erp-voucher',
                disabled: "true"
            },
            {
                text: '打印粘贴单和料单',
                cid: 'expensePaste',
                iconCls: 'printer_icon_16'
            },
            {
                text: '打印料单',
                cid: 'printMaterialRep',
                iconCls: 'printer_icon_16'
            }
        );
        me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(true);
        me.getCmp("editPanel->editToolbar->printMaterialRep").setDisabled(true);
        me.getCmp("Btn_annul").setVisible(false);
        me.getCmp("Btn_certificate").setVisible(false);

        //根据角色判断按钮显示
        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("会计") > -1) {
            me.getCmp("Btn_annul").setVisible(true);
            me.getCmp("Btn_certificate").setVisible(true);
        }
    },
    initSupplier: function () {
        var view = this;
        var fadInvoiceForm = view.getCmp("fadInvoiceDto");
        var supplierId = fadInvoiceForm.getCmp("supplierId");
        supplierId.afterSotValue = function () {
            var supplierCode = supplierId.gotValue();
            if (Scdp.ObjUtil.isNotEmpty(supplierCode)) {
                var postData = {supplierCode: supplierCode};
                Scdp.doAction("scmcontract-querysupplierbankinfo", postData, function (result) {
                    if (Scdp.ObjUtil.isNotEmpty(result)) {
                        fadInvoiceForm.getCmp("bankId").reload(fadInvoiceForm);
                        fadInvoiceForm.getCmp("bankId").sotValue(result.bankId);
                        fadInvoiceForm.getCmp("bank").sotValue(result.bankName);
                    }
                });
            } else {
                fadInvoiceForm.getCmp("bankId").reload(fadInvoiceForm);
                fadInvoiceForm.getCmp("bankId").sotValue("");
                fadInvoiceForm.getCmp("bank").sotValue("");
            }
        }
    },
    resetToolbar: function () {
        var me = this;
        me.getCmp('editToolbar->copyAddBtn').setVisible(false);
    },
    resetDetailToolbar: function () {
        var me = this;
        var contractInvoiceGrid = me.getCmp("scmContractInvoiceDto");
//        var invoiceMaterialGrid = me.getCmp("scmInvoiceMaterialDto");
        contractInvoiceGrid.doAddRow = function (model) {
            var contractInvoiceController = Scdp.getActiveModule().controller;
            contractInvoiceController.pickContract();
        };
        contractInvoiceGrid.afterDeleteRow = function (model) {
            var contractInvoiceController = Scdp.getActiveModule().controller;
            contractInvoiceController.afterDeleteRow();
        };
//        invoiceMaterialGrid.doAddRow = function (model) {//入库单
//            var  contractInvoiceController = Scdp.getActiveModule().controller;
//            contractInvoiceController.pickContractDetail();
//        };
        contractInvoiceGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        contractInvoiceGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        contractInvoiceGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        contractInvoiceGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        contractInvoiceGrid.getCmp("toolbar->copyAddRowBtn").hide();
//        invoiceMaterialGrid.getCmp("toolbar->rowMoveTopBtn").hide();
//        invoiceMaterialGrid.getCmp("toolbar->rowMoveUpBtn").hide();
//        invoiceMaterialGrid.getCmp("toolbar->rowMoveDownBtn").hide();
//        invoiceMaterialGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
//        invoiceMaterialGrid.getCmp("toolbar->copyAddRowBtn").hide();

    },
    rowColorConfigFn: function (record) {
        return 'erp-grid-background-color-red';
    },
    setUIStatus: function () {
        var me = this;
        me.callParent(arguments);
        if (me.uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(false);
            me.getCmp("editPanel->editToolbar->printMaterialRep").setDisabled(false);
        } else if (me.uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || me.uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(true);
            me.getCmp("editPanel->editToolbar->printMaterialRep").setDisabled(true);
        }
    }
});