Ext.define("FadReport.controller.NonFadReqDetailController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.NonFadReqDetailView',
    queryAction: 'non_fad_cashreq_detail-query',
    extraEvents: [
        {cid: 'btnRtn', name: 'click', fn: 'doReturn'}
    ],
    beforeQuery: function () {
        var me = this;
        var view = this.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;

        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            var year = actionParams.year;
            if (Scdp.ObjUtil.isNotEmpty(year)) {
                view.getCmp("year").sotValue(year);
            }
            var officeId = actionParams.officeId;
            if (Scdp.ObjUtil.isNotEmpty(officeId)) {
                view.getCmp("officeId").putValue(officeId);
            }
            var fadSubjectCode = actionParams.fadSubjectCode;
            if (Scdp.ObjUtil.isNotEmpty(fadSubjectCode)) {
                view.getCmp("fadSubjectCode").sotValue(fadSubjectCode);
            }
            me.doQuery();
        }
    },
    doReturn: function () {
        var menuCode = "ERP_OP_REPORT_JCB_1";
        Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
    }
})
;