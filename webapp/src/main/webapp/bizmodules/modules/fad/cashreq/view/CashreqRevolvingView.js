Ext.define('Cashreq.view.CashreqRevolvingView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/cashreq',
    cpHeight: 80,
    epHeight: 260,
    allowNullConditions: true,
    queryLayoutFile: 'cashreqrevolving-query-layout.xml',
    editLayoutFile: 'cashreqrevolving-edit-layout.xml',
    bindings: ['fadCashReqDto', 'fadCashReqDto.fadCashReqInvoiceDto', 'fadCashReqDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Non_Prm_CashReq_Revolving',
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
        var view = me.view;
        me.resetToobar();
        me.getResultPanel().down().grid.rowColorConfigFn = me.rowColorConfigFn;

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
})
