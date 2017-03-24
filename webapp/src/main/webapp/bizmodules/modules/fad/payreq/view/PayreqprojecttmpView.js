Ext.define('Payreq.view.PayreqprojecttmpView', {
    extend: 'Payreq.view.PayreqView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/payreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 50,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'payreqtmp-query-layout.xml',
    editLayoutFile: 'payreqtmp-edit-layout.xml',
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_PurchasePaymentRequestInterimPayment',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },

    afterInit: function () {
        var me = this;
        this.callParent(arguments);

        var grid = me.getCmp("fadPayReqCDto");
        grid.getColumnBydataIndex("approveMoney").editor.readOnly = true;
        if (me.controller.isScmVp) {
            grid.getColumnBydataIndex("purchaseChiefMoney").editor.readOnly = false;
            grid.getColumnBydataIndex("auditMoney").editor.readOnly = false;
            me.getCmp("payBtn").setVisible(true);
            me.getCmp("cashReqBtn").setVisible(true);
        } else {
            grid.getColumnBydataIndex("purchaseChiefMoney").editor.readOnly = true;
            grid.getColumnBydataIndex("auditMoney").editor.readOnly = true;
            me.getCmp("payBtn").setVisible(false);
            me.getCmp("cashReqBtn").setVisible(false);

            if (me.controller.actionParams.type == "project") {
                grid.getColumnBydataIndex("purchaseManagerMoney").hide();
                grid.getColumnBydataIndex("purchaseManagerMoneyRate").hide();

                grid.getColumnBydataIndex("purchaseChiefMoney").hide();
                grid.getColumnBydataIndex("purchaseChiefMoneyRate").hide();

                if (me.controller.isBizVp) {
                    grid.getColumnBydataIndex("approveMoney").editor.readOnly = false;
                }
            }
        }
    }
})
;