Ext.define('Cashreq.view.CashreqDepositView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/cashreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 260,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'cashreqdeposit-query-layout.xml',
    editLayoutFile: 'cashreqdeposit-edit-layout.xml',
    //extraLayoutFile: 'cashreq-extra-layout.xml',
    bindings: ['fadCashReqDto', 'fadCashReqDto.fadCashReqInvoiceDto', 'fadCashReqDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Non_Prm_CashReq_Deposit',
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
        me.getCmp("editPanel->copyAddBtn").hide();
    },

    afterInit: function () {
        var me = this;
        me.resetToobar();
        me.getResultPanel().down().grid.rowColorConfigFn = me.rowColorConfigFn;
        var opBizIdCmp = me.getCmp("fadCashReqDto->operateBusinessBidInfoId");
        opBizIdCmp.afterSotValue = function (obj) {
            if (Scdp.ObjUtil.isNotEmpty(opBizIdCmp.gotValue())) {
                var postData = {};
                postData.uuid = me.getCmp("fadCashReqDto->uuid").gotValue();
                postData.operateBusinessBidInfoId = opBizIdCmp.gotValue();
                Scdp.doAction("fad-deposit-wrapper-operate-info", postData, function (result) {
                    if (result && result.operateBusinessBidInfo) {
                        if (Scdp.ObjUtil.isEmpty(result.errorInfo)) {
                            me.getCmp("fadCashReqDto->money").setReadOnly(true);
                            me.getCmp("fadCashReqDto->fadSubjectCode").setReadOnly(true);
                            me.getCmp("fadCashReqDto->fadSubjectCode").sotValue(null);
                            var operateObj = result.operateBusinessBidInfo;
                            me.getCmp("fadCashReqDto->money").sotValue(operateObj.bidBond);
                            me.getCmp("fadCashReqDto->depositType").sotValue("投标资审保证金");
                        } else {
                            me.getCmp("fadCashReqDto->operateBusinessBidInfoId").sotValue(null);
                            Scdp.MsgUtil.warn(result.errorInfo);
                        }
                    }
                }, null, true);
            }
            else {
                me.getCmp("fadCashReqDto->money").sotValue(null);
                me.getCmp("fadCashReqDto->money").setReadOnly(false);
                me.getCmp("fadCashReqDto->fadSubjectCode").setReadOnly(false);
            }
        };

    },
    //行颜色设置
    rowColorConfigFn: function (record) {
        if (record.data.isCleared == "1") {
            return 'erp-grid-font-color-green';
        }
        return null;
    },
    UIStatusChanged: function (view, uistatus) {
        if (uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(false);
        } else if (uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            view.getCmp("editPanel->editToolbar->expenseRequest").setDisabled(true);
        }

        if (uistatus == Scdp.Const.UI_INFO_STATUS_NEW
            || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY) {
            view.getCmp("fileUpload").setDisabled(false);
            view.getCmp("fileDelete").setDisabled(false);
        } else {
            view.getCmp("fileUpload").setDisabled(true);
            view.getCmp("fileDelete").setDisabled(true);
        }
    }
});