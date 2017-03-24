Ext.define('Cashreq.view.CashreqScmView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/cashreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'cashreqscm-query-layout.xml',
    editLayoutFile: 'cashreqscm-edit-layout.xml',
    //extraLayoutFile: 'cashreq-extra-layout.xml',
    bindings: ['fadCashReqDto', 'fadCashReqDto.fadCashReqInvoiceDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_PurchasePaymentRequestContract',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    resetToobar: function () {
        var me = this;
        me.getCmp("queryPanel-\x3ebatchDelBtn").hide();
        me.getEditToolbar().add({
                xtype: 'button',
                text: '作废',
                cid: 'trash',
                iconCls: "erp-trash"
            },
            {
                xtype: 'button',
                text: '修正',
                cid: 'editBtn',
                iconCls: "modify_btn"
            },
            {
                text: '凭证生成',
                cid: 'certificate',
                iconCls: 'erp-voucher'
            },
            {
                text: '凭证',
                cid: 'toCertificate',
                iconCls: 'erp-voucher'
            },
            {
                text: '打印请款单',
                cid: 'expenseRequest',
                iconCls: 'printer_icon_16'
            });
        me.getCmp("editPanel->certificate").setDisabled(true);
        me.getCmp("editPanel->trash").setDisabled(true);
        me.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
    },

    afterInit: function () {
        var me = this;
        me.resetToobar();
        if (Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME) != "供应链管理部") {
            me.getCmp("editPanel->addNew2Btn").setVisible(false);
            me.getCmp("editPanel->copyAddBtn").setVisible(false);
            me.getCmp("editPanel->deleteBtn").setVisible(false);
            if (Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME) == "计划财务部") {
                me.getCmp("jdInfo->jdPassword").inputEl.dom.type = "text";
            }
        }
        me.getCmp("fadCashReqDto->purchaseContractIdPlus").afterCallbackFn = function (obj) {
            var purchaseContractId = me.getCmp("fadCashReqDto->purchaseContractId").gotValue();
            var supplierName = me.getCmp("fadCashReqDto->supplierName").gotValue();
            var officeId = me.getCmp("fadCashReqDto->officeId").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(supplierName)) {
                me.getCmp("fadCashReqDto->electricCommercialStore").filterConditions = {selfconditions: " t1.office_id = '" + officeId + "' and  t2.complete_name = '" + supplierName + "'"};
                me.getCmp("fadCashReqDto->jdName").filterConditions = {selfconditions: " office_id = '" + officeId + "' and  scm_supplier_name = '" + supplierName + "'"};
                var param = { purchaseContractId: purchaseContractId};
                Scdp.doAction("scminfo-search", param, function (result) {
                    if (Scdp.ObjUtil.isNotEmpty(result.scmContract)) {
                        if (Scdp.ObjUtil.isNotEmpty(result.scmContract.electricCommercialStore)) {

                        }
                        if (Scdp.ObjUtil.isNotEmpty(result.scmContract.electricCommercialStore)) {
                            me.getCmp("fadCashReqDto->electricCommercialStore").putValue(result.scmContract.electricCommercialStore);
                            if (Scdp.ObjUtil.isNotEmpty(result.scmContract.jdName)) {
                                me.getCmp("fadCashReqDto->jdName").putValue(result.scmContract.jdName);
                            }
                            if (Scdp.ObjUtil.isNotEmpty(result.scmContract.jdPassword)) {
                                me.getCmp("fadCashReqDto->jdPassword").sotValue(result.scmContract.jdPassword);
                            }
                        }
                    }
                });
            }
        };
        me.getCmp("fadCashReqDto->purchaseContractIdPlus").inputEl.on('click', me.gotoScmContract);
        me.getCmp("fadCashReqDto->purchaseContractIdPlus").setFieldStyle("color: blue;line-height:1;height:21px;text-decoration: underline;cursor:pointer");
    },
    gotoScmContract: function () {
        var me = this;
        if (me.dom.readOnly == true && Scdp.ObjUtil.isNotEmpty(me.id)) {
            var purchaseContractIdPlusId = me.id.substr(0, me.id.lastIndexOf("-inputEl"));
            var scmContractId = Ext.getCmp(purchaseContractIdPlusId).ownerCt.getCmp("purchaseContractId").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(scmContractId)) {
                var param = {uuid: scmContractId};
                var menuCode = 'CONTRACTESTABLISHMENT';
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        }
    },
//行颜色设置
    rowColorConfigFn: function (record) {
        if (record.data.isCleared == "1") {
            return 'erp-grid-font-color-green';
        }
        return null;
    },
    UIStatusChanged: function (view, uistatus) {
        view.getCmp("editPanel->purchaseContractIdPlus").setFieldStyle("color: black;line-height:0;height:21px;text-decoration: none");
        if (uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(false);
            view.getCmp("editPanel->purchaseContractIdPlus").setFieldStyle("color: blue;line-height:1;height:21px;text-decoration: underline");
        } else if (uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
        }
    }
})
;