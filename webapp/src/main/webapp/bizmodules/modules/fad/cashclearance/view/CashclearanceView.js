Ext.define('Cashclearance.view.CashclearanceView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/cashclearance',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'cashclearance-query-layout.xml',
    editLayoutFile: 'cashclearance-edit-layout.xml',
    //extraLayoutFile: 'cashclearance-extra-layout.xml',
    bindings: ['fadCashClearanceDto', 'fadCashClearanceDto.fadCashReqInvoiceDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Fin_Cash_Offset',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        var fadCashReqInvoiceController = me.controller;
        fadCashReqInvoiceGrid.doAddRow = function (model) {
            fadCashReqInvoiceController.pickCashReq();
        };
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->copyAddRowBtn").hide();
        fadCashReqInvoiceGrid.afterEditGrid = me.changeFadCashReqInvoiceGrid;
        var clearancePersonName = me.getCmp("fadCashClearanceDto->clearancePersonName");
        clearancePersonName.afterSotValue = function () {
            fadCashReqInvoiceGrid.store.removeAll();
        };

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
            });
        me.getCmp("Btn_annul").setVisible(false);
        me.getCmp("Btn_certificate").setVisible(false);
        //根据角色判断按钮显示
        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("会计") > -1) {
            me.getCmp("Btn_annul").setVisible(true);
        }
        if (role.ROLE.indexOf("会计") > -1) {
            me.getCmp("Btn_certificate").setVisible(true);
        }

        // M7_C6_F1_查询优化
        me.certificateJump(me);
    },

    changeFadCashReqInvoiceGrid: function (eventObj, isChange) {
        if (!isChange) {
            return;
        }
        var grid = eventObj.grid;
        var view = grid.up("IView");
        var field = eventObj.field;
        if (!(field == "clearanceMoney")) {
            return;
        }
        if (view && view.controller) {
            view.controller.fadCashReqInvoiceGridChange();
        }
    },
    afterLoadItem: function () {
        this.callParent(arguments);
    },
    afterCancel: function () {
        this.callParent(arguments);
    },
    afterSave: function () {
        this.callParent(arguments);
    },

    // M7_C6_F1_查询优化
    certificateJump : function (view) {
        openCertificate = function (uuid) {
            var postData = {uuid: uuid};
            var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
            Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
        };
        var runningNoColumns = view.getResultPanel().getColumnBydataIndex("runningNo");
        var fadCashReqInvoiceGrid = view.getCmp("fadCashReqInvoiceDto");
        var editRunningNoColumns = fadCashReqInvoiceGrid.getColumnBydataIndex("runningNo");

        runningNoColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openCertificate(\'' + record.data.uuid + '\');" style="color: blue">' + value + '</a>'
            );
        };
        // M7_C6_F2_新增优化
        editRunningNoColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openCertificate(\'' + record.data.fadCashReqId + '\');" style="color: blue">' + value + '</a>'
            );
        };

    }
})
;