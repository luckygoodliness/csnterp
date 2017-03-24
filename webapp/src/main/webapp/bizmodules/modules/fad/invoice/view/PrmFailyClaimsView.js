Ext.define('Invoice.view.PrmFailyClaimsView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/invoice',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmFailyClaims-query-layout.xml',
    editLayoutFile: 'prmFailyClaims-edit-layout.xml',
    //extraLayoutFile: 'invoice-extra-layout.xml',
    bindings: ['fadInvoiceDto', 'fadInvoiceDto.fadCashReqInvoiceDto', 'fadInvoiceDto.fadInvoiceMaterialDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Faily_Claims',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    certificateJump : function (view) {
        // M7_C7_F1_查询优化
        openCertificate = function (uuid) {
            var postData = {uuid: uuid};
            var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
            Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
        };

        var runningNoColumns = view.getResultPanel().getColumnBydataIndex("invoiceReqNo");

        runningNoColumns.renderer = function (value, p, record) {

            return Ext.String.format(
                '<a href="javascript:openCertificate(\'' + record.data.uuid + '\');" style="color: blue">' + value + '</a>'
            );
        };
    },
    afterInit: function () {
        var me = this;
        // M7_C7_F1_查询优化
        me.certificateJump(me);
        me.resetToolbar();
        me.resetDetailToolbar();
        me.afterSotPrmProjectMainIdValue();
        me.initSupplier();
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.beforeEditGrid = function (eventObj) {
            return false;
        };
        me.getCmp("fadInvoiceMaterialDto").afterEditGrid = me.changeFadInvoiceMaterial;
        var subjectNameCmp = me.getCmp("fadInvoiceDto->subjectName");
        subjectNameCmp.filterConditions = {selfconditions: " budget_code!='TRAVEL_CHARGE' and IS_PURCHASE = '0' "};
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
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
                text: '打印粘贴单',
                cid: 'expensePaste',
                iconCls: 'printer_icon_16'
            },
            {
                text: '打印出入库单',
                cid: 'printInvoiceReceipt',
                iconCls: 'printer_icon_16',
                disabled: "true"
            });
        me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(true);
        me.getCmp("editPanel->editToolbar->printInvoiceReceipt").setDisabled(true);
        me.getCmp("Btn_annul").setVisible(false);
        me.getCmp("Btn_certificate").setVisible(false);
        //根据角色判断按钮显示
        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("会计") > -1) {
            me.getCmp("Btn_annul").setVisible(true);
            me.getCmp("Btn_certificate").setVisible(true);
        }
        //M3_C12_F1_发票内容导入
        //发票内容toolbar增加excel导入按钮
        me.getCmp("fadInvoiceMaterialDto").getCmp("toolbar").add({
            cid: 'excelSubmit',
            tooltip: 'excel导入明细',
            iconCls: 'file_upload_icon',
            disabled: "true"
        });
        //me.hideMaterialTab(this);
    },
    resetToolbar: function () {
        var me = this;
        me.getCmp('editToolbar->copyAddBtn').setVisible(false);
    },

    //hideMaterialTab: function(view){
    //    var materialGrid = view.getCmp("fadInvoiceMaterialDto");
    //    if (materialGrid.getStore().getCount() > 0) {
    //        view.getCmp("editPanel->editToolbar->printInvoiceReceipt").setDisabled(false);
    //    }else{
    //        view.getCmp("editPanel->editToolbar->printInvoiceReceipt").setDisabled(true);
    //    }
    //},
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
    resetDetailToolbar: function () {
        var me = this;
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
//        var invoiceMaterialGrid = me.getCmp("scmInvoiceMaterialDto");
        fadCashReqInvoiceGrid.doAddRow = function (model) {
            var prmFailyClaimsController = Scdp.getActiveModule().controller;
            prmFailyClaimsController.pickCashReq();
        };
        fadCashReqInvoiceGrid.afterDeleteRow = function (model) {
            var prmFailyClaimsController = Scdp.getActiveModule().controller;
            prmFailyClaimsController.fadCashReqInvoiceGridChange();
        };
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    afterSotPrmProjectMainIdValue: function () {
        var view = this;
        var prmProjectMainIdCmp = view.getCmp("fadInvoiceDto->prmProjectMainId");
        prmProjectMainIdCmp.afterSotValue = function () {
            var subjecNameCmp = view.getCmp("fadInvoiceDto->subjectName");
            var subjectCodeCmp = view.getCmp("fadInvoiceDto->subjectCode");
            var subjectIdCmp = view.getCmp("fadInvoiceDto->subjectId");
            subjecNameCmp.sotValue();
            subjectCodeCmp.sotValue();
            subjectIdCmp.sotValue();
            var prmProjectMainId = view.getCmp("fadInvoiceDto->prmProjectMainId").gotValue();
            if (prmProjectMainId == "" || prmProjectMainId == null) {
                subjecNameCmp.sotEditable(false);
            } else {
                subjecNameCmp.sotEditable(true);
            }
        }
    },
    setUIStatus: function () {
        var me = this;
        me.callParent(arguments);
        if (me.uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(false);
            me.getCmp("editPanel->editToolbar->printInvoiceReceipt").setDisabled(false);
        } else if (me.uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || me.uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(true);
            me.getCmp("editPanel->editToolbar->printInvoiceReceipt").setDisabled(true);
        }
    },
    changeFadInvoiceMaterial: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }
        var field = eventObj.field;
        var record = eventObj.record;
        if (field == "unitPrice") {
            record.set("amount", record.get("unitPrice") * record.get("actualNum"));
        } else if (field == "actualNum") {
            record.set("amount", record.get("unitPrice") * record.get("actualNum"));
        }
    }
});