Ext.define('Invoice.view.PrmBusinessTripClaimsView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/invoice',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmBusinessTripClaims-query-layout.xml',
    editLayoutFile: 'prmBusinessTripClaims-edit-layout.xml',
    //extraLayoutFile: 'invoice-extra-layout.xml',
    bindings: ['fadInvoiceDto', 'fadInvoiceDto.fadCashReqInvoiceDto', 'fadInvoiceDto.fadInvoiceSubsidyDto', 'fadInvoiceDto.fadInvoiceTravelDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Businesstrip_Claims',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.resetToolbar();
        me.resetDetailToolbar();
        me.initRemainBudget();
        me.initSupplier();
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.beforeEditGrid = function (eventObj) {
            return false;
        };
        var fadInvoiceSubsidyGrid = me.getCmp("fadInvoiceSubsidyDto");
        fadInvoiceSubsidyGrid.beforeEditGrid = function (eventObj) {
            return false;
        };
        var fadInvoiceTravelGrid = me.getCmp("fadInvoiceTravelDto");
        fadInvoiceTravelGrid.beforeEditGrid = function (eventObj) {
            return false;
        };
        //var renderPersonNameCmp = me.getCmp("fadInvoiceDto->renderPersonName");
        //renderPersonNameCmp.filterConditions = {orgfilter: " org_code='" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "' "};
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
                xtype: 'button',
                text: '废止',
                cid: 'Btn_annul',
                iconCls: 'erp-trash',
                disabled: "true"
            },
            {
                xtype: 'button',
                text: '生成凭证',
                cid: 'Btn_certificate',
                iconCls: 'erp-voucher'
                //disabled: "true"
            },
            {
                xtype: 'button',
                text: '凭证',
                cid: 'toCertificate',
                iconCls: 'erp-voucher'
                //disabled: "true"
            },
            {
                text: '打印粘贴单',
                cid: 'expensePaste',
                iconCls: 'printer_icon_16'
            },
            {
                text: '打印报销单',
                cid: 'businessTripPrint',
                iconCls: 'printer_icon_16'
            });
        me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(true);
        me.getCmp("editPanel->editToolbar->businessTripPrint").setDisabled(true);
        me.getCmp("Btn_annul").setVisible(false);
        me.getCmp("Btn_certificate").setVisible(false);
        //根据角色判断按钮显示
        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("会计") > -1) {
            me.getCmp("Btn_annul").setVisible(true);
            me.getCmp("Btn_certificate").setVisible(true);
        }
//        var editToolbar = me.getEditToolbar();
//        editToolbar.add({
//            text: '提交审核',
//            cid: 'but_submit',
//            iconCls: 'temp_icon_16'
//            //disabled: "true"
//        });
//        editToolbar.add({
//            text: '审核通过',
//            cid: 'but_approved',
//            iconCls: 'pass_icon_16'
//            //disabled: "true"
//        });
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
    initRemainBudget: function () {
        var view = this;
        var prmProjectMainIdCmp = view.getCmp("fadInvoiceDto->prmProjectMainId");
        prmProjectMainIdCmp.afterSotValue = function () {
            var officeId = view.getCmp("fadInvoiceDto->officeId").gotValue();
            var projectId = view.getCmp("fadInvoiceDto->prmProjectMainId").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(officeId) && Scdp.ObjUtil.isNotEmpty(projectId)) {
                var postData = {isPrm: "1", officeId: officeId, projectId: projectId};
                Scdp.doAction("invoice-getremainbudget", postData, function (retData) {
                    view.getCmp("fadInvoiceDto->remainBudget").sotValue(retData.remainBudget);
                    view.getCmp("fadInvoiceDto->subjectId").sotValue(retData.subjectId);
                });
            }
        }
    },
    resetDetailToolbar: function () {
        var me = this;
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        var fadInvoiceSubsidyGrid = me.getCmp("fadInvoiceSubsidyDto");
        var fadInvoiceTravelGrid = me.getCmp("fadInvoiceTravelDto");
//        var invoiceMaterialGrid = me.getCmp("scmInvoiceMaterialDto");
        fadCashReqInvoiceGrid.doAddRow = function (model) {
            var prmBusinessTripController = Scdp.getActiveModule().controller;
            prmBusinessTripController.pickCashReq();
        };
        fadCashReqInvoiceGrid.afterDeleteRow = function (model) {
            var prmBusinessTripController = Scdp.getActiveModule().controller;
            prmBusinessTripController.updateClearanceMoney();
        };
        fadInvoiceSubsidyGrid.afterDeleteRow = function (model) {
            var prmBusinessTripController = Scdp.getActiveModule().controller;
            prmBusinessTripController.updateSubsidyMoney();
        };
        fadInvoiceTravelGrid.afterDeleteRow = function (model) {
            var prmBusinessTripController = Scdp.getActiveModule().controller;
            prmBusinessTripController.updateExpensesMoney();
        };

        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->copyAddRowBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
//        fadInvoiceSubsidyGrid.getCmp("toolbar->copyAddRowBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
//        fadInvoiceTravelGrid.getCmp("toolbar->copyAddRowBtn").hide();

    },
    setUIStatus: function () {
        var me = this;
        me.callParent(arguments);
        if (me.uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(false);
            me.getCmp("editPanel->editToolbar->businessTripPrint").setDisabled(false);
        } else if (me.uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || me.uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            me.getCmp("editPanel->editToolbar->expensePaste").setDisabled(true);
            me.getCmp("editPanel->editToolbar->businessTripPrint").setDisabled(true);
        }
    }
});