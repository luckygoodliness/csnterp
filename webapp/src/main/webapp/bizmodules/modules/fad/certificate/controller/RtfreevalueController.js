Ext.define("Certificate.controller.RtfreevalueController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Certificate.view.RtfreevalueView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'accountInfoCode', name: 'blur', fn: 'accountInfoCodeChange'}
        ,
        {cid: 'accountInfoName', name: 'blur', fn: 'accountInfoNameChange'}
    ],
    queryAction: 'certificate-query',
    afterInit: function () {
        var me = this;

        if (window.parent.rtfreevalueAccountInfoCode == undefined) {
            me.view.getCmp("accountInfoCode").sotValue(Erp.Util.GetCookie("rtfreevalueAccountInfoCode"));
        }
        else {
            me.view.getCmp("accountInfoCode").sotValue(window.parent.rtfreevalueAccountInfoCode);
        }

        if (window.parent.rtfreevalueAccountNo == undefined) {
            me.view.getCmp("accountNo").sotValue(Erp.Util.GetCookie("rtfreevalueAccountNo"));
        }
        else {
            me.view.getCmp("accountNo").sotValue(window.parent.rtfreevalueAccountNo);
        }

        if (me.view.getCmp("accountNo").gotValue() == "|73|") {
            me.view.getCmp("accountNo").sotValue("|73|NEIBU|WAIBU|");
        }

        if (me.view.getCmp("accountNo").gotValue() == "|96|") {
            if (window.parent.accsubjRtfreevalueSubjectName == undefined) {
                if (Erp.Util.GetCookie("accsubjRtfreevalueSubjectName").indexOf("银行存款-") == 0) {
                    me.view.getCmp("accountInfoName").sotValue(Erp.Util.GetCookie("accsubjRtfreevalueSubjectName").replace("银行存款-", ""));
                }
                else if (Erp.Util.GetCookie("accsubjRtfreevalueSubjectName").indexOf("银行存款") == 0) {
                    me.view.getCmp("accountInfoName").sotValue(Erp.Util.GetCookie("accsubjRtfreevalueSubjectName").replace("银行存款", ""));
                }
            }
            else {
                if (window.parent.accsubjRtfreevalueSubjectName.indexOf("银行存款-") == 0) {
                    me.view.getCmp("accountInfoName").sotValue(window.parent.accsubjRtfreevalueSubjectName.replace("银行存款-", ""));
                }
                else if (window.parent.accsubjRtfreevalueSubjectName.indexOf("银行存款") == 0) {
                    me.view.getCmp("accountInfoName").sotValue(window.parent.accsubjRtfreevalueSubjectName.replace("银行存款", ""));
                }
            }
            me.view.getCmp("accountInfoName").setReadOnly(true);
            me.view.getCmp("accountInfoName").resetOriginalValue();
        }
        else {
            if (window.parent.rtfreevalueAccountInfoName == undefined) {
                me.view.getCmp("accountInfoName").sotValue(Erp.Util.GetCookie("rtfreevalueAccountInfoName"));
            }
            else {
                me.view.getCmp("accountInfoName").sotValue(window.parent.rtfreevalueAccountInfoName);
            }
        }
        me.view.getCmp("accountNo").setReadOnly(true);
        me.view.getCmp("accountNo").resetOriginalValue();

        if (me.view.getCmp("accountNo").gotValue() != "|96|") {
            me.view.getCmp("accountInfoCode").sotValue("");
            me.view.getCmp("accountInfoName").sotValue("");
        }
        else {
            me.view.getCmp("accountInfoCode").sotValue("");
        }

        me.doQuery();
    },
    accountInfoCodeChange: function () {
        var me = this;
        me.doQuery();
    },
    accountInfoNameChange: function () {
        var me = this;
        me.doQuery();
    }
});