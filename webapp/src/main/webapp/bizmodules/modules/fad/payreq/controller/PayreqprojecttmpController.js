Ext.define("Payreq.controller.PayreqprojecttmpController", {
    extend: 'Payreq.controller.PayreqController',
    viewClass: 'Payreq.view.PayreqprojecttmpView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'searchBtn', name: 'click', fn: 'btnSearchClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'expenseRequest', name: 'click', fn: 'doPrintExpenseRequest'},
        {cid: 'btnAnalysis', name: 'click', fn: 'btnAnalysisClick'},
        {cid: 'editToolbar->payBtn', name: 'click', fn: 'doPay'},
        {cid: 'editToolbar->cashReqBtn', name: 'click', fn: 'doCashReq'},
        {cid: 'btnSendMsg', name: 'click', fn: 'btnSendMsgClick'}

    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto',
    queryAction: 'payreq-query',
    loadAction: 'payreq-load',
    addAction: 'payreq-add',
    modifyAction: 'payreq-modify',
    deleteAction: 'payreq-delete',
    exportXlsAction: "payreq-exportxls",

    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },

    beforeAdd: function () {
        var me = this;
        return true;
    },

    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadPayReqHDto->reqType").sotValue("0");
        view.getCmp("fadPayReqHDto->state").sotValue("0");
        view.getCmp("fadPayReqHDto->reqno").sotValue("");
        view.getCmp("fadPayReqHDto->createTime").sotValue(new Date());
    },

    beforeCopyAdd: function () {
        var me = this;
        return true;
    },

    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
    },

    beforeModify: function () {
        var me = this;
        return true;
    },

    afterModify: function () {
        var me = this;
        var view = me.view;
    },

    doSave: function () {
        var me = this;
        if (arguments.length == 2) {
            var succFn = function () {
                me.view.getCmp("fadPayReqCDto").startFilter();
            }
            me.superclass.doSave.call(me, arguments[0], arguments[1], succFn);
        } else {
            me.callParent(arguments);
        }
    },

    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(retData.reqno)) {
            me.view.getCmp("fadPayReqHDto->reqno").sotValue(retData.reqno);
        }
    },

    beforeLoadItem: function () {
        var me = this;
        return true;
    },

    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
    },

    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadPayReqCDto").store.clearFilter();
        if (view.getCmp("fadPayReqCDto").store.count() == 0) {
            Scdp.MsgUtil.warn("当前支付申请没有支付明细，无法提交！");
            return false;
        } else {
            return true;
        }
    },

    beforeCancel: function () {
        var me = this;
        return true;
    },

    afterCancel: function () {
        var me = this;
    },

    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if (state != "0" && state != "5") {
            Scdp.MsgUtil.warn("非新增状态的支付，删除失败！")
            return false;
        }
        return true;
    },

    afterDelete: function () {
        var me = this;
    },

    beforeBatchDel: function () {
        var me = this;
        return true;
    },

    afterBatchDel: function () {
        var me = this;
    },

    beforeExport: function () {
        var me = this;
        return true;
    },

    afterExport: function () {
        var me = this;
    },
    pickContract: function () {
        var me = this;
        me.callParent(arguments);
    }
});

