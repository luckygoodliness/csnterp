Ext.define("Certificate.controller.AccsubjRtfreevalueController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Certificate.view.AccsubjRtfreevalueView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'certificate-query',
    afterInit: function () {
        var me = this;
        if (window.parent.accsubjRtfreevalueSubjectCode == undefined) {
            me.view.getCmp("subjectCode").sotValue(Erp.Util.GetCookie("accsubjRtfreevalueSubjectCode"));
        }
        else {
            me.view.getCmp("subjectCode").sotValue(window.parent.accsubjRtfreevalueSubjectCode);
        }
        me.doQuery();
    }
});