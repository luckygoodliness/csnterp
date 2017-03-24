Ext.define("FadReport.controller.PrmReceiptsTotalController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.PrmReceiptsTotalView',
    queryAction: 'report-prm-receipts-total-query-action',
    extraEvents: [
        {cid: 'queryPanel->btnSendWeixin', name: 'click', fn: 'doSendWeixin'}
    ],
    afterInit: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;

        var dt = new Date();
        view.getCmp("startTime").sotValue(new Date(dt.getFullYear(), 0, 1));
        view.getCmp("endTime").sotValue(dt);
    },
    doSendWeixin: function () {
        var me = this;
        var view = me.view;
        var conditionForm = view.getConditionPanel();
        if (!conditionForm.validate()) {
            Scdp.MsgUtil.info(Erp.I18N.INFO_NOT_ENOUGH);
            return false;
        }
        var callback = function (form) {
            if (!form.validate()) {
                Scdp.MsgUtil.info(Erp.I18N.INFO_NOT_ENOUGH);
                return false;
            }

            var postData = conditionForm.gotValue();
            postData = Ext.Object.merge(postData, form.gotValue());
            Scdp.doAction("report-prm-receipts-total-weixin-action", postData, function () {
                Scdp.MsgUtil.info("发送成功！");
                form.up("window").close();
            })
        };

        Erp.Util.showWeixinMsgWindow(callback);
    }
});