Ext.define("Certificatesetrule.controller.NonProjectSubjectSubjectController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Certificatesetrule.view.NonProjectSubjectSubjectView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'certificatesetrule-query',
    afterInit: function () {
        var me = this;

        me.view.getCmp("office").sotValue(Erp.Util.isNullReturnEmpty(Erp.Util.GetCookie("queryOfficeChangeNonProjectSubjectSubjectOfficeId")));

        me.doQuery();
    }
});