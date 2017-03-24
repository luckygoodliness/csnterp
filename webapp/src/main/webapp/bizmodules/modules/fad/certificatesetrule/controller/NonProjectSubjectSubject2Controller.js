Ext.define("Certificatesetrule.controller.NonProjectSubjectSubject2Controller", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Certificatesetrule.view.NonProjectSubjectSubject2View',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'certificatesetrule-query',
    afterInit: function () {
        var me = this;

        me.view.getCmp("office").sotValue(Erp.Util.isNullReturnEmpty(Erp.Util.GetCookie("editOfficeChangeNonProjectSubjectSubjectOfficeId")));

        me.doQuery();
    }
});