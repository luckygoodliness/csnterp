Ext.define("Certificate.controller.ReceiverOrPayerController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Certificate.view.ReceiverOrPayerView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'certificate-query',
    afterInit: function () {
        var me = this;

        if (window.parent.receiverOrPayerType == undefined) {
            me.view.getCmp("receiverOrPayerType").sotValue(Erp.Util.GetCookie("receiverOrPayerType"));
        }
        else {
            me.view.getCmp("receiverOrPayerType").sotValue(window.parent.receiverOrPayerType);
        }

        if (window.parent.receiverOrPayerCode == undefined) {
            me.view.getCmp("receiverOrPayerCode").sotValue(Erp.Util.GetCookie("receiverOrPayerCode"));
        }
        else {
            me.view.getCmp("receiverOrPayerCode").sotValue(window.parent.receiverOrPayerCode);
        }

        if (window.parent.receiverOrPayerName == undefined) {
            me.view.getCmp("receiverOrPayerName").sotValue(Erp.Util.GetCookie("receiverOrPayerName"));
        }
        else {
            me.view.getCmp("receiverOrPayerName").sotValue(window.parent.receiverOrPayerName);
        }

        me.view.getCmp("receiverOrPayerType").setReadOnly(true);
        me.view.getCmp("receiverOrPayerType").resetOriginalValue();
        me.doQuery();
    }
});