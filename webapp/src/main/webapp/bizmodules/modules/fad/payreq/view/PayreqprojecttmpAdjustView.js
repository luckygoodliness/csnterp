Ext.define('Payreq.view.PayreqprojecttmpAdjustView', {
    extend: 'Payreq.view.PayreqView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/payreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 50,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'payreqadjusttmp-query-layout.xml',
    editLayoutFile: 'payreqadjusttmp-edit-layout.xml',
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_PurchasePaymentRequestAdjustInterimPayment',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },

    resetToolbar: function () {
        var me = this;
        me.getEditToolbar().add({
            xtype: 'button',
            text: '支付分析报表',
            cid: 'btnAnalysis',
            iconCls: "erp-chart"
        });
    },
    afterInit: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterEditGrid: function (a, b) {

    },
    rowColorConfigFnReqCGrid: function (a, b) {

    }
})
;