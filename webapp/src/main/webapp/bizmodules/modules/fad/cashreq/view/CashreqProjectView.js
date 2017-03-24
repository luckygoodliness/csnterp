Ext.define('Cashreq.view.CashreqProjectView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/cashreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    bindings: ['fadCashReqDto', 'fadCashReqDto.fadCashReqInvoiceDto', 'fadCashReqDto.cdmFileRelationDto', 'fadCashReqDto.fadCashReqBudgetDto'],
    queryLayoutFile: 'cashreqproject-query-layout.xml',
    editLayoutFile: 'cashreqproject-edit-layout.xml',
    //extraLayoutFile: 'cashreq-extra-layout.xml',
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Cash_Request',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },

    resetToobar: function () {
        var me = this;
        me.getEditToolbar().add({
                text: '作废',
                cid: 'trash',
                iconCls: "erp-trash"
            },
            //M7_C2_F2_新增优化
            {
                text: '凭证生成',
                cid: 'certificate',
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
                text: '凭证',
                cid: 'toCertificate',
                iconCls: 'erp-voucher'
            });
        //M3_C6_F2_批量打印
        me.getQueryToolbar().add({
            text: '批量打印请款单',
            cid: 'batchExpenseRequest',
            iconCls: 'printer_icon_16'
        });
        me.getCmp("editPanel->trash").setDisabled(true);
        me.getCmp("editPanel->certificate").setDisabled(true);
        me.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
        me.getCmp("editPanel->editToolbar->meetingPrint").setDisabled(true);
    },

    afterInit: function () {
        var me = this;

        me.getCmp("queryPanel-\x3ebatchDelBtn").hide();
        me.resetToobar();
        var subjectCodeCmp = me.getCmp("fadCashReqDto->subjectName");
        subjectCodeCmp.filterConditions = {selfconditions: "  t.budget_code in (select s.subject_no from fad_base_subject s where  s.subject_type = '2')  and IS_PURCHASE = '0' "};
        me.getResultPanel().down().grid.rowColorConfigFn = me.rowColorConfigFn;
        // M7_C2_F1_查询优化
        me.certificateJump(me);
    },
    //M3_C6_F3_逾期标识
    //行颜色设置
    rowColorConfigFn: function (record) {
        if (record.data.isCleared == "1") {
            return 'erp-grid-font-color-green';
        }
        if (record.data.preclearDate) {
            var preclearDateStr = record.data.preclearDate.replace(/-/g, "/").replace(/T/g, " ");
            var currDate = new Date().valueOf();
            var preclearDate = new Date(preclearDateStr).valueOf();
            var dateRes = parseInt((preclearDate - currDate) / (1000 * 60 * 60 * 24));
            if (dateRes < 0) {
                if (record.data.records == 0) {
                    return 'erp-grid-font-color-red';
                }
            }
        }
        return null;
    },

    UIStatusChanged: function (view, uistatus) {
        if (uistatus == Scdp.Const.UI_INFO_STATUS_NEW
            || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY) {
            view.getCmp("fileUpload").setDisabled(false);
            view.getCmp("fileDelete").setDisabled(false);
        } else {
            view.getCmp("fileUpload").setDisabled(true);
            view.getCmp("fileDelete").setDisabled(true);
        }
        if (uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(false);
            view.getCmp("editPanel->editToolbar->meetingPrint").setDisabled(false);
        } else if (uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
            view.getCmp("editPanel->editToolbar->meetingPrint").setDisabled(true);
        }
    },

    // M7_C2_F1_查询优化
    certificateJump: function (view) {
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
});