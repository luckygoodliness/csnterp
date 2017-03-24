Ext.define('Cashreq.view.CashreqView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/cashreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'cashreq-query-layout.xml',
    editLayoutFile: 'cashreq-edit-layout.xml',
    //extraLayoutFile: 'cashreq-extra-layout.xml',
    bindings: ['fadCashReqDto', 'fadCashReqDto.fadCashReqInvoiceDto', 'fadCashReqDto.fadCashReqShareDto', 'fadCashReqDto.fadCashReqBudgetDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Non_Prm_Request',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    resetToobar: function () {
        var me = this;
        me.getEditToolbar().add({
                xtype: 'button',
                text: '作废',
                cid: 'trash',
                iconCls: "erp-trash"
            },
            {
                xtype: 'button',
                text: '凭证生成',
                cid: 'certificate',
                iconCls: 'erp-voucher'
            },
            {
                xtype: 'button',
                text: '凭证',
                cid: 'toCertificate',
                iconCls: 'erp-voucher'
            },
            {
                text: '打印请款单',
                cid: 'expenseRequest',
                iconCls: 'printer_icon_16'
            },
            {
                text: '打印会务预算表',
                cid: 'meetingPrint',
                iconCls: 'printer_icon_16'
            },
            {
                text: '打印商城支付单',
                cid: 'expenseElectric',
                iconCls: 'printer_icon_16'
            }
        );
        me.getQueryToolbar().add({
            text: '附件下载',
            cid: 'fileDownloadQ',
            iconCls: 'file_download_icon'
        },
            //M4_C6_F3_批量打印
            {
                text:'批量打印请款单',
                cid:'batchExpenseRequest',
                iconCls:'printer_icon_16'
            }
        );
        me.getCmp("queryPanel->fileDownloadQ").setVisible(false);
        me.getCmp("editPanel->certificate").setDisabled(true);
        me.getCmp("editPanel->trash").setDisabled(true);
        me.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
        me.getCmp("editPanel->editToolbar->expenseElectric").setDisabled(true);
        me.getCmp("editPanel->editToolbar->meetingPrint").setDisabled(true);
    },

    afterInit: function () {
        var me = this;
        me.resetToobar();
        me.getCmp("editPanel->copyAddBtn").hide();
        var grid = me.getCmp("fadCashReqShareDto");
        grid.getCmp("toolbar->copyAddRowBtn").hide();
        grid.doAddRow = function (model) {
            var controller = Scdp.getActiveModule().controller;
            controller.pickContract();
        };
        me.getResultPanel().down().grid.rowColorConfigFn = me.rowColorConfigFn;

        var officeId = me.getCmp("fadCashReqDto->officeId");

        officeId.afterSotValue = me.officeIdAfterSotValue;
        // M7_C3_F1_查询优化
        me.certificateJump(me);
    },
    officeIdAfterSotValue: function (a, b) {
        var me = this;
        var view = me.up("IView");
        var subjectCode = view.getCmp('fadCashReqDto->subjectCode');
        var fadSubjectCode = view.getCmp('fadCashReqDto->fadSubjectCode');
        if (Scdp.ObjUtil.isEmpty(a.rawValue) || a.rawValue != a.oldValue) {
            view.getCmp('fadCashReqDto->subjectCode').sotValue("");
            view.getCmp('fadCashReqDto->subjectName').sotValue("");
            view.getCmp('fadCashReqDto->fadSubjectCode').sotValue("");
            view.getCmp('fadCashReqDto->budgetCUuid').sotValue("");
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
        var me = this;
        var state = view.getCmp('fadCashReqDto->state').gotValue();
        if (state == "2" || state == "3" || state == "4" || state == "8") {
            if (view.getCmp("editPanel->workFlowCommitBtn"))
                view.getCmp("editPanel->workFlowCommitBtn").setDisabled(true);
        }
        if (uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(false);
            view.getCmp("editPanel->editToolbar->expenseElectric").setDisabled(false);
            view.getCmp("editPanel->editToolbar->meetingPrint").setDisabled(false);
            if (state == "0") {

            }
        } else if (uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
            view.getCmp("editPanel->editToolbar->expenseElectric").setDisabled(true);
            view.getCmp("editPanel->editToolbar->meetingPrint").setDisabled(true);
        }

    },

    // M7_C3_F1_查询优化
    certificateJump : function (view) {
        openCertificate = function (uuid) {
            var postData = {uuid: uuid};
            var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
            Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
        };

        var runningNoColumns = view.getResultPanel().getColumnBydataIndex("runningNo");

        runningNoColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openCertificate(\'' + record.data.uuid + '\');" style="color: blue">' + value + '</a>'
            );
        };
    }
})
;